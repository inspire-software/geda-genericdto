
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.nested;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 24, 2010
 * Time: 10:43:18 PM
 */
@Dto
@Ignore
public class TestDto4ComplexSubClass {

    @DtoField("name")
	private String nestedName;

    /** {@inheritDoc} */
    public String getNestedName() {
        return nestedName;
    }

    /** {@inheritDoc} */
    public void setNestedName(final String nestedName) {
        this.nestedName = nestedName;
    }
}
