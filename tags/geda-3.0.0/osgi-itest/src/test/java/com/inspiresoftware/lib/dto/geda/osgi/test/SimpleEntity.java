package com.inspiresoftware.lib.dto.geda.osgi.test;

import java.math.BigDecimal;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 8:21 AM
 */
public interface SimpleEntity {
    String getString();

    void setString(String string);

    BigDecimal getDecimal();

    void setDecimal(BigDecimal decimal);

    int getInteger();

    void setInteger(int integer);
}
