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

import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 2:48:43 PM
 */
@Ignore
public class TestEntity32Class implements TestEntity32Interface {

    private String field32;

    public String getField32() {
        return field32;
    }

    public void setField32(final String field32) {
        this.field32 = field32;
    }
}
