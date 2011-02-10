
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

import dp.lib.dto.geda.adapter.meta.PipeMetadata;

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
	 * @throws IllegalArgumentException when fails to find descriptors for fields
	 */
	public static Pipe build(
			final MethodSynthesizer synthesizer,
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final PipeMetadata meta, final Pipe pipe) throws IllegalArgumentException {
		
		final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
				dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		
		final DataReader entityFieldRead = synthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = synthesizer.synthesizeWriter(entityFieldDesc);
		
		return new DataPipeChain(entityFieldRead, entityFieldWrite, pipe, meta);
		
	}
	
}
