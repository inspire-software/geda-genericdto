
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.autowire;

import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto("com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.TestEntity1Class")
@Ignore
public class TestDto1Class implements TestDto1Interface {

	@DtoField("entityId")
	private Long myLong;
	@DtoField("name")
	private String myString;
	@DtoField("number")
	private Double myDouble;

	/** {@inheritDoc} */
	public Long getMyLong() {
		return myLong;
	}
	/** {@inheritDoc} */
	public void setMyLong(final Long myLong) {
		this.myLong = myLong;
	}
	/** {@inheritDoc} */
	public String getMyString() {
		return myString;
	}
	/** {@inheritDoc} */
	public void setMyString(final String myString) {
		this.myString = myString;
	}
	/** {@inheritDoc} */
	public Double getMyDouble() {
		return myDouble;
	}
	/** {@inheritDoc} */
	public void setMyDouble(final Double myDouble) {
		this.myDouble = myDouble;
	}

}
