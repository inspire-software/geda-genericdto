
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
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.meta.CollectionPipeMetadata;
import dp.lib.dto.geda.adapter.meta.FieldPipeMetadata;
import dp.lib.dto.geda.adapter.meta.PipeMetadata;
import dp.lib.dto.geda.annotations.Dto;

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
	public static final String SETTING_ASSEMBLER_CACHE_CLEANUP_CYCLE = "dp.lib.dto.geda.assembler.DTOAssembler.ASSEMBLER_CACHE_CLEANUP_CYCLE";
	/**
	 * int number that allows to define the number of JavassitMethodSynthesizer.READER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE = "dp.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE";

	/**
	 * int number that allows to define the number of JavassitMethodSynthesizer.WRITER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE = "dp.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE";
	
	
	private static final Cache<String, DTOAssembler> CACHE = new SoftReferenceCache<String, DTOAssembler>(50);	
	
	/**
	 * Setup allows to configure some of the behaviour of GeDA. Currently it is used to tune the caching cleanup cycles.
	 * There are two caches used in GeDA:
	 * <ul>
	 * <li>DTOAssembler cache - that caches the assemblers instances</li>
	 * <li>Dynamic classes cache - that caches the instances of {@link DataReader}s and {@link DataWriter}s</li>
	 * </ul>
	 * 
	 * @param props properties with key specified by DTOAssembler.SETTINGS_* keys
	 * 
	 * @throws NumberFormatException if the number of cycles specified in properties cannot be converted to int.
	 */
	public static void setup(final Properties props) throws NumberFormatException {
		final String assemblerCache = props.getProperty(SETTING_ASSEMBLER_CACHE_CLEANUP_CYCLE);
		final String javassistReaderCache = props.getProperty(SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE);
		final String javassistWriterCache = props.getProperty(SETTING_DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE);
		if (assemblerCache != null) {
			((SoftReferenceCache<String, DTOAssembler>) CACHE).setCleanUpCycle(Integer.valueOf(assemblerCache));
		}
		if (javassistReaderCache != null) {
			((JavassitMethodSynthesizer) SYNTHESIZER).setCleanUpReaderCycle(Integer.valueOf(javassistReaderCache));
		}
		if (javassistWriterCache != null) {
			((JavassitMethodSynthesizer) SYNTHESIZER).setCleanUpWriterCycle(Integer.valueOf(javassistWriterCache));
		}
	}
	
	private final Class dtoClass;
	private final Class entityClass;
	
	private final Map<String, Pipe> relationMapping = new HashMap<String, Pipe>();
	
	private static final MethodSynthesizer SYNTHESIZER = new JavassitMethodSynthesizer(); 
	
	private DTOAssembler(final Class dto, final Class entity) 
		throws IllegalArgumentException {
		dtoClass = dto;
		entityClass = entity;
		
		Class dtoMap = dto;
		while (dtoMap != null) { // when we reach Object.class this should be null
			
			mapRelationMapping(dtoMap, entity);
			dtoMap = (Class) dtoMap.getGenericSuperclass();
			
		}
		
	}

	private void mapRelationMapping(final Class dto, final Class entity) {		

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
			final Field dtoField, final List<PipeMetadata> metas, final int index) {
		
		final PipeMetadata meta = metas.get(index);
		
		if (index + 1 == metas.size()) {
			if (meta instanceof FieldPipeMetadata) {
				// create field pipe
				return DataPipeBuilder.build(SYNTHESIZER, dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (FieldPipeMetadata) meta);
			} else if (meta instanceof CollectionPipeMetadata) {
				// create collection
				return CollectionPipeBuilder.build(SYNTHESIZER, dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (CollectionPipeMetadata) meta);
			} else if (meta instanceof MapPipeMetadata) {
				// create map
				return MapPipeBuilder.build(SYNTHESIZER, dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (MapPipeMetadata) meta);
			} else {
				throw new IllegalArgumentException("Unknown pipe meta: " + meta.getClass());
			}
		}
		
		final PropertyDescriptor nested = PropertyInspector.getEntityPropertyDescriptorForField(
				dto, entity, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		final PropertyDescriptor[] nestedEntityPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClassReturnedByGet(nested);
		
		// build a chain pipe
		return DataPipeChainBuilder.build(SYNTHESIZER, dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, meta, 
				createPipeChain(dto, dtoPropertyDescriptors, entity, nestedEntityPropertyDescriptors, dtoField, metas, index + 1)	
			);
		
	}

    private void validateNewBinding(final String binding) {
        if (relationMapping.containsKey(binding)) {
            throw new IllegalArgumentException(
                    "Binding for '" + binding + "' already exists in Dto: "
                    + dtoClass.getCanonicalName());
        }
    }
    
    /**
     * @param dto Dto concrete class that is annotated.
     * @param entity the entity class or interface that has appropriate getters and setters
     * @return assembler instance for this conversion.
     * @throws IllegalArgumentException if dto class is not annotated 
     *         with {@link dp.lib.dto.geda.annotations.Dto}, or if Dto annotation mapping is not correct
     *         in respect to entity object
     */
    public static DTOAssembler newAssembler(
    		final Class< ? > dto, final Class< ? > entity) throws IllegalArgumentException {
    	
    	if (dto.getAnnotation(Dto.class) == null) {
    		throw new IllegalArgumentException("Dto " + dto.getName() + " must be annotated with @Dto");
    	}
    	
    	return createNewAssembler(dto, entity);
    }

	private static DTOAssembler createNewAssembler(final Class<?> dto, final Class<?> entity) {
		final String key = createAssemberKey(dto, entity);
    	
		DTOAssembler assembler = CACHE.get(key);
		if (assembler == null) {
			assembler = new DTOAssembler(dto, entity);
	    	CACHE.put(key, assembler);
		}
    	return assembler;
	}
    	
	/**
	 * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
	 * @return assembler instance for this conversion.
	 * @throws IllegalArgumentException if dto class is not annotated 
	 *         with {@link dp.lib.dto.geda.annotations.Dto}, or if Dto annotation mapping is not correct
	 *         in respect to entity object
	 */
	public static DTOAssembler newAssembler(
			final Class< ? > dto) throws IllegalArgumentException {
		
		final Dto ann = dto.getAnnotation(Dto.class);
		if (ann == null || ann.value() == null || ann.value().length() == 0) {
			throw new IllegalArgumentException("Dto " + dto.getName() + " must be annotated with @Dto and value paramter is specified");
		}
		
		Class entity = null;
		
		try {
			entity = Class.forName(ann.value());
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalArgumentException("Specified entity class " + ann.value() + " for Dto " + dto.getName() + " cannot be loaded...");
		}
		
		return createNewAssembler(dto, entity);
	}

	private static <DTO, Entity> String createAssemberKey(final Class<DTO> dto,
			final Class<Entity> entity) {
		return dto.getCanonicalName() + "-" + entity.getCanonicalName();
	}
	
	/**
	 * Assembles dto from current entity by using annotations of the dto.
	 * @param dto the dto to insert data to
	 * @param entity the entity to get data from
	 * @param converters the converters to be used during conversion. The rationale for injecting the converters
	 *        during conversion is to enforce them being stateless and unattached to assembler.
	 * @param dtoBeanFactory bean factory for creating new instances of nested DTO objects mapped by
	 *        {@link dp.lib.dto.geda.annotations.DtoField#dtoBeanKeys()} key.
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         reflection pipe fails
	 */
	public void assembleDto(final Object dto, final Object entity,
			final Map<String, Object> converters,
			final BeanFactory dtoBeanFactory)
	throws IllegalArgumentException {
		
		validateDtoAndEntity(dto, entity);
		
		for (Pipe pipe : relationMapping.values()) {
			try {
				pipe.writeFromEntityToDto(entity, dto, converters, dtoBeanFactory);
			} catch (IllegalAccessException iae) {
				throw new IllegalArgumentException(iae);
			} catch (InvocationTargetException ite) {
				throw new IllegalArgumentException(ite);
			} 
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
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         reflection pipe fails; or dto's collection is null or not empty; or entity collection
	 *         is null; or a new instance of dtoClass cannot be created.
	 */
	public void assembleDtos(final Collection dtos, final Collection entities,
                            final Map<String, Object> converters,
                            final BeanFactory dtoBeanFactory)
		throws IllegalArgumentException {
		
		if (dtos instanceof Collection && dtos.isEmpty() && entities instanceof Collection) {
		
			for (Object entity : entities) {
				try {
					final Object dto = this.dtoClass.newInstance();
					assembleDto(dto, entity, converters, dtoBeanFactory);
					dtos.add(dto);
				} catch (Exception exp) {
					throw new IllegalArgumentException(
							"Unable to create dto instance for: " + this.dtoClass.getName(), exp);
				}
			}
			
		} else {
			throw new IllegalArgumentException(
					"Collections must not be null and dtos collection should be empty");
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
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         reflection pipe fails
	 */
	public void assembleEntity(final Object dto, final Object entity,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory)
		throws IllegalArgumentException {
		
		validateDtoAndEntity(dto, entity);
		
		for (Pipe pipe : relationMapping.values()) {
			try {
				pipe.writeFromDtoToEntity(entity, dto, converters, entityBeanFactory);
			} catch (IllegalAccessException iae) {
				throw new IllegalArgumentException(iae);
			} catch (InvocationTargetException ite) {
				throw new IllegalArgumentException(ite);
			}
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
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         reflection pipe fails; or dto's collection is null; or entity collection
	 *         is null or not empty; or a new instance of entityClass cannot be created.
	 */
	public void assembleEntities(final Collection dtos, final Collection entities,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory)
		throws IllegalArgumentException {
		
		if (dtos instanceof Collection && entities instanceof Collection && entities.isEmpty()) {
			
			for (Object dto : dtos) {
				try {
					final Object entity = this.entityClass.newInstance();
					assembleEntity(dto, entity, converters, entityBeanFactory);
					entities.add(entity);
				} catch (Exception exp) {
					throw new IllegalArgumentException(
							"Unable to create entity instance for: " + this.dtoClass.getName());
				}
			}
			
		} else {
			throw new IllegalArgumentException(
				"Collections must not be null and entities collection should be empty");
		}	
		
	}
	
	
	private void validateDtoAndEntity(final Object dto, final Object entity) {
		if (!this.dtoClass.isInstance(dto)) {
			throw new IllegalArgumentException(
					"This assembler is only applicable for dto: "
					+ this.dtoClass.getCanonicalName() + (dto != null ? ", found: " + dto.getClass() : ""));
		}
		if (!this.entityClass.isInstance(entity)) {
			throw new IllegalArgumentException(
					"This assembler is only applicable for entity: "
					+ this.entityClass.getCanonicalName() + (entity != null ? ", found: " + entity.getClass() : ""));
		}
	}
	
}
