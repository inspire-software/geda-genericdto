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

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:39:45 AM
 */
@Dto
@Ignore
public class TestDto9InheritanceChildClass extends TestDto9InheritanceParentClass {
	
	@DtoField
	private String nameChild;

	/** {@inheritDoc} */
	public String getNameChild() {
		return nameChild;
	}

	/** {@inheritDoc} */
	public void setNameChild(final String name) {
		this.nameChild = name;
	}

}
