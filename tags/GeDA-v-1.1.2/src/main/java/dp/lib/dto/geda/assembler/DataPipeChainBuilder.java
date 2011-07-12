
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

import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.assembler.meta.PipeMetadata;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionBindingNotFoundException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Assembles DataPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class DataPipeChainBuilder {
	
	private DataPipeChainBuilder() {
		// prevent instantiation
	}
	
	/**
	 * Builds the pipe.
	 * 
	 * @param synthesizer method synthesizer
	 * @param dtoClass dto class
	 * @param entityClass entity class
	 * @param dtoPropertyDescriptors all DTO descriptors.
	 * @param entityPropertyDescriptors all entity descriptors
	 * @param meta pipe meta
	 * @param pipe the pipe to wrap in chain
	 * @return data pipe.
	 * @throws InspectionBindingNotFoundException when fails to find descriptors for fields
	 * @throws UnableToCreateInstanceException when fails to create a data reader/writer
	 * @throws InspectionPropertyNotFoundException when fails to create a data reader/writer
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
	public static Pipe build(
			final MethodSynthesizer synthesizer,
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final PipeMetadata meta, final Pipe pipe) 
		throws InspectionBindingNotFoundException, InspectionPropertyNotFoundException, UnableToCreateInstanceException, GeDARuntimeException {
		
		final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
				dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		
		final DataReader entityFieldRead = synthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = meta.isReadOnly() ? null : synthesizer.synthesizeWriter(entityFieldDesc);
		
		return new DataPipeChain(entityFieldRead, entityFieldWrite, pipe, meta);
		
	}
	
}
