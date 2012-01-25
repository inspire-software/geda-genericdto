
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.performance;

/**
 * Interface to provide generic method to verify DTOs and Entities.
 * 
 * @author DPavlov
 */
public interface Verifiable {

	/**
	 * @param predicate validation source
	 * @return true if this object is valid against given predicate
	 */
	boolean isValid(final Object predicate);
	
}
