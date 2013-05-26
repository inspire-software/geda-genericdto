
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.autowire;

/**
 * Interface for testing purposes.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface TestDto1Interface {

	/**
	 * @return long value.
	 */
	Long getMyLong();

	/**
	 * @param myLong long value.
	 */
	void setMyLong(final Long myLong);

	/**
	 * @return string value.
	 */
	String getMyString();

	/**
	 * @param myString string value.
	 */
	void setMyString(final String myString);

	/**
	 * @return double value.
	 */
	Double getMyDouble();

	/**
	 * @param myDouble double value.
	 */
	void setMyDouble(final Double myDouble);

}