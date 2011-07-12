
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
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto3Class {
	
	/**
	 * Test enum for conversion testing.
	 */
	public enum Decision { 
		/** true. */
		Decided, 
		/** false. */
		Undecided 
	};

	@DtoField(value = "decision", converter = "boolToEnum")
	private Decision myEnum;

	/**
	 * @return enum (converted from boolean).
	 */
	public Decision getMyEnum() {
		return myEnum;
	}
	/**
	 * @param myEnum enum (converted from boolean).
	 */
	public void setMyEnum(final Decision myEnum) {
		this.myEnum = myEnum;
	}

	

}
