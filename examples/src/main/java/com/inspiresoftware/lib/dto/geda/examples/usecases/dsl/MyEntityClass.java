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

import java.util.Set;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 3:20 PM
 */
@Ignore
public class MyEntityClass implements MyEntity {

    private Field1 field1;
    private MyEntityField2Class field2;
    private MyEntityField3Class field3;
    private MyEntityField4Class field4parent;
    private Set<MyEntityField3Class> field6;

    public Field1 getField1() {
        return field1;
    }

    public void setField1(final Field1 field1) {
        this.field1 = field1;
    }

    public MyEntityField2Class getField2() {
        return field2;
    }

    public void setField2(final MyEntityField2Class field2) {
        this.field2 = field2;
    }

    public MyEntityField3Class getField3() {
        return field3;
    }

    public void setField3(final MyEntityField3Class field3) {
        this.field3 = field3;
    }

    public MyEntityField4Class getField4parent() {
        return field4parent;
    }

    public void setField4parent(final MyEntityField4Class field4parent) {
        this.field4parent = field4parent;
    }

    public Set<MyEntityField3Class> getField6() {
        return field6;
    }

    public void setField6(final Set<MyEntityField3Class> field6) {
        this.field6 = field6;
    }
}
