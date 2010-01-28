/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
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
class TestDto2Class {

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
