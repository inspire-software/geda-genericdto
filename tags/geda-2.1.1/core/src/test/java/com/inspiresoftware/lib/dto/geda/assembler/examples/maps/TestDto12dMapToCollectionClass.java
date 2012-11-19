
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.maps;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;
import com.inspiresoftware.lib.dto.geda.assembler.examples.collections.TestDto12CollectionItemIterface;
import org.junit.Ignore;

import java.util.Map;


/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto12dMapToCollectionClass implements TestDto12MapIterface {
	
	@DtoMap(value = "collectionWrapper.items",
			   dtoBeanKey = "dtoItem",
			   entityBeanKeys = { "nestedEntity", "entityItem" },
			   dtoToEntityMatcherKey = "Test12KeyMapToEntityMatcher.class",
			   dtoMapClassKey = "dtoMap",
			   entityMapOrCollectionClassKey = "entityMapOrCollection",
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
