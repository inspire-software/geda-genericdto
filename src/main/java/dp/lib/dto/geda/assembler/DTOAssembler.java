
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.assembler.extension.Cache;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.assembler.extension.impl.SoftReferenceCache;
import dp.lib.dto.geda.assembler.meta.CollectionPipeMetadata;
import dp.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import dp.lib.dto.geda.assembler.meta.PipeMetadata;
import dp.lib.dto.geda.exception.AnnotationDuplicateBindingException;
import dp.lib.dto.geda.exception.AnnotationMissingAutobindingException;
import dp.lib.dto.geda.exception.AnnotationMissingBeanKeyException;
import dp.lib.dto.geda.exception.AnnotationMissingBindingException;
import dp.lib.dto.geda.exception.AnnotationMissingException;
import dp.lib.dto.geda.exception.AnnotationValidatingBindingException;
import dp.lib.dto.geda.exception.AutobindingClassNotFoundException;
import dp.lib.dto.geda.exception.BeanFactoryNotFoundException;
import dp.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException;
import dp.lib.dto.geda.exception.CollectionEntityGenericReturnTypeException;
import dp.lib.dto.geda.exception.DtoToEntityMatcherNotFoundException;
import dp.lib.dto.geda.exception.EntityRetrieverNotFoundException;
import dp.lib.dto.geda.exception.GeDAException;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionBindingNotFoundException;
import dp.lib.dto.geda.exception.InspectionInvalidDtoInstanceException;
import dp.lib.dto.geda.exception.InspectionInvalidEntityInstanceException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.InspectionScanningException;
import dp.lib.dto.geda.exception.InvalidDtoCollectionException;
import dp.lib.dto.geda.exception.InvalidEntityCollectionException;
import dp.lib.dto.geda.exception.NotDtoToEntityMatcherException;
import dp.lib.dto.geda.exception.NotEntityRetrieverException;
import dp.lib.dto.geda.exception.NotValueConverterException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;
import dp.lib.dto.geda.exception.ValueConverterNotFoundException;

