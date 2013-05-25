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
 * Time: 9:42 AM
 */
public interface MyEntityWithSameFieldsInterface {
    String getField1();

    void setField1(String field1);

    Boolean getField2();

    void setField2(Boolean field2);

    boolean getField3();

    void setField3(boolean field3);

    Integer getField4();

    void setField4(Integer field4);

    int getField5();

    void setField5(int field5);

    Long getField6();

    void setField6(Long field6);

    long getField7();

    void setField7(long field7);

    Double getField8();

    void setField8(Double field8);

    double getField9();

    void setField9(double field9);

    BigDecimal getField10();

    void setField10(BigDecimal field10);

    Object getField11();

    void setField11(Object field11);

    String getWrongNameOnEntity();

    void setWrongNameOnEntity(String wrongNameOnEntity);

    Double getWrongType();

    void setWrongType(Double wrongType);
}
