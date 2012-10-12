
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
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;


/**
 * Assembles DataPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
class DataPipeChainBuilder extends BasePipeBuilder<PipeMetadata> {

	/**
	 * {@inheritDoc}
     *
	 * @throws InspectionBindingNotFoundException when fails to find descriptors for fields
	 * @throws UnableToCreateInstanceException when fails to create a data reader/writer
	 * @throws InspectionPropertyNotFoundException when fails to create a data reader/writer
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
	public Pipe build(
            final Registry registry,
			final MethodSynthesizer synthesizer,
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final PipeMetadata meta, final Pipe pipe) 
		throws InspectionBindingNotFoundException, InspectionPropertyNotFoundException, UnableToCreateInstanceException, GeDARuntimeException {

        final boolean isMapEntity = Map.class.isAssignableFrom(entityClass);
        final boolean isListEntity = !isMapEntity && List.class.isAssignableFrom(entityClass);

        final MethodSynthesizer entitySynthesizer;
        final PropertyDescriptor entityFieldDesc;

        if (isMapEntity || isListEntity) {
            if (isMapEntity) {
                entitySynthesizer = mapSynthesizer;
            } else {
                entitySynthesizer = listSynthesizer;
            }
            entityFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
                    dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);
        } else {
            entitySynthesizer = synthesizer;
            entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
				    dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
        }
		
		final DataReader entityFieldRead = entitySynthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = meta.isReadOnly() ? null : entitySynthesizer.synthesizeWriter(entityFieldDesc);
		
		return new DataPipeChain(entityFieldRead, entityFieldWrite, pipe, meta);
		
	}
	
}
