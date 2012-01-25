
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
 * Denotes exception for failure to inspect properties of a class.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class InspectionScanningException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;

	/**
	 * @param className bean class name
	 * @param cause cause of the failure
	 */
	public InspectionScanningException(
			final String className,
			final Throwable cause) {
		super("Unable to scan fields of '" + className + "'", cause);
		this.className = className;
	}

	/**
	 * @return bean class name
	 */
	public String getClassName() {
		return className;
	}

	
}
