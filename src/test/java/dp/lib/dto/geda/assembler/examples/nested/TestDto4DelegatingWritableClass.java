
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.examples.nested;

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
public class TestDto4DelegatingWritableClass {

	@DtoField(
            value = "wrapper.name")
	private String nestedString;

	/**
	 * @return nested property.
	 */
	public String getNestedString() {
		return nestedString;
	}
	/**
	 * @param nestedString nested property.
	 */
	public void setNestedString(final String nestedString) {
		this.nestedString = nestedString;
	}

}
