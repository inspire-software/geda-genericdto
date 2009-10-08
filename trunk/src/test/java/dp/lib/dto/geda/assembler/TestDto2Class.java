/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.Field;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
class TestDto2Class {

	@Field("entityId")
	private Long myLong;
	@Field("name")
	private String myString;
	@Field("number")
	private Double myDouble;
	@Field("decision")
	private Boolean myBoolean;

	/**
	 * @return boolean value.
	 */
	public Boolean getMyBoolean() {
		return myBoolean;
	}
	/**
	 * @param myBoolean boolean value.
	 */
	public void setMyBoolean(final Boolean myBoolean) {
		this.myBoolean = myBoolean;
	}
	/**
	 * @return long value.
	 */
	public Long getMyLong() {
		return myLong;
	}
	/**
	 * @param myLong long value.
	 */
	public void setMyLong(final Long myLong) {
		this.myLong = myLong;
	}
	/**
	 * @return string value.
	 */
	public String getMyString() {
		return myString;
	}
	/**
	 * @param myString string value.
	 */
	public void setMyString(final String myString) {
		this.myString = myString;
	}
	/**
	 * @return double value.
	 */
	public Double getMyDouble() {
		return myDouble;
	}
	/**
	 * @param myDouble double value.
	 */
	public void setMyDouble(final Double myDouble) {
		this.myDouble = myDouble;
	}

}
