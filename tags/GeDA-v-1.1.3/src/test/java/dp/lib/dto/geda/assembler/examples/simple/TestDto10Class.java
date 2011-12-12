
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
@Dto
@Ignore
public class TestDto10Class {
	
	@DtoField(value = "nested.im1", entityBeanKeys = { "nested" })
	private String im1;
	@DtoField(value = "nested.im2", entityBeanKeys = { "nested" })
	private String im2;
	@DtoField(value = "nested.im3", entityBeanKeys = { "nested" })
	private String im3;
	
	/**
	 * @return test for multi interface inheritance
	 */
	public String getIm1() {
		return im1;
	}
	/**
	 * @param im1 test for multi interface inheritance
	 */
	public void setIm1(final String im1) {
		this.im1 = im1;
	}
	/**
	 * @return test for multi interface inheritance
	 */
	public String getIm2() {
		return im2;
	}
	/**
	 * @param im2 test for multi interface inheritance
	 */
	public void setIm2(final String im2) {
		this.im2 = im2;
	}
	/**
	 * @return test for multi interface inheritance
	 */
	public String getIm3() {
		return im3;
	}
	/**
	 * @param im3 test for multi interface inheritance
	 */
	public void setIm3(final String im3) {
		this.im3 = im3;
	}
	
	

}
