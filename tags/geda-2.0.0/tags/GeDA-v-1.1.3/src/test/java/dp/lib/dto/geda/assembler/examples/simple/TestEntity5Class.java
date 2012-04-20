
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.examples.simple;

import org.junit.Ignore;

import dp.lib.dto.geda.assembler.examples.nested.TestEntity4Class;

/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity5Class {
	
	private TestEntity4Class wrapper;

	/**
	 * @return property wrapper.
	 */
	public TestEntity4Class getWrapper() {
		return wrapper;
	}
	/**
	 * @param wrapper property wrapper.
	 */
	public void setWrapper(final TestEntity4Class wrapper) {
		this.wrapper = wrapper;
	}

		

}
