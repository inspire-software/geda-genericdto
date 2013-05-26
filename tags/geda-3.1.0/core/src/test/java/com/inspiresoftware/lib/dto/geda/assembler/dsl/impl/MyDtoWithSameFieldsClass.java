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

import java.math.BigDecimal;

/**
 * User: denispavlov
 * Date: 13-04-24
 * Time: 9:34 AM
 */
public class MyDtoWithSameFieldsClass {

    private String field1;
    private Boolean field2;
    private boolean field3;
    private Integer field4;
    private int field5;
    private Long field6;
    private long field7;
    private Double field8;
    private double field9;
    private BigDecimal field10;
    private Object field11;

    private String wrongName;
    private Integer wrongType;

    public String getField1() {
        return field1;
    }

    public void setField1(final String field1) {
        this.field1 = field1;
    }

    public Boolean getField2() {
        return field2;
    }

    public void setField2(final Boolean field2) {
        this.field2 = field2;
    }

    public boolean getField3() {
        return field3;
    }

    public void setField3(final boolean field3) {
        this.field3 = field3;
    }

    public Integer getField4() {
        return field4;
    }

    public void setField4(final Integer field4) {
        this.field4 = field4;
    }

    public int getField5() {
        return field5;
    }

    public void setField5(final int field5) {
        this.field5 = field5;
    }

    public Long getField6() {
        return field6;
    }

    public void setField6(final Long field6) {
        this.field6 = field6;
    }

    public long getField7() {
        return field7;
    }

    public void setField7(final long field7) {
        this.field7 = field7;
    }

    public Double getField8() {
        return field8;
    }

    public void setField8(final Double field8) {
        this.field8 = field8;
    }

    public double getField9() {
        return field9;
    }

    public void setField9(final double field9) {
        this.field9 = field9;
    }

    public BigDecimal getField10() {
        return field10;
    }

    public void setField10(final BigDecimal field10) {
        this.field10 = field10;
    }

    public Object getField11() {
        return field11;
    }

    public void setField11(final Object field11) {
        this.field11 = field11;
    }

    public String getWrongName() {
        return wrongName;
    }

    public void setWrongName(final String wrongName) {
        this.wrongName = wrongName;
    }

    public Integer getWrongType() {
        return wrongType;
    }

    public void setWrongType(final Integer wrongType) {
        this.wrongType = wrongType;
    }
}
