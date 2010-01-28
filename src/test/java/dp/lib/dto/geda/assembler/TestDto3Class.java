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
class TestDto3Class {
	
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
