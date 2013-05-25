
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.nested;

import org.junit.Ignore;

/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity4Class {
	
	private TestEntity4SubClass wrapper;

	/**
	 * @return property wrapper.
	 */
	public TestEntity4SubClass getWrapper() {
		return wrapper;
	}
	/**
	 * @param wrapper property wrapper.
	 */
	public void setWrapper(final TestEntity4SubClass wrapper) {
		this.wrapper = wrapper;
	}

		

}
