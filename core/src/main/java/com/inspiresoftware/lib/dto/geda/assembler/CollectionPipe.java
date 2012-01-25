
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.CollectionPipeMetadata;
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
    
    private final MethodSynthesizer synthesizer;

	private final DataReader dtoRead;
	private final DataWriter dtoWrite;

	private final DataReader entityRead;
	private final DataWriter entityWrite;

    /**
     * @param synthesizer synthesizer
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     * @param meta collection pipe meta
     * @throws AnnotationValidatingBindingException when pipe binding is invalid
     */
    CollectionPipe(final MethodSynthesizer synthesizer,
    			   final DataReader dtoRead,
                   final DataWriter dtoWrite,
                   final DataReader entityRead,
                   final DataWriter entityWrite,
                   final CollectionPipeMetadata meta) throws AnnotationValidatingBindingException {
    	
    	this.meta = meta;
    	
    	this.synthesizer = synthesizer;

        this.dtoWrite = dtoWrite;
        this.entityRead = entityRead;

        if (this.meta.isReadOnly()) {
			this.dtoRead = null;
			this.entityWrite = null;
            PipeValidator.validateReadPipeTypes(this.dtoWrite, this.meta.getDtoFieldName(), 
            		this.entityRead, this.meta.getEntityFieldName());
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
            PipeValidator.validatePipeTypes(this.dtoRead, this.dtoWrite, this.meta.getDtoFieldName(), 
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

            Object newDto = this.meta.newDtoBean(dtoBeanFactory);

            try {
                final DTOAssembler assembler = DTOAssembler.newCustomAssembler(newDto.getClass(), this.meta.getReturnType(), synthesizer);

                for (Object object : entities) {

                    assembler.assembleDto(newDto, object, converters, dtoBeanFactory);
                    dtos.add(newDto);

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
    	   
    	   final Object entity;
           if (entityObj instanceof NewDataProxy) {
        	   entity = ((NewDataProxy) entityObj).create();
           } else {
        	   entity = entityObj;
           }
           
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

       } else if (entityObj != null && !(entityObj instanceof NewDataProxy)) {
    	   final Object originalEntityColl = this.entityRead.read(entityObj);
    	   if (originalEntityColl instanceof Collection) {
	          // if there were items then clear it
	          ((Collection) originalEntityColl).clear();
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
		        return 
		        DTOAssembler.newCustomAssembler(dtoItem.getClass(), this.meta.getReturnType(), synthesizer);
		    } catch (InspectionInvalidEntityInstanceException invEntity) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
		    } catch (InspectionInvalidDtoInstanceException invDto) {
				throw new CollectionEntityGenericReturnTypeException(
						dtoItem.getClass().getCanonicalName(), this.meta.getDtoFieldName(), 
						this.meta.getReturnType() != null ? this.meta.getReturnType().getCanonicalName() : "unspecified");
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

        DTOAssembler assembler = null;
        final DtoToEntityMatcher matcher = this.meta.getDtoToEntityMatcher(converters);
        for (Object dtoItem : dtos) {

            boolean toAdd = true;
            for (Object orItem : original) {

                if (matcher.match(dtoItem, orItem)) {
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
