

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.virtual;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoVirtualField;
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

	@DtoVirtualField(converter = "VirtualMyBoolean")
	private Boolean myBoolean;

	@DtoVirtualField(converter = "VirtualMyLong")
	private Long myLong;

	/**
	 * @return boolean value.
	 */
	public Boolean getMyBoolean() {
		return myBoolean;
	}
	/**
	 * @param myBoolean boolean value.
	 */
	public void setMyBoolean(final Boolean myBoolean) {
		this.myBoolean = myBoolean;
	}
	
	/**
	 * @return long value
	 */
	public Long getMyLong() {
		return myLong;
	}
	
	/**
	 * @param myLong long value.
	 */
	public void setMyLong(final Long myLong) {
		this.myLong = myLong;
	}

	
	
}
