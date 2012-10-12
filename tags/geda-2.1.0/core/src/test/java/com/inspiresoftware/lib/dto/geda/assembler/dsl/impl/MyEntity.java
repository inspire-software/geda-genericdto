/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl.impl;

import java.util.Set;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 3:20 PM
 */
public interface MyEntity {
    Field1 getField1();

    void setField1(Field1 field1);

    MyEntityField2Class getField2();

    void setField2(MyEntityField2Class field2);

    MyEntityField3Class getField3();

    void setField3(MyEntityField3Class field3);

    MyEntityField4Class getField4parent();

    void setField4parent(MyEntityField4Class field4parent);

    Set<MyEntityField3Class> getField6();

    void setField6(Set<MyEntityField3Class> field6);

    public enum Field1 { YES, NO }
}
