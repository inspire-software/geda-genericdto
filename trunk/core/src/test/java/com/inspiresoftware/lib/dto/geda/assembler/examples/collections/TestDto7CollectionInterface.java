
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
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:30:03 AM
 */
@Ignore
public interface TestDto7CollectionInterface {
    
	/** {@inheritDoc} */
    java.util.Collection<TestDto7CollectionSubInterface> getNestedString();

    /** {@inheritDoc} */
    void setNestedString(java.util.Collection<TestDto7CollectionSubInterface> nestedString);
}
