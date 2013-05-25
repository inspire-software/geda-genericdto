
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.autowire;

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
@Dto("com.inspiresoftware.lib.dto.geda.examples.usecases.autowire.MyEntityClass")
@Ignore
public class MyDtoClass implements MyDtoInterface {

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
