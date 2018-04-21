
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
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import org.junit.Ignore;

/**
 * .
 *
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 1:53:58 PM
 */
@Dto
@Ignore
public class TestDto7CollectionClass {

    @DtoCollection(
            value = "collection",
            entityBeanKeys = "com.inspiresoftware.lib.dto.geda.assembler.TestEntity7CollectionSubClass",
            dtoBeanKey = "com.inspiresoftware.lib.dto.geda.assembler.TestDto7CollectionSubClass",
            dtoToEntityMatcher = Test7Matcher.class,
            entityGenericType = TestEntity7CollectionSubClass.class
    )
	private java.util.Collection<TestDto7CollectionSubClass> nestedString;

	/**
	 * @return nested property.
	 */
	public java.util.Collection<TestDto7CollectionSubClass> getNestedString() {
		return nestedString;
	}
	/**
	 * @param nestedString nested property.
	 */
	public void setNestedString(final java.util.Collection<TestDto7CollectionSubClass> nestedString) {
		this.nestedString = nestedString;
	}

}
