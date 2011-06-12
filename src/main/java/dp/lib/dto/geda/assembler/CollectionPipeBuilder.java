
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

import dp.lib.dto.geda.assembler.meta.CollectionPipeMetadata;
import dp.lib.dto.geda.exception.AnnotationValidatingBindingException;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionBindingNotFoundException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Assembles CollectionPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class CollectionPipeBuilder {
	
	private CollectionPipeBuilder() {
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
	 * 
	 * @throws InspectionBindingNotFoundException when inspecting entity
	 * @throws UnableToCreateInstanceException if unable to create instance of data reader/writer
	 * @throws InspectionPropertyNotFoundException when unable to locate required property for data reader/writer
	 * @throws AnnotationValidatingBindingException when data reader/writer have mismatching parameters/return types
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
    public static Pipe build(
    		final MethodSynthesizer synthesizer,
    		final Class dtoClass, final Class entityClass,
    		final PropertyDescriptor[] dtoPropertyDescriptors, 
    		final PropertyDescriptor[] entityPropertyDescriptors, 
    		final CollectionPipeMetadata meta) 
    throws InspectionBindingNotFoundException, InspectionPropertyNotFoundException, UnableToCreateInstanceException, 
           AnnotationValidatingBindingException, GeDARuntimeException  {
    	
        final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
        		dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);

		final DataReader entityFieldRead = synthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = meta.isReadOnly() ? null : synthesizer.synthesizeWriter(entityFieldDesc);

        final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
        		dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

        final DataReader dtoFieldRead = synthesizer.synthesizeReader(dtoFieldDesc);
		final DataWriter dtoFieldWrite = synthesizer.synthesizeWriter(dtoFieldDesc);

        return new CollectionPipe(
                dtoFieldRead, dtoFieldWrite,
                entityFieldRead, entityFieldWrite,
                meta);
    }
	
}
