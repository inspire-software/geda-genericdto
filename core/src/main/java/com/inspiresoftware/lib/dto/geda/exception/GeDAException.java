
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
 * General GeDA exception.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class GeDAException extends
		IllegalArgumentException {

	private static final long serialVersionUID = 20110609L;

	/**
	 * GeDA exception.
	 */
	public GeDAException() {
		super();
	}

	/**
	 * GeDA exception.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public GeDAException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * GeDA exception.
	 * 
	 * @param cause cause
	 */
	public GeDAException(final Throwable cause) {
		super(cause);
	}

	/**
	 * GeDA exception.
	 * 
	 * @param message message
	 */
	public GeDAException(final String message) {
		super(message);
	}
	
}
