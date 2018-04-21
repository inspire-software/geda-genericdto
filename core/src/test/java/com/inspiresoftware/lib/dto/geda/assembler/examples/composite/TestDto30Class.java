/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.composite;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

/**
 * .
 *
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 2:49:07 PM
 */
@Ignore
@Dto({
        "com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestEntity30Class",
        "com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestEntity31Class",
        "com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestEntity32Class"
    })
public class TestDto30Class implements TestDto30Interface {

    @DtoField
    private String field30;
    @DtoField
    private String field31;
    @DtoField
    private String field32;

    public String getField30() {
        return field30;
    }

    public void setField30(final String field30) {
        this.field30 = field30;
    }

    public String getField31() {
        return field31;
    }

    public void setField31(final String field31) {
        this.field31 = field31;
    }

    public String getField32() {
        return field32;
    }

    public void setField32(final String field32) {
        this.field32 = field32;
    }
}
