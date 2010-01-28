/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.annotations.Dto;
import org.junit.Ignore;

import java.util.Collection;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:41:47 AM
 */
@Dto
@Ignore
public class TestEntity7iCollectionClass implements TestEntity7CollectionInterface {

    private Collection<TestEntity7CollectionSubInterface> collection;

    public Collection<TestEntity7CollectionSubInterface> getCollection() {
        return collection;
    }

    public void setCollection(final Collection<TestEntity7CollectionSubInterface> collection) {
        this.collection = collection;
    }
}
