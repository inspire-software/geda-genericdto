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

import com.inspiresoftware.lib.dto.geda.dsl.DtoEntityContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoFieldContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoParentContext;
import com.inspiresoftware.lib.dto.geda.dsl.DtoVirtualFieldContext;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 2:41 PM
 */
public class DtoFieldContextImpl implements DtoFieldContext {

    private final DtoEntityContext dtoEntityContext;

    private final String dtoField;
    private String entityField;

    private boolean readOnly;
    private String[] entityBeanKeys;
    private String dtoBeanKey;

    private boolean virtual;
    private String converter;

    private boolean dtoParent;
    private String dtoParentPrimaryKey;
    private String dtoParentRetriever;

    public DtoFieldContextImpl(final DtoEntityContext dtoEntityContext, final String fieldName) {
        this.dtoEntityContext = dtoEntityContext;
        this.dtoField = fieldName;
        this.entityField = fieldName;
        this.virtual = false;
        this.readOnly = false;
        this.dtoParent = false;
    }

    /** {@inheritDoc} */
    public DtoFieldContext forField(final String fieldName) {
        this.entityField = fieldName;
        return this;
    }

    /** {@inheritDoc} */
    public DtoVirtualFieldContext forVirtual() {
        this.virtual = true;
        return new DtoVirtualFieldContext() {
            public DtoFieldContext converter(final String converter) {
                return DtoFieldContextImpl.this.converter(converter);
            }
        };
    }

    /** {@inheritDoc} */
    public DtoFieldContext readOnly() {
        this.readOnly = true;
        return this;
    }

    /** {@inheritDoc} */
    public DtoFieldContext converter(final String converter) {
        this.converter = converter;
        return this;
    }

    public DtoFieldContext entityBeanKeys(final String... entityBeanKeys) {
        this.entityBeanKeys = entityBeanKeys;
        return this;
    }

    /** {@inheritDoc} */
    public DtoFieldContext dtoBeanKey(final String dtoBeanKey) {
        this.dtoBeanKey = dtoBeanKey;
        return this;
    }

    /** {@inheritDoc} */
    public DtoParentContext dtoParent(final String primaryKeyFieldName) {
        this.dtoParent = true;
        this.dtoParentPrimaryKey = primaryKeyFieldName;
        return new DtoParentContext() {
            public DtoFieldContext retriever(final String retriever) {
                DtoFieldContextImpl.this.dtoParentRetriever = retriever;
                return DtoFieldContextImpl.this;
            }
        };
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
    public boolean getValueOfVirtual() {
        return virtual;
    }

    /** {@inheritDoc} */
    public String getValueOfConverter() {
        return converter;
    }

    /** {@inheritDoc} */
    public boolean getValueOfDtoParent() {
        return dtoParent;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoParentPrimaryKey() {
        return dtoParentPrimaryKey;
    }

    /** {@inheritDoc} */
    public String getValueOfDtoParentRetriever() {
        return dtoParentRetriever;
    }

}
