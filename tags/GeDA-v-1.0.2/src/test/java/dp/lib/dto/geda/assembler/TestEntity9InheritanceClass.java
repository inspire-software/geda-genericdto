/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:39:45 AM
 */
@Ignore
public class TestEntity9InheritanceClass {
	
	private String name;
	private String nameChild;

	/** {@inheritDoc} */
	public String getNameChild() {
		return nameChild;
	}

	/** {@inheritDoc} */
	public void setNameChild(final String name) {
		this.nameChild = name;
	}
	
	/** {@inheritDoc} */
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	public void setName(final String name) {
		this.name = name;
	}

}
