
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.maps;

import java.util.Map;

import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;
import com.inspiresoftware.lib.dto.geda.assembler.examples.collections.TestDto12CollectionItemIterface;
import com.inspiresoftware.lib.dto.geda.assembler.examples.collections.TestEntity12CollectionItemInterface;


/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto12MapToMapByKeyClass implements TestDto12MapByKeyIterface {
	
	@DtoMap(value = "items",
			   dtoBeanKey = "dtoItem",
			   entityBeanKeys = "entityItem",
			   dtoToEntityMatcher = Test12MapEntityByKeyMatcher.class,
			   entityGenericType = TestEntity12CollectionItemInterface.class,
			   useEntityMapKey = true)
	private Map<TestDto12CollectionItemIterface, String> items;

	/**
	 * @return items
	 */
	public Map<TestDto12CollectionItemIterface, String> getItems() {
		return items;
	}

	/**
	 * @param items items
	 */
	public void setItems(final Map<TestDto12CollectionItemIterface, String> items) {
		this.items = items;
	} 
	
	
	
}
