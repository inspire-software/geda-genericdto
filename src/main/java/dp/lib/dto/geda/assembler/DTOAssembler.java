/**
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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.annotations.Dto;

/**
 * Assemble DTO and Entities depending on the annotations of Dto.
 *
 * @param <DTO> the Dto class
 * @param <Entity> the entity class
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
public final class DTOAssembler<DTO, Entity> {
	
	private static final Map<String, DTOAssembler> CACHE = new HashMap<String, DTOAssembler>();	
	
	private final Class dtoClass;
	private final Class entityClass;
	
	private final Map<String, Pipe> relationMapping = new HashMap<String, Pipe>();
	
	private DTOAssembler(final Class dto, final Class entity) 
		throws IllegalArgumentException {
		dtoClass = dto;
		entityClass = entity;
		
		try {
			final PropertyDescriptor[] dtoPropertyDescriptors = 
				Introspector.getBeanInfo(dtoClass).getPropertyDescriptors();
			final PropertyDescriptor[] entityPropertyDescriptors = 
				Introspector.getBeanInfo(entityClass).getPropertyDescriptors();
			
			final Field[] dtoFields = dtoClass.getDeclaredFields();
			for (Field dtoField : dtoFields) {
				
				final dp.lib.dto.geda.annotations.Field dtoFieldAnn = 
					(dp.lib.dto.geda.annotations.Field) dtoField.getAnnotation(dp.lib.dto.geda.annotations.Field.class);
				if (dtoFieldAnn != null) {
					
					final String binding = dtoFieldAnn.value();
					final String converter = dtoFieldAnn.converter();
					final String[] beanKeysChain;
					if (binding.indexOf('.') != -1) {
						final dp.lib.dto.geda.annotations.Dto dtoBeanMappingAnn = 
							(dp.lib.dto.geda.annotations.Dto) dtoField.getAnnotation(dp.lib.dto.geda.annotations.Dto.class);
						if (dtoBeanMappingAnn.entityBeanKeys() == null || dtoBeanMappingAnn.entityBeanKeys().length == 0) {
							throw new IllegalArgumentException(
									"Binding for '" + binding 
									+ "' must be annotated with @Dto and entityBeanKeys must be specified for each nested entity in [" 
									+ dtoClass.getCanonicalName() + "]");
						}
						beanKeysChain = dtoBeanMappingAnn.entityBeanKeys();
						
					} else {
						beanKeysChain = null;
					}
					
					final boolean readOnly = dtoFieldAnn.readOnly();
					if (relationMapping.containsKey(binding)) {
						throw new IllegalArgumentException(
								"Binding for '" + binding + "' already exists in Dto: " 
								+ dtoClass.getCanonicalName());
					}
					
					
					final String[] bindingChain = createFieldBindingChain(binding);
										
					final Pipe pipe = createPipe(
							dtoPropertyDescriptors, entityPropertyDescriptors,
							dtoField, bindingChain, beanKeysChain, 0, converter, readOnly);
					

					relationMapping.put(binding, pipe);
					
				}
			}
		} catch (IntrospectionException intr) {
			throw new IllegalArgumentException(intr);
		}
		
	}

	private String[] createFieldBindingChain(final String binding) {
		if (binding.indexOf('.') == -1) {
			return new String[] { binding };
		} 
		return binding.split("\\.");
	}

	private Pipe createPipe(
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final Field dtoField, final String[] binding, final String[] beanKeysChain, 
			final int chainIndex, final String converter, final boolean readOnly) 
		throws IntrospectionException {

		if (chainIndex + 1 == binding.length) {
		
			return createDataPipe(dtoPropertyDescriptors,
					entityPropertyDescriptors, dtoField, binding[chainIndex],
					converter, readOnly);
		}
			
		final PropertyDescriptor entityFieldDesc = getEntityPropertyDescriptorForField(
				entityPropertyDescriptors, dtoField, binding[chainIndex]);
		
		final Method entityFieldRead = entityFieldDesc.getReadMethod();
		final Method entityFieldWrite = entityFieldDesc.getWriteMethod();
		final Class returnType = (Class) entityFieldRead.getGenericReturnType();
		
		final PropertyDescriptor[] entitySubPropertyDescriptors = 
			Introspector.getBeanInfo(returnType).getPropertyDescriptors();
			
		return new DataPipeChain(entityFieldRead, entityFieldWrite,
				createPipe(dtoPropertyDescriptors, entitySubPropertyDescriptors, 
						dtoField, binding, beanKeysChain, chainIndex + 1, converter, readOnly),
						beanKeysChain[chainIndex]);

	}

	private Pipe createDataPipe(
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final Field dtoField, final String binding, final String converter, final boolean readOnly) {
		
		final PropertyDescriptor dtoFieldDesc = getDtoPropertyDescriptorForField(
				dtoField, dtoPropertyDescriptors);

		final PropertyDescriptor entityFieldDesc = getEntityPropertyDescriptorForField(
				entityPropertyDescriptors, dtoField, binding);
		
		return new DataPipe(
				dtoFieldDesc.getReadMethod(),
				dtoFieldDesc.getWriteMethod(),
				entityFieldDesc.getReadMethod(),
				entityFieldDesc.getWriteMethod(),
				converter, readOnly
		);
	}

	private PropertyDescriptor getEntityPropertyDescriptorForField(
			final PropertyDescriptor[] entityPropertyDescriptors,
			final Field dtoField, final String binding) {
		
		for (PropertyDescriptor current : entityPropertyDescriptors) {
			if (current.getName().equals(binding)) {
				return current;
			}
		}

		throw new IllegalArgumentException(
				"Unable to bind Dto field '" + dtoClass.getCanonicalName() + "#" 
				+ dtoField.getName() + "' to '" 
				+ entityClass.getCanonicalName() + "#" + binding + "'");
	}

	private PropertyDescriptor getDtoPropertyDescriptorForField(
			final Field dtoField,
			final PropertyDescriptor[] dtoPropertyDescriptors) {

		for (PropertyDescriptor current : dtoPropertyDescriptors) {
			if (current.getName().equals(dtoField.getName())) {
				return current;
			}
		}
		
		throw new IllegalArgumentException(
				"Unable to locale Dto field '" + dtoClass.getCanonicalName() + "#" 
				+ dtoField.getName() + "'");
	}
	
	/**
	 * @param <DTO> Dto class
	 * @param dto Dto class
	 * @param <Entity> the entity class
	 * @param entity the entity class
	 * @return assembler instance for this conversion.
	 * @throws IllegalArgumentException if dto class is not annotated 
	 *         with {@link dp.lib.dto.geda.annotations.Dto}, or if Dto annotation mapping is not correct
	 *         in respect to entity object
	 */
	public static <DTO, Entity> DTOAssembler<DTO, Entity> newAssembler(
			final Class<DTO> dto, final Class<Entity> entity) throws IllegalArgumentException {
		
		if (dto.getAnnotation(Dto.class) == null) {
			throw new IllegalArgumentException("Dto must be annotated with @Dto");
		}
		
		final String key = createAssemberKey(dto, entity);
		
		if (CACHE.containsKey(key)) {
			return CACHE.get(key);
		}
		
		final DTOAssembler<DTO, Entity> assembler = new DTOAssembler<DTO, Entity>(dto, entity);
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
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         refrlection pipe fails
	 */
	public void assembleDto(final DTO dto, final Object entity, final Map<String, ValueConverter> converters)
		throws IllegalArgumentException {
		
		validateDtoAndEntity(dto, entity);
		
		for (Pipe pipe : relationMapping.values()) {
			try {
				pipe.writeFromEntityToDto(entity, dto, converters);
			} catch (IllegalAccessException iae) {
				throw new IllegalArgumentException(iae);
			} catch (InvocationTargetException ite) {
				throw new IllegalArgumentException(ite);
			}
		}
		
	}



	/**
	 * Assembles entity from current dto by unsing annotations of the dto.
	 * @param dto the dto to get data from
	 * @param entity the entity to copy data to
	 * @param converters the converters to be used during conversion. Optional parameter that provides map with 
	 *        value converters mapped by {@link dp.lib.dto.geda.annotations.Field#converter()}. If no converters
	 *        are required for this DTO then a <code>null</code> can be passed in. The rationale for injecting the converters
	 *        during conversion is to enforce them being stateless and unattached to assembler.
	 * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO by
	 *        {@link dp.lib.dto.geda.annotations.Dto#entityBeanKeys()} key.
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         refrlection pipe fails
	 */
	public void assembleEntity(final DTO dto, final Object entity, 
			final Map<String, ValueConverter> converters, final BeanFactory entityBeanFactory)
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
	
	private void validateDtoAndEntity(final DTO dto, final Object entity) {
		if (!this.dtoClass.isInstance(dto)) {
			throw new IllegalArgumentException(
					"This assembler is only applicable for dto: "
					+ this.dtoClass.getCanonicalName());
		}
		if (!this.entityClass.isInstance(entity)) {
			throw new IllegalArgumentException(
					"This assembler is only applicable for dto: "
					+ this.dtoClass.getCanonicalName());
		}
	}
	
}
