/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 12:00:16 PM
 */
@Ignore
public class Test7iMatcher implements DtoToEntityMatcher<TestDto7CollectionSubInterface, TestEntity7CollectionSubInterface> {

    public boolean match(final TestDto7CollectionSubInterface testDto7CollectionSubClass,
                         final TestEntity7CollectionSubInterface testEntity7CollectionSubClass) {

        final String dtoName = testDto7CollectionSubClass.getName();
        final String entityName = testEntity7CollectionSubClass.getName();

        return dtoName != null && entityName != null && dtoName.equals(entityName);
    }
}
