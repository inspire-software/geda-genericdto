
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.converter;

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
public class DtoClass {
	
	/**
	 * Test enum for conversion testing.
	 */
	public enum Decision { 
		/** true. */
		Decided, 
		/** false. */
		Undecided 
	};

	@DtoField(value = "decision", converter = "boolToEnum")
	private Decision myEnum;

	/**
	 * @return enum (converted from boolean).
	 */
	public Decision getMyEnum() {
		return myEnum;
	}
	/**
	 * @param myEnum enum (converted from boolean).
	 */
	public void setMyEnum(final Decision myEnum) {
		this.myEnum = myEnum;
	}

	

}
