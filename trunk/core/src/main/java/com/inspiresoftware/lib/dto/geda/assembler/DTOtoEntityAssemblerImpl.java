
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
import com.inspiresoftware.lib.dto.geda.assembler.extension.Configurable;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.CollectionPipeMetadata;
import com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import com.inspiresoftware.lib.dto.geda.assembler.meta.MapPipeMetadata;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.dsl.Registries;
import com.inspiresoftware.lib.dto.geda.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Assemble DTO and Entities depending on the annotations of Dto.
 *
 * @author Denis Pavlov
 * @since 2.0.2
 *
 */
@SuppressWarnings("unchecked")
public final class DTOtoEntityAssemblerImpl implements Assembler, AssemblerContext, Configurable {

    private static final MetadataChainBuilder ANNOTATIONS = new MetadataChainAnnotationBuilder();

    private static final PipeBuilder<FieldPipeMetadata> FIELD = new DataPipeBuilder();
    private static final PipeBuilder<FieldPipeMetadata> VIRTUAL = new DataVirtualPipeBuilder();
    private static final PipeBuilder<CollectionPipeMetadata> COLLECTION = new CollectionPipeBuilder();
    private static final PipeBuilder<MapPipeMetadata> MAP = new MapPipeBuilder();
    private static final PipeBuilder<PipeMetadata> CHAIN = new DataPipeChainBuilder();


    private final Class dtoClass;
	private final Class entityClass;
	private final MethodSynthesizer synthesizer;
    private final Registry dslRegistry;

    private final Reference<ClassLoader> classLoader;

    private Pipe[] pipes;

	DTOtoEntityAssemblerImpl(final Class dto,
                             final Class entity,
                             final ClassLoader classLoader,
                             final MethodSynthesizer synthesizer,
                             final Registry registry)
		throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
		       GeDARuntimeException, AnnotationDuplicateBindingException {

        this(dto, entity, classLoader, synthesizer, registry, true);
    }

	DTOtoEntityAssemblerImpl(final Class dto, final Class entity,
                             final ClassLoader classLoader,
                             final MethodSynthesizer synthesizer,
                             final Registry registry,
                             final boolean strict)
		throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
		       GeDARuntimeException, AnnotationDuplicateBindingException {
		dtoClass = dto;
		entityClass = entity;
		this.synthesizer = synthesizer;
        this.classLoader = new SoftReference<ClassLoader>(classLoader);
        dslRegistry = registry;

        final MetadataChainBuilder metaBuilder;
        if (registry == null) {
            metaBuilder = ANNOTATIONS;
        } else {
            metaBuilder = new MetadataChainDSLBuilder(registry, dtoClass, entityClass);
        }

		Class dtoMap = dto;
        final LinkedList pipes = new LinkedList();
        while (dtoMap != null) { // when we reach Object.class this should be null

			mapRelationMapping(dtoMap, entity, strict, pipes, metaBuilder);
            final Type type = dtoMap.getGenericSuperclass();
            if (type != null) {
                dtoMap = PropertyInspector.getClassForType(type);
            } else {
                dtoMap = null;
            }

		}
        this.pipes = (Pipe[]) pipes.toArray(new Pipe[pipes.size()]);

    }

	private void mapRelationMapping(final Class dto, final Class entity, final boolean strict, final List<Pipe> pipes, final MetadataChainBuilder metaBuilder)
		throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
		       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
		       GeDARuntimeException, AnnotationDuplicateBindingException {

        final boolean isMapOrListEntity = Map.class.isAssignableFrom(entity) || List.class.isAssignableFrom(entity);

        final PropertyDescriptor[] dtoPropertyDescriptors =
			PropertyInspector.getPropertyDescriptorsForClass(dto);
		final PropertyDescriptor[] entityPropertyDescriptors;
        if (isMapOrListEntity) {
            entityPropertyDescriptors = null;
        } else {
            entityPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClass(entity);
        }

        final Set<String> bindings = new TreeSet<String>();
        final Field[] dtoFields = dto.getDeclaredFields();
		for (Field dtoField : dtoFields) {

			final List<PipeMetadata> metas = metaBuilder.build(dtoField);
			if (metas == null || metas.isEmpty()) {
				continue;
			}
            try {
			    final Pipe pipe = createPipeChain(dtoClass, dtoPropertyDescriptors,
                                                  entityClass, entityPropertyDescriptors,
                                                  dtoField, metas, 0, isMapOrListEntity);
                final String binding = pipe.getBinding();

                if (bindings.contains(binding)) {
                    throw new AnnotationDuplicateBindingException(dtoClass.getCanonicalName(), binding);
                }
                bindings.add(binding);
                pipes.add(pipe);
            } catch (InspectionBindingNotFoundException noField) {
                if (strict) { // only throw exception if we are in strict mode
                    throw noField;
                }
            }
		}
	}

