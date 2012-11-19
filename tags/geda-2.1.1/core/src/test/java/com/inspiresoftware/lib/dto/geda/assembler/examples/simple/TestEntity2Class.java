
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler.examples.simple;

import com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.TestEntity1Class;
import org.junit.Ignore;


/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity2Class extends TestEntity1Class {
	
	private boolean decision;

	/**
	 * @return boolean value.
	 */
	public boolean getDecision() {
		return decision;
	}

	/**
	 * @param decision boolean value.
	 */
	public void setDecision(final boolean decision) {
		this.decision = decision;
	}
	
	

}
