
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;
import dp.lib.dto.geda.annotations.DtoField;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Assemble DTO and Entities depending on the annotations of Dto.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
public final class DTOAssembler {

    private static final String[] EMPTY_ARR = new String[0];
	
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
				
				final DtoField dtoFieldAnn =
					(DtoField) dtoField.getAnnotation(DtoField.class);
				if (dtoFieldAnn != null) {

					relationMapping.put(dtoFieldAnn.value(),
                            createFieldPipeChain(dtoPropertyDescriptors, entityPropertyDescriptors, dtoField, dtoFieldAnn));
					continue;
				}

                final DtoCollection dtoCollAnn =
                     (DtoCollection) dtoField.getAnnotation(DtoCollection.class);
                if (dtoCollAnn != null) {

                    relationMapping.put(dtoCollAnn.value(),
                            createCollectionPipeChain(dtoPropertyDescriptors, entityPropertyDescriptors, dtoField, dtoCollAnn));


                }

			}
		} catch (IntrospectionException intr) {
			throw new IllegalArgumentException(intr);
		}
		
	}

    private Pipe createCollectionPipeChain(final PropertyDescriptor[] dtoPropertyDescriptors, 
    		final PropertyDescriptor[] entityPropertyDescriptors, final Field dtoField, 
    		final DtoCollection dtoCollAnn) throws IntrospectionException {
        final String binding = dtoCollAnn.value();

        validateNewBinding(binding);


        final String entityBeanKey = dtoCollAnn.entityBeanKey();
        final String dtoBeanKey = dtoCollAnn.dtoBeanKey();

        final Class< ? extends Collection> dtoCollectionClass = dtoCollAnn.dtoCollectionClass();
        final Class< ? extends Collection> entityCollectionClass = dtoCollAnn.entityCollectionClass();

        final Class entityGenericType = dtoCollAnn.entityGenericType();
        final Class< ? extends DtoToEntityMatcher> dtoToEntityMatcher = dtoCollAnn.dtoToEntityMatcher();

        if (binding.indexOf('.') != -1) {
            throw new IllegalArgumentException(
                        "entityBeanKeys must specify single member path for binding '" + binding
                        + "' of collection of [" + dtoClass.getCanonicalName() + "]");

        }


        final boolean readOnly = dtoCollAnn.readOnly();

        final PropertyDescriptor entityFieldDesc = getEntityPropertyDescriptorForField(
				entityPropertyDescriptors, dtoField, binding);

		final Method entityFieldRead = entityFieldDesc.getReadMethod();
		final Method entityFieldWrite = entityFieldDesc.getWriteMethod();

        final PropertyDescriptor dtoFieldDesc = getDtoPropertyDescriptorForField(dtoField, dtoPropertyDescriptors);

        final Method dtoFieldRead = dtoFieldDesc.getReadMethod();
		final Method dtoFieldWrite = dtoFieldDesc.getWriteMethod();


        return new CollectionPipe(
                dtoFieldRead, dtoFieldWrite,
                entityFieldRead, entityFieldWrite,
                readOnly,
                dtoBeanKey, entityBeanKey, entityGenericType,
                dtoCollectionClass, entityCollectionClass,
                dtoToEntityMatcher);
    }

    private void validateNewBinding(final String binding) {
        if (relationMapping.containsKey(binding)) {
            throw new IllegalArgumentException(
                    "Binding for '" + binding + "' already exists in Dto: "
                    + dtoClass.getCanonicalName());
        }
    }

    private Pipe createFieldPipeChain(final PropertyDescriptor[] dtoPropertyDescriptors, final PropertyDescriptor[] entityPropertyDescriptors, final Field dtoField, final DtoField dtoFieldAnn) throws IntrospectionException {
        final String binding = dtoFieldAnn.value();

        validateNewBinding(binding);

        final String converter = dtoFieldAnn.converter();
        final String[] entityBeanKeys = dtoFieldAnn.entityBeanKeys();
        final String[] dtoBeanKeys = dtoFieldAnn.dtoBeanKeys();

        final String[] beanKeysChain;
        if (binding.indexOf('.') != -1) {
            if (entityBeanKeys == null || entityBeanKeys.length == 0) {
                throw new IllegalArgumentException(
                        "entityBeanKeys must be specified for binding '" + binding
                        + "' for each entity sub path of [" + dtoClass.getCanonicalName() + "]");
            }
            beanKeysChain = entityBeanKeys;

        } else if (entityBeanKeys != null) {
            beanKeysChain = entityBeanKeys;
        } else {
            beanKeysChain = EMPTY_ARR;
        }
        final String[] dtoKeysChain;
        if (dtoBeanKeys != null) {
            dtoKeysChain = dtoBeanKeys;
        } else {
            dtoKeysChain = EMPTY_ARR;
        }

        final boolean readOnly = dtoFieldAnn.readOnly();


        final String[] bindingChain = createFieldBindingChain(binding);

        return createPipe(
                dtoPropertyDescriptors, entityPropertyDescriptors,
                dtoField, bindingChain, beanKeysChain, dtoKeysChain, 0, converter, readOnly);
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
			final Field dtoField, final String[] binding,
            final String[] beanKeysChain, final String[] dtoKeysChain,
			final int chainIndex, final String converter, final boolean readOnly) 
		throws IntrospectionException {


        final String dtoBeanKey;
        if (dtoKeysChain.length > chainIndex) {
            dtoBeanKey = dtoKeysChain[chainIndex];
        } else {
            dtoBeanKey = "";
        }

        final String entityBeanKey;
        if (beanKeysChain.length > chainIndex) {
            entityBeanKey = beanKeysChain[chainIndex];
        } else {
            entityBeanKey = "";
        }

		if (chainIndex + 1 == binding.length) {

        	return createDataPipe(dtoPropertyDescriptors,
					entityPropertyDescriptors, dtoField, binding[chainIndex],
					converter, readOnly, dtoBeanKey, entityBeanKey);
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
						dtoField, binding, beanKeysChain, dtoKeysChain, chainIndex + 1, converter, readOnly),
						entityBeanKey);

	}

	private Pipe createDataPipe(
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final Field dtoField, final String binding, final String converter,
            final boolean readOnly, final String dtoBeanKey, final String entityBeanKey) {
		
		final PropertyDescriptor dtoFieldDesc = getDtoPropertyDescriptorForField(
				dtoField, dtoPropertyDescriptors);

		final PropertyDescriptor entityFieldDesc = getEntityPropertyDescriptorForField(
				entityPropertyDescriptors, dtoField, binding);
		
		return new DataPipe(
				dtoFieldDesc.getReadMethod(),
				dtoFieldDesc.getWriteMethod(),
				entityFieldDesc.getReadMethod(),
				entityFieldDesc.getWriteMethod(),
				converter, readOnly, dtoBeanKey, entityBeanKey
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
			throw new IllegalArgumentException("Dto must be annotated with @Dto");
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
	 * @throws IllegalArgumentException if dto or entity are not of correct class or
	 *         refrlection pipe fails
	 */
	public void assembleDto(final Object dto, final Object entity,
                            final Map<String, ValueConverter> converters,
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
	 *         refrlection pipe fails
	 */
	public void assembleEntity(final Object dto, final Object entity,
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
