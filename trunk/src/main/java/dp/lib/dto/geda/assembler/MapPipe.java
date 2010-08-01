
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.adapter.meta.MapPipeMetadata;

/**
 * Object that handles read and write streams between Dto and Entity objects.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 12:10:02 PM
 */
@SuppressWarnings("unchecked")
class MapPipe implements Pipe {

    private final MapPipeMetadata meta;

	private final Method dtoRead;
	private final Method dtoWrite;

	private final Method entityRead;
	private final Method entityWrite;
	
	private final Method entityCollectionKeyRead;

    /**
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param entityCollectionKeyRead for reading the key of collection
     * @param meta collection pipe meta
     */
    MapPipe(final Method dtoRead,
                   final Method dtoWrite,
                   final Method entityRead,
                   final Method entityWrite,
                   final Method entityCollectionKeyRead,
                   final MapPipeMetadata meta) {
    	
    	this.meta = meta;

        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;
        this.entityCollectionKeyRead = entityCollectionKeyRead;

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
	public String getBinding() {
		return meta.getEntityFieldName();
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

            final Map dtos = this.meta.newDtoMap();

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                final DTOAssembler assembler = DTOAssembler.newAssembler(newDto.getClass(), this.meta.getReturnType());

                for (Object object : entities) {

                	final Object key = this.entityCollectionKeyRead.invoke(object);
                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.put(key, newDto);

                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.invoke(dto, dtos);

            } catch (IllegalArgumentException iae) {
                if (iae.getMessage().startsWith("This assembler is only applicable for entity")) {
                    throw new IllegalArgumentException("A missmatch in return type of entity is detected," 
                    		+ "please check @DtoCollection.entityGenericType()", iae);
                }
                throw iae;
            }

        } else if (entityCollection instanceof Map) {
        	
            final Map entities = (Map) entityCollection;

            final Map dtos = this.meta.newDtoMap();

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                final DTOAssembler assembler = DTOAssembler.newAssembler(newDto.getClass(), this.meta.getReturnType());

                for (Object key : entities.keySet()) {

                	final Object object = entities.get(key);
                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.put(key, newDto);

                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.invoke(dto, dtos);

            } catch (IllegalArgumentException iae) {
                if (iae.getMessage().startsWith("This assembler is only applicable for entity")) {
                    throw new IllegalArgumentException("A missmatch in return type of entity is detected," 
                    		+ "please check @DtoCollection.entityGenericType()", iae);
                }
                throw iae;
            }
        	
        }
        
    }

    /** {@inheritDoc} */
    public void writeFromDtoToEntity(final Object entityObj,
                                     final Object dto,
                                     final Map<String, Object> converters,
                                     final BeanFactory entityBeanFactory)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

       if (this.meta.isReadOnly()) {
           return;
       }

       final Object dtoColl = this.dtoRead.invoke(dto);

