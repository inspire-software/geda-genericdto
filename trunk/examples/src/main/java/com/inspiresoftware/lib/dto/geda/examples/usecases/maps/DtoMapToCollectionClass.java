
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.maps;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;
import org.junit.Ignore;

import java.util.ArrayList;
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
public class DtoMapToCollectionClass implements DtoMapIterface {
	
	@DtoMap(value = "items",
			   dtoBeanKey = "dtoItem",
			   entityBeanKeys = "entityItem",
			   dtoToEntityMatcher = KeyMapToEntityMatcher.class,
			   entityGenericType = EntityItemInterface.class,
			   entityMapOrCollectionClass = ArrayList.class,
			   entityCollectionMapKey = "name")
	private Map<String, DtoItemIterface> items;

	/**
	 * @return items.
	 */
	public Map<String, DtoItemIterface> getItems() {
		return items;
	}

	/**
	 * @param items items
	 */
	public void setItems(final Map<String, DtoItemIterface> items) {
		this.items = items;
	} 
	
	
	
}
