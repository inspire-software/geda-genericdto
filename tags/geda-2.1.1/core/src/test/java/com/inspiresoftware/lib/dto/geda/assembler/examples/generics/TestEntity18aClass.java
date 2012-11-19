

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.generics;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.1.1
 * 
 * @param <V> simple value
 *
 */
@Ignore
public class TestEntity18aClass<V> {

	private V myProp;
	
	/**
	 * @return property
	 */
	public V getMyProp() {
		return myProp;
	}

	/**
	 * @param myProp property
	 */
	public void setMyProp(final V myProp) {
		this.myProp = myProp;
	}
	
}
