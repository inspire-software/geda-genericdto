/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.impl;

import com.inspiresoftware.lib.dto.geda.DTOAdaptersRegistrar;
import com.inspiresoftware.lib.dto.geda.DTODSLRegistrar;
import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.repository.AdaptersRepository;
import com.inspiresoftware.lib.dto.geda.adapter.repository.impl.AdaptersRepositoryImpl;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.impl.DefaultDSLRegistry;
import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class DTOSupportImpl implements DTOSupport, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DTOSupportImpl.class);

    private BeanFactory beanFactory;

    private DTOAdaptersRegistrar adaptersRegistrar;
    private DTODSLRegistrar dslRegistrar;

    private final AdaptersRepository dtoValueConverters = new AdaptersRepositoryImpl();
    private Registry dslRegistry = new DefaultDSLRegistry();

    private DTOEventListener onDtoAssembly;
    private DTOEventListener onEntityAssembly;

    private DTOEventListener onDtoAssembled;
    private DTOEventListener onDtoFailed;
    private DTOEventListener onEntityAssembled;
    private DTOEventListener onEntityFailed;

    private final Map<Integer, Boolean> isClassAnnotatedCache = new ConcurrentHashMap<Integer, Boolean>();

    public void setBeanFactory(final ExtensibleBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.dslRegistry = new DefaultDSLRegistry(beanFactory);
    }

    public void setAdaptersRegistrar(final DTOAdaptersRegistrar adaptersRegistrar) {
        this.adaptersRegistrar = adaptersRegistrar;
    }

    public void setDslRegistrar(final DTODSLRegistrar dslRegistrar) {
        this.dslRegistrar = dslRegistrar;
    }

    public void afterPropertiesSet() throws Exception {
        this.registerCoreAdapters();
        this.registerDSLMappings();
    }

    /**
     * Extension hook to register mappings via DSL registry immediately after
     * bean construction.
     */
    protected void registerDSLMappings() {
        if (this.dslRegistrar != null) {
            this.dslRegistrar.registerMappings(this, this.dslRegistry);
        }
    }

    /**
     * Extension hook to register converters, retrievers and matchers immediately after
     * bean construction.
     */
    protected void registerCoreAdapters() {
        if (this.adaptersRegistrar != null) {
            this.adaptersRegistrar.registerAdapters(this);
        }
    }

    /** {@inheritDoc} */
    public <T> T assembleDto(final T dto, final Object entity, final String context) {
        return assembleDto(null, dto, entity, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleDtoByKey(final String dtoKey, final Object entity, final String context) {
        final Object dto = this.beanFactory.get(dtoKey);
        if (dto == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoKey);
        }
        return (T) assembleDto(null, dto, entity, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleDto(final String dtoFilter, final T dto, final Object entity, final String context) {
        final Class dtoClassFilter;
        if (dtoFilter == null) {
            dtoClassFilter = dto.getClass();
        } else {
            dtoClassFilter = this.beanFactory.getClazz(dtoFilter);
            if (dtoClassFilter == null) {
                throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
            }
        }
        try {
            if (this.onDtoAssembly != null) {
                this.onDtoAssembly.onEvent(context, dto, entity);
            }

            if (isAnnotatedClass(dtoClassFilter)) {
                DTOAssembler.newAssembler(dtoClassFilter, entity.getClass())
                        .assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
            } else {
                DTOAssembler.newAssembler(dtoClassFilter, entity.getClass(), dslRegistry)
                        .assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
            }

            if (this.onDtoAssembled != null) {
                this.onDtoAssembled.onEvent(context, dto, entity);
            }
            return dto;
        } catch (final RuntimeException re) {
            if (this.onDtoFailed != null) {
                this.onDtoFailed.onEvent(context, dto, entity, re);

                LOG.error("Exception skipped by event listener", re);

                return null;
            }
            throw re; // re-throw
        }
    }

    /** {@inheritDoc} */
    public <T> T assembleDtoByKey(final String dtoFilter, final String dtoKey, final Object entity, final String context) {
        final Object dto = this.beanFactory.get(dtoKey);
        if (dto == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoKey);
        }
        return (T) assembleDto(dtoFilter, dto, entity, context);
    }

    /** {@inheritDoc} */
    public <T> void assembleDtos(final String keyDto, final Collection<T> dtos, final Collection entities, final String context) {
        assembleDtos(null, keyDto, dtos, entities, context);
    }

    /** {@inheritDoc} */
    public <T> void assembleDtos(final String dtoFilter, final String keyDto,
                                 final Collection<T> dtos, final Collection entities, final String context) {
        if (!CollectionUtils.isEmpty(entities)) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = this.beanFactory.getClazz(keyDto);
                if (dtoClassFilter == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + keyDto);
                }
            } else {
                dtoClassFilter = this.beanFactory.getClazz(dtoFilter);
                if (dtoClassFilter == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
                }
            }
            final Class entityClass = entities.iterator().next().getClass();

            final Assembler asm;
            if (isAnnotatedClass(dtoClassFilter)) {
                asm = DTOAssembler.newAssembler(dtoClassFilter, entityClass);
            } else {
                asm = DTOAssembler.newAssembler(dtoClassFilter, entityClass, dslRegistry);
            }

            for (final Object entity : entities) {
                final Object dto = this.beanFactory.get(keyDto);
                if (dto == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + keyDto);
                }
                try {
                    if (this.onDtoAssembly != null) {
                        this.onDtoAssembly.onEvent(context, dto, entity);
                    }
                    asm.assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
                    dtos.add((T) dto);
                    if (this.onDtoAssembled != null) {
                        this.onDtoAssembled.onEvent(context, dto, entity);
                    }
                } catch (final RuntimeException re) {
                    if (this.onDtoFailed != null) {
                        this.onDtoFailed.onEvent(context, dto, entity, re);

                        LOG.error("Exception skipped by event listener", re);

                        continue;
                    }
                    throw re; // re-throw
                }
            }
        }
    }

    /** {@inheritDoc} */
    public <T> T assembleEntity(final Object dto, final T entity, final String context) {
        return assembleEntity(null, dto, entity, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleEntityByKey(final Object dto, final String entityKey, final String context) {
        final Object entity = this.beanFactory.get(entityKey);
        if (entity == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
        }
        return (T) assembleEntity(null, dto, entity, context);
    }

    /** {@inheritDoc} */
    public <T> T assembleEntityByKey(final String dtoFilter, final Object dto, final String entityKey, final String context) {
        final Object entity = this.beanFactory.get(entityKey);
        if (entity == null) {
            throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
        }
        return (T) assembleEntity(dtoFilter, dto, entity, context);
    }


    /** {@inheritDoc} */
    public <T> T assembleEntity(final String dtoFilter, final Object dto, final T entity, final String context) {

        final Class dtoClassFilter;
        if (dtoFilter == null) {
            dtoClassFilter = dto.getClass();
        } else {
            dtoClassFilter = this.beanFactory.getClazz(dtoFilter);
            if (dtoClassFilter == null) {
                throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
            }
        }

        try {
            if (this.onEntityAssembly != null) {
                this.onEntityAssembly.onEvent(context, dto, entity);
            }

            if (isAnnotatedClass(dtoClassFilter)) {
                DTOAssembler.newAssembler(dtoClassFilter, entity.getClass())
                        .assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
            } else {
                DTOAssembler.newAssembler(dtoClassFilter, entity.getClass(), dslRegistry)
                        .assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
            }

            if (this.onEntityAssembled != null) {
                this.onEntityAssembled.onEvent(context, dto, entity);
            }
            return entity;
        } catch (final RuntimeException re) {
            if (this.onEntityFailed != null) {
                this.onEntityFailed.onEvent(context, dto, entity, re);

                LOG.error("Exception skipped by event listener", re);

                return null;
            }
            throw re; // re-throw
        }
    }

    /** {@inheritDoc} */
    public <T> void assembleEntities(final String entityKey, final Collection dtos, final Collection<T> entities, final String context) {
        assembleEntities(null, entityKey, dtos, entities, context);


    }

    /** {@inheritDoc} */
    public <T> void assembleEntities(final String dtoFilter, final String entityKey,
                                     final Collection dtos, final Collection<T> entities, final String context) {
        if (!CollectionUtils.isEmpty(dtos)) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = dtos.iterator().next().getClass();
            } else {
                dtoClassFilter = this.beanFactory.getClazz(dtoFilter);
                if (dtoClassFilter == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + dtoFilter);
                }
            }
            final Class entityClass =  this.beanFactory.getClazz(entityKey);
            if (entityClass == null) {
                throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
            }

            final Assembler asm;
            if (isAnnotatedClass(dtoClassFilter)) {
                asm = DTOAssembler.newAssembler(dtoClassFilter, entityClass);
            } else {
                asm = DTOAssembler.newAssembler(dtoClassFilter, entityClass, dslRegistry);
            }

            for (final Object dto : dtos) {
                final Object entity = this.beanFactory.get(entityKey);
                if (entity == null) {
                    throw new IllegalArgumentException("DTO factory has no class specified for key: " + entityKey);
                }
                try {
                    if (this.onEntityAssembly != null) {
                        this.onEntityAssembly.onEvent(context, dto, entity);
                    }
                    asm.assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
                    entities.add((T) entity);
                    if (this.onEntityAssembled != null) {
                        this.onEntityAssembled.onEvent(context, dto, entity);
                    }
                } catch (final RuntimeException re) {
                    if (this.onEntityFailed != null) {
                        this.onEntityFailed.onEvent(context, dto, entity, re);

                        LOG.error("Exception skipped by event listener", re);

                        continue;
                    }
                    throw re; // re-throw
                }
            }
        }
    }

    /** {@inheritDoc} */
    public void registerAdapter(final String key, final Object converter) {

        LOG.debug("Registering [{}] with key [{}]", converter, key);

        this.dtoValueConverters.registerAdapter(key, converter);
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


    private Boolean isAnnotatedClass(final Class clazz) {
        final Integer classHash = Integer.valueOf(clazz.hashCode());
        Boolean isAnn;
        if (isClassAnnotatedCache.containsKey(classHash)) {
            isAnn = isClassAnnotatedCache.get(classHash);
        } else {
            isAnn = Boolean.valueOf(clazz.getAnnotation(Dto.class) != null);
            isClassAnnotatedCache.put(classHash, isAnn);
        }
        return isAnn;
    }

}
