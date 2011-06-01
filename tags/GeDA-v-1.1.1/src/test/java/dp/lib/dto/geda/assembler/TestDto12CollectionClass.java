
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.util.Collection;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto12CollectionClass implements TestDto12CollectionIterface {
	
	@DtoCollection(value = "collectionWrapper.items",
				   dtoBeanKey = "dtoItem",
				   entityBeanKeys = { "nestedEntity", "entityItem" },
				   dtoToEntityMatcher = Test12CollectionItemsMatcher.class,
				   entityGenericType = TestEntity12CollectionItemInterface.class)
	private Collection<TestDto12CollectionItemIterface> items;

	/**
	 * @return items
	 */
	public Collection<TestDto12CollectionItemIterface> getItems() {
		return items;
	}

	/**
	 * @param items items
	 */	
	public void setItems(final Collection<TestDto12CollectionItemIterface> items) {
		this.items = items;
	} 
	
	
	
}
