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
            dtoBeanKeys = { "dp.lib.dto.geda.assembler.TestDto4ComplexSubClass" })
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
