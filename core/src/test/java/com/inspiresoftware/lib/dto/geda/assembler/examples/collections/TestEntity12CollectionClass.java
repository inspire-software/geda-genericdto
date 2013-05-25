
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
