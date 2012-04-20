
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.examples.collections;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;

import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:38:26 AM
 */
@Ignore
@Dto
public class TestDto7iCollectionClass implements TestDto7CollectionInterface {

    @DtoCollection(
            value = "collection",
            entityBeanKeys = "dp.lib.dto.geda.assembler.TestEntity7iCollectionSubClass",
            dtoBeanKey = "dp.lib.dto.geda.assembler.TestDto7iCollectionSubClass",
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
