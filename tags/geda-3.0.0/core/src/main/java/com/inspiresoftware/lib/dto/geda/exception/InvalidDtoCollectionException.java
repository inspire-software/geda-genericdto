
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.exception;

/**
 * Denotes exception for invalid collection for batch processing.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class InvalidDtoCollectionException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	/** Constructor. */
	public InvalidDtoCollectionException() {
		super("Collections must not be null and dtos collection should be empty");
	}
	
}
