/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

/**
 * Test interface for entity.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
interface TestEntity1Interface {

	/**
	 * @return a long property.
	 */
	Long getEntityId();

	/**
	 * @param entityId a long property.
	 */
	void setEntityId(final Long entityId);

	/**
	 * @return  a string property.
	 */
	String getName();

	/**
	 * @param name a string property.
	 */
	void setName(final String name);

	/**
	 * @return a double property.
	 */
	Double getNumber();

	/**
	 * @param number a double property.
	 */
	void setNumber(final Double number);

}