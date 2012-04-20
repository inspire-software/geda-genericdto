
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.examples.parent;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity11ChildClass implements TestEntity11ChildInterface {
	
	private TestEntity11ParentInterface parent;
	private String name;
	
	/**
	 * @return parent entity.
	 */
	public TestEntity11ParentInterface getParent() {
		return parent;
	}
	/**
	 * @param parent parent entity.
	 */
	public void setParent(final TestEntity11ParentInterface parent) {
		this.parent = parent;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
}
