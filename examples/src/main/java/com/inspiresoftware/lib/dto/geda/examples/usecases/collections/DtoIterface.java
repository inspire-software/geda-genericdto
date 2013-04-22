
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.collections;

import org.junit.Ignore;

import java.util.Collection;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface DtoIterface {
	
	/**
	 * @return items of collection
	 */
	Collection<DtoItemIterface> getItems();
	/**
	 * @param items items of collection
	 */
	void setItems(Collection<DtoItemIterface> items);
	
}
