
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
import com.inspiresoftware.lib.dto.geda.assembler.meta.CollectionPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

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

    private final CollectionPipeMetadata meta;
    
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
     * @throws AnnotationValidatingBindingException when pipe binding is invalid
     */
    CollectionPipe(final AssemblerContext context,
                   final DataReader dtoRead,
                   final DataWriter dtoWrite,
                   final DataReader entityRead,
                   final DataWriter entityWrite,
                   final CollectionPipeMetadata meta) throws AnnotationValidatingBindingException {

        this.meta = meta;
    	
    	this.context = context;

        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;

        if (this.meta.isReadOnly()) {
			this.dtoRead = null;
			this.entityWrite = null;
            PipeValidator.validateReadPipeTypes(context.getDslRegistry(), this.dtoWrite, this.meta.getDtoFieldName(),
            		this.entityRead, this.meta.getEntityFieldName());
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            PipeValidator.validatePipeTypes(context.getDslRegistry(), this.dtoRead, this.dtoWrite, this.meta.getDtoFieldName(),
            		this.entityRead, this.entityWrite, this.meta.getEntityFieldName());
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
    	throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, UnableToCreateInstanceException, 
    		   CollectionEntityGenericReturnTypeException, AnnotationMissingException, InspectionScanningException, 
    		   InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, 
    		   AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, 
    		   NotValueConverterException, ValueConverterNotFoundException {

        final Object entityCollection = this.entityRead.read(entity);

        if (entityCollection instanceof Collection) {

            final Collection entities = (Collection) entityCollection;

            final Collection dtos = this.meta.newDtoCollection(dtoBeanFactory);

            final Class entityRepresentative = this.meta.getReturnType(dtoBeanFactory);

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                Assembler assembler = null;

                for (Object object : entities) {

                    assembler = lazyCreateAssembler(assembler, newDto, object, dtoBeanFactory);
                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.add(newDto);

                    newDto = this.meta.newDtoBean(dtoBeanFactory);
                }

                this.dtoWrite.write(dto, dtos);

            } catch (BeanFactoryUnableToLocateRepresentationException bfulr) {
                throw new CollectionEntityGenericReturnTypeException(
                        newDto.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        "Not found for key provided");
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
    		   CollectionEntityGenericReturnTypeException, AnnotationMissingException, InspectionInvalidDtoInstanceException, 
    		   InspectionInvalidEntityInstanceException, NotEntityRetrieverException, EntityRetrieverNotFoundException, 
    		   NotValueConverterException, ValueConverterNotFoundException, AnnotationMissingBeanKeyException, 
    		   InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
    		   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
    		   AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

       if (this.meta.isReadOnly()) {
           return;
       }

       final Object dtoColl = this.dtoRead.read(dto);

       if (dtoColl instanceof Collection) {
           // need to synch
    	   
    	   final Object entity = entityObj;

           final Object originalEntityColl = this.entityRead.read(entity);

           Collection original = null;
           if (originalEntityColl instanceof Collection) {
               original = (Collection) originalEntityColl;
           } else {
               original = this.meta.newEntityCollection(entityBeanFactory);
               this.entityWrite.write(entity,  original);
           }

           final Collection dtos = (Collection) dtoColl;

           removeDeletedItems(original, dtos, converters, entityBeanFactory);

           addOrUpdateItems(dto, converters, entityBeanFactory, original, dtos);

       } else if (entityObj != null) {
    	   final Object originalEntityColl = this.entityRead.read(entityObj);
    	   if (originalEntityColl instanceof Collection) {
	          // if there were items then clear it
	          ((Collection) originalEntityColl).clear();
    	   }
       } // else it was null anyways

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
		    } catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        representative != null ? representative.getCanonicalName() : "unspecified");
		    } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(),
                        representative != null ? representative.getCanonicalName() : "unspecified");
		    }   
		}
		return assembler;
	}

    private void addOrUpdateItems(final Object dto, final Map<String, Object> converters, 
    		final BeanFactory entityBeanFactory, final Collection original, final Collection dtos) 
    	throws CollectionEntityGenericReturnTypeException, AnnotationMissingException, BeanFactoryNotFoundException, 
    	       BeanFactoryUnableToCreateInstanceException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
    	       NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException, 
    	       ValueConverterNotFoundException, AnnotationMissingBeanKeyException, UnableToCreateInstanceException, 
    	       InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
    	       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
    	       AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

        Assembler assembler = null;
        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
        for (Object dtoItem : dtos) {

            boolean toAdd = true;
            for (Object orItem : original) {

                if (matcher.match(dtoItem, orItem)) {
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

    private void removeDeletedItems(final Collection original, final Collection dtos, final Map<String, Object> converters, 
    			final BeanFactory entityBeanFactory) throws DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
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
