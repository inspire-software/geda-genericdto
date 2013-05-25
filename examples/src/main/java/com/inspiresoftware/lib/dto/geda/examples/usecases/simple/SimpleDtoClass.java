

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
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
public class SimpleDtoClass {

	@DtoField("entityId")
	private Long myLong;
	@DtoField("name")
	private String myString;
	@DtoField("number")
	private Double myDouble;
	@DtoField("decision")
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
