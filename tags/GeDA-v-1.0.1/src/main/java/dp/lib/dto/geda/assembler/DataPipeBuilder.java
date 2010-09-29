
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

import dp.lib.dto.geda.adapter.meta.FieldPipeMetadata;

/**
 * Assembles DataPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class DataPipeBuilder {
	
	/**
	 * Assembles DataPipe.
	 * @param dtoClass
	 * @param entityClass
	 */
	private DataPipeBuilder() {
		// prevent instatiation
	}
	
	/**
	 * Builds the pipe.
	 * @param dtoClass dto class
	 * @param entityClass entity class
	 * @param dtoPropertyDescriptors all DTO descriptors.
	 * @param entityPropertyDescriptors all entity descriptors
	 * @param meta meta data for this pipe
	 * @return data pipe.
	 * @throws IllegalArgumentException when fails to find descriptors for fields
	 */
	public static Pipe build(
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final FieldPipeMetadata meta) throws IllegalArgumentException {
		
		final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
				dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

		final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
				dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		
		final Method dtoParentReadMethod;
		

		if (meta.isChild()) {
			final Method parentGet = dtoFieldDesc.getReadMethod();
			final Class returnType = (Class) parentGet.getGenericReturnType();
			final PropertyDescriptor[] dtoSubPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClass(returnType);
			final PropertyDescriptor dtoParentDesc = PropertyInspector.getDtoPropertyDescriptorForField(
					dtoClass, meta.getParentEntityPrimaryKeyField(), dtoSubPropertyDescriptors);
			dtoParentReadMethod = dtoParentDesc.getReadMethod();

		} else {
			
			dtoParentReadMethod = null;
			
		}
		
		return new DataPipe(
				dtoFieldDesc.getReadMethod(),
				dtoFieldDesc.getWriteMethod(),
				dtoParentReadMethod,
				entityFieldDesc.getReadMethod(),
				entityFieldDesc.getWriteMethod(),
				meta
		);
	}
	
}
