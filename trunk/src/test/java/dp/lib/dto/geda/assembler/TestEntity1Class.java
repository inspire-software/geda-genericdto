/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
class TestEntity1Class implements TestEntity1Interface {
	
	private Long entityId;
	private String name;
	private Double number;
	
	/** {@inheritDoc} */
	public Long getEntityId() {
		return entityId;
	}
	/** {@inheritDoc} */
	public void setEntityId(final Long entityId) {
		this.entityId = entityId;
	}
	/** {@inheritDoc} */
	public String getName() {
		return name;
	}
	/** {@inheritDoc} */
	public void setName(final String name) {
		this.name = name;
	}
	/** {@inheritDoc} */
	public Double getNumber() {
		return number;
	}
	/** {@inheritDoc} */
	public void setNumber(final Double number) {
		this.number = number;
	}
	
	

}
