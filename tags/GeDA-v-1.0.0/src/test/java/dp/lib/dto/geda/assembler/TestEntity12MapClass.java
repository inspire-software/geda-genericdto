
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.util.Map;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity12MapClass implements TestEntity12MapInterface {
	
	private Map<String, TestEntity12CollectionItemInterface> items;

	/** {@inheritDoc} */
	public Map<String, TestEntity12CollectionItemInterface> getItems() {
		return items;
	}

	/** {@inheritDoc} */
	public void setItems(final Map<String, TestEntity12CollectionItemInterface> items) {
		this.items = items;
	}
	
}
