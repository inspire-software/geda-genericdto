/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.adapter.ValueConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Object that handles read and write streams between Dto and Entity objects.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 12:10:02 PM
 */
@SuppressWarnings("unchecked")
class CollectionPipe implements Pipe {

    private final boolean readOnly;
    private final String dtoBeanKey;
    private final String entityBeanKey;

    private final Class< ? extends Collection> dtoCollectionClass;
    private final Class< ? extends Collection> entityCollectionClass;

    private final Class< ? > returnType;
    private final DtoToEntityMatcher dtoToEntityMatcher;

	private final Method dtoRead;
	private final Method dtoWrite;

	private final Method entityRead;
	private final Method entityWrite;

    /**
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param readOnly if set to true the aseembly of entity will not include this data
     * @param dtoBeanKey bean key for this data delegate
     * @param entityBeanKey bean key for this data delegate
     * @param returnType return type of entity collection.
     * @param dtoCollectionClass class that defined the type of collection to be used for DTO's
     * @param entityCollectionClass class that defined the type of collection to be used for Entities
     * @param dtoToEntityMatcher matcher for synchronizing collections
     */
    CollectionPipe(final Method dtoRead,
                   final Method dtoWrite,
                   final Method entityRead,
                   final Method entityWrite,
                   final boolean readOnly,
                   final String dtoBeanKey,
                   final String entityBeanKey,
                   final Class<?> returnType,
                   final Class<? extends Collection> dtoCollectionClass,
                   final Class<? extends Collection> entityCollectionClass,
                   final Class<? extends DtoToEntityMatcher> dtoToEntityMatcher) {
        this.readOnly = readOnly;
        this.dtoBeanKey = dtoBeanKey;
        this.entityBeanKey = entityBeanKey;
        this.dtoCollectionClass = dtoCollectionClass;
        this.entityCollectionClass = entityCollectionClass;

        this.returnType = returnType;

        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;

        if (readOnly) {
			this.dtoRead = null;
			this.entityWrite = null;
            PipeValidator.validateReadPipeTypes(this.dtoWrite, this.entityRead);
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            PipeValidator.validatePipeTypes(this.dtoRead, this.dtoWrite, this.entityRead, this.entityWrite);
		}

        this.dtoToEntityMatcher = newBeanForClass(
                dtoToEntityMatcher, "Unable to create matcher: " + dtoToEntityMatcher.getCanonicalName()
                        + " for: " + this.dtoBeanKey + " - " + this.entityBeanKey);

    }

    /** {@inheritDoc} */
    public void writeFromEntityToDto(final Object entity,
                                     final Object dto,
                                     final Map<String, ValueConverter> converters,
                                     final BeanFactory dtoBeanFactory)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        final Object entityCollection = this.entityRead.invoke(entity);

        if (entityCollection instanceof Collection) {
            final Collection entities = (Collection) entityCollection;

            final Collection dtos = newCollection(dtoCollectionClass);

            Object newDto = newBean(dtoBeanFactory, this.dtoBeanKey);

            try {
                final DTOAssembler assembler = DTOAssembler.newAssembler(newDto.getClass(), this.returnType);

                for (Object object : entities) {

                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.add(newDto);

                    newDto = newBean(dtoBeanFactory, this.dtoBeanKey);
                }

                this.dtoWrite.invoke(dto, dtos);

            } catch (IllegalArgumentException iae) {
                if (iae.getMessage().startsWith("This assembler is only applicable for entity")) {
                    throw new IllegalArgumentException("A missmatch in return type of entity is detected," +
                            "please check @DtoCollection.entityGenericType()", iae);
                }
                throw iae;
            }

        }
        
    }

    /** {@inheritDoc} */
    public void writeFromDtoToEntity(final Object entity,
                                     final Object dto,
                                     final Map<String, ValueConverter> converters,
                                     final BeanFactory entityBeanFactory)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

       if (this.readOnly) {
           return;
       }

       final Object originalEntityColl = this.entityRead.invoke(entity);
       final Object dtoColl = this.dtoRead.invoke(dto);

       if (dtoColl instanceof Collection) {
           // need to synch

           Collection original = null;
           if (originalEntityColl instanceof Collection) {
               original = (Collection) originalEntityColl;
           } else {
               original = newCollection(this.entityCollectionClass);
               this.entityWrite.invoke(entity,  original);
           }

           final Collection dtos = (Collection) dtoColl;

           removeDeletedItems(original, dtos);

           addOrUpdateItems(dto, converters, entityBeanFactory, original, dtos);

       } else if (originalEntityColl instanceof Collection) {
           // if there were items then clear it
           ((Collection) originalEntityColl).clear();
       } // else it was null anyways

    }

    private void addOrUpdateItems(final Object dto, final Map<String, ValueConverter> converters, final BeanFactory entityBeanFactory, final Collection original, final Collection dtos) {

        DTOAssembler assembler = null;
        for (Object dtoItem : dtos) {

            boolean toAdd = true;
            for (Object orItem : original) {

                if (this.dtoToEntityMatcher.match(dtoItem, orItem)) {
                    assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
                    toAdd = false;
                    break;
                }

            }

            if (toAdd) {
                if (assembler == null) {
                    try {
                        assembler = DTOAssembler.newAssembler(dtoItem.getClass(), this.returnType);
                    } catch (IllegalArgumentException iae) {
                        if (iae.getMessage().startsWith("This assembler is only applicable for entity")) {
                            throw new IllegalArgumentException("A missmatch in return type of entity is detected," +
                                    "please check @DtoCollection.entityGenericType()", iae);
                        }
                        throw iae;
                    }   
                }
                final Object newItem = newBean(entityBeanFactory, this.entityBeanKey);
                assembler.assembleEntity(dtoItem, newItem, converters, entityBeanFactory);
                original.add(newItem);
            }

        }
    }

    private void removeDeletedItems(final Collection original, final Collection dtos) {
        Iterator orIt = original.iterator();
        while (orIt.hasNext()) {

            final Object orItem = orIt.next();

            boolean isRemoved = true;
            for (Object dtoItem : dtos) {

                if (dtoToEntityMatcher.match(dtoItem, orItem)) {
                    isRemoved = false;
                    break;
                }
            }

            if (isRemoved) {
                orIt.remove();
            }

        }
    }

    private Collection newCollection(final Class< ? extends Collection> clazz) {
        return newBeanForClass(clazz, "Unable to create collection: " + clazz.getCanonicalName());
    }

    private <T> T newBeanForClass(final Class<T> clazz, final String errMsg) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException iex) {
            throw new IllegalArgumentException(errMsg, iex);
        } catch (IllegalAccessException iaex) {
            throw new IllegalArgumentException(errMsg, iaex);
        }
    }

    private Object newBean(final BeanFactory factory, final String beanKey) {
        final Object bean = factory.get(beanKey);
        if (bean == null) {
            throw new IllegalArgumentException("Cannot create bean for key: " + beanKey);
        }
        return bean;
    }
}
