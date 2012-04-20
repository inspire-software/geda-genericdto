
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.meta.MapPipeMetadata;

import java.beans.PropertyDescriptor;

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
	 * 
	 * @param synthesizer method synthesizer
	 * @param dtoClass dto class
	 * @param entityClass entity class
	 * @param dtoPropertyDescriptors all DTO descriptors.
	 * @param entityPropertyDescriptors all entity descriptors
	 * @param meta meta for this pipe
	 * @return data pipe.
	 * @throws IllegalArgumentException when fails to find descriptors for fields
	 */
    public static Pipe build(
    		final MethodSynthesizer synthesizer,
    		final Class dtoClass, final Class entityClass,
    		final PropertyDescriptor[] dtoPropertyDescriptors, 
    		final PropertyDescriptor[] entityPropertyDescriptors, 
    		final MapPipeMetadata meta) throws IllegalArgumentException {
    	
        final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
        		dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);

		final DataReader entityFieldRead = synthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = meta.isReadOnly() ? null : synthesizer.synthesizeWriter(entityFieldDesc);

        final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
        		dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

        final DataReader dtoFieldRead = synthesizer.synthesizeReader(dtoFieldDesc);
		final DataWriter dtoFieldWrite = synthesizer.synthesizeWriter(dtoFieldDesc);
		
		final DataReader entityItemKeyRead;
		if (meta.getMapKeyForCollection() != null && meta.getMapKeyForCollection().length() > 0) {
		
			final PropertyDescriptor[] itemDesc = PropertyInspector.getPropertyDescriptorsForClass(meta.getReturnType());
			final PropertyDescriptor itemKeyDesc = PropertyInspector.getEntityPropertyDescriptorForField(
	        		dtoClass, meta.getReturnType(), meta.getDtoFieldName(), meta.getMapKeyForCollection(), itemDesc);
			entityItemKeyRead = synthesizer.synthesizeReader(itemKeyDesc);
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
