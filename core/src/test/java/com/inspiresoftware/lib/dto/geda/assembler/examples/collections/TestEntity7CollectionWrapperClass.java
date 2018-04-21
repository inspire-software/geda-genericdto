
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

import org.junit.Ignore;

/**
 * .
 *
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 1:55:45 PM
 */
@Ignore
public class TestEntity7CollectionWrapperClass implements TestEntity7CollectionWrapperInterface  {

    private TestEntity7CollectionInterface wrapper;

    /** {@inheritDoc} */
    public TestEntity7CollectionInterface getWrapper() {
        return wrapper;
    }

    /** {@inheritDoc} */
    public void setWrapper(final TestEntity7CollectionInterface collection) {
        this.wrapper = collection;
    }

}
