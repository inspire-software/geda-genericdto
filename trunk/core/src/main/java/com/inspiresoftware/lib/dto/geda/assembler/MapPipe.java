
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
import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.MapPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.*;


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
    
    private final MethodSynthesizer synthesizer;

	private final DataReader dtoRead;
	private final DataWriter dtoWrite;

	private final DataReader entityRead;
	private final DataWriter entityWrite;
	
	private final DataReader entityCollectionKeyRead;

    /**
     * @param synthesizer synthesizer
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param entityCollectionKeyRead for reading the key of collection
     * @param meta collection pipe meta
     * 
     * @throws AnnotationValidatingBindingException when missmaped binding
     */
    MapPipe(
    			   final MethodSynthesizer synthesizer,
    			   final DataReader dtoRead,
                   final DataWriter dtoWrite,
                   final DataReader entityRead,
                   final DataWriter entityWrite,
                   final DataReader entityCollectionKeyRead,
                   final MapPipeMetadata meta) throws AnnotationValidatingBindingException {

    	this.meta = meta;

    	this.synthesizer = synthesizer;
    	
        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;
        this.entityCollectionKeyRead = entityCollectionKeyRead;

        if (this.meta.isReadOnly()) {
			this.dtoRead = null;
			this.entityWrite = null;
            PipeValidator.validateReadPipeTypes(
            		this.dtoWrite, this.meta.getDtoFieldName(), this.entityRead, this.meta.getEntityFieldName());
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            PipeValidator.validatePipeTypes(
            		this.dtoRead, this.dtoWrite, this.meta.getDtoFieldName(), this.entityRead, this.entityWrite, this.meta.getEntityFieldName());
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
    		throws UnableToCreateInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, 
    		       CollectionEntityGenericReturnTypeException, AnnotationMissingException, InspectionScanningException, 
    		       InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, 
    		       AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, 
    		       NotValueConverterException, ValueConverterNotFoundException {

        final Object entityCollection = this.entityRead.read(entity);

        if (entityCollection instanceof Collection) {
            final Collection entities = (Collection) entityCollection;

            final Map dtos = this.meta.newDtoMap(dtoBeanFactory);

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                final DTOAssembler assembler = DTOAssembler.newCustomAssembler(newDto.getClass(), this.meta.getReturnType(), synthesizer);

                for (Object object : entities) {

                	final Object key = this.entityCollectionKeyRead.read(object);
                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.put(key, newDto);

                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.write(dto, dtos);

            } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
			} catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
			}

        } else if (entityCollection instanceof Map) {
        	
            final Map entities = (Map) entityCollection;

            final Map dtos = this.meta.newDtoMap(dtoBeanFactory);

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);
            
            final boolean useKey = this.meta.isEntityMapKey();

            try {
                final DTOAssembler assembler = DTOAssembler.newCustomAssembler(newDto.getClass(), this.meta.getReturnType(), synthesizer);

                for (Object key : entities.keySet()) {

                	if (useKey) {
                		final Object value = entities.get(key);
                		assembler.assembleDto(newDto, key, converters, dtoBeanFactory);
                		dtos.put(newDto, value);
                	} else {
	                	final Object object = entities.get(key);
	                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
	                    dtos.put(key, newDto);
                	}
                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.write(dto, dtos);

            } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
			} catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
			}
        	
        }
        
    }

    /** {@inheritDoc} */
    public void writeFromDtoToEntity(final Object entityObj,
                                     final Object dto,
                                     final Map<String, Object> converters,
                                     final BeanFactory entityBeanFactory) 
    	throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, UnableToCreateInstanceException, 
    	       CollectionEntityGenericReturnTypeException, AnnotationMissingException, InspectionScanningException, 
    	       InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, 
    	       AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, 
    	       InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, NotEntityRetrieverException, 
    	       EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, 
    	       AnnotationMissingBeanKeyException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

       if (this.meta.isReadOnly()) {
           return;
       }

       final Object dtoColl = this.dtoRead.read(dto);

       if (dtoColl instanceof Map) {
           // need to synch

    	   final Object entity;
           if (entityObj instanceof NewDataProxy) {
        	   entity = ((NewDataProxy) entityObj).create();
           } else {
        	   entity = entityObj;
           }
           
           final Object originalEntityColl = this.entityRead.read(entity);

    	   
           Object original = null;
           if (originalEntityColl instanceof Collection || originalEntityColl instanceof Map) {
               original = originalEntityColl;
           } else {
               original = this.meta.newEntityMapOrCollection(entityBeanFactory);
               this.entityWrite.write(entity,  original);
           }

           final Map dtos = (Map) dtoColl;

           if (original instanceof Collection) {
	           removeDeletedItems(converters, entityBeanFactory, (Collection) original, dtos);
	           addOrUpdateItems(dto, converters, entityBeanFactory, (Collection) original, dtos);
           } else if (original instanceof Map) {
	           removeDeletedItems(converters, entityBeanFactory, (Map) original, dtos);
	           addOrUpdateItems(dto, converters, entityBeanFactory, (Map) original, dtos);
           }    

       } else if (entityObj != null && !(entityObj instanceof NewDataProxy)) {
    	   final Object originalEntityColl = this.entityRead.read(entityObj);
    	   if (originalEntityColl instanceof Collection) {
	           // if there were items then clear it
	           ((Collection) originalEntityColl).clear();
	       } else if (originalEntityColl instanceof Map) {
	           // if there were items then clear it
	           ((Map) originalEntityColl).clear();
	       }
       } // else it was null anyways

    }
    
	private DTOAssembler lazyCreateAssembler(final DTOAssembler assembler, final Object dtoItem) 
			throws CollectionEntityGenericReturnTypeException, AnnotationMissingException, InspectionScanningException, 
			       UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
			       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
			       AnnotationDuplicateBindingException {
		if (assembler == null) {
		    try {
		    	if (Object.class.equals(this.meta.getReturnType())) {
		    		throw new CollectionEntityGenericReturnTypeException(
							dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
							this.meta.getReturnType().getCanonicalName());
		    	}
		        return DTOAssembler.newCustomAssembler(dtoItem.getClass(), this.meta.getReturnType(), synthesizer);
            } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
			} catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
			}  
		}
		return assembler;
	}

	private void addOrUpdateItems(final Object dto, final Map<String, Object> converters, 
			final BeanFactory entityBeanFactory, final Collection original, final Map dtos) 
		throws CollectionEntityGenericReturnTypeException, AnnotationMissingException, BeanFactoryNotFoundException, 
			   BeanFactoryUnableToCreateInstanceException, InspectionScanningException, UnableToCreateInstanceException, 
			   InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, 
			   AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, 
			   InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, NotEntityRetrieverException, 
			   EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, 
			   AnnotationMissingBeanKeyException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
		
		DTOAssembler assembler = null;
		final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
		for (Object dtoKey : dtos.keySet()) {
			
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
		
		}
	}
	
    private void addOrUpdateItems(final Object dto, final Map<String, Object> converters, 
    		final BeanFactory entityBeanFactory, final Map original, final Map dtos) 
    	throws CollectionEntityGenericReturnTypeException, AnnotationMissingException, BeanFactoryNotFoundException, 
    	       BeanFactoryUnableToCreateInstanceException, InspectionScanningException, UnableToCreateInstanceException, 
    	       InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, 
    	       AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, 
    	       InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, NotEntityRetrieverException, 
    	       EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, 
    	       AnnotationMissingBeanKeyException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

        DTOAssembler assembler = null;
        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
        final boolean useKey = this.meta.isEntityMapKey();
        for (Object dtoKey : dtos.keySet()) {

        	final Object dtoItem = dtos.get(dtoKey);
            boolean toAdd = true;
            for (Object orKey : original.keySet()) {

                if (matcher.match(dtoKey, orKey)) {
                	if (useKey) {
	                	assembler = lazyCreateAssembler(assembler, dtoKey);
	                    assembler.assembleEntity(dtoKey, orKey, converters, entityBeanFactory);
	                    original.put(orKey, dtoItem);
                	} else {
	                	final Object orItem = original.get(orKey);
	                	assembler = lazyCreateAssembler(assembler, dtoItem);
	                    assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
                	}
                    toAdd = false;
                    break;
                }

            }

            if (toAdd) {
            	if (useKey) {
            		assembler = lazyCreateAssembler(assembler, dtoKey);
	                final Object newItem = this.meta.newEntityBean(entityBeanFactory);
	                assembler.assembleEntity(dtoKey, newItem, converters, entityBeanFactory);
	                original.put(newItem, dtoItem);
            	} else {
	                assembler = lazyCreateAssembler(assembler, dtoItem);
	                final Object newItem = this.meta.newEntityBean(entityBeanFactory);
	                assembler.assembleEntity(dtoItem, newItem, converters, entityBeanFactory);
	                original.put(dtoKey, newItem);
            	}
            }

        }
    }

    private void removeDeletedItems(final Map<String, Object> converters, final BeanFactory entityBeanFactory, final Collection original, 
			final Map dtos)  throws DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
    	final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
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
    
    private void removeDeletedItems(final Map<String, Object> converters, final BeanFactory entityBeanFactory, final Map original, 
			final Map dtos)  throws DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
    	final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
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
