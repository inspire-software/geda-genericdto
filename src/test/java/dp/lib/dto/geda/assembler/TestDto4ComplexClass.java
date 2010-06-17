
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 24, 2010
 * Time: 10:42:07 PM
 */
@Dto
@Ignore
public class TestDto4ComplexClass {

	@DtoField(
            value = "wrapper",
            entityBeanKeys = { "dp.lib.dto.geda.assembler.TestEntity4SubClass" },
            dtoBeanKey = "dp.lib.dto.geda.assembler.TestDto4ComplexSubClass")
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
