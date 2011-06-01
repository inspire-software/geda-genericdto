
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.meta.CollectionPipeMetadata;

import java.beans.PropertyDescriptor;

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
	 * @throws IllegalArgumentException when fails to find descriptors for fields
	 */
    public static Pipe build(
    		final MethodSynthesizer synthesizer,
    		final Class dtoClass, final Class entityClass,
    		final PropertyDescriptor[] dtoPropertyDescriptors, 
    		final PropertyDescriptor[] entityPropertyDescriptors, 
    		final CollectionPipeMetadata meta) throws IllegalArgumentException {
    	
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
