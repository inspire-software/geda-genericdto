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
import com.inspiresoftware.lib.dto.geda.dsl.DtoCollectionContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoEntityContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoFieldContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoMapContext;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 1:56 PM
 */
public class DtoEntityContextByClass implements DtoEntityContext {

    private final Class dtoClass;
    private final Class entityClass;
    private final ExtensibleBeanFactory beanFactory;

    private final Map<Integer, Object> fields = new ConcurrentHashMap<Integer, Object>();

    public DtoEntityContextByClass(final Class dtoClass,
                                   final Class entityClass,
                                   final ExtensibleBeanFactory beanFactory) {
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
        this.beanFactory = beanFactory;
    }

    /** {@inheritDoc} */
    public Class getDtoClass() {
        return dtoClass;
    }

    /** {@inheritDoc} */
    public Class getEntityClass() {
        return entityClass;
    }

    /** {@inheritDoc} */
    public DtoEntityContext alias(final String beanKey) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Alias for " + beanKey + " cannot be registered. Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        this.beanFactory.registerDto(beanKey, dtoClass.getCanonicalName());
        return this;
    }

    /** {@inheritDoc} */
    public DtoFieldContext withField(final String fieldName) {
        final int hash = fieldName.hashCode();
        if (fields.containsKey(hash)) {
            final Object field = fields.get(hash);
            if (field instanceof DtoFieldContext) {
                return (DtoFieldContext) field;
            }
            throw new AnnotationDuplicateBindingException(dtoClass.getCanonicalName(), fieldName);
        }
        final DtoFieldContext field = new DtoFieldContextImpl(this, fieldName);
        fields.put(hash, field);
        return field;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext withCollection(final String fieldName) {
        final int hash = fieldName.hashCode();
        if (fields.containsKey(hash)) {
            final Object field = fields.get(hash);
            if (field instanceof DtoCollectionContext) {
                return (DtoCollectionContext) field;
            }
            throw new AnnotationDuplicateBindingException(dtoClass.getCanonicalName(), fieldName);
        }
        final DtoCollectionContext field = new DtoCollectionContextImpl(this, fieldName);
        fields.put(hash, field);
        return field;
    }

    /** {@inheritDoc} */
    public DtoMapContext withMap(final String fieldName) {
        final int hash = fieldName.hashCode();
        if (fields.containsKey(hash)) {
            final Object field = fields.get(hash);
            if (field instanceof DtoMapContext) {
                return (DtoMapContext) field;
            }
            throw new AnnotationDuplicateBindingException(dtoClass.getCanonicalName(), fieldName);
        }
        final DtoMapContext field = new DtoMapContextImpl(this, fieldName);
        fields.put(hash, field);
        return field;
    }

    /** {@inheritDoc} */
    public Object has(final String fieldName) {
        final int hash = fieldName.hashCode();
        if (fields.containsKey(hash)) {
            return fields.get(hash);
        }
        return null;
    }
}
