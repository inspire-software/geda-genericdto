
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.examples.maps;

import java.util.Map;

import org.junit.Ignore;

import dp.lib.dto.geda.assembler.examples.collections.TestEntity12CollectionItemInterface;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestEntity12MapInterface {
	
	/**
	 * @return items
	 */
	Map<String, TestEntity12CollectionItemInterface> getItems();

	/**
	 * @param items items
	 */
	void setItems(Map<String, TestEntity12CollectionItemInterface> items);
}
