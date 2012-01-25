
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.simple;

import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto6Class {

	@DtoField("entityId")
	private Long myLong;
	@DtoField(value = "name", readOnly = true)
	private String myString;
	@DtoField(value = "number", readOnly = true)
	private Double myDouble;

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
