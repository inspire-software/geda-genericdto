
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;

import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;


/**
 * Assembles DataVirtualPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class DataVirtualPipeBuilder {
	
	/**
	 * Assembles DataPipe.
	 * @param dtoClass
	 * @param entityClass
	 */
	private DataVirtualPipeBuilder() {
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
		
		return new DataVirtualPipe(
				synthesizer.synthesizeReader(dtoFieldDesc),
				synthesizer.synthesizeWriter(dtoFieldDesc),
				meta
		);
	}
	
}
