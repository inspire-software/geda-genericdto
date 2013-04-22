
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import org.junit.Ignore;

/**
 * Test entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class EntityChainLv2Class {
	
	private EntityChainLv3Class wrapper;

	/**
	 * @return property wrapper.
	 */
	public EntityChainLv3Class getWrapper() {
		return wrapper;
	}
	/**
	 * @param wrapper property wrapper.
	 */
	public void setWrapper(final EntityChainLv3Class wrapper) {
		this.wrapper = wrapper;
	}

		

}
