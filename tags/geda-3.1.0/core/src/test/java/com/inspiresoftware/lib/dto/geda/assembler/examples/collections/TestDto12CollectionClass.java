
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

import java.util.Collection;


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
