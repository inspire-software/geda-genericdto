/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl.impl;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.DtoEntityContext;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.DtoMapContext;

import java.util.HashMap;
import java.util.Map;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 2:43 PM
 */
public class DtoMapContextImpl implements DtoMapContext {

    private final DtoEntityContext dtoEntityContext;

    private final String dtoField;
    private String entityField;

    private boolean readOnly;
    private String[] entityBeanKeys;
    private String dtoBeanKey;

    private Class entityMapOrCollectionClass;
    private String entityMapOrCollectionClassKey;

    private Class dtoMapClass;
    private String dtoMapClassKey;

    private Class entityGenericType;
    private String entityGenericTypeKey;

    private String entityCollectionMapKey;
    private boolean useEntityMapKey;

    private Class<? extends DtoToEntityMatcher> dtoToEntityMatcher;
    private String dtoToEntityMatcherKey;

    public DtoMapContextImpl(final DtoEntityContext dtoEntityContext, final String fieldName) {
        this.dtoEntityContext = dtoEntityContext;
        this.dtoField = fieldName;
        this.entityField = fieldName;
        this.readOnly = false;
        this.useEntityMapKey = false;
        this.entityMapOrCollectionClass = HashMap.class;
        this.dtoMapClass = HashMap.class;
        this.entityGenericType = Object.class;
        this.dtoToEntityMatcher = DtoToEntityMatcher.class;
    }

    /** {@inheritDoc} */
    public DtoMapContext forField(final String fieldName) {
        this.entityField = fieldName;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext readOnly() {
        this.readOnly = true;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext entityMapOrCollectionClass(final Class entityMapOrCollectionClass) {
        this.entityMapOrCollectionClass = entityMapOrCollectionClass;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext entityMapOrCollectionClassKey(final String entityMapOrCollectionClassKey) {
        this.entityMapOrCollectionClassKey = entityMapOrCollectionClassKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext dtoMapClass(final Class<? extends Map> dtoMapClass) {
        this.dtoMapClass = dtoMapClass;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext dtoMapClassKey(final String dtoMapClassKey) {
        this.dtoMapClassKey = dtoMapClassKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext entityBeanKeys(final String... entityBeanKeys) {
        this.entityBeanKeys = entityBeanKeys;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext dtoBeanKey(final String dtoBeanKey) {
        this.dtoBeanKey = dtoBeanKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext entityGenericType(final Class entityGenericType) {
        this.entityGenericType = entityGenericType;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext entityGenericTypeKey(final String entityGenericTypeKey) {
        this.entityGenericTypeKey = entityGenericTypeKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext entityCollectionMapKey(final String entityCollectionMapKey) {
        this.entityCollectionMapKey = entityCollectionMapKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext useEntityMapKey() {
        this.useEntityMapKey = true;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext dtoToEntityMatcher(final Class<? extends DtoToEntityMatcher> dtoToEntityMatcher) {
        this.dtoToEntityMatcher = dtoToEntityMatcher;
        return this;
    }

    /** {@inheritDoc} */
    public DtoMapContext dtoToEntityMatcherKey(final String dtoToEntityMatcherKey) {
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
    public Class getValueOfEntityMapOrCollectionClass() {
        return entityMapOrCollectionClass;
    }

    /** {@inheritDoc} */
    public String getValueOfEntityMapOrCollectionClassKey() {
        return entityMapOrCollectionClassKey;
    }

    /** {@inheritDoc} */
    public Class getValueOfDtoMapClass() {
        return dtoMapClass;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoMapClassKey() {
        return dtoMapClassKey;
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
    public String getValueOfEntityCollectionMapKey() {
        return entityCollectionMapKey;
    }

    /** {@inheritDoc} */
    public boolean getValueOfUseEntityMapKey() {
        return useEntityMapKey;
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
