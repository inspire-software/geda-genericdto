
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.collections;

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
public class DtoClass implements DtoIterface {
	
	@DtoCollection(value = "items",
				   dtoBeanKey = "dtoItem",
				   entityBeanKeys = "entityItem",
				   dtoToEntityMatcher = ItemsMatcher.class,
				   entityGenericType = EntityItemInterface.class)
	private Collection<DtoItemIterface> items;

	/**
	 * @return items
	 */
	public Collection<DtoItemIterface> getItems() {
		return items;
	}

	/**
	 * @param items items
	 */	
	public void setItems(final Collection<DtoItemIterface> items) {
		this.items = items;
	} 
	
	
	
}
