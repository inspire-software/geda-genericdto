/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl.impl;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.DtoContext;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToLocateRepresentationException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 1:16 PM
 */
public class DefaultDSLRegistry implements Registry {

    private final ExtensibleBeanFactory beanFactory;

    private final Map<Integer, DtoContext> contexts = new ConcurrentHashMap<Integer, DtoContext>();

    /**
     * Default registry instance without bean factory support.
     */
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
}