	private Pipe createPipeChain(
            final Class dto, final PropertyDescriptor[] dtoPropertyDescriptors,
            final Class entity, final PropertyDescriptor[] entityPropertyDescriptors,
            final Field dtoField, final List<PipeMetadata> metas, final int index, final boolean isMapOrListEntity)
		throws InspectionPropertyNotFoundException, InspectionBindingNotFoundException, InspectionScanningException,
			   UnableToCreateInstanceException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException {

		final PipeMetadata meta = metas.get(index);

        if (index + 1 == metas.size() || isMapOrListEntity) {
            // build actual pipe for last in chain, maps or lists
            // (since maps and lists do not have any nested properties, maybe another feature?)
			if (meta instanceof FieldPipeMetadata) {
				if (meta.getEntityFieldName().startsWith("#this#")) {
					// create virtual field pipe
					return VIRTUAL.build(this,
							dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors,
                            (FieldPipeMetadata) meta, null);
				} else {
					// create field pipe
					return FIELD.build(this,
							dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors,
                            (FieldPipeMetadata) meta, null);
				}
			} else if (meta instanceof CollectionPipeMetadata) {
				// create collection
				return COLLECTION.build(this,
						dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors,
                        (CollectionPipeMetadata) meta, null);
			} else if (meta instanceof MapPipeMetadata) {
				// create map
				return MAP.build(this,
						dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors,
                        (MapPipeMetadata) meta, null);
			} else {
				throw new GeDARuntimeException("Unknown pipe meta: " + meta.getClass());
			}
		}

		final PropertyDescriptor nested = PropertyInspector.getEntityPropertyDescriptorForField(
				dto, entity, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
		final PropertyDescriptor[] nestedEntityPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClassReturnedByGet(nested);

		// build a chain pipe
		return CHAIN.build(this, dto, entity, dtoPropertyDescriptors, entityPropertyDescriptors, meta,
				createPipeChain(dto, dtoPropertyDescriptors, entity, nestedEntityPropertyDescriptors, dtoField, metas, index + 1, isMapOrListEntity)
			);

	}

	/**
	 * Configure synthesizer.
	 *
	 * @param configuration configuration name
	 * @param value value to set
	 * @return true if configuration was set, false if not set or invalid
	 * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException in case there are errors
	 */
	public boolean configure(final String configuration, final Object value) throws GeDAException {
		return this.synthesizer.configure(configuration, value);
	}

    private BeanFactory resolveBeanFactory(BeanFactory beanFactory) {
        if (beanFactory == null) {
            if (dslRegistry != null) {
                return Registries.beanFactory(dslRegistry);
            }
        }
        return beanFactory;
    }

	/** {@inheritDoc} */
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

		for (Pipe pipe : pipes) {
			pipe.writeFromEntityToDto(entity, dto, converters, resolveBeanFactory(dtoBeanFactory));
		}

	}

    /** {@inheritDoc} */
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

            final BeanFactory beanFactory = resolveBeanFactory(dtoBeanFactory);
			for (Object entity : entities) {
				try {
					final Object dto = this.dtoClass.newInstance();
					assembleDto(dto, entity, converters, beanFactory);
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

    /** {@inheritDoc} */
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

		for (Pipe pipe : pipes) {
			pipe.writeFromDtoToEntity(entity, dto, converters, resolveBeanFactory(entityBeanFactory));
		}

	}

    /** {@inheritDoc} */
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

            final BeanFactory beanFactory = resolveBeanFactory(entityBeanFactory);
			for (Object dto : dtos) {
				try {
					final Object entity = this.entityClass.newInstance();
					assembleEntity(dto, entity, converters, beanFactory);
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

    /** {@inheritDoc} */
    public Assembler newAssembler(final Class<?> dto, final Class<?> entity) {
        if (dslRegistry == null) {
            return DTOAssembler.newCustomAssembler(dto, entity, getClassLoader(), synthesizer);
        }
        return  DTOAssembler.newCustomAssembler(dto, entity, getClassLoader(), dslRegistry, synthesizer);
    }

    /** {@inheritDoc} */
    public MethodSynthesizer getMethodSynthesizer() {
        return synthesizer;
    }

    /** {@inheritDoc} */
    public Registry getDslRegistry() {
        return dslRegistry;
    }

    /** {@inheritDoc} */
    public ClassLoader getClassLoader() throws GeDARuntimeException {
        ClassLoader cl = classLoader.get();
        if (cl == null) { // Cl was garbage collected - something gone really wrong
            throw new GeDARuntimeException("Class loader has been gc'ed");
        }
        return cl;
    }

    /** {@inheritDoc} */
    public void releaseResources() {
        synthesizer.releaseResources();
        if (dslRegistry != null) {
            dslRegistry.releaseResources();
        }
    }
}