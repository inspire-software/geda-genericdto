/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.osgi.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.repository.AdaptersRepository;
import com.inspiresoftware.lib.dto.geda.adapter.repository.impl.AdaptersRepositoryImpl;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.OSGiJavassistMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;
import com.inspiresoftware.lib.dto.geda.osgi.DTOSupportAnnotationsService;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;

/**
 * Basic implementation of the DTOSupport interface that provides the connection point
 * between Spring AOP and the GeDA assembler worlds.
 *
 * This particular implementation supports the following context listeners:
 * onDtoAssembly - fired just before transfer takes place
 * onEntityAssembly - fired just before transfer takes place
 * onDtoAssembled - fired immediately after assembly
 * onDtoFailed - fired immediately after failed assembly
 * onEntityAssembled - fired immediately after assembly
 * onEntityFailed - fired immediately after failed assembly
 *
 * All event listeners provided with the following context:
 * listener.onEvent(context, DTO, Entity [, Throwable])
 *
 * <p/>
 * User: denispavlov
 * Date: Sep 27, 2011
 * Time: 5:33:05 PM
 */
public class DTOSupportAnnotationsServiceImpl implements DTOSupportAnnotationsService, DisposableContainer {

    private final AdaptersRepository adaptersRepository = new AdaptersRepositoryImpl();

    private final MethodSynthesizer osgiJavassisstSynthesizer;
    private final Reference<ClassLoader> classLoader;

    private DTOEventListener onDtoAssembly;
    private DTOEventListener onEntityAssembly;

    private DTOEventListener onDtoAssembled;
    private DTOEventListener onDtoFailed;
    private DTOEventListener onEntityAssembled;
    private DTOEventListener onEntityFailed;

    public DTOSupportAnnotationsServiceImpl(final ClassLoader classLoader) {

        this.classLoader = new SoftReference<ClassLoader>(classLoader);
        osgiJavassisstSynthesizer = new OSGiJavassistMethodSynthesizer(classLoader);

    }

    /** {@inheritDoc} */
    public <T> T assembleDto(final T dto, final Object entity, final BeanFactory beanFactory, final String context) {
        return assembleDto(null, dto, entity, beanFactory, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleDtoByKey(final String dtoKey,
                                  final Object entity,
                                  final BeanFactory beanFactory,
                                  final String context) {
        final Object dto = beanFactory.get(dtoKey);
        if (dto == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoKey);
        }
        return (T) assembleDto(null, dto, entity, beanFactory, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleDto(final String dtoFilter,
                             final T dto,
                             final Object entity,
                             final BeanFactory beanFactory,
                             final String context) {
        final Class dtoClassFilter;
        if (dtoFilter == null) {
            dtoClassFilter = dto.getClass();
        } else {
            dtoClassFilter = beanFactory.getClazz(dtoFilter);
            if (dtoClassFilter == null) {
                throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
            }
        }
        try {
            if (this.onDtoAssembly != null) {
                this.onDtoAssembly.onEvent(context, dto, entity);
            }

            DTOAssembler.newCustomAssembler(dtoClassFilter, entity.getClass(), classLoader.get(), osgiJavassisstSynthesizer)
                    .assembleDto(dto, entity, this.adaptersRepository.getAll(), beanFactory);

            if (this.onDtoAssembled != null) {
                this.onDtoAssembled.onEvent(context, dto, entity);
            }
            return dto;
        } catch (final RuntimeException re) {
            if (this.onDtoFailed != null) {
                this.onDtoFailed.onEvent(context, dto, entity, re);

                return null;
            }
            throw re; // re-throw
        }
    }

    /** {@inheritDoc} */
    public <T> T assembleDtoByKey(final String dtoFilter,
                                  final String dtoKey,
                                  final Object entity,
                                  final BeanFactory beanFactory,
                                  final String context) {
        final Object dto = beanFactory.get(dtoKey);
        if (dto == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoKey);
        }
        return (T) assembleDto(dtoFilter, dto, entity, beanFactory, context);
    }

    /** {@inheritDoc} */
    public <T> void assembleDtos(final String keyDto,
                                 final Collection<T> dtos,
                                 final Collection entities,
                                 final BeanFactory beanFactory,
                                 final String context) {
        assembleDtos(null, keyDto, dtos, entities, beanFactory, context);
    }

    /** {@inheritDoc} */
    public <T> void assembleDtos(final String dtoFilter,
                                 final String keyDto,
                                 final Collection<T> dtos,
                                 final Collection entities,
                                 final BeanFactory beanFactory,
                                 final String context) {
        if (entities != null && !entities.isEmpty()) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = beanFactory.getClazz(keyDto);
                if (dtoClassFilter == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + keyDto);
                }
            } else {
                dtoClassFilter = beanFactory.getClazz(dtoFilter);
                if (dtoClassFilter == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
                }
            }
            final Class entityClass = entities.iterator().next().getClass();

            final Assembler asm = DTOAssembler.newCustomAssembler(dtoClassFilter, entityClass, classLoader.get(), osgiJavassisstSynthesizer);

            for (final Object entity : entities) {
                final Object dto = beanFactory.get(keyDto);
                if (dto == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + keyDto);
                }
                try {
                    if (this.onDtoAssembly != null) {
                        this.onDtoAssembly.onEvent(context, dto, entity);
                    }
                    asm.assembleDto(dto, entity, this.adaptersRepository.getAll(), beanFactory);
                    dtos.add((T) dto);
                    if (this.onDtoAssembled != null) {
                        this.onDtoAssembled.onEvent(context, dto, entity);
                    }
                } catch (final RuntimeException re) {
                    if (this.onDtoFailed != null) {
                        this.onDtoFailed.onEvent(context, dto, entity, re);

                        continue;
                    }
                    throw re; // re-throw
                }
            }
        }
    }

    /** {@inheritDoc} */
    public <T> T assembleEntity(final Object dto,
                                final T entity,
                                final BeanFactory beanFactory,
                                final String context) {
        return assembleEntity(null, dto, entity, beanFactory, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleEntityByKey(final Object dto,
                                     final String entityKey,
                                     final BeanFactory beanFactory,
                                     final String context) {
        final Object entity = beanFactory.get(entityKey);
        if (entity == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
        }
        return (T) assembleEntity(null, dto, entity, beanFactory, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleEntityByKey(final String dtoFilter,
                                     final Object dto,
                                     final String entityKey,
                                     final BeanFactory beanFactory,
                                     final String context) {
        final Object entity = beanFactory.get(entityKey);
        if (entity == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
        }
        return (T) assembleEntity(dtoFilter, dto, entity, beanFactory, context);
    }


    /** {@inheritDoc} */
    public <T> T assembleEntity(final String dtoFilter,
                                final Object dto,
                                final T entity,
                                final BeanFactory beanFactory,
                                final String context) {

        final Class dtoClassFilter;
        if (dtoFilter == null) {
            dtoClassFilter = dto.getClass();
        } else {
            dtoClassFilter = beanFactory.getClazz(dtoFilter);
            if (dtoClassFilter == null) {
                throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
            }
        }

        try {
            if (this.onEntityAssembly != null) {
                this.onEntityAssembly.onEvent(context, dto, entity);
            }

            DTOAssembler.newCustomAssembler(dtoClassFilter, entity.getClass(), classLoader.get(), osgiJavassisstSynthesizer)
                    .assembleEntity(dto, entity, this.adaptersRepository.getAll(), beanFactory);

            if (this.onEntityAssembled != null) {
                this.onEntityAssembled.onEvent(context, dto, entity);
            }
            return entity;
        } catch (final RuntimeException re) {
            if (this.onEntityFailed != null) {
                this.onEntityFailed.onEvent(context, dto, entity, re);

                return null;
            }
            throw re; // re-throw
        }
    }

    /** {@inheritDoc} */
    public <T> void assembleEntities(final String entityKey,
                                     final Collection dtos,
                                     final Collection<T> entities,
                                     final BeanFactory beanFactory,
                                     final String context) {
        assembleEntities(null, entityKey, dtos, entities, beanFactory, context);


    }

    /** {@inheritDoc} */
    public <T> void assembleEntities(final String dtoFilter,
                                     final String entityKey,
                                     final Collection dtos,
                                     final Collection<T> entities,
                                     final BeanFactory beanFactory,
                                     final String context) {
        if (dtos != null && !dtos.isEmpty()) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = dtos.iterator().next().getClass();
            } else {
                dtoClassFilter = beanFactory.getClazz(dtoFilter);
                if (dtoClassFilter == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
                }
            }
            final Class entityClass =  beanFactory.getClazz(entityKey);
            if (entityClass == null) {
                throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
            }

            final Assembler asm = DTOAssembler.newCustomAssembler(dtoClassFilter, entityClass, classLoader.get(), osgiJavassisstSynthesizer);

            for (final Object dto : dtos) {
                final Object entity = beanFactory.get(entityKey);
                if (entity == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
                }
                try {
                    if (this.onEntityAssembly != null) {
                        this.onEntityAssembly.onEvent(context, dto, entity);
                    }
                    asm.assembleEntity(dto, entity, this.adaptersRepository.getAll(), beanFactory);
                    entities.add((T) entity);
                    if (this.onEntityAssembled != null) {
                        this.onEntityAssembled.onEvent(context, dto, entity);
                    }
                } catch (final RuntimeException re) {
                    if (this.onEntityFailed != null) {
                        this.onEntityFailed.onEvent(context, dto, entity, re);

                        continue;
                    }
                    throw re; // re-throw
                }
            }
        }
    }

    /** {@inheritDoc} */
    public void registerAdapter(final String key, final Object adapter) {

        this.adaptersRepository.registerAdapter(key, adapter);

    }

    /** {@inheritDoc} */
    public void addListener(final String dtoEvent, final DTOEventListener eventListener) {
        if ("OnDtoAssembled".equals(dtoEvent)) {
            this.setOnDtoAssembled(eventListener);
        } else if ("OnDtoFailed".equals(dtoEvent)) {
            this.setOnDtoFailed(eventListener);
        } else if ("OnDtoAssembly".equals(dtoEvent)) {
            this.setOnDtoAssembly(eventListener);
        } else if ("OnEntityAssembled".equals(dtoEvent)) {
            this.setOnEntityAssembled(eventListener);
        } else if ("OnEntityFailed".equals(dtoEvent)) {
            this.setOnEntityFailed(eventListener);
        } else if ("OnEntityAssembly".equals(dtoEvent)) {
            this.setOnEntityAssembly(eventListener);
        }
    }

    /** {@inheritDoc} */
    public void removeListener(final DTOEventListener eventListener) {
        if (eventListener == null) {
            return;
        }
        if (eventListener.equals(this.onDtoAssembled)) {
            this.setOnDtoAssembled(null);
        } else if (eventListener.equals(this.onDtoFailed)) {
            this.setOnDtoFailed(null);
        } else if (eventListener.equals(this.onDtoAssembly)) {
            this.setOnDtoAssembly(null);
        } else if (eventListener.equals(this.onEntityAssembled)) {
            this.setOnEntityAssembled(null);
        } else if (eventListener.equals(this.onEntityFailed)) {
            this.setOnEntityFailed(null);
        } else if (eventListener.equals(this.onEntityAssembly)) {
            this.setOnEntityAssembly(null);
        }
    }

    /**
     * @param onDtoAssembled listener  invoked after assembly
     */
    public void setOnDtoAssembled(final DTOEventListener onDtoAssembled) {
        this.onDtoAssembled = onDtoAssembled;
    }

    /**
     * @param onDtoFailed  listener invoked after failed assembly
     */
    public void setOnDtoFailed(final DTOEventListener onDtoFailed) {
        this.onDtoFailed = onDtoFailed;
    }

    /**
     * @param onEntityAssembled  listener invoked after assembly
     */
    public void setOnEntityAssembled(final DTOEventListener onEntityAssembled) {
        this.onEntityAssembled = onEntityAssembled;
    }

    /**
     * @param onEntityFailed  listener
     */
    public void setOnEntityFailed(final DTOEventListener onEntityFailed) {
        this.onEntityFailed = onEntityFailed;
    }

    /**
     * @param onDtoAssembly listener  invoked before assembly
     */
    public void setOnDtoAssembly(final DTOEventListener onDtoAssembly) {
        this.onDtoAssembly = onDtoAssembly;
    }

    /**
     * @param onEntityAssembly listener invoked before assembly
     */
    public void setOnEntityAssembly(final DTOEventListener onEntityAssembly) {
        this.onEntityAssembly = onEntityAssembly;
    }

    /** {@inheritDoc} */
    public void releaseResources() {

        osgiJavassisstSynthesizer.releaseResources();
        adaptersRepository.releaseResources();

        onDtoAssembly = null;
        onEntityAssembly = null;
        onDtoAssembled = null;
        onDtoFailed = null;
        onEntityAssembled = null;
        onEntityFailed = null;

        DTOAssembler.disposeOfDtoAssemblersBy(classLoader.get());

        classLoader.clear();

    }
}
