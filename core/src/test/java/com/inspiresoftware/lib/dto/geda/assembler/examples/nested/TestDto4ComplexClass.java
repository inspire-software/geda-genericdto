
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
 *
 * User: Denis Pavlov
 * Date: Jan 24, 2010
 * Time: 10:42:07 PM
 */
@Dto
@Ignore
public class TestDto4ComplexClass {

	@DtoField(
            value = "wrapper",
            entityBeanKeys = { "com.inspiresoftware.lib.dto.geda.assembler.TestEntity4SubClass" },
            dtoBeanKey = "com.inspiresoftware.lib.dto.geda.assembler.TestDto4ComplexSubClass")
	private TestDto4ComplexSubClass nestedString;

	/**
	 * @return nested property.
	 */
	public TestDto4ComplexSubClass getNestedString() {
		return nestedString;
	}
	/**
	 * @param nestedString nested property.
	 */
	public void setNestedString(final TestDto4ComplexSubClass nestedString) {
		this.nestedString = nestedString;
	}

}
