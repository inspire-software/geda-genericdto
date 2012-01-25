
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.collections;


import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 1:53:58 PM
 */
@Dto
@Ignore
public class TestDto7NestedCollectionClass implements TestDto7CollectionInterface {

    @DtoCollection(
            value = "wrapper.collection",
            entityBeanKeys = { "wrapper", "com.inspiresoftware.lib.dto.geda.assembler.TestEntity7CollectionSubClass" },
            dtoToEntityMatcher = Test7iMatcher.class,
            entityGenericType = TestEntity7CollectionSubInterface.class
    )
	private java.util.Collection<TestDto7CollectionSubInterface> nestedString;

	/**
	 * @return nested property.
	 */
	public java.util.Collection<TestDto7CollectionSubInterface> getNestedString() {
		return nestedString;
	}
	/**
	 * @param nestedString nested property.
	 */
	public void setNestedString(final java.util.Collection<TestDto7CollectionSubInterface> nestedString) {
		this.nestedString = nestedString;
	}

}
