
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
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.adapter.meta.CollectionPipeMetadata;

/**
 * Object that handles read and write streams between Dto and Entity objects.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 12:10:02 PM
 */
@SuppressWarnings("unchecked")
class CollectionPipe implements Pipe {

    private final CollectionPipeMetadata meta;

	private final Method dtoRead;
	private final Method dtoWrite;

	private final Method entityRead;
	private final Method entityWrite;

    /**
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param meta collection pipe meta
     */
    CollectionPipe(final Method dtoRead,
                   final Method dtoWrite,
                   final Method entityRead,
                   final Method entityWrite,
                   final CollectionPipeMetadata meta) {
    	
    	this.meta = meta;

        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;

        if (this.meta.isReadOnly()) {
			this.dtoRead = null;
			this.entityWrite = null;
            PipeValidator.validateReadPipeTypes(this.dtoWrite, this.entityRead);
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            PipeValidator.validatePipeTypes(this.dtoRead, this.dtoWrite, this.entityRead, this.entityWrite);
		}

    }

    /** {@inheritDoc} */
    public void writeFromEntityToDto(final Object entity,
                                     final Object dto,
                                     final Map<String, Object> converters,
                                     final BeanFactory dtoBeanFactory)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        final Object entityCollection = this.entityRead.invoke(entity);

        if (entityCollection instanceof Collection) {
            final Collection entities = (Collection) entityCollection;

            final Collection dtos = this.meta.newDtoCollection();

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                final DTOAssembler assembler = DTOAssembler.newAssembler(newDto.getClass(), this.meta.getReturnType());

                for (Object object : entities) {

                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.add(newDto);

                    newDto = this.meta.newDtoBean(dtoBeanFactory);
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
                                     final Map<String, Object> converters,
                                     final BeanFactory entityBeanFactory)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

       if (this.meta.isReadOnly()) {
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
               original = this.meta.newEntityCollection();
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

    private void addOrUpdateItems(final Object dto, final Map<String, Object> converters, final BeanFactory entityBeanFactory, final Collection original, final Collection dtos) {

        DTOAssembler assembler = null;
        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher();
        for (Object dtoItem : dtos) {

            boolean toAdd = true;
            for (Object orItem : original) {

                if (matcher.match(dtoItem, orItem)) {
                    assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
                    toAdd = false;
                    break;
                }

            }

            if (toAdd) {
                if (assembler == null) {
                    try {
                        assembler = DTOAssembler.newAssembler(dtoItem.getClass(), this.meta.getReturnType());
                    } catch (IllegalArgumentException iae) {
                        if (iae.getMessage().startsWith("This assembler is only applicable for entity")) {
                            throw new IllegalArgumentException("A missmatch in return type of entity is detected," +
                                    "please check @DtoCollection.entityGenericType()", iae);
                        }
                        throw iae;
                    }   
                }
                final Object newItem = this.meta.newEntityBean(entityBeanFactory);
                assembler.assembleEntity(dtoItem, newItem, converters, entityBeanFactory);
                original.add(newItem);
            }

        }
    }

    private void removeDeletedItems(final Collection original, final Collection dtos) {
    	final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher();
        Iterator orIt = original.iterator();
        while (orIt.hasNext()) {

            final Object orItem = orIt.next();

            boolean isRemoved = true;
            for (Object dtoItem : dtos) {

                if (matcher.match(dtoItem, orItem)) {
                    isRemoved = false;
                    break;
                }
            }

            if (isRemoved) {
                orIt.remove();
            }

        }
    }
    
}
