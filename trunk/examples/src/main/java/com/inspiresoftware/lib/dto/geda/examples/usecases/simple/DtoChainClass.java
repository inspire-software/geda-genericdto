
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class DtoChainClass {
		
	@DtoField(
            value = "wrapper.wrapper.name",
            entityBeanKeys = { "wrapper.key", "wrapper.wrapper.key" })
	private String nestedString;

	/**
	 * @return nested property.
	 */
	public String getNestedString() {
		return nestedString;
	}
	/**
	 * @param nestedString nested property.
	 */
	public void setNestedString(final String nestedString) {
		this.nestedString = nestedString;
	}
	
	
	
}