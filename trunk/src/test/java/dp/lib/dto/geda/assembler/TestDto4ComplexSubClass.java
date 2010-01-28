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
 * Time: 10:43:18 PM
 */
@Dto
@Ignore
public class TestDto4ComplexSubClass {

    @DtoField("name")
	private String nestedName;

    
    public String getNestedName() {
        return nestedName;
    }

    public void setNestedName(final String nestedName) {
        this.nestedName = nestedName;
    }
}
