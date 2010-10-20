
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.util.Collection;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity12CollectionClass implements TestEntity12CollectionInterface {
	
	private Collection<TestEntity12CollectionItemInterface> items;

	/** {@inheritDoc} */
	public Collection<TestEntity12CollectionItemInterface> getItems() {
		return items;
	}

	/** {@inheritDoc} */
	public void setItems(final Collection<TestEntity12CollectionItemInterface> items) {
		this.items = items;
	}
	
}
