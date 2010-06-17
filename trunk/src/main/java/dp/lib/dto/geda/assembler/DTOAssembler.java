
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static final Map<String, DTOAssembler> CACHE = new HashMap<String, DTOAssembler>();	
	
	private final Class dtoClass;
	private final Class entityClass;
	
	private final Map<String, Pipe> relationMapping = new HashMap<String, Pipe>();
	
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
		try {
			
			final PropertyDescriptor[] dtoPropertyDescriptors = 
				Introspector.getBeanInfo(dto).getPropertyDescriptors();
			final PropertyDescriptor[] entityPropertyDescriptors = 
				Introspector.getBeanInfo(entity).getPropertyDescriptors();

			
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
		} catch (IntrospectionException intr) {
			throw new IllegalArgumentException(intr);
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
				return DataPipeBuilder.build(dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (FieldPipeMetadata) meta);
			} else if (meta instanceof CollectionPipeMetadata) {
				// create collection
				return CollectionPipeBuilder.build(dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (CollectionPipeMetadata) meta);
			} else if (meta instanceof MapPipeMetadata) {
				// create map
				return MapPipeBuilder.build(dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, (MapPipeMetadata) meta);
			} else {
				throw new IllegalArgumentException("Unknown pipe meta: " + meta.getClass());
			}
		}
		
		final PropertyDescriptor nested = PropertyInspector.getEntityPropertyDescriptorForField(
				dto, entity, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		final PropertyDescriptor[] nestedEntityPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClassReturnedByGet(nested);
		
		// build a chain pipe
		return DataPipeChainBuilder.build(dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, meta, 
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
		
		final String key = createAssemberKey(dto, entity);
		
		if (CACHE.containsKey(key)) {
			return CACHE.get(key);
		}
		
		final DTOAssembler assembler = new DTOAssembler(dto, entity);
		CACHE.put(key, assembler);
		
		return assembler;
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
							"Unable to create dto instance for: " + this.dtoClass.getName());
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
					+ this.dtoClass.getCanonicalName());
		}
		if (!this.entityClass.isInstance(entity)) {
			throw new IllegalArgumentException(
					"This assembler is only applicable for entity: "
					+ this.entityClass.getCanonicalName());
		}
	}
	
}
