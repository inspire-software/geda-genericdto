
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.maps;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;
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
public class DtoMapToMapByKeyClass implements DtoMapByKeyIterface {
	
	@DtoMap(value = "items",
			   dtoBeanKey = "dtoItem",
			   entityBeanKeys = "entityItem",
			   dtoToEntityMatcher = MapEntityByKeyMatcher.class,
			   entityGenericType = EntityItemInterface.class,
			   useEntityMapKey = true)
	private Map<DtoItemIterface, String> items;

	/**
	 * @return items
	 */
	public Map<DtoItemIterface, String> getItems() {
		return items;
	}

	/**
	 * @param items items
	 */
	public void setItems(final Map<DtoItemIterface, String> items) {
		this.items = items;
	} 
	
	
	
}