/**
 * Assemble DTO and Entities depending on the annotations of Dto.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
public final class DTOAssembler {
	
	/**
	 * int number that allows to define the number of DTOAssembler.CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 50 (i.e. after 50 put's the clean up is launched).
	 */
	public static final String SETTING_ASSEMBLER_CACHE_CLEANUP_CYCLE = 
		"dp.lib.dto.geda.assembler.DTOAssembler.ASSEMBLER_CACHE_CLEANUP_CYCLE";
	/**
	 * int number that allows to define the number of JavassitMethodSynthesizer.READER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE = 
		"dp.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE";
	
	/**
	 * int number that allows to define the number of JavassitMethodSynthesizer.WRITER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE = 
		"dp.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE";

	/**
	 * String that defines key for synthesizer implementation to use.
	 */
	public static final String SETTING_SYNTHESIZER_IMPL = 
		"dp.lib.dto.geda.assembler.DTOAssembler.SETTING_SYNTHESIZER_IMPL";
	
	
	private static final Cache<String, DTOAssembler> CACHE = new SoftReferenceCache<String, DTOAssembler>(50);	
	
	/**
	 * Setup allows to configure some of the behaviour of GeDA. Currently it is used to tune the caching cleanup cycles.
	 * There are two caches used in GeDA:
	 * <ul>
	 * <li>DTOAssembler cache - that caches the assemblers instances</li>
	 * <li>Dynamic classes cache - that caches the instances of {@link dp.lib.dto.geda.assembler.extension.DataReader}s 
	 * and {@link dp.lib.dto.geda.assembler.extension.DataWriter}s</li>
	 * </ul>
	 * 
	 * @param props properties with key specified by DTOAssembler.SETTINGS_* keys
	 * 
	 * @throws NumberFormatException if the number of cycles specified in properties cannot be converted to int.
	 * @throws GeDAException if any of the configurations cause exception
	 */
	public static void setup(final Properties props) throws NumberFormatException, GeDAException {
		final String assemblerCache = props.getProperty(SETTING_ASSEMBLER_CACHE_CLEANUP_CYCLE);
		final String javassistReaderCache = props.getProperty(SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE);
		final String javassistWriterCache = props.getProperty(SETTING_DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE);
		final String synthesizerImpl = props.getProperty(SETTING_SYNTHESIZER_IMPL);
		if (assemblerCache != null) {
			CACHE.configure("cleanUpCycle", Integer.valueOf(assemblerCache));
		}
		if (javassistReaderCache != null) {
			SYNTHESIZER.configure("readerCleanUpCycle", Integer.valueOf(javassistReaderCache));
		}
		if (javassistWriterCache != null) {
			SYNTHESIZER.configure("writerCleanUpCycle", Integer.valueOf(javassistWriterCache));
		}
		if (synthesizerImpl != null) {
			SYNTHESIZER.configure("synthesizerImpl", synthesizerImpl);
		}
	}
	
	private final Class dtoClass;
	private final Class entityClass;
	private final MethodSynthesizer synthesizer;
	
	private final Map<String, Pipe> relationMapping = new HashMap<String, Pipe>();
	
	private static final MethodSynthesizer SYNTHESIZER = new MethodSynthesizerProxy();
	
	private DTOAssembler(final Class dto, final Class entity, final MethodSynthesizer synthesizer) 
		throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
		       GeDARuntimeException, AnnotationDuplicateBindingException {
		dtoClass = dto;
		entityClass = entity;
		this.synthesizer = synthesizer;
		
		Class dtoMap = dto;
		while (dtoMap != null) { // when we reach Object.class this should be null
			
			mapRelationMapping(dtoMap, entity);
			Object supType = dtoMap.getGenericSuperclass();
			if (supType instanceof ParameterizedType) {
				dtoMap = (Class) ((ParameterizedType) supType).getRawType();
			} else {
				dtoMap = (Class) supType;
			}
			
		}
		
	}

	private void mapRelationMapping(final Class dto, final Class entity) 
		throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
		       GeDARuntimeException, AnnotationDuplicateBindingException {		

		final PropertyDescriptor[] dtoPropertyDescriptors = 
			PropertyInspector.getPropertyDescriptorsForClass(dto);
		final PropertyDescriptor[] entityPropertyDescriptors = 
			PropertyInspector.getPropertyDescriptorsForClass(entity);

		
		final Field[] dtoFields = dto.getDeclaredFields();
		for (Field dtoField : dtoFields) {
			
			final List<PipeMetadata> metas = MetadataChainBuilder.build(dtoField);
			if (metas == null || metas.isEmpty()) {
				continue;
			}
			final Pipe pipe = createPipeChain(dtoClass, dtoPropertyDescriptors, entityClass, entityPropertyDescriptors, dtoField, metas, 0);
			final String binding = pipe.getBinding();
			validateNewBinding(binding);
			relationMapping.put(binding, pipe);
		}
	}
	
	private Pipe createPipeChain(
			final Class dto, final PropertyDescriptor[] dtoPropertyDescriptors, 
			final Class entity, final PropertyDescriptor[] entityPropertyDescriptors,
			final Field dtoField, final List<PipeMetadata> metas, final int index) 
		throws InspectionPropertyNotFoundException, InspectionBindingNotFoundException, InspectionScanningException, 
			   UnableToCreateInstanceException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException {
		
		final PipeMetadata meta = metas.get(index);
		
		if (index + 1 == metas.size()) {
			if (meta instanceof FieldPipeMetadata) {
				if ("#this#".equals(meta.getEntityFieldName())) {
					// create virtual field pipe
					return DataVirtualPipeBuilder.build(this.synthesizer, 
							dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (FieldPipeMetadata) meta);
				} else {
					// create field pipe
					return DataPipeBuilder.build(this.synthesizer, 
							dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (FieldPipeMetadata) meta);
				}
			} else if (meta instanceof CollectionPipeMetadata) {
				// create collection
				return CollectionPipeBuilder.build(this.synthesizer, 
						dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (CollectionPipeMetadata) meta);
			} else if (meta instanceof MapPipeMetadata) {
				// create map
				return MapPipeBuilder.build(this.synthesizer, 
						dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (MapPipeMetadata) meta);
			} else {
				throw new GeDARuntimeException("Unknown pipe meta: " + meta.getClass());
			}
		}
		
		final PropertyDescriptor nested = PropertyInspector.getEntityPropertyDescriptorForField(
				dto, entity, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		final PropertyDescriptor[] nestedEntityPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClassReturnedByGet(nested);
		
		// build a chain pipe
		return DataPipeChainBuilder.build(this.synthesizer, dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, meta, 
				createPipeChain(dto, dtoPropertyDescriptors, entity, nestedEntityPropertyDescriptors, dtoField, metas, index + 1)	
			);
		
	}

    private void validateNewBinding(final String binding) throws AnnotationDuplicateBindingException {
        if (relationMapping.containsKey(binding)) {
            throw new AnnotationDuplicateBindingException(dtoClass.getCanonicalName(), binding);
        }
    }
    
	private static DTOAssembler createNewAssembler(final Class< ? > dto, final Class< ? > entity, final Object synthesizer) 
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			       GeDARuntimeException, AnnotationDuplicateBindingException {
		
		final MethodSynthesizer syn = synthesizer == null ? SYNTHESIZER : new MethodSynthesizerProxy(synthesizer);
		final String key = createAssemberKey(dto, entity, syn);
    	
		DTOAssembler assembler = CACHE.get(key);
		if (assembler == null) {
			assembler = new DTOAssembler(dto, entity, syn);
	    	CACHE.put(key, assembler);
		}
    	return assembler;
	}

	private static Dto getDtoAnnotation(final Class< ? > dto)
			throws AnnotationMissingAutobindingException {
		final Dto ann = dto.getAnnotation(Dto.class);
		if (ann == null || ann.value() == null || ann.value().length() == 0) {
			throw new AnnotationMissingAutobindingException(dto.getCanonicalName());
		}
		return ann;
	}

	private static Class detectAutobinding(final Class< ? > dto, final Dto ann)
			throws AutobindingClassNotFoundException {
		try {
			return Class.forName(ann.value());
		} catch (ClassNotFoundException cnfe) {
			throw new AutobindingClassNotFoundException(dto.getCanonicalName(), ann.value());
		}
	}

	private static <DTO, Entity> String createAssemberKey(final Class<DTO> dto,
			final Class<Entity> entity, final MethodSynthesizer synthesizer) {
		return dto.getCanonicalName() + "-" + entity.getCanonicalName() + "-" + synthesizer.toString();
	}
	

	/**
	 * Configure synthesizer.
	 * 
	 * @param configuration configuration name
	 * @param value value to set
	 * @return true if configuration was set, false if not set or invalid
	 * @throws GeDAException in case there are errors
	 */
	public boolean configureSynthesizer(final String configuration, final Object value) throws GeDAException {
		return this.synthesizer.configure(configuration, value);
	}

	
	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entity the entity class or interface that has appropriate getters and setters
     * @param synthesizer custom method synthesizer to use (see {@link dp.lib.dto.geda.assembler.MethodSynthesizerProxy} )
	 * @return assembler instance for this conversion.
	 * 
	 * @throws AnnotationMissingException if dto class is missing the {@link dp.lib.dto.geda.annotations.Dto} annotation.
	 * @throws InspectionInvalidDtoInstanceException if dto instance used for read/write operation is not valid
	 * @throws InspectionInvalidEntityInstanceException if entity instance used for read/write operation is not valid
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
	 * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	public static DTOAssembler newCustomAssembler(
			final Class< ? > dto, final Class< ? > entity, final Object synthesizer) 
		throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
			   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			   GeDARuntimeException, AnnotationDuplicateBindingException {
		
		if (dto.getAnnotation(Dto.class) == null) {
			throw new AnnotationMissingException(dto.getName());
		}
		
		return createNewAssembler(dto, entity, synthesizer);
	}
	
    /**
     * @param dto Dto concrete class that is annotated.
     * @param entity the entity class or interface that has appropriate getters and setters
     * @return assembler instance for this conversion.
     * 
     * @throws AnnotationMissingException if dto class is missing the {@link dp.lib.dto.geda.annotations.Dto} annotation.
     * @throws InspectionInvalidDtoInstanceException if dto instance used for read/write operation is not valid
     * @throws InspectionInvalidEntityInstanceException if entity instance used for read/write operation is not valid
     * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    public static DTOAssembler newAssembler(
    		final Class< ? > dto, final Class< ? > entity) 
    	throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
    	       InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
    	       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
    	       GeDARuntimeException, AnnotationDuplicateBindingException {
    	
    	if (dto.getAnnotation(Dto.class) == null) {
    		throw new AnnotationMissingException(dto.getName());
    	}
    	
    	return createNewAssembler(dto, entity, null);
    }
    
    /**
     * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
     * @param synthesizer custom method synthesizer to use (see {@link dp.lib.dto.geda.assembler.MethodSynthesizerProxy} )
     * @return assembler instance for this conversion.
     * 
     * @throws AnnotationMissingAutobindingException if autobinding parameter is missing
     * @throws AutobindingClassNotFoundException if class specified for auto binding cannot be found
     * @throws AnnotationDuplicateBindingException if same field name is mapped more than once
     * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    public static DTOAssembler newCustomAssembler(
    		final Class< ? > dto, final Object synthesizer) 
	    throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException, 
			   UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
			   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
			   AnnotationDuplicateBindingException  {
    	
		return createNewAssembler(dto, detectAutobinding(dto, getDtoAnnotation(dto)), synthesizer);

    }
    	
	/**
	 * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
	 * @return assembler instance for this conversion.
	 * 
	 * @throws AnnotationMissingAutobindingException if autobinding parameter is missing
	 * @throws AutobindingClassNotFoundException if class specified for auto binding cannot be found
	 * @throws AnnotationDuplicateBindingException if same field name is mapped more than once
	 * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	public static DTOAssembler newAssembler(
			final Class< ? > dto) 
		throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException, 
		       UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
		       AnnotationDuplicateBindingException  {
		
		return createNewAssembler(dto, detectAutobinding(dto, getDtoAnnotation(dto)), null);
	}
	
	/**
	 * Assembles dto from current entity by using annotations of the dto.
	 * @param dto the dto to insert data to
	 * @param entity the entity to get data from
	 * @param converters the converters to be used during conversion. The rationale for injecting the converters
	 *        during conversion is to enforce them being stateless and unattached to assembler.
	 * @param dtoBeanFactory bean factory for creating new instances of nested DTO objects mapped by
	 *        {@link dp.lib.dto.geda.annotations.DtoField#dtoBeanKeys()} key.
	 *        
	 * @throws InspectionInvalidEntityInstanceException if sub entity assembler encounters a mismatch 
	 * @throws InspectionInvalidDtoInstanceException if sub entity assembler encounters a mismatch
	 * @throws CollectionEntityGenericReturnTypeException thrown by a collection pipe if a mismatch in item types is detected
	 * @throws UnableToCreateInstanceException if unable to create instances of collections
	 * @throws ValueConverterNotFoundException if converter under given key is not a valid converter
	 * @throws NotValueConverterException if converter under given key is not a valid converter
	 * @throws AnnotationMissingException if value converter is not found in #converters map
	 * @throws BeanFactoryUnableToCreateInstanceException if bean factory is unable to create instance for sub DTO's
	 * @throws BeanFactoryNotFoundException if bean factory is required and not specified
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
	 * @throws InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	public void assembleDto(final Object dto, final Object entity,
			final Map<String, Object> converters,
			final BeanFactory dtoBeanFactory) 
		throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, 
		       BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, NotValueConverterException, 
		       ValueConverterNotFoundException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException, 
		       InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
		       AnnotationDuplicateBindingException {
		
		validateDtoAndEntity(dto, entity);
		
		for (Pipe pipe : relationMapping.values()) {
			pipe.writeFromEntityToDto(entity, dto, converters, dtoBeanFactory); 
		}
		
	}
	
	/**
	 * Assembles dtos from current entities by using annotations of the dto.
	 * @param dtos the non-null and empty dtos collection to insert data to
	 * @param entities the the non-null entity collection to get data from
	 * @param converters the converters to be used during conversion. The rationale for injecting the converters
	 *        during conversion is to enforce them being stateless and unattached to assembler.
	 * @param dtoBeanFactory bean factory for creating new instances of nested DTO objects mapped by
	 *        {@link dp.lib.dto.geda.annotations.DtoField#dtoBeanKeys()} key.
	 *        
	 * @throws InvalidDtoCollectionException dto collection is null of not empty
	 * @throws UnableToCreateInstanceException if unable to create dto class instance
	 * @throws InspectionInvalidEntityInstanceException if sub entity assembler encounters a mismatch 
	 * @throws InspectionInvalidDtoInstanceException if sub entity assembler encounters a mismatch
	 * @throws CollectionEntityGenericReturnTypeException thrown by a collection pipe if a mismatch in item types is detected
	 * @throws ValueConverterNotFoundException if converter under given key is not a valid converter
	 * @throws NotValueConverterException if converter under given key is not a valid converter
	 * @throws AnnotationMissingException if value converter is not found in #converters map
	 * @throws BeanFactoryUnableToCreateInstanceException if bean factory is unable to create instance for sub DTO's
	 * @throws BeanFactoryNotFoundException if bean factory is required and not specified
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is specified to bind to
	 * @throws InspectionPropertyNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors 
	 */
	public void assembleDtos(final Collection dtos, final Collection entities,
                            final Map<String, Object> converters,
                            final BeanFactory dtoBeanFactory) 
		throws InvalidDtoCollectionException, UnableToCreateInstanceException, InspectionInvalidDtoInstanceException, 
		       InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, 
		       AnnotationMissingException, NotValueConverterException, ValueConverterNotFoundException, 
		       CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException, 
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
		       GeDARuntimeException, AnnotationDuplicateBindingException {
		
		if (dtos instanceof Collection && dtos.isEmpty() && entities instanceof Collection) {
		
			for (Object entity : entities) {
				try {
					final Object dto = this.dtoClass.newInstance();
					assembleDto(dto, entity, converters, dtoBeanFactory);
					dtos.add(dto);
				} catch (InstantiationException exp) {
					throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(), 
							"Unable to create dto instance for: " + this.dtoClass.getName(), exp);
				} catch (IllegalAccessException exp) {
					throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(), 
							"Unable to create dto instance for: " + this.dtoClass.getName(), exp);
				}
			}
			
		} else {
			throw new InvalidDtoCollectionException();
		}
		
	}

	/**
	 * Assembles entity from current dto by unsing annotations of the dto.
	 * @param dto the dto to get data from
	 * @param entity the entity to copy data to
	 * @param converters the converters to be used during conversion. Optional parameter that provides map with 
	 *        value converters mapped by {@link dp.lib.dto.geda.annotations.DtoField#converter()}. If no converters
	 *        are required for this DTO then a <code>null</code> can be passed in. The rationale for injecting the converters
	 *        during conversion is to enforce them being stateless and unattached to assembler.
	 * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO by
	 *        {@link dp.lib.dto.geda.annotations.DtoField#entityBeanKeys()} key.
	 *        
	 * @throws InspectionInvalidEntityInstanceException thrown by sub entities and collections on the fly assemblers
	 * @throws InspectionInvalidDtoInstanceException thrown by sub entities and collections on the fly assemblers
	 * @throws CollectionEntityGenericReturnTypeException collections generic type mismatch 
	 * @throws UnableToCreateInstanceException auto created entities exception (instantiated by GeDA directly)
	 * @throws AnnotationMissingException thrown by sub entities and collections on the fly assemblers
	 * @throws AnnotationMissingBeanKeyException bean key missing on annotation when entity on the fly creation is required
	 * @throws ValueConverterNotFoundException value converter not found
	 * @throws NotValueConverterException invalid value converter
	 * @throws EntityRetrieverNotFoundException  entity retriever not found
	 * @throws NotEntityRetrieverException invalid entity retriever
	 * @throws BeanFactoryUnableToCreateInstanceException exception for bean factory instantiation (usually when it returns null)
	 * @throws BeanFactoryNotFoundException no bean factory supplied
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
	public void assembleEntity(final Object dto, final Object entity,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory) 
		throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, 
		       BeanFactoryUnableToCreateInstanceException, NotEntityRetrieverException, EntityRetrieverNotFoundException, 
		       NotValueConverterException, ValueConverterNotFoundException, AnnotationMissingBeanKeyException, 
		       AnnotationMissingException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException, 
		       InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
		       AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
		
		validateDtoAndEntity(dto, entity);
		
		for (Pipe pipe : relationMapping.values()) {
			pipe.writeFromDtoToEntity(entity, dto, converters, entityBeanFactory);
		}
		
	}
	
	/**
	 * Assembles entities from current dtos by unsing annotations of the dto.
	 * @param dtos the dto to get data from
	 * @param entities the entity to copy data to
	 * @param converters the converters to be used during conversion. Optional parameter that provides map with 
	 *        value converters mapped by {@link dp.lib.dto.geda.annotations.DtoField#converter()}. If no converters
	 *        are required for this DTO then a <code>null</code> can be passed in. The rationale for injecting the converters
	 *        during conversion is to enforce them being stateless and unattached to assembler.
	 * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO by
	 *        {@link dp.lib.dto.geda.annotations.DtoField#entityBeanKeys()} key.
	 *        
	 * @throws InspectionInvalidEntityInstanceException thrown by sub entities and collections on the fly assemblers
	 * @throws InspectionInvalidDtoInstanceException thrown by sub entities and collections on the fly assemblers
	 * @throws CollectionEntityGenericReturnTypeException collections generic type mismatch 
	 * @throws UnableToCreateInstanceException auto created entities exception (instantiated by GeDA directly)
	 * @throws AnnotationMissingException thrown by sub entities and collections on the fly assemblers
	 * @throws AnnotationMissingBeanKeyException bean key missing on annotation when entity on the fly creation is required
	 * @throws ValueConverterNotFoundException value converter not found
	 * @throws NotValueConverterException invalid value converter
	 * @throws EntityRetrieverNotFoundException  entity retriever not found
	 * @throws NotEntityRetrieverException invalid entity retriever
	 * @throws BeanFactoryUnableToCreateInstanceException exception for bean factory instantiation (usually when it returns null)
	 * @throws BeanFactoryNotFoundException no bean factory supplied
	 * @throws InvalidEntityCollectionException if entity collection is null or not empty
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
	public void assembleEntities(final Collection dtos, final Collection entities,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory) 
		throws UnableToCreateInstanceException, InvalidEntityCollectionException, InspectionInvalidDtoInstanceException, 
		       InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, 
		       NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException, 
		       ValueConverterNotFoundException, AnnotationMissingBeanKeyException, AnnotationMissingException, 
		       CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException, 
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
		       GeDARuntimeException, AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, 
		       NotDtoToEntityMatcherException {
		
		if (dtos instanceof Collection && entities instanceof Collection && entities.isEmpty()) {
			
			for (Object dto : dtos) {
				try {
					final Object entity = this.entityClass.newInstance();
					assembleEntity(dto, entity, converters, entityBeanFactory);
					entities.add(entity);
				} catch (InstantiationException exp) {
					throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(),
							"Unable to create entity instance for: " + this.dtoClass.getName(), exp);
				} catch (IllegalAccessException exp) {
					throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(),
							"Unable to create entity instance for: " + this.dtoClass.getName(), exp);
				}
			}
			
		} else {
			throw new InvalidEntityCollectionException();
		}	
		
	}
	
	
	private void validateDtoAndEntity(final Object dto, final Object entity) 
			throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException {
		if (!this.dtoClass.isInstance(dto)) {
			throw new InspectionInvalidDtoInstanceException(this.dtoClass.getCanonicalName(), dto);
		}
		if (!this.entityClass.isInstance(entity)) {
			throw new InspectionInvalidEntityInstanceException(this.entityClass.getCanonicalName(), entity);
		}
	}
	
}
