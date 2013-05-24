
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.maps;

import org.junit.Ignore;

import java.util.Map;


/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class EntityMapByKeyClass implements EntityMapByKeyInterface {
	
	private Map<EntityItemInterface, String> items;

	/** {@inheritDoc} */
	public Map<EntityItemInterface, String> getItems() {
		return items;
	}

	/** {@inheritDoc} */
	public void setItems(final Map<EntityItemInterface, String> items) {
		this.items = items;
	}
	
}