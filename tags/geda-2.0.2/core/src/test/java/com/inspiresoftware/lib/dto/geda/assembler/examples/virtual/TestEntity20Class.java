
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler.examples.virtual;

import org.junit.Ignore;

/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity20Class {
	
	private boolean decided;

	private long pk;
	
	/**
	 * @return result of a complex decision.
	 */
	public Boolean whatWasComplexDecision() {
		return Boolean.TRUE; 
	}
	
	/**
	 * @param dtoValue value from dto object.
	 */
	public void makeComplexDecision(final Boolean dtoValue) {
		this.decided = dtoValue;
	}
	
	/**
	 * @return boolean value
	 */
	public boolean isDecided() {
		return decided;
	}

	/**
	 * @return some PK
	 */
	public long getPk() {
		return pk;
	}

	/**
	 * @param pk some PK
	 */
	public void setPk(final long pk) {
		this.pk = pk;
	}
	
	
	
}
