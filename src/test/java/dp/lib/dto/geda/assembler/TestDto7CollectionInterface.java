/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:30:03 AM
 */
@Ignore
public interface TestDto7CollectionInterface {
    
    java.util.Collection<TestDto7CollectionSubInterface> getNestedString();

    void setNestedString(java.util.Collection<TestDto7CollectionSubInterface> nestedString);
}
