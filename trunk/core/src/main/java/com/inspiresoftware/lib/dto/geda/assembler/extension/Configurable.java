
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension;

import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

/**
 * Define configurable objects.
 * 
 * @author denispavlov
 *
 */
public interface Configurable {

	/**
	 * @param configuration configuration name
	 * @param value value to set
	 * @return true if configuration was set, false if not set or invalid
	 * @throws GeDAException in case there are errors
	 */
	boolean configure(final String configuration, final Object value) throws GeDAException;
	
}
