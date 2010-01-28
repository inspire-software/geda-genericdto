/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

/**
 * Interface for testing purposes.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
interface TestDto1Interface {

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