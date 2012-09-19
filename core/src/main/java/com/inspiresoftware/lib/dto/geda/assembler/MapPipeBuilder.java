
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
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.MapPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;


/**
 * Assembles CollectionPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class MapPipeBuilder extends BasePipeBuilder {
	
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
	 * @throws InspectionBindingNotFoundException when fails to find descriptors for fields
	 * @throws UnableToCreateInstanceException when data reader/writer cannot be created
	 * @throws InspectionPropertyNotFoundException when data reader/writer cannot be created
	 * @throws InspectionScanningException when map key reader/writer cannot be created
	 * @throws AnnotationValidatingBindingException when map property binding is invalid
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
    public static Pipe build(
    		final MethodSynthesizer synthesizer,
    		final Class dtoClass, final Class entityClass,
    		final PropertyDescriptor[] dtoPropertyDescriptors, 
    		final PropertyDescriptor[] entityPropertyDescriptors, 
    		final MapPipeMetadata meta) 
    	throws InspectionBindingNotFoundException, InspectionPropertyNotFoundException, UnableToCreateInstanceException, InspectionScanningException, 
    		   AnnotationValidatingBindingException, GeDARuntimeException  {

        final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
                dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

        final DataReader dtoFieldRead = synthesizer.synthesizeReader(dtoFieldDesc);
        final DataWriter dtoFieldWrite = synthesizer.synthesizeWriter(dtoFieldDesc);


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
            entityFieldDesc = dtoFieldDesc;
        } else {
            entitySynthesizer = synthesizer;
            entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
        		dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
        }

		final DataReader entityFieldRead = entitySynthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = meta.isReadOnly() ? null : entitySynthesizer.synthesizeWriter(entityFieldDesc);

        return new MapPipe(synthesizer,
                dtoFieldRead, dtoFieldWrite,
                entityFieldRead, entityFieldWrite,
                meta);
    }
	
}
