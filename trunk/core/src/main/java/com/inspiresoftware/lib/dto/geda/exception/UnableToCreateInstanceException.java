
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
 * Denotes exception when bean factory is unable to create instance of dto/entity.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class UnableToCreateInstanceException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;

	/**
	 * @param className class name
	 * @param message message
	 * @param cause cause
	 */
	public UnableToCreateInstanceException(
			final String className,
			final String message,
			final Throwable cause) {
		super(message, cause);
		this.className = className;
	}

	/**
	 * @return class name
	 */
	public String getClassName() {
		return className;
	}

}
