/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */
package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;


/**
 * Object that handles read and write streams between Dto and Entity objects.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipe implements Pipe {
	
	private final String requiresConverter;
	private final boolean readOnly;
    private final String dtoBeanKey;
    private final String entityBeanKey;

	private final Method dtoRead;
	private final Method dtoWrite;
	
	private final Method entityRead;
	private final Method entityWrite;
	
	/**
	 * @param dtoRead method for reading data from DTO field
	 * @param dtoWrite method for writting data to DTO field
	 * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writting data to Entity field
	 * @param requiresConverter true if data cannot be directly mapped and needs a converter.
	 * @param readOnly if set to true the aseembly of entity will not include this data
     * @param dtoBeanKey bean key for this data delegate
     * @param entityBeanKey bean key for this data delegate
	 */
	public DataPipe(final Method dtoRead,
					final Method dtoWrite,
					final Method entityRead,
					final Method entityWrite,
					final String requiresConverter,
					final boolean readOnly,
                    final String dtoBeanKey,
                    final String entityBeanKey) {
		this.readOnly = readOnly;
		this.requiresConverter = requiresConverter;
        this.dtoBeanKey = dtoBeanKey;
        this.entityBeanKey = entityBeanKey;

		this.dtoWrite = dtoWrite;
		this.entityRead = entityRead;
		if (readOnly) {
			this.dtoRead = null;
			this.entityWrite = null;
            if (requiresConverter == null) {
                PipeValidator.validateReadPipeTypes(this.dtoWrite, this.entityRead);
            }
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            if (requiresConverter == null) {
                PipeValidator.validatePipeTypes(this.dtoRead, this.dtoWrite, this.entityRead, this.entityWrite);
            }
		}
		
	}
	
	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity,
                                     final Object dto,
                                     final Map<String, ValueConverter> converters,
                                     final BeanFactory dtoBeanFactory)
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (entity == null) {
			return;
		}

		final Object entityData = this.entityRead.invoke(entity);

        if (hasSubEntity()) {

            if (entityData != null) {
                createDtoAndWriteFromEntityToDto(dto, converters, dtoBeanFactory, entityData);
            }

        } else {
		
            if (usesConverter()) {
                this.dtoWrite.invoke(dto, getConverter(converters).convertToDto(entityData, dtoBeanFactory));
            } else {
                this.dtoWrite.invoke(dto, entityData);
            }
        }
	}

    private void createDtoAndWriteFromEntityToDto(final Object dto,
                                                  final Map<String, ValueConverter> converters,
                                                  final BeanFactory dtoBeanFactory,
                                                  final Object entityData)
            throws IllegalAccessException, InvocationTargetException {
        final Object dtoDataDelegate = new NewObjectProxy(
                dtoBeanFactory,
                this.dtoBeanKey,
                dto,
                this.dtoWrite
        ).create();
        final DTOAssembler assembler = DTOAssembler.newAssembler(dtoDataDelegate.getClass(), entityData.getClass());
        assembler.assembleDto(dtoDataDelegate, entityData,  converters, dtoBeanFactory);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
	public void writeFromDtoToEntity(final Object entity,
                                     final Object dto,
			                         final Map<String, ValueConverter> converters,
                                     final BeanFactory entityBeanFactory)
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		if (readOnly) {
			return;
		}

		
        final Object dtoValue;
        final Object dtoData = this.dtoRead.invoke(dto);

        if (usesConverter()) {
            if (entity instanceof NewObjectProxy) {
                dtoValue = getConverter(converters).convertToEntity(dtoData, null, entityBeanFactory);
            } else {
                dtoValue = getConverter(converters).convertToEntity(dtoData, entity, entityBeanFactory);
            }
        } else {
            dtoValue = dtoData;
        }

        if (dtoValue != null) {
            final Object parentEntity;
            if (entity instanceof NewObjectProxy) {
                parentEntity = ((NewObjectProxy) entity).create();
            } else {
                parentEntity = entity;
            }
            if (parentEntity instanceof Collection) {
                // if we are dealing with collections need to copy values accross
                ((Collection) parentEntity).addAll((Collection) dtoValue);
            } else if (hasSubEntity()) {

                Object dataEntity = this.entityRead.invoke(parentEntity);
                if (dataEntity == null) {
                    if (entityBeanFactory == null || this.entityBeanKey == null) {
                        throw new IllegalArgumentException("Need to specify bean factory and bean key for DTO's entity " + dtoValue.getClass());
                    }
                    dataEntity = entityBeanFactory.get(this.entityBeanKey);
                    if (dataEntity == null) {
                        throw new IllegalArgumentException("Unable to get bean instance for key: " + this.entityBeanKey);
                    }
                    this.entityWrite.invoke(parentEntity, dataEntity);
                }

                final DTOAssembler assembler = DTOAssembler.newAssembler(dtoValue.getClass(), dataEntity.getClass());
                assembler.assembleEntity(dtoValue, dataEntity, converters, entityBeanFactory);
            } else {
                this.entityWrite.invoke(parentEntity, dtoValue);
            }
        } else if (!(entity instanceof NewObjectProxy)) {
            // if the dtoValue is null the setting only makes sense if the entity bean existed.
            if (entity instanceof Collection) {
                ((Collection) entity).clear();
            } else {
                this.entityWrite.invoke(entity, null);
            }
        }


	}
	
	private boolean usesConverter() {
		return this.requiresConverter != null && this.requiresConverter.length() > 0;
	}

    private boolean hasSubEntity() {
        return this.dtoBeanKey != null && this.dtoBeanKey.length() > 0;
    }
	
	private ValueConverter getConverter(final Map<String, ValueConverter> converters) 
		throws IllegalArgumentException {
		
		if (converters != null && !converters.isEmpty() && converters.containsKey(this.requiresConverter)) {
            return converters.get(this.requiresConverter);
		}
		throw new IllegalArgumentException("Required converter: " + this.requiresConverter 
				+ " cannot be located");
	}
	
}
