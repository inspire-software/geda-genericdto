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
class TestDto5Class {
		
	@DtoField(
            value = "wrapper.wrapper.name",
            entityBeanKeys = { "wrapper.key", "wrapper.wrapper.key" })
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
