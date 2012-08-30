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

import java.util.Collection;
import java.util.Map;

/**
 * Generic interface for all DTO assemblers.
 *
 * @author Denis Pavlov
 * @since 2.0.2
 *
 * <p/>
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 8:30:16 AM
 */
public interface Assembler {
    
    /**
     * Assembles dto from current entity by using annotations of the dto.
     * @param dto the dto to insert data to
     * @param entity the entity to get data from
     * @param converters the converters to be used during conversion. The rationale for injecting the converters
     *        during conversion is to enforce them being stateless and unattached to assembler.
     * @param dtoBeanFactory bean factory for creating new instances of nested DTO objects mapped by
     *        {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField#dtoBeanKey()} key.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidEntityInstanceException if sub entity assembler encounters a mismatch
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidDtoInstanceException if sub entity assembler encounters a mismatch
     * @throws com.inspiresoftware.lib.dto.geda.exception.CollectionEntityGenericReturnTypeException thrown by a collection pipe if a mismatch in item types is detected
     * @throws com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException if unable to create instances of collections
     * @throws com.inspiresoftware.lib.dto.geda.exception.ValueConverterNotFoundException if converter under given key is not a valid converter
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotValueConverterException if converter under given key is not a valid converter
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingException if value converter is not found in #converters map
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException if bean factory is unable to create instance for sub DTO's
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException if bean factory is required and not specified
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    void assembleDto(Object dto, Object entity,
            Map<String, Object> converters, BeanFactory dtoBeanFactory)
        throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException,
            BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, NotValueConverterException,
               ValueConverterNotFoundException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException,
               InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
               AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
               AnnotationDuplicateBindingException;

    /**
     * Assembles dtos from current entities by using annotations of the dto.
     * @param dtos the non-null and empty dtos collection to insert data to
     * @param entities the the non-null entity collection to get data from
     * @param converters the converters to be used during conversion. The rationale for injecting the converters
     *        during conversion is to enforce them being stateless and unattached to assembler.
     * @param dtoBeanFactory bean factory for creating new instances of nested DTO objects mapped by
     *        {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField#dtoBeanKey()} key.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.InvalidDtoCollectionException dto collection is null of not empty
     * @throws com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException if unable to create dto class instance
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidEntityInstanceException if sub entity assembler encounters a mismatch
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidDtoInstanceException if sub entity assembler encounters a mismatch
     * @throws com.inspiresoftware.lib.dto.geda.exception.CollectionEntityGenericReturnTypeException thrown by a collection pipe if a mismatch in item types is detected
     * @throws com.inspiresoftware.lib.dto.geda.exception.ValueConverterNotFoundException if converter under given key is not a valid converter
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotValueConverterException if converter under given key is not a valid converter
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingException if value converter is not found in #converters map
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException if bean factory is unable to create instance for sub DTO's
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException if bean factory is required and not specified
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    void assembleDtos(Collection dtos, Collection entities,
            Map<String, Object> converters, BeanFactory dtoBeanFactory)
        throws InvalidDtoCollectionException, UnableToCreateInstanceException, InspectionInvalidDtoInstanceException,
               InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException,
               AnnotationMissingException, NotValueConverterException, ValueConverterNotFoundException,
               CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException,
               InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
               GeDARuntimeException, AnnotationDuplicateBindingException;

    /**
     * Assembles entity from current dto by using annotations of the dto.
     * @param dto the dto to get data from
     * @param entity the entity to copy data to
     * @param converters the converters to be used during conversion. Optional parameter that provides map with
     *        value converters mapped by {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField#converter()}. If no converters
     *        are required for this DTO then a <code>null</code> can be passed in. The rationale for injecting the converters
     *        during conversion is to enforce them being stateless and unattached to assembler.
     * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO by
     *        {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField#entityBeanKeys()} key.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidEntityInstanceException thrown by sub entities and collections on the fly assemblers
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidDtoInstanceException thrown by sub entities and collections on the fly assemblers
     * @throws com.inspiresoftware.lib.dto.geda.exception.CollectionEntityGenericReturnTypeException collections generic type mismatch
     * @throws com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException auto created entities exception (instantiated by GeDA directly)
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingException thrown by sub entities and collections on the fly assemblers
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBeanKeyException bean key missing on annotation when entity on the fly creation is required
     * @throws com.inspiresoftware.lib.dto.geda.exception.ValueConverterNotFoundException value converter not found
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotValueConverterException invalid value converter
     * @throws com.inspiresoftware.lib.dto.geda.exception.EntityRetrieverNotFoundException  entity retriever not found
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotEntityRetrieverException invalid entity retriever
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException exception for bean factory instantiation (usually when it returns null)
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException no bean factory supplied
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotDtoToEntityMatcherException when converter retrieved by matcher key is not valid
     * @throws com.inspiresoftware.lib.dto.geda.exception.DtoToEntityMatcherNotFoundException exception when entity matcher key configuration is used rather than a class but
     *         is not found in the converters
     */
    void assembleEntity(Object dto, Object entity,
            Map<String, Object> converters, BeanFactory entityBeanFactory)
        throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException,
               BeanFactoryUnableToCreateInstanceException, NotEntityRetrieverException, EntityRetrieverNotFoundException,
               NotValueConverterException, ValueConverterNotFoundException, AnnotationMissingBeanKeyException,
               AnnotationMissingException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException,
               InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
               AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
               AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException;

