/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactoryProvider;
import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.dsl.DtoContext;
import com.inspiresoftware.lib.dto.geda.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToLocateRepresentationException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default DSL registry implementation. Keeps reference to extensible bean factory if
 * one was supplied during construction of this registry.
 *
 * This is a throw away disposable container that should not be used once
 * releaseResources had been invoked.
 *
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 1:16 PM
 */
public class DefaultDSLRegistry implements Registry, BeanFactory, BeanFactoryProvider {

    private final ExtensibleBeanFactory beanFactory;

    private final Map<Integer, DtoContext> contexts = new ConcurrentHashMap<Integer, DtoContext>();

    /**
     * Default registry instance without bean factory support.
     *
     * @deprecated DSL registry must always be bound to a bean factory to avoid some mapping features errors
     */
    @Deprecated
    public DefaultDSLRegistry() {

        this(null);

    }

    /**
     * Default registry instance with bean factory support.
     *
     * @param beanFactory bean factory
     */
    public DefaultDSLRegistry(final ExtensibleBeanFactory beanFactory) {

        this.beanFactory = beanFactory;

    }

    /** {@inheritDoc} */
    public DtoContext has(final Class dtoClass) {
        if (dtoClass == null) {
            throw new GeDARuntimeException("dtoClass must not be null");
        }
        final int hash = dtoClass.hashCode();
        if (contexts.containsKey(hash)) {
            return contexts.get(hash);
        }
        return null;
    }

    /** {@inheritDoc} */
    public DtoContext dto(final Class dtoClass) {
        if (dtoClass == null) {
            throw new GeDARuntimeException("dtoClass must not be null");
        }
        final int hash = dtoClass.hashCode();
        if (contexts.containsKey(hash)) {
            return contexts.get(hash);
        }
        final DtoContext ctx = new DtoContextByClass(dtoClass, beanFactory);
        contexts.put(hash, ctx);
        return ctx;
    }

    /** {@inheritDoc} */
    public DtoContext dto(final Object dtoInstance) {
        if (dtoInstance == null) {
            throw new GeDARuntimeException("dtoInstance must not be null");
        }
        return dto(dtoInstance.getClass());
    }

    /** {@inheritDoc} */
    public DtoContext dto(final String beanKey) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        final Class representative = beanFactory.getClazz(beanKey);
        if (representative == null) {
            throw new BeanFactoryUnableToLocateRepresentationException(beanFactory.toString(), "top level", beanKey, true);
        }
        return dto(representative);
    }

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        return beanFactory.getClazz(entityBeanKey);
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        return beanFactory.get(entityBeanKey);
    }

    /** {@inheritDoc} */
    public BeanFactory getBeanFactory() {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        return beanFactory;
    }

    /** {@inheritDoc} */
    public void releaseResources() {
        synchronized (this) {
            contexts.clear();
            if (beanFactory != null) {
                beanFactory.releaseResources();
            }
        }
    }
}
