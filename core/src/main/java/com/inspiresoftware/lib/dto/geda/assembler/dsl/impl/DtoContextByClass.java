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
import com.inspiresoftware.lib.dto.geda.assembler.dsl.DtoEntityContext;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToLocateRepresentationException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 1:30 PM
 */
public class DtoContextByClass implements DtoContext {

    private final Class dtoClass;
    private final ExtensibleBeanFactory beanFactory;

    private final Map<Integer, DtoEntityContext> contexts = new ConcurrentHashMap<Integer, DtoEntityContext>();

    public DtoContextByClass(final Class dtoClass,
                             final ExtensibleBeanFactory beanFactory) {
        this.dtoClass = dtoClass;
        this.beanFactory = beanFactory;
    }

    /** {@inheritDoc} */
    public Class getDtoClass() {
        return dtoClass;
    }

    /** {@inheritDoc} */
    public DtoContext alias(final String beanKey) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Alias for " + beanKey + " cannot be registered. Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        this.beanFactory.registerDto(beanKey, dtoClass.getCanonicalName());
        return this;
    }

    /** {@inheritDoc} */
    public DtoEntityContext forEntity(final Class entityClass) {
        if (entityClass == null) {
            throw new GeDARuntimeException("dtoClass must not be null");
        }
        final int hash = entityClass.hashCode();
        if (contexts.containsKey(hash)) {
            return contexts.get(hash);
        }
        final DtoEntityContext ctx = new DtoEntityContextByClass(dtoClass, entityClass, beanFactory);
        contexts.put(hash, ctx);
        return ctx;
    }

    /** {@inheritDoc} */
    public DtoEntityContext forEntity(final Object entityInstance) {
        if (entityInstance == null) {
            throw new GeDARuntimeException("entityInstance must not be null");
        }
        return forEntity(entityInstance.getClass());
    }

    /** {@inheritDoc} */
    public DtoEntityContext forEntity(final String beanKey) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        final Class representative = beanFactory.getClazz(beanKey);
        if (representative == null) {
            throw new BeanFactoryUnableToLocateRepresentationException(beanFactory.toString(), "top level", beanKey, false);
        }
        return forEntity(representative);
    }
}