    /**
     * Assembles entities from current dtos by using annotations of the dto.
     * @param dtos the dto to get data from
     * @param entities the entity to copy data to
     * @param converters the converters to be used during conversion. Optional parameter that provides map with
     *        value converters mapped by {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField#converter()}. If no converters
     *        are required for this DTO then a <code>null</code> can be passed in. The rationale for injecting the converters
     *        during conversion is to enforce them being stateless and unattached to assembler.
     * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO by
     *        {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField#entityBeanKeys()} key.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidEntityInstanceException thrown by sub entities and collections on the fly assemblers
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionInvalidDtoInstanceException thrown by sub entities and collections on the fly assemblers
     * @throws com.inspiresoftware.lib.dto.geda.exception.CollectionEntityGenericReturnTypeException collections generic type mismatch
     * @throws com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException auto created entities exception (instantiated by GeDA directly)
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingException thrown by sub entities and collections on the fly assemblers
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBeanKeyException bean key missing on annotation when entity on the fly creation is required
     * @throws com.inspiresoftware.lib.dto.geda.exception.ValueConverterNotFoundException value converter not found
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotValueConverterException invalid value converter
     * @throws com.inspiresoftware.lib.dto.geda.exception.EntityRetrieverNotFoundException  entity retriever not found
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotEntityRetrieverException invalid entity retriever
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException exception for bean factory instantiation (usually when it returns null)
     * @throws com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException no bean factory supplied
     * @throws com.inspiresoftware.lib.dto.geda.exception.InvalidEntityCollectionException if entity collection is null or not empty
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws com.inspiresoftware.lib.dto.geda.exception.AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
     * @throws com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     * @throws com.inspiresoftware.lib.dto.geda.exception.NotDtoToEntityMatcherException when converter retrieved by matcher key is not valid
     * @throws com.inspiresoftware.lib.dto.geda.exception.DtoToEntityMatcherNotFoundException exception when entity matcher key configuration is used rather than a class but
     *         is not found in the converters
     */
    void assembleEntities(Collection dtos, Collection entities,
            Map<String, Object> converters, BeanFactory entityBeanFactory)
        throws UnableToCreateInstanceException, InvalidEntityCollectionException, InspectionInvalidDtoInstanceException,
               InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException,
               NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException,
               ValueConverterNotFoundException, AnnotationMissingBeanKeyException, AnnotationMissingException,
               CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException,
               InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
               GeDARuntimeException, AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException,
               NotDtoToEntityMatcherException;
}
