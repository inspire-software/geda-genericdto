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

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.DSLUtils;
import com.inspiresoftware.lib.dto.geda.dsl.*;
import com.inspiresoftware.lib.dto.geda.exception.AnnotationDuplicateBindingException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 1:56 PM
 */
public class DtoEntityContextByClass extends DSLUtils implements DtoEntityContext {

    private final Class dtoClass;
    private final Class entityClass;
    private final ExtensibleBeanFactory beanFactory;

    private final DtoEntityContextAppender self = new DtoEntityContextAppender() {
        /** {@inheritDoc} */
        public DtoEntityContext and() {
            return DtoEntityContextByClass.this;
        }
    };

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
    public DtoEntityContext alias(final String beanKey, final Class representative) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Alias for " + beanKey + " cannot be registered. Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        if (entityClass.isInterface()) {
            throw new GeDARuntimeException("No alias for " + beanKey + " as it is mapped to entity interface");
        }
        if (representative == null) {
            this.beanFactory.registerEntity(beanKey, entityClass.getCanonicalName(), entityClass.getCanonicalName());
        } else if (representative.isInterface() && representative.isAssignableFrom(entityClass)) {
            this.beanFactory.registerEntity(beanKey, entityClass.getCanonicalName(), representative.getCanonicalName());
        } else {
            throw new GeDARuntimeException("Alias for " + beanKey + " has invalid interface for given entity class");
        }
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
    public DtoEntityContextAppender withFieldsSameAsIn(final String beanKey, final String... excluding) {
        if (beanFactory == null) {
            throw new GeDARuntimeException("Bean for key " + beanKey + " cannot be looked up. Bean factory must be specified. Use constructor DefaultDSLRegistry(BeanFactory)");
        }
        final Class clazz = beanFactory.getClazz(beanKey);
        if (clazz == null) {
            throw new GeDARuntimeException("Bean for key " + beanKey + " is not in the bean factory");
        }
        return withFieldsSameAsIn(clazz, excluding);
    }

    /** {@inheritDoc} */
    public DtoEntityContextAppender withFieldsSameAsIn(final Class clazz, final String... excluding) {
        final Map<String, String> dtoFields = scanFieldNamesOnClass(getDtoClass());
        if (excluding != null && excluding.length > 0) {
            for (final String exclude : excluding) {
                dtoFields.remove(exclude);
            }
        }
        final Map<String, String> entityFields;
        if (clazz.isInterface()) {
            entityFields = scanGetterNamesOnClass(clazz);
        } else {
            entityFields = scanFieldNamesOnClass(clazz);
        }
        for (final Map.Entry<String, String> dtoField : dtoFields.entrySet()) {
            final String entityFieldType = entityFields.get(dtoField.getKey());
            if (entityFieldType != null && entityFieldType.equals(dtoField.getValue())) {
                // same name, same type - just map it with all defaults
                withField(dtoField.getKey());
            }
        }
        return self;
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
