
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


/**
 * Assembles CollectionPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class MapPipeBuilder {
	
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
    	
        final PropertyDescriptor entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
        		dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);

		final DataReader entityFieldRead = synthesizer.synthesizeReader(entityFieldDesc);
		final DataWriter entityFieldWrite = meta.isReadOnly() ? null : synthesizer.synthesizeWriter(entityFieldDesc);

        final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
        		dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

        final DataReader dtoFieldRead = synthesizer.synthesizeReader(dtoFieldDesc);
		final DataWriter dtoFieldWrite = synthesizer.synthesizeWriter(dtoFieldDesc);
		
		final DataReader entityItemKeyRead;
		if (meta.getMapKeyForCollection() != null && meta.getMapKeyForCollection().length() > 0) {
		
			final PropertyDescriptor[] itemDesc = PropertyInspector.getPropertyDescriptorsForClass(meta.getReturnType());
			final PropertyDescriptor itemKeyDesc = PropertyInspector.getEntityPropertyDescriptorForField(
	        		dtoClass, meta.getReturnType(), meta.getDtoFieldName(), meta.getMapKeyForCollection(), itemDesc);
			entityItemKeyRead = synthesizer.synthesizeReader(itemKeyDesc);
		} else {
			entityItemKeyRead = null;
		}

        return new MapPipe(synthesizer,
                dtoFieldRead, dtoFieldWrite,
                entityFieldRead, entityFieldWrite,
                entityItemKeyRead,
                meta);
    }
	
}
