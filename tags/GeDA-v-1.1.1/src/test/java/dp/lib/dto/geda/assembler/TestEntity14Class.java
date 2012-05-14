
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
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity14Class implements TestEntity14IfaceDescriptable {

	private String name;
	private String desc;
	
	/** {@inheritDoc} */
	public String getName() {
		return name;
	}
	
	/** {@inheritDoc} */
	public void setName(final String name) {
		this.name = name;
	}
	
	/** {@inheritDoc} */
	public String getDesc() {
		return desc;
	}
	
	/** {@inheritDoc} */
	public void setDesc(final String desc) {
		this.desc = desc;
	}
	
}