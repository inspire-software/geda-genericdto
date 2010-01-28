/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import org.junit.Ignore;

/**
 * Test matches that matches the Strings.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 3:34:16 PM
 */
@Ignore
public class Test7Matcher implements DtoToEntityMatcher<TestDto7CollectionSubClass, TestEntity7CollectionSubClass> {

    public boolean match(final TestDto7CollectionSubClass testDto7CollectionSubClass, final TestEntity7CollectionSubClass testEntity7CollectionSubClass) {
        final String dtoName = testDto7CollectionSubClass.getName();
        final String entityName = testEntity7CollectionSubClass.getName();

        return dtoName != null && entityName != null && dtoName.equals(entityName);
    }
}
