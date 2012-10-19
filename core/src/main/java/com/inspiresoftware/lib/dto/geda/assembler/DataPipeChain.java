
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Map;



/**
 * Pipe chain describes delegation of nested beans.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipeChain implements Pipe {

	private final PipeMetadata meta;
	
	private final DataReader entityRead;
	private final DataWriter entityWrite;

    private final DataReader dtoRead;

    private final Pipe pipe;
	
	/**
     * @param dtoRead method for reading data from DTO field
     * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writing data to Entity field
	 * @param pipe the inner pipe.
	 * @param meta meta data for this data delegate
	 */
	public DataPipeChain(final DataReader dtoRead,
                         final DataReader entityRead,
					     final DataWriter entityWrite,
						 final Pipe pipe,
						 final PipeMetadata meta) {
        if (meta.isReadOnly()) {
            this.dtoRead = null;
        } else {
            this.dtoRead = dtoRead;
        }
		this.entityRead = entityRead;
		this.entityWrite = entityWrite;
		this.pipe = pipe;
		this.meta = meta;
	}
	
	/** {@inheritDoc} */
	public String getBinding() {
		return meta.getEntityFieldName() + "." + pipe.getBinding();
	}

	/** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity, final Object dto,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory) 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, NotEntityRetrieverException, 
		       EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, 
		       AnnotationMissingBeanKeyException, AnnotationMissingException, UnableToCreateInstanceException, 
		       CollectionEntityGenericReturnTypeException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
		       InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
		       AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

        if (this.meta.isReadOnly()) {
            return;
        }

        final Object dtoData = this.dtoRead.read(dto);

        Object entityData = this.entityRead.read(entity);
        if (entityData == null) {
            if (dtoData == null) {
                return; // no data and nothing was there
            } else {
                if (entityBeanFactory == null) {
                    throw new BeanFactoryNotFoundException(meta.getEntityFieldName(), meta.getEntityBeanKey(), false);
                }

                entityData = this.meta.newEntityBean(entityBeanFactory);
                this.entityWrite.write(entity, entityData);
            }
        }

		pipe.writeFromDtoToEntity(entityData, dto, converters, entityBeanFactory);
		
	}

	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, Object> converters, final BeanFactory dtoBeanFactory) 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, 
		       NotValueConverterException, ValueConverterNotFoundException, UnableToCreateInstanceException, 
		       CollectionEntityGenericReturnTypeException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
		       InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
		       AnnotationDuplicateBindingException {
		
		if (entity == null) {
			return;
		}

		final Object entityDataDelegate = this.entityRead.read(entity);
		pipe.writeFromEntityToDto(entityDataDelegate, dto, converters, dtoBeanFactory);
		
	}
	
	
	
}
