
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import java.util.Map;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBeanKeyException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException;
import com.inspiresoftware.lib.dto.geda.exception.CollectionEntityGenericReturnTypeException;
import com.inspiresoftware.lib.dto.geda.exception.DtoToEntityMatcherNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.EntityRetrieverNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidDtoInstanceException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidEntityInstanceException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException;
import com.inspiresoftware.lib.dto.geda.exception.NotDtoToEntityMatcherException;
import com.inspiresoftware.lib.dto.geda.exception.NotEntityRetrieverException;
import com.inspiresoftware.lib.dto.geda.exception.NotValueConverterException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import com.inspiresoftware.lib.dto.geda.exception.ValueConverterNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException.MissingBindingType;



/**
 * Object that handles read and write streams between Dto and Entity objects.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipe implements Pipe {
	
    private final FieldPipeMetadata meta;
    
    private final DataReader dtoParentKeyRead;
    
    private final MethodSynthesizer synthesizer;

    private final DataReader dtoRead;
	private final DataWriter dtoWrite;
	
	private final DataReader entityRead;
	private final DataWriter entityWrite;
	
	private static final Object NULL = null;
	
	/**
	 * @param synthesizer synthesizer
	 * @param dtoRead method for reading data from DTO field
	 * @param dtoWrite method for writting data to DTO field
	 * @param dtoParentKeyRead method for reading Parent key data from DTO field
	 * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writting data to Entity field
	 * @param meta meta data for this pipe.
	 * @throws AnnotationMissingBindingException if some of the parameter missing from the annotation
	 * @throws AnnotationValidatingBindingException if binding pipes are invalid
	 */
	public DataPipe(final MethodSynthesizer synthesizer,
					final DataReader dtoRead,
					final DataWriter dtoWrite,
					final DataReader dtoParentKeyRead,
					final DataReader entityRead,
					final DataWriter entityWrite,
					final FieldPipeMetadata meta) throws AnnotationMissingBindingException, AnnotationValidatingBindingException {
		
		this.meta = meta;

		this.synthesizer = synthesizer;
		
		this.dtoWrite = dtoWrite;
		this.entityRead = entityRead;
		if (meta.isReadOnly()) {
			
			PipeValidator.validateReadPipeNonNull(this.dtoWrite, this.meta.getDtoFieldName(), 
					this.entityRead, this.meta.getEntityFieldName());
			
			this.dtoRead = null;
			this.entityWrite = null;
            if (!usesConverter()) {
                PipeValidator.validateReadPipeTypes(this.dtoWrite, this.meta.getDtoFieldName(), 
                		this.entityRead, this.meta.getEntityFieldName());
            }
		} else {
			
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;

			PipeValidator.validatePipeNonNull(this.dtoRead, this.dtoWrite, this.meta.getDtoFieldName(), 
					this.entityRead, this.entityWrite, this.meta.getEntityFieldName());
			
			if (!usesConverter()) {
                PipeValidator.validatePipeTypes(this.dtoRead, this.dtoWrite, this.meta.getDtoFieldName(), 
                		this.entityRead, this.entityWrite, this.meta.getEntityFieldName());
            }
		}
		if (this.meta.isChild()) {
			
			this.dtoParentKeyRead = dtoParentKeyRead;
			PipeValidator.validatePipeNonNull(this.dtoParentKeyRead, MissingBindingType.PARENT_READ, this.meta.getDtoFieldName());
			
		} else {
			
			this.dtoParentKeyRead = null;
			
		}
		
	}
	
	/** {@inheritDoc} */
	public String getBinding() {
		return meta.getEntityFieldName();
	}

	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity,
                                     final Object dto,
                                     final Map<String, Object> converters,
                                     final BeanFactory dtoBeanFactory) 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, 
			   AnnotationMissingException, NotValueConverterException, ValueConverterNotFoundException, 
			   InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, InspectionScanningException, 
			   UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
			   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
			   AnnotationDuplicateBindingException, CollectionEntityGenericReturnTypeException {
		
		if (entity == null) {
			return;
		}

		final Object entityData = this.entityRead.read(entity);

		if (entityData != null) {
	        if (hasSubEntity()) {
	
                createDtoAndWriteFromEntityToDto(dto, converters, dtoBeanFactory, entityData);
	
	        } else {
			
	            if (usesConverter()) {
	                this.dtoWrite.write(dto, getConverter(converters).convertToDto(entityData, dtoBeanFactory));
	            } else {
	                this.dtoWrite.write(dto, entityData);
	            }
	            
	        }
		} else {
			
			this.dtoWrite.write(dto, NULL);
		
		}
	}

    private void createDtoAndWriteFromEntityToDto(final Object dto,
                                                  final Map<String, Object> converters,
                                                  final BeanFactory dtoBeanFactory,
                                                  final Object entityData) 
    		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, 
    		       InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, InspectionScanningException, 
    		       UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
    		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
    		       AnnotationDuplicateBindingException, NotValueConverterException, ValueConverterNotFoundException, 
    		       CollectionEntityGenericReturnTypeException {
        final Object dtoDataDelegate = new NewDataProxy(
                dtoBeanFactory,
                this.meta,
                true,
                dto,
                this.dtoWrite
        ).create();
        final DTOAssembler assembler = DTOAssembler.newCustomAssembler(dtoDataDelegate.getClass(), entityData.getClass(), synthesizer);
        
        assembler.assembleDto(dtoDataDelegate, entityData,  converters, dtoBeanFactory);
    }

    /** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity,
                                     final Object dto,
			                         final Map<String, Object> converters,
                                     final BeanFactory entityBeanFactory) 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, 
			   NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException, 
			   ValueConverterNotFoundException, AnnotationMissingBeanKeyException, AnnotationMissingException, 
			   InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, InspectionScanningException, 
			   UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
			   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
			   AnnotationDuplicateBindingException, CollectionEntityGenericReturnTypeException, DtoToEntityMatcherNotFoundException, 
			   NotDtoToEntityMatcherException {

		if (meta.isReadOnly()) {
			return;
		}

		
        final Object dtoData = this.dtoRead.read(dto);
        
        if (this.meta.isChild()) {
        	
        	writeParentObject(dtoData, entity, converters, entityBeanFactory);
        	return;
        	
        }

        final Object dtoValue = getDtoValue(dtoData, entity, converters, entityBeanFactory);

        if (dtoValue != null) {
        	
            final Object parentEntity = getOrCreateParentEntityForDtoValue(entity);
            
            if (hasSubEntity()) {

                assembleSubEntity(dtoValue, parentEntity, converters, entityBeanFactory);
                
            } else {
                this.entityWrite.write(parentEntity, dtoValue);
            }
        } else if (entity != null && !(entity instanceof NewDataProxy)) {
            // if the dtoValue is null the setting only makes sense if the entity bean existed.
            this.entityWrite.write(entity, NULL);
        }


	}

	private Object getDtoValue(final Object dtoData, final Object entity, final Map<String, Object> converters,
			final BeanFactory entityBeanFactory) throws NotValueConverterException, ValueConverterNotFoundException {
        if (usesConverter()) {
            if (entity instanceof NewDataProxy) {
                return getConverter(converters).convertToEntity(dtoData, null, entityBeanFactory);
            }
            return getConverter(converters).convertToEntity(dtoData, entity, entityBeanFactory);
        }
        return dtoData;
	}

	private void assembleSubEntity(final Object dtoValue, final Object parentEntity,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory) 
		throws BeanFactoryNotFoundException, AnnotationMissingBeanKeyException, BeanFactoryUnableToCreateInstanceException, 
		       AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
		       InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
		       GeDARuntimeException, AnnotationDuplicateBindingException, NotEntityRetrieverException, 
		       EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, 
		       CollectionEntityGenericReturnTypeException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
		
		Object dataEntity = this.entityRead.read(parentEntity);
		if (dataEntity == null) {
		    if (entityBeanFactory == null) {
		    	throw new BeanFactoryNotFoundException(
		    			this.meta.getDtoFieldName() + ":" + dtoValue.getClass(), this.meta.getEntityBeanKey(), false);
		    } else if (this.meta.getEntityBeanKey() == null) {
		        throw new AnnotationMissingBeanKeyException(this.meta.getDtoFieldName() + ":" + dtoValue.getClass(), false);
		    }
		    dataEntity = this.meta.newEntityBean(entityBeanFactory);
		    this.entityWrite.write(parentEntity, dataEntity);
		}

		final DTOAssembler assembler = DTOAssembler.newCustomAssembler(dtoValue.getClass(), dataEntity.getClass(), synthesizer);
		assembler.assembleEntity(dtoValue, dataEntity, converters, entityBeanFactory);
	}

	private Object getOrCreateParentEntityForDtoValue(final Object entity) 
			throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException  {
		if (entity instanceof NewDataProxy) {
		    return ((NewDataProxy) entity).create();
		} 
		return entity;
	}

	@SuppressWarnings("unchecked")
	private void writeParentObject(final Object dtoData, final Object entity,
		final Map<String, Object> converters, final BeanFactory entityBeanFactory) 
			throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, 
				   NotEntityRetrieverException, EntityRetrieverNotFoundException {
		if (dtoData == null) {
			if (entity != null) {
		        // if the dtoValue is null the setting only makes sense if the entity bean existed.
		        this.entityWrite.write(entity, NULL);
		    }
		} else {
			
			final Object primaryKey = this.dtoParentKeyRead.read(dtoData);
			final Class returnType = this.entityRead.getReturnType();
			if (entityBeanFactory == null || this.meta.getEntityBeanKey() == null) {
				throw new BeanFactoryNotFoundException(
						dtoData.getClass() + ":" + this.meta.getParentEntityPrimaryKeyField(), this.meta.getEntityBeanKey(), false);
		    }
			final Class beanClass = this.meta.newEntityBean(entityBeanFactory).getClass(); // overhead but need to be stateless!!!
			final Object entityForPk = getRetriever(converters).retrieveByPrimaryKey(returnType, beanClass, primaryKey);
			// if we did not find anything, setting null. Maybe need to throw exception here or maybe it is retiriever's job?
			this.entityWrite.write(entity, entityForPk);
		}
	}
	
	private boolean usesConverter() {
		return this.meta.getConverterKey() != null && this.meta.getConverterKey().length() > 0;
	}

    private boolean hasSubEntity() {
        return this.meta.getDtoBeanKey() != null && this.meta.getDtoBeanKey().length() > 0;
    }
	
    private ValueConverter getConverter(final Map<String, Object> converters) 
    		throws NotValueConverterException, ValueConverterNotFoundException {
    	
    	if (converters != null && !converters.isEmpty() && converters.containsKey(this.meta.getConverterKey())) {
    		final Object conv = converters.get(this.meta.getConverterKey());
    		if (conv instanceof ValueConverter) {
    			return (ValueConverter) conv;
    		}
    		throw new NotValueConverterException(
        			this.meta.getDtoFieldName(), this.meta.getEntityFieldName(), this.meta.getConverterKey());
    	}
    	throw new ValueConverterNotFoundException(
    			this.meta.getDtoFieldName(), this.meta.getEntityFieldName(), this.meta.getConverterKey());
    }
    
	private EntityRetriever getRetriever(final Map<String, Object> converters) 
			throws NotEntityRetrieverException, EntityRetrieverNotFoundException {
		
		if (converters != null && !converters.isEmpty() && converters.containsKey(this.meta.getEntityRetrieverKey())) {
			final Object conv = converters.get(this.meta.getEntityRetrieverKey());
			if (conv instanceof EntityRetriever) {
				return (EntityRetriever) conv;
			}
			throw new NotEntityRetrieverException(
					this.meta.getEntityFieldName(), this.meta.getDtoFieldName(), this.meta.getConverterKey());
		}
		throw new EntityRetrieverNotFoundException(
				this.meta.getEntityFieldName(), this.meta.getDtoFieldName(), this.meta.getEntityRetrieverKey());
	}
	
}
