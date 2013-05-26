
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

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity10SubClass implements TestEntity10Interface {
	
	private String im1;
	private String im2;
	private String im3;
	
	/** {@inheritDoc} */
	public String getIm1() {
		return im1;
	}
	/** {@inheritDoc} */
	public void setIm1(final String im1) {
		this.im1 = im1;
	}
	/** {@inheritDoc} */
	public String getIm2() {
		return im2;
	}
	/** {@inheritDoc} */
	public void setIm2(final String im2) {
		this.im2 = im2;
	}
	/** {@inheritDoc} */
	public String getIm3() {
		return im3;
	}
	/** {@inheritDoc} */
	public void setIm3(final String im3) {
		this.im3 = im3;
	}
	
	

}
