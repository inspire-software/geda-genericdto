
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.meta.MapPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;
import java.util.*;


/**
 * Object that handles read and write streams between Dto and Entity objects.
 *
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 12:10:02 PM
 */
@SuppressWarnings("unchecked")
class MapPipe implements Pipe {

    private final MapPipeMetadata meta;
    
    private final AssemblerContext context;

    private final DataReader dtoRead;
	private final DataWriter dtoWrite;

	private final DataReader entityRead;
	private final DataWriter entityWrite;

    /**
     * @param context assembler context
     * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writing data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writing data to Entity field
     * @param meta collection pipe meta
     *
     * @throws AnnotationValidatingBindingException when miss-mapped binding
     */
    MapPipe(final AssemblerContext context,
            final DataReader dtoRead,
            final DataWriter dtoWrite,
            final DataReader entityRead,
            final DataWriter entityWrite,
            final MapPipeMetadata meta) throws AnnotationValidatingBindingException {

        this.meta = meta;

    	this.context = context;

        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;

        if (this.meta.isReadOnly()) {
			this.dtoRead = null;
			this.entityWrite = null;
            PipeValidator.validateReadPipeTypes(
                    context.getDslRegistry(), this.dtoWrite, this.meta.getDtoFieldName(), this.entityRead, this.meta.getEntityFieldName());
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            PipeValidator.validatePipeTypes(
                    context.getDslRegistry(), this.dtoRead, this.dtoWrite, this.meta.getDtoFieldName(), this.entityRead, this.entityWrite, this.meta.getEntityFieldName());
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

            final Class entityRepresentative = this.meta.getReturnType(dtoBeanFactory);

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                Assembler assembler = null;
                DataReader keyRead = null;

                for (Object object : entities) {

                    assembler = lazyCreateAssembler(assembler, newDto, object, dtoBeanFactory);
                    keyRead = lazyCollectionKeyRead(keyRead, newDto, object, dtoBeanFactory);
                	final Object key = keyRead.read(object);
                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.put(key, newDto);

                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.write(dto, dtos);

            } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        entityRepresentative != null ? entityRepresentative.getCanonicalName() : "unspecified");
			} catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        entityRepresentative != null ? entityRepresentative.getCanonicalName() : "unspecified");
			}

        } else if (entityCollection instanceof Map) {
        	
            final Map entities = (Map) entityCollection;

            final Map dtos = this.meta.newDtoMap(dtoBeanFactory);

            final Class entityRepresentative = this.meta.getReturnType(dtoBeanFactory);

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);
            
            final boolean useKey = this.meta.isEntityMapKey();

            try {
                Assembler assembler = null;

                for (Map.Entry entry : (Set<Map.Entry>) entities.entrySet()) {

                	if (useKey) {
                		final Object value = entry.getValue();
                        assembler = lazyCreateAssembler(assembler, newDto, entry.getKey(), dtoBeanFactory);
                		assembler.assembleDto(newDto, entry.getKey(), converters, dtoBeanFactory);
                		dtos.put(newDto, value);
                	} else {
	                	final Object object = entry.getValue();
                        assembler = lazyCreateAssembler(assembler, newDto, object, dtoBeanFactory);
	                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
	                    dtos.put(entry.getKey(), newDto);
                	}
                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.write(dto, dtos);

            } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        entityRepresentative != null ? entityRepresentative.getCanonicalName() : "unspecified");
			} catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        entityRepresentative != null ? entityRepresentative.getCanonicalName() : "unspecified");
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

    	   final Object entity = entityObj;

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

       } else if (entityObj != null) {
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

    private DataReader lazyCollectionKeyRead(DataReader reader, final Object dtoItem, final Object entityItem, final BeanFactory beanFactory) {
        if (reader == null) {
            if (meta.getMapKeyForCollection() != null && meta.getMapKeyForCollection().length() > 0) {

                Class representative = this.meta.getReturnType(beanFactory);
                if (Object.class.equals(representative) && entityItem != null) {
                    representative = entityItem.getClass();
                }
                final PropertyDescriptor[] itemDesc = PropertyInspector.getPropertyDescriptorsForClass(representative);
                final PropertyDescriptor itemKeyDesc = PropertyInspector.getEntityPropertyDescriptorForField(
                        dtoItem.getClass(), representative, meta.getDtoFieldName(), meta.getMapKeyForCollection(), itemDesc);
                reader = context.getMethodSynthesizer().synthesizeReader(itemKeyDesc);
            } else {
                throw new AnnotationValidatingBindingException(
                        meta.getDtoFieldName(),
                        dtoWrite.getClass().getCanonicalName(),
                        dtoWrite.getParameterType().getSimpleName(),
                        meta.getEntityFieldName(),
                        "", "", false);
            }
        }
        return reader;
    }
    
	private Assembler lazyCreateAssembler(final Assembler assembler, final Object dtoItem, final Object entityItem, final BeanFactory beanFactory)
			throws CollectionEntityGenericReturnTypeException, AnnotationMissingException, InspectionScanningException, 
			       UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
			       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
			       AnnotationDuplicateBindingException {
		if (assembler == null) {

            Class representative = this.meta.getReturnType(beanFactory);
            if (Object.class.equals(representative) && entityItem != null) {
                representative = entityItem.getClass();
            }

            try {
		    	if (Object.class.equals(representative)) {
		    		throw new CollectionEntityGenericReturnTypeException(
							dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                            representative.getCanonicalName());
		    	}
                return context.newAssembler(dtoItem.getClass(), representative);
            } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        representative != null ? representative.getCanonicalName() : "unspecified");
			} catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        representative != null ? representative.getCanonicalName() : "unspecified");
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
		
		Assembler assembler = null;
		final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
		for (Object dtoKey : dtos.keySet()) {
			
			final Object dtoItem = dtos.get(dtoKey);

			boolean toAdd = true;
			for (Object orItem : original) {
				
				if (matcher.match(dtoKey, orItem)) {
					assembler = lazyCreateAssembler(assembler, dtoItem, orItem, entityBeanFactory);
					assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
					toAdd = false;
					break;
				}
				
			}
			
			if (toAdd) {
                final Object newItem = this.meta.newEntityBean(entityBeanFactory);
                assembler = lazyCreateAssembler(assembler, dtoItem, newItem, entityBeanFactory);
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

        Assembler assembler = null;
        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
        final boolean useKey = this.meta.isEntityMapKey();
        for (Object dtoKey : dtos.keySet()) {

        	final Object dtoItem = dtos.get(dtoKey);
            boolean toAdd = true;
            for (Map.Entry orEntry : (Set<Map.Entry>) original.entrySet()) {

                if (matcher.match(dtoKey, orEntry.getKey())) {
                	if (useKey) {
	                	assembler = lazyCreateAssembler(assembler, dtoKey, orEntry.getKey(), entityBeanFactory);
	                    assembler.assembleEntity(dtoKey, orEntry.getKey(), converters, entityBeanFactory);
	                    original.put(orEntry.getKey(), dtoItem);
                	} else {
	                	final Object orItem = orEntry.getValue();
	                	assembler = lazyCreateAssembler(assembler, dtoItem, orItem, entityBeanFactory);
	                    assembler.assembleEntity(dtoItem, orItem, converters, entityBeanFactory);
                	}
                    toAdd = false;
                    break;
                }

            }

            if (toAdd) {
            	if (useKey) {
                    final Object newItem = this.meta.newEntityBean(entityBeanFactory);
                    assembler = lazyCreateAssembler(assembler, dtoKey, newItem, entityBeanFactory);
	                assembler.assembleEntity(dtoKey, newItem, converters, entityBeanFactory);
	                original.put(newItem, dtoItem);
            	} else {
                    final Object newItem = this.meta.newEntityBean(entityBeanFactory);
                    assembler = lazyCreateAssembler(assembler, dtoItem, newItem, entityBeanFactory);
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
