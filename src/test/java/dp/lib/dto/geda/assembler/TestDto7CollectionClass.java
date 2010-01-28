/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;
import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 1:53:58 PM
 */
@Dto
@Ignore
public class TestDto7CollectionClass {

    @DtoCollection(
            value = "collection",
            entityBeanKey = "dp.lib.dto.geda.assembler.TestEntity7CollectionSubClass",
            dtoBeanKey = "dp.lib.dto.geda.assembler.TestDto7CollectionSubClass",
            dtoToEntityMatcher = Test7Matcher.class,
            entityGenericType = TestEntity7CollectionSubClass.class
    )
	private java.util.Collection<TestDto7CollectionSubClass> nestedString;

	/**
	 * @return nested property.
	 */
	public java.util.Collection<TestDto7CollectionSubClass> getNestedString() {
		return nestedString;
	}
	/**
	 * @param nestedString nested property.
	 */
	public void setNestedString(final java.util.Collection<TestDto7CollectionSubClass> nestedString) {
		this.nestedString = nestedString;
	}

}
