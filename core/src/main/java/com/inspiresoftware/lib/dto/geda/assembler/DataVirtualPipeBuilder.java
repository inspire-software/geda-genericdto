
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;


/**
 * Assembles DataVirtualPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
class DataVirtualPipeBuilder extends BasePipeBuilder<FieldPipeMetadata> {

	/**
	 * {@inheritDoc}
	 * 
	 * @throws InspectionPropertyNotFoundException when fails to find descriptors for fields
	 * @throws InspectionBindingNotFoundException when fails to find property on entity
	 * @throws InspectionScanningException when fails to find parent entity properties
	 * @throws UnableToCreateInstanceException when fails to create data reader for parent class
	 * @throws AnnotationValidatingBindingException when fails to bind the parent field
	 * @throws AnnotationMissingBindingException when fails to bind the parent field
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
	public Pipe build(
            final Registry registry,
			final MethodSynthesizer synthesizer,
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final FieldPipeMetadata meta, final Pipe pipe)
		throws InspectionPropertyNotFoundException, InspectionBindingNotFoundException, InspectionScanningException, 
		       UnableToCreateInstanceException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException {
		
		final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
				dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);
		
		return new DataVirtualPipe(
                meta.isReadOnly() ? null : synthesizer.synthesizeReader(dtoFieldDesc),
				synthesizer.synthesizeWriter(dtoFieldDesc),
				meta
		);
	}
	
}
