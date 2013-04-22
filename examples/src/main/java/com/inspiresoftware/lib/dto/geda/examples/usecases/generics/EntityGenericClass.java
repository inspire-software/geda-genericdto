

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.1.1
 * 
 * @param <V> simple value
 * @param <C> collection
 * @param <M> map
 *
 */
@Ignore
public class EntityGenericClass<V, C, M> {

	private V myProp;
	
	private C myColl;

	private M myMap;

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

	/**
	 * @return property
	 */
	public C getMyColl() {
		return myColl;
	}

	/**
	 * @param myColl property
	 */
	public void setMyColl(final C myColl) {
		this.myColl = myColl;
	}

	/**
	 * @return property
	 */
	public M getMyMap() {
		return myMap;
	}

	/**
	 * @param myMap property
	 */
	public void setMyMap(final M myMap) {
		this.myMap = myMap;
	}
	
}
