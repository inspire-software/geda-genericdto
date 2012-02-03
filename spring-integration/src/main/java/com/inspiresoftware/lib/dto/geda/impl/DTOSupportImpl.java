/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.impl;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.repository.ValueConverterRepository;
import com.inspiresoftware.lib.dto.geda.adapter.repository.impl.ValueConverterRepositoryImpl;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 27, 2011
 * Time: 5:33:05 PM
 */
public class DTOSupportImpl implements DTOSupport {

    private static final Logger LOG = LoggerFactory.getLogger(DTOSupportImpl.class);

    private final BeanFactory beanFactory;
    private final ValueConverterRepository dtoValueConverters = new ValueConverterRepositoryImpl();

    private DTOEventListener onDtoAssembled;
    private DTOEventListener onDtoFailed;
    private DTOEventListener onEntityAssembled;
    private DTOEventListener onEntityFailed;


    public DTOSupportImpl(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.registerCoreConverters();
    }

    /**
     * Extension hook to register converters at the time of bean construction.
     */
    protected void registerCoreConverters() {
        // register all converters, matchers and retrievers here.
    }

    /** {@inheritDoc} */
    public <T> T assembleDto(final T dto, final Object entity) {
        return assembleDto(null, dto, entity);
    }

    /** {@inheritDoc} */
    public <T> T assembleDtoByKey(final String dtoKey, final Object entity) {
        final Object dto = this.beanFactory.get(dtoKey);
        return (T) assembleDto(null, dto, entity);
    }

    /** {@inheritDoc} */
    public <T> T assembleDto(final String dtoFilter, final T dto, final Object entity) {
        final Class dtoClassFilter;
        if (dtoFilter == null) {
            dtoClassFilter = dto.getClass();
        } else {
            dtoClassFilter = this.beanFactory.get(dtoFilter).getClass();
        }
        try {
            DTOAssembler.newAssembler(dtoClassFilter, entity.getClass())
                    .assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
            if (this.onDtoAssembled != null) {
                this.onDtoAssembled.onEvent(dto, entity);
            }
            return dto;
        } catch (final RuntimeException re) {
            if (this.onDtoFailed != null) {
                this.onDtoFailed.onEvent(dto, entity, re);
                if (LOG.isErrorEnabled()) {
                    LOG.error("Exception skipped by event listener", re);
                }
                return null;
            }
            throw re; // re-throw
        }
    }


    /** {@inheritDoc} */
    public <T> T assembleDtoByKey(final String dtoFilter, final String dtoKey, final Object entity) {
        final Object dto = this.beanFactory.get(dtoKey);
        return (T) assembleDto(dtoFilter, dto, entity);
    }

    /** {@inheritDoc} */
    public <T> void assembleDtos(final String keyDto, final Collection<T> dtos, final Collection entities) {
        assembleDtos(null, keyDto, dtos, entities);
    }

    /** {@inheritDoc} */
    public <T> void assembleDtos(final String dtoFilter, final String keyDto,
                                 final Collection<T> dtos, final Collection entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = this.beanFactory.get(keyDto).getClass();
            } else {
                dtoClassFilter = this.beanFactory.get(dtoFilter).getClass();
            }
            final Class entityClass = entities.iterator().next().getClass();

            final DTOAssembler asm = DTOAssembler.newAssembler(dtoClassFilter, entityClass);
            for (final Object entity : entities) {
                final Object dto = this.beanFactory.get(keyDto);
                try {
                    asm.assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
                    dtos.add((T) dto);
                    if (this.onDtoAssembled != null) {
                        this.onDtoAssembled.onEvent(dto, entity);
                    }
                } catch (final RuntimeException re) {
                    if (this.onDtoFailed != null) {
                        this.onDtoFailed.onEvent(dto, entity, re);
                        if (LOG.isErrorEnabled()) {
                            LOG.error("Exception skipped by event listener", re);
                        }
                        continue;
                    }
                    throw re; // re-throw
                }
            }
        }
    }

    /** {@inheritDoc} */
    public <T> T assembleEntity(final Object dto, final T entity) {
        return assembleEntity(null, dto, entity);
    }

    /** {@inheritDoc} */
    public <T> T assembleEntityByKey(final Object dto, final String entityKey) {
        final Object entity = this.beanFactory.get(entityKey);
        return (T) assembleEntity(null, dto, entity);
    }

    /** {@inheritDoc} */
    public <T> T assembleEntityByKey(final String dtoFilter, final Object dto, final String entityKey) {
        final Object entity = this.beanFactory.get(entityKey);
        return (T) assembleEntity(dtoFilter, dto, entity);
    }


    /** {@inheritDoc} */
    public <T> T assembleEntity(final String dtoFilter, final Object dto, final T entity) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = dto.getClass();
            } else {
                dtoClassFilter = this.beanFactory.get(dtoFilter).getClass();
            }        try {
            DTOAssembler.newAssembler(dtoClassFilter, entity.getClass())
                    .assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
            if (this.onEntityAssembled != null) {
                this.onEntityAssembled.onEvent(dto, entity);
            }
            return entity;
        } catch (final RuntimeException re) {
            if (this.onEntityFailed != null) {
                this.onEntityFailed.onEvent(dto, entity, re);
                if (LOG.isErrorEnabled()) {
                    LOG.error("Exception skipped by event listener", re);
                }
                return null;
            }
            throw re; // re-throw
        }
    }

    /** {@inheritDoc} */
    public <T> void assembleEntities(final String entityKey, final Collection dtos, final Collection<T> entities) {
        assembleEntities(null, entityKey, dtos, entities);


    }

    /** {@inheritDoc} */
    public <T> void assembleEntities(final String dtoFilter, final String entityKey,
                                     final Collection dtos, final Collection<T> entities) {
        if (!CollectionUtils.isEmpty(dtos)) {
            final Class dtoClassFilter;
            if (dtoFilter == null) {
                dtoClassFilter = dtos.iterator().next().getClass();
            } else {
                dtoClassFilter = this.beanFactory.get(dtoFilter).getClass();
            }
            final Class entityClass = this.beanFactory.get(entityKey).getClass();

            final DTOAssembler asm = DTOAssembler.newAssembler(dtoClassFilter, entityClass);
            for (final Object dto : dtos) {
                final Object entity = this.beanFactory.get(entityKey);
                try {
                    asm.assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.beanFactory);
                    entities.add((T) entity);
                    if (this.onEntityAssembled != null) {
                        this.onEntityAssembled.onEvent(dto, entity);
                    }
                } catch (final RuntimeException re) {
                    if (this.onEntityFailed != null) {
                        this.onEntityFailed.onEvent(dto, entity, re);
                        if (LOG.isErrorEnabled()) {
                            LOG.error("Exception skipped by event listener", re);
                        }
                        continue;
                    }
                    throw re; // re-throw
                }
            }
        }
    }

    /** {@inheritDoc} */
    public void registerValueConverter(final String key, final Object converter) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Registering [" + converter.toString() + "] with key [" + key + "]");
        }
        this.dtoValueConverters.registerValueConverter(key, converter);
    }

    /**
     * @param onDtoAssembled listener
     */
    public void setOnDtoAssembled(final DTOEventListener onDtoAssembled) {
        this.onDtoAssembled = onDtoAssembled;
    }

    /**
     * @param onDtoFailed  listener
     */
    public void setOnDtoFailed(final DTOEventListener onDtoFailed) {
        this.onDtoFailed = onDtoFailed;
    }

    /**
     * @param onEntityAssembled  listener
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
}
