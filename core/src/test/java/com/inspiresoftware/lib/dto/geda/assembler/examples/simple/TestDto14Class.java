
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.simple;

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
@Ignore
@Dto
public class TestDto14Class implements TestDto14IfaceDescriptable {

	@DtoField
	private String name;
	@DtoField
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
