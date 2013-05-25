
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.collections;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
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

    /** {@inheritDoc} */
    public Collection<TestEntity7CollectionSubInterface> getCollection() {
        return collection;
    }

    /** {@inheritDoc} */
    public void setCollection(final Collection<TestEntity7CollectionSubInterface> collection) {
        this.collection = collection;
    }
}