       if (dtoColl instanceof Map) {
           // need to synch

    	   final Object entity;
           if (entityObj instanceof NewDataProxy) {
        	   entity = ((NewDataProxy) entityObj).create();
           } else {
        	   entity = entityObj;
           }
           
           final Object originalEntityColl = this.entityRead.invoke(entity);

    	   
           Object original = null;
           if (originalEntityColl instanceof Collection || originalEntityColl instanceof Map) {
               original = originalEntityColl;
           } else {
               original = this.meta.newEntityMapOrCollection();
               this.entityWrite.invoke(entity,  original);
           }

           final Map dtos = (Map) dtoColl;

           if (original instanceof Collection) {
	           removeDeletedItems((Collection) original, dtos);
	           addOrUpdateItems(dto, converters, entityBeanFactory, (Collection) original, dtos);
           } else if (original instanceof Map) {
	           removeDeletedItems((Map) original, dtos);
	           addOrUpdateItems(dto, converters, entityBeanFactory, (Map) original, dtos);
           }    

       } else if (entityObj != null && !(entityObj instanceof NewDataProxy)) {
    	   final Object originalEntityColl = this.entityRead.invoke(entityObj);
    	   if (originalEntityColl instanceof Collection) {
	           // if there were items then clear it
	           ((Collection) originalEntityColl).clear();
	       } else if (originalEntityColl instanceof Map) {
	           // if there were items then clear it
	           ((Map) originalEntityColl).clear();
	       }
       } // else it was null anyways

    }
    
	private DTOAssembler lazyCreateAssembler(final DTOAssembler assembler, final Object dtoItem) {
		if (assembler == null) {
		    try {
		    	if (Object.class.equals(this.meta.getReturnType())) {
		    		throw new IllegalArgumentException("This mapping requires a genericReturnType for DtoCollection mapping.");
		    	}
		        return DTOAssembler.newAssembler(dtoItem.getClass(), this.meta.getReturnType());
		    } catch (IllegalArgumentException iae) {
		        if (iae.getMessage().startsWith("This assembler is only applicable for entity")) {
		            throw new IllegalArgumentException("A missmatch in return type of entity is detected," 
		            		+ "please check @DtoCollection.entityGenericType()", iae);
		        }
		        throw iae;
		    }   
		}
		return assembler;
	}

	private void addOrUpdateItems(final Object dto, final Map<String, Object> converters, 
			final BeanFactory entityBeanFactory, final Collection original, final Map dtos) {
		
		DTOAssembler assembler = null;
		final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher();
		for (Object dtoKey : dtos.keySet()) {
			
			try {
				final Object dtoItem = dtos.get(dtoKey);
				boolean toAdd = true;
				for (Object orItem : original) {
					
					if (matcher.match(dtoKey, orItem)) {
						assembler = lazyCreateAssembler(assembler, dtoItem);
						assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
						toAdd = false;
						break;
					}
					
				}
				
				if (toAdd) {
					assembler = lazyCreateAssembler(assembler, dtoItem);
					final Object newItem = this.meta.newEntityBean(entityBeanFactory);
					assembler.assembleEntity(dtoItem, newItem, converters, entityBeanFactory);
					original.add(newItem);
				}
        	} catch (IllegalArgumentException iae) {
				if (iae.getMessage().startsWith("This assembler is only applicable for entity:")) {
					throw new IllegalArgumentException("Check return type of collection", iae);
				}
			}
			
		}
	}
	
    private void addOrUpdateItems(final Object dto, final Map<String, Object> converters, 
    		final BeanFactory entityBeanFactory, final Map original, final Map dtos) {

        DTOAssembler assembler = null;
        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher();
        for (Object dtoKey : dtos.keySet()) {

        	try {
	        	final Object dtoItem = dtos.get(dtoKey);
	            boolean toAdd = true;
	            for (Object orKey : original.keySet()) {
	
	                if (matcher.match(dtoKey, orKey)) {
	                	final Object orItem = original.get(orKey);
	                	assembler = lazyCreateAssembler(assembler, dtoItem);
	                    assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
	                    toAdd = false;
	                    break;
	                }
	
	            }
	
	            if (toAdd) {
	                assembler = lazyCreateAssembler(assembler, dtoItem);
	                final Object newItem = this.meta.newEntityBean(entityBeanFactory);
	                assembler.assembleEntity(dtoItem, newItem, converters, entityBeanFactory);
	                original.put(dtoKey, newItem);
	            }
        	} catch (IllegalArgumentException iae) {
				if (iae.getMessage().startsWith("This assembler is only applicable for entity:")) {
					throw new IllegalArgumentException("Check return type of map", iae);
				}
			}
        }
    }

    private void removeDeletedItems(final Collection original, final Map dtos) {
    	final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher();
    	Iterator orIt = original.iterator();
    	while (orIt.hasNext()) { // must be iterator to avoid concurrent modification exception while #remove()
    		
    		final Object orItem = orIt.next();
    		
    		boolean isRemoved = true;
    		for (Object dtoKey : dtos.keySet()) {
    			
    			if (matcher.match(dtoKey, orItem)) {
    				isRemoved = false;
    				break;
    			}
    		}
    		
    		if (isRemoved) {
    			orIt.remove();
    		}
    		
    	}
    }
    
    private void removeDeletedItems(final Map original, final Map dtos) {
    	final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher();
    	final List keysToRemove = new ArrayList(); // must save to avoid concurrent modification exception while #remove(key)
    	for (Object orKey : original.keySet()) { 
        	
            boolean isRemoved = true;
            for (Object dtoKey : dtos.keySet()) {

                if (matcher.match(dtoKey, orKey)) {
                    isRemoved = false;
                    break;
                }
            }

            if (isRemoved) {
            	keysToRemove.add(orKey);
            }

        }
    	
    	for (Object orKey : keysToRemove) {
    		original.remove(orKey);
    	}
    }
    
}
