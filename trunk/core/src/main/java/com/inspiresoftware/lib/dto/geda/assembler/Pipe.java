
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
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Map;


/**
 * A pipe is an object that allows to pipe data between Dto and Entity field.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
interface Pipe {
	
	/**
	 * @return full binding for this pipe.
	 */
	String getBinding();

	/**
	 * write data from entity field to dto field.
	 * 
	 * @param entity the entity to read data from.
	 * @param dto the dto to write data to.
	 * @param converters the converters to be used during conversion.
     * @param dtoBeanFactory bean factory for creating new instances of nested DTO's.
     * 
	 * @throws AnnotationMissingException missing {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} in sub DTO's
	 * @throws BeanFactoryUnableToCreateInstanceException if bean factory is unable to create instance for sub DTO's
	 * @throws BeanFactoryNotFoundException  if bean factory is required and not specified
	 * @throws ValueConverterNotFoundException if value converter is not found in #converters map
	 * @throws NotValueConverterException  if converter under given key is not a valid converter
	 * @throws UnableToCreateInstanceException if unable to create instances of collections
	 * @throws CollectionEntityGenericReturnTypeException thrown by a collection pipe if a mismatch in item types is detected
	 * @throws InspectionInvalidEntityInstanceException if sub entity assembler encounters a mismatch
	 * @throws InspectionInvalidDtoInstanceException if sub entity assembler encounters a mismatch
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
	 * @throws InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, Object> converters,
            final BeanFactory dtoBeanFactory)
		throws BeanFactoryNotFoundException, 
			   BeanFactoryUnableToCreateInstanceException, 
			   AnnotationMissingException, 
			   NotValueConverterException, 
			   ValueConverterNotFoundException, 
			   UnableToCreateInstanceException, 
			   CollectionEntityGenericReturnTypeException, 
			   InspectionInvalidDtoInstanceException, 
			   InspectionInvalidEntityInstanceException, 
			   InspectionScanningException, 
			   InspectionPropertyNotFoundException, 
			   InspectionBindingNotFoundException, 
			   AnnotationMissingBindingException, 
			   AnnotationValidatingBindingException, 
			   GeDARuntimeException, 
			   AnnotationDuplicateBindingException;

	/**
	 * write data from dto field to entity field.
	 * @param entity the entity to write data to.
	 * @param dto the dto to read data from.
	 * @param converters the converters to be used during conversion.
	 * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO.
	 * 
	 * @throws EntityRetrieverNotFoundException when entity retriever is not found in #converters map 
	 * @throws NotEntityRetrieverException when retriever in #converters is not valid
	 * @throws BeanFactoryUnableToCreateInstanceException when bean factory unable to create instance of sub entity
	 * @throws BeanFactoryNotFoundException when bean factory is not provided 
	 * @throws ValueConverterNotFoundException if value converter is not found in #converters map
	 * @throws NotValueConverterException  if converter under given key is not a valid converter
	 * @throws AnnotationMissingException when sub DTO is missing {@link com.inspiresoftware.lib.dto.geda.annotations.Dto}
	 * @throws AnnotationMissingBeanKeyException when DTO does not specify correct key for Entity instances
	 * @throws UnableToCreateInstanceException if unable to create instances of collections
	 * @throws CollectionEntityGenericReturnTypeException thrown by collection pipe on mismatch of items of collection
	 * @throws InspectionInvalidEntityInstanceException if sub entity assembler encounters a mismatch
	 * @throws InspectionInvalidDtoInstanceException if sub entity assembler encounters a mismatch
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
	 * @throws InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 * @throws NotDtoToEntityMatcherException when converter retrieved by matcher key is not valid
	 * @throws DtoToEntityMatcherNotFoundException exception when entity matcher key configuration is used rather than a class but 
	 *         is not found in the converters

	 */
	void writeFromDtoToEntity(final Object entity, final Object dto,
			final Map<String, Object> converters,
			final BeanFactory entityBeanFactory)
		throws BeanFactoryNotFoundException, 
			   BeanFactoryUnableToCreateInstanceException, 
			   NotEntityRetrieverException, 
			   EntityRetrieverNotFoundException, 
			   NotValueConverterException, 
			   ValueConverterNotFoundException, 
			   AnnotationMissingBeanKeyException, 
			   AnnotationMissingException, 
			   UnableToCreateInstanceException, 
			   CollectionEntityGenericReturnTypeException, 
			   InspectionInvalidDtoInstanceException, 
			   InspectionInvalidEntityInstanceException, 
			   InspectionScanningException, 
			   InspectionPropertyNotFoundException, 
			   InspectionBindingNotFoundException, 
			   AnnotationMissingBindingException, 
			   AnnotationValidatingBindingException, 
			   GeDARuntimeException, 
			   AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException;

}