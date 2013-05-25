
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.parent;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.annotations.DtoParent;
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
public class DtoChildClass implements DtoChildInterface {
	
	@DtoParent
	@DtoField(value = "parent",
			  dtoBeanKey = "DtoParentClass",
			  entityBeanKeys = { "EntityParentClass" })
	private DtoParentInterface parent;
	
	@DtoField
	private String name;
	
	/**
	 * @return parent entity.
	 */
	public DtoParentInterface getParent() {
		return parent;
	}
	/**
	 * @param parent parent entity.
	 */
	public void setParent(final DtoParentInterface parent) {
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
