
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoMap;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto12MapToCollectionClass implements TestDto12MapIterface {
	
	@DtoMap(value = "collectionWrapper.items",
			   dtoBeanKey = "dtoItem",
			   entityBeanKeys = { "nestedEntity", "entityItem" },
			   dtoToEntityMatcher = Test12KeyMapToEntityMatcher.class,
			   entityGenericType = TestEntity12CollectionItemInterface.class,
			   entityMapOrCollectionClass = ArrayList.class,
			   entityCollectionMapKey = "name")
	private Map<String, TestDto12CollectionItemIterface> items;

	/**
	 * @return items.
	 */
	public Map<String, TestDto12CollectionItemIterface> getItems() {
		return items;
	}

	/**
	 * @param items items
	 */
	public void setItems(final Map<String, TestDto12CollectionItemIterface> items) {
		this.items = items;
	} 
	
	
	
}
