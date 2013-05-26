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

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.dsl.DtoCollectionContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoEntityContext;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 2:42 PM
 */
public class DtoCollectionContextImpl implements DtoCollectionContext {

    private final DtoEntityContext dtoEntityContext;

    private final String dtoField;
    private String entityField;

    private boolean readOnly;
    private String[] entityBeanKeys;
    private String dtoBeanKey;

    private Class entityCollectionClass;
    private String entityCollectionClassKey;

    private Class dtoCollectionClass;
    private String dtoCollectionClassKey;

    private Class entityGenericType;
    private String entityGenericTypeKey;

    private Class<? extends DtoToEntityMatcher> dtoToEntityMatcher;
    private String dtoToEntityMatcherKey;

    public DtoCollectionContextImpl(final DtoEntityContext dtoEntityContext, final String fieldName) {
        this.dtoEntityContext = dtoEntityContext;
        this.dtoField = fieldName;
        this.entityField = fieldName;
        this.readOnly = false;
        this.entityCollectionClass = ArrayList.class;
        this.dtoCollectionClass = ArrayList.class;
        this.entityGenericType = Object.class;
        this.dtoToEntityMatcher = DtoToEntityMatcher.class;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext forField(final String fieldName) {
        this.entityField = fieldName;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext readOnly() {
        this.readOnly = true;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext entityCollectionClass(final Class<? extends Collection> entityCollectionClass) {
        this.entityCollectionClass = entityCollectionClass;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext entityCollectionClassKey(final String entityCollectionClassKey) {
        this.entityCollectionClassKey = entityCollectionClassKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext dtoCollectionClass(final Class<? extends Collection> dtoCollectionClass) {
        this.dtoCollectionClass = dtoCollectionClass;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext dtoCollectionClassKey(final String dtoCollectionClassKey) {
        this.dtoCollectionClassKey = dtoCollectionClassKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext entityBeanKeys(final String... entityBeanKeys) {
        this.entityBeanKeys = entityBeanKeys;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext dtoBeanKey(final String dtoBeanKey) {
        this.dtoBeanKey = dtoBeanKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext entityGenericType(final Class entityGenericType) {
        this.entityGenericType = entityGenericType;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext entityGenericTypeKey(final String entityGenericTypeKey) {
        this.entityGenericTypeKey = entityGenericTypeKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext dtoToEntityMatcher(final Class<? extends DtoToEntityMatcher> dtoToEntityMatcher) {
        this.dtoToEntityMatcher = dtoToEntityMatcher;
        return this;
    }

    /** {@inheritDoc} */
    public DtoCollectionContext dtoToEntityMatcherKey(final String dtoToEntityMatcherKey) {
        this.dtoToEntityMatcherKey = dtoToEntityMatcherKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoEntityContext and() {
        return dtoEntityContext;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoField() {
        return dtoField;
    }

    /** {@inheritDoc} */
    public String getValueOfEntityField() {
        return entityField;
    }

    /** {@inheritDoc} */
    public boolean getValueOfReadOnly() {
        return readOnly;
    }

    /** {@inheritDoc} */
    public String[] getValueOfEntityBeanKeys() {
        return entityBeanKeys;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoBeanKey() {
        return dtoBeanKey;
    }

    /** {@inheritDoc} */
    public Class getValueOfEntityCollectionClass() {
        return entityCollectionClass;
    }

    /** {@inheritDoc} */
    public String getValueOfEntityCollectionClassKey() {
        return entityCollectionClassKey;
    }

    /** {@inheritDoc} */
    public Class getValueOfDtoCollectionClass() {
        return dtoCollectionClass;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoCollectionClassKey() {
        return dtoCollectionClassKey;
    }

    /** {@inheritDoc} */
    public Class getValueOfEntityGenericType() {
        return entityGenericType;
    }

    /** {@inheritDoc} */
    public String getValueOfEntityGenericTypeKey() {
        return entityGenericTypeKey;
    }

    /** {@inheritDoc} */
    public Class<? extends DtoToEntityMatcher> getValueOfDtoToEntityMatcher() {
        return dtoToEntityMatcher;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoToEntityMatcherKey() {
        return dtoToEntityMatcherKey;
    }

}
