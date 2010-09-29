
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import dp.lib.dto.geda.adapter.meta.MapPipeMetadata;

/**
 * Assembles CollectionPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class MapPipeBuilder {
	
	private MapPipeBuilder() {
		// prevent instatiation
	}

	/**
	 * Builds the pipe.
	 * @param dtoClass dto class
	 * @param entityClass entity class
	 * @param dtoPropertyDescriptors all DTO descriptors.
	 * @param entityPropertyDescriptors all entity descriptors
	 * @param meta meta for this pipe
	 * @return data pipe.
	 * @throws IllegalArgumentException when fails to find descriptors for fields
	 */
    public static Pipe build(
    		final Class dtoClass, final Class entityClass,
    		final PropertyDescriptor[] dtoPropertyDescriptors, 
    		final PropertyDescriptor[] entityPropertyDescriptors, 
    		final MapPipeMetadata meta) throws IllegalArgumentException {
    	
        final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
        		dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);

		final Method entityFieldRead = entityFieldDesc.getReadMethod();
		final Method entityFieldWrite = entityFieldDesc.getWriteMethod();

        final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
        		dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

        final Method dtoFieldRead = dtoFieldDesc.getReadMethod();
		final Method dtoFieldWrite = dtoFieldDesc.getWriteMethod();
		
		final Method entityItemKeyRead;
		if (meta.getMapKeyForCollection() != null && meta.getMapKeyForCollection().length() > 0) {
		
			final PropertyDescriptor[] itemDesc = PropertyInspector.getPropertyDescriptorsForClass(meta.getReturnType());
			final PropertyDescriptor itemKeyDesc = PropertyInspector.getEntityPropertyDescriptorForField(
	        		dtoClass, meta.getReturnType(), meta.getDtoFieldName(), meta.getMapKeyForCollection(), itemDesc);
			entityItemKeyRead = itemKeyDesc.getReadMethod();
		} else {
			entityItemKeyRead = null;
		}

        return new MapPipe(
                dtoFieldRead, dtoFieldWrite,
                entityFieldRead, entityFieldWrite,
                entityItemKeyRead,
                meta);
    }
	
}
