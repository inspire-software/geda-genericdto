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

    private final BeanFactory dtoFactory;
    private final ValueConverterRepository dtoValueConverters = new ValueConverterRepositoryImpl();

    private DTOEventListener onDtoAssembled;
    private DTOEventListener onDtoFailed;
    private DTOEventListener onEntityAssembled;
    private DTOEventListener onEntityFailed;


    public DTOSupportImpl(final BeanFactory dtoFactory) {
        this.dtoFactory = dtoFactory;
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
        try {
            DTOAssembler.newAssembler(dto.getClass(), entity.getClass())
                    .assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.dtoFactory);
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
    public <T> T assembleDto(final Class dtoClass, final T dto, final Object entity) {
        try {
            DTOAssembler.newAssembler(dtoClass, entity.getClass())
                    .assembleDto(dto, entity, this.dtoValueConverters.getAll(), this.dtoFactory);
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
    public <T> Collection<T> assembleDtos(final Class dtoClass, final Collection<T> dtos, final Collection entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            try {
                DTOAssembler.newAssembler(dtoClass, entities.iterator().next().getClass())
                    .assembleDtos(dtos, entities, this.dtoValueConverters.getAll(), this.dtoFactory);
            } catch (final RuntimeException re) {
                if (this.onDtoFailed != null) {
                    this.onDtoFailed.onEvent(dtos, entities, re);
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Exception skipped by event listener", re);
                    }
                    return dtos;
                }
                throw re; // re-throw
            }
        }
        if (this.onDtoAssembled != null) {
            this.onDtoAssembled.onEvent(dtos, entities);
        }
        return dtos;
    }

    /** {@inheritDoc} */
    public <T> T assembleEntity(final Object dto, final T entity) {
        try {
            DTOAssembler.newAssembler(dto.getClass(), entity.getClass())
                    .assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.dtoFactory);
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
    public <T> T assembleEntity(final Class dtoClass, final Object dto, final T entity) {
        try {
            DTOAssembler.newAssembler(dtoClass, entity.getClass())
                    .assembleEntity(dto, entity, this.dtoValueConverters.getAll(), this.dtoFactory);
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
    public <T> Collection<T> assembleEntities(final Class entityClass, final Collection dtos, final Collection<T> entities) {
        if (!CollectionUtils.isEmpty(dtos)) {
            try {
                DTOAssembler.newAssembler(dtos.iterator().next().getClass(), entityClass)
                    .assembleEntities(dtos, entities, this.dtoValueConverters.getAll(), this.dtoFactory);
            } catch (final RuntimeException re) {
                if (this.onEntityFailed != null) {
                    this.onEntityFailed.onEvent(dtos, entities, re);
                    if (LOG.isErrorEnabled()) {
                        LOG.error("Exception skipped by event listener", re);
                    }
                    return entities;
                }
                throw re; // re-throw
            }
        }
        if (this.onEntityAssembled != null) {
            this.onEntityAssembled.onEvent(dtos, entities);
        }
        return entities;
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
