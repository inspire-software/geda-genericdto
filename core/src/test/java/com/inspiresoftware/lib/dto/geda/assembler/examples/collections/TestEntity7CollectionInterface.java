
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

import java.util.Collection;

/**
 * .
 *
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:33:40 AM
 */
@Ignore
public interface TestEntity7CollectionInterface {

	/** {@inheritDoc} */
    Collection<TestEntity7CollectionSubInterface> getCollection();

    /** {@inheritDoc} */
    void setCollection(Collection<TestEntity7CollectionSubInterface> collection);
}
