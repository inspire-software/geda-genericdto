
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

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity12WrapCollectionClass implements TestEntity12WrapCollectionInterface {
	
	private TestEntity12CollectionInterface collectionWrapper;

	/** {@inheritDoc} */
	public TestEntity12CollectionInterface getCollectionWrapper() {
		return collectionWrapper;
	}

	/** {@inheritDoc} */
	public void setCollectionWrapper(final TestEntity12CollectionInterface collectionWrapper) {
		this.collectionWrapper = collectionWrapper;
	}

}
