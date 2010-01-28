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
class TestDto1Class implements TestDto1Interface {

	@DtoField("entityId")
	private Long myLong;
	@DtoField("name")
	private String myString;
	@DtoField("number")
	private Double myDouble;

	/** {@inheritDoc} */
	public Long getMyLong() {
		return myLong;
	}
	/** {@inheritDoc} */
	public void setMyLong(final Long myLong) {
		this.myLong = myLong;
	}
	/** {@inheritDoc} */
	public String getMyString() {
		return myString;
	}
	/** {@inheritDoc} */
	public void setMyString(final String myString) {
		this.myString = myString;
	}
	/** {@inheritDoc} */
	public Double getMyDouble() {
		return myDouble;
	}
	/** {@inheritDoc} */
	public void setMyDouble(final Double myDouble) {
		this.myDouble = myDouble;
	}

}
