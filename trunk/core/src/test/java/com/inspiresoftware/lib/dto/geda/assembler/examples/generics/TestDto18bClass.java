

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.generics;

import java.util.ArrayList;

import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;


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
@Dto
@Ignore
public class TestDto18bClass<V, C, M> {

	/** Basic matcher. */
    public static class TestDto18Matcher implements DtoToEntityMatcher<TestDto18aClass<String>, TestEntity18aClass<String>> {
    	/** {@inheritDoc} */
    	public boolean match(final TestDto18aClass<String> testDto15Class, final TestEntity18aClass<String> testEntity15Class) {
            return testDto15Class.getMyProp().equals(testEntity15Class.getMyProp());
        }
    }
	
	@DtoField("myProp")
	private V myProp;
	
	@DtoCollection(value = "myColl",
			   	   dtoBeanKey = "collItem",
			   	   entityBeanKeys = { "entityItem" },
			   	   dtoToEntityMatcher = TestDto18Matcher.class,
			   	   entityGenericType = TestEntity18aClass.class)
	private C myColl;

	@DtoMap(value = "myMap",
			dtoBeanKey = "colItem",
			entityBeanKeys = { "entityItem" },
			dtoToEntityMatcher = TestDto18Matcher.class,
			entityGenericType = TestEntity18aClass.class,
			entityMapOrCollectionClass = ArrayList.class,
			entityCollectionMapKey = "myProp")
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
