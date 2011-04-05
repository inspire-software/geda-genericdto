
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.EntityRetriever;
import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.adapter.meta.FieldPipeMetadata;


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

    private final DataReader dtoRead;
	private final DataWriter dtoWrite;
	
	private final DataReader entityRead;
	private final DataWriter entityWrite;
	
	private static final Object NULL = null;
	
	/**
	 * @param dtoRead method for reading data from DTO field
	 * @param dtoWrite method for writting data to DTO field
	 * @param dtoParentKeyRead method for reading Parent key data from DTO field
	 * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writting data to Entity field
	 * @param meta meta data for this pipe.
	 */
	public DataPipe(final DataReader dtoRead,
					final DataWriter dtoWrite,
					final DataReader dtoParentKeyRead,
					final DataReader entityRead,
					final DataWriter entityWrite,
					final FieldPipeMetadata meta) {
		
		try {
			this.meta = meta;
	
			this.dtoWrite = dtoWrite;
			this.entityRead = entityRead;
			if (meta.isReadOnly()) {
				
				PipeValidator.validateReadPipeNonNull(this.dtoWrite, this.entityRead);
				
				this.dtoRead = null;
				this.entityWrite = null;
	            if (!usesConverter()) {
	                PipeValidator.validateReadPipeTypes(this.dtoWrite, this.entityRead);
	            }
			} else {
				
				this.dtoRead = dtoRead;
				this.entityWrite = entityWrite;
	
				PipeValidator.validatePipeNonNull(this.dtoRead, this.dtoWrite, this.entityRead, this.entityWrite);
				
				if (!usesConverter()) {
	                PipeValidator.validatePipeTypes(this.dtoRead, this.dtoWrite, this.entityRead, this.entityWrite);
	            }
			}
			if (this.meta.isChild()) {
				
				this.dtoParentKeyRead = dtoParentKeyRead;
				PipeValidator.validatePipeNonNull(this.dtoParentKeyRead, "parentKey pipe is null");
				
			} else {
				
				this.dtoParentKeyRead = null;
				
			}
		} catch (IllegalArgumentException iae) {
			throw new IllegalArgumentException("Dto Field: " + this.meta.getDtoFieldName(), iae);
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
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
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
            throws IllegalAccessException, InvocationTargetException {
        final Object dtoDataDelegate = new NewDataProxy(
                dtoBeanFactory,
                this.meta,
                true,
                dto,
                this.dtoWrite
        ).create();
        final DTOAssembler assembler = DTOAssembler.newAssembler(dtoDataDelegate.getClass(), entityData.getClass());
        assembler.assembleDto(dtoDataDelegate, entityData,  converters, dtoBeanFactory);
    }

    /** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity,
                                     final Object dto,
			                         final Map<String, Object> converters,
                                     final BeanFactory entityBeanFactory)
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

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
			final BeanFactory entityBeanFactory) {
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
		throws IllegalAccessException, InvocationTargetException {
		
		Object dataEntity = this.entityRead.read(parentEntity);
		if (dataEntity == null) {
		    if (entityBeanFactory == null || this.meta.getEntityBeanKey() == null) {
		        throw new IllegalArgumentException("Need to specify bean factory and bean key for DTO's entity " + dtoValue.getClass());
		    }
		    dataEntity = this.meta.newEntityBean(entityBeanFactory);
		    this.entityWrite.write(parentEntity, dataEntity);
		}

		final DTOAssembler assembler = DTOAssembler.newAssembler(dtoValue.getClass(), dataEntity.getClass());
		assembler.assembleEntity(dtoValue, dataEntity, converters, entityBeanFactory);
	}

	private Object getOrCreateParentEntityForDtoValue(final Object entity) throws IllegalAccessException, InvocationTargetException {
		if (entity instanceof NewDataProxy) {
		    return ((NewDataProxy) entity).create();
		} 
		return entity;
	}

	@SuppressWarnings("unchecked")
	private void writeParentObject(final Object dtoData, final Object entity,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory) throws IllegalAccessException,
			InvocationTargetException {
		if (dtoData == null) {
			if (entity != null) {
		        // if the dtoValue is null the setting only makes sense if the entity bean existed.
		        this.entityWrite.write(entity, NULL);
		    }
		} else {
			
			final Object primaryKey = this.dtoParentKeyRead.read(dtoData);
			final Class returnType = this.entityRead.getReturnType();
			if (entityBeanFactory == null || this.meta.getEntityBeanKey() == null) {
		        throw new IllegalArgumentException("Need to specify bean factory and bean key for DTO's entity " + dtoData.getClass());
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
    	throws IllegalArgumentException {
    	
    	if (converters != null && !converters.isEmpty() && converters.containsKey(this.meta.getConverterKey())) {
    		final Object conv = converters.get(this.meta.getConverterKey());
    		if (conv instanceof ValueConverter) {
    			return (ValueConverter) conv;
    		}
    		throw new IllegalArgumentException("Required converter: " + this.meta.getConverterKey() 
    				+ " is invalid for converting: " + this.meta.getDtoFieldName() + " to " + this.meta.getEntityFieldName()
    				+ " because it must implement dp.lib.dto.geda.adapter.ValueConverter interface");
    	}
    	throw new IllegalArgumentException("Required converter: " + this.meta.getConverterKey() 
    			+ " cannot be located to convert: " + this.meta.getDtoFieldName() + " to " + this.meta.getEntityFieldName());
    }
    
	private EntityRetriever getRetriever(final Map<String, Object> converters) 
		throws IllegalArgumentException {
		
		if (converters != null && !converters.isEmpty() && converters.containsKey(this.meta.getEntityRetrieverKey())) {
			final Object conv = converters.get(this.meta.getEntityRetrieverKey());
			if (conv instanceof EntityRetriever) {
				return (EntityRetriever) conv;
			}
			throw new IllegalArgumentException("Required retriever: " + this.meta.getConverterKey() 
					+ " is invalid for  retrieving: " + this.meta.getEntityFieldName() + " for " + this.meta.getDtoFieldName()
					+ " because it must implement dp.lib.dto.geda.adapter.EntityRetriever interface");
		}
		throw new IllegalArgumentException("Required retriever: " + this.meta.getConverterKey() 
				+ " cannot be located to retrieve: " + this.meta.getEntityFieldName() + " for " + this.meta.getDtoFieldName());
	}
	
}
