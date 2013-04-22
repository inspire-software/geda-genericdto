/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.dsl;

import org.junit.Ignore;

import java.util.List;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 1:48 PM
 */
@Ignore
public class MyDtoClass {

    private boolean field1;
    private String field2;
    private MyDtoField3Class field3;
    private MyDtoField4Class field4parent;
    private String field5virtual;
    private List<MyDtoField3Class> field6;

    public boolean getField1() {
        return field1;
    }

    public void setField1(final boolean field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(final String field2) {
        this.field2 = field2;
    }

    public MyDtoField3Class getField3() {
        return field3;
    }

    public void setField3(final MyDtoField3Class field3) {
        this.field3 = field3;
    }

    public MyDtoField4Class getField4parent() {
        return field4parent;
    }

    public void setField4parent(final MyDtoField4Class field4parent) {
        this.field4parent = field4parent;
    }

    public String getField5virtual() {
        return field5virtual;
    }

    public void setField5virtual(final String field5virtual) {
        this.field5virtual = field5virtual;
    }

    public List<MyDtoField3Class> getField6() {
        return field6;
    }

    public void setField6(final List<MyDtoField3Class> field6) {
        this.field6 = field6;
    }
}
