
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

import dp.lib.dto.geda.assembler.examples.collections.TestDto12CollectionItemIterface;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestDto12MapByKeyIterface {
	
	/**
	 * @return items of collection
	 */
	Map<TestDto12CollectionItemIterface, String> getItems();
	/**
	 * @param items items of collection
	 */
	void setItems(Map<TestDto12CollectionItemIterface, String> items);
	
}
