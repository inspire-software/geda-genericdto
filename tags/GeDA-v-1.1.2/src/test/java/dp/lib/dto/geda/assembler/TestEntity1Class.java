
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
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
public class TestEntity1Class implements TestEntity1Interface {
	
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
