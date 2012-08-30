
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;


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
	 * 
	 * @param synthesizer method synthesizer
	 * @param dtoClass dto class
	 * @param entityClass entity class
	 * @param dtoPropertyDescriptors all DTO descriptors.
	 * @param entityPropertyDescriptors all entity descriptors
	 * @param meta meta data for this pipe
	 * @return data pipe.
	 * 
	 * @throws InspectionPropertyNotFoundException when fails to find descriptors for fields
	 * @throws InspectionBindingNotFoundException when fails to find property on entity
	 * @throws InspectionScanningException when fails to find parent entity properties
	 * @throws UnableToCreateInstanceException when fails to create data reader for parent class
	 * @throws AnnotationValidatingBindingException when fails to bind the parent field
	 * @throws AnnotationMissingBindingException when fails to bind the parent field
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
	public static Pipe build(
			final MethodSynthesizer synthesizer,
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final FieldPipeMetadata meta) 
		throws InspectionPropertyNotFoundException, InspectionBindingNotFoundException, InspectionScanningException, 
		       UnableToCreateInstanceException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException {
		
		final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
				dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

		final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
				dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		
		final DataReader dtoParentReadMethod;
		

		if (meta.isChild()) {
			final Method parentGet = dtoFieldDesc.getReadMethod();
			final Class returnType = (Class) parentGet.getGenericReturnType();
			final PropertyDescriptor[] dtoSubPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClass(returnType);
			final PropertyDescriptor dtoParentDesc = PropertyInspector.getDtoPropertyDescriptorForField(
					dtoClass, meta.getParentEntityPrimaryKeyField(), dtoSubPropertyDescriptors);
			dtoParentReadMethod = synthesizer.synthesizeReader(dtoParentDesc);

		} else {
			
			dtoParentReadMethod = null;
			
		}
		
		return new DataPipe(synthesizer,
				synthesizer.synthesizeReader(dtoFieldDesc),
				synthesizer.synthesizeWriter(dtoFieldDesc),
				dtoParentReadMethod,
				synthesizer.synthesizeReader(entityFieldDesc),
				meta.isReadOnly() ? null : synthesizer.synthesizeWriter(entityFieldDesc),
				meta
		);
	}
	
}
