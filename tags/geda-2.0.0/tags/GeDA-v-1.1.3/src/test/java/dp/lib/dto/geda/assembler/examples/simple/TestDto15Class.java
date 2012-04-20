
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
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
@Dto
public class TestDto15Class {

	@DtoField(readOnly = true)
	private String name;
	@DtoField(readOnly = true)
	private String desc;

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

    /**
     * @return description
     */
	public String getDesc() {
		return desc;
	}

    /**
     * @param desc description
     */
	public void setDesc(final String desc) {
		this.desc = desc;
	}

}