
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

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public interface TestDto14IfaceDescriptable {

	/**
	 * @return description.
	 */
	String getDesc();
	/**
	 * @param desc description.
	 */
	void setDesc(String desc);
	
	/**
	 * @return name.
	 */
	String getName();
	/**
	 * @param name name.
	 */
	void setName(String name);
	
}
