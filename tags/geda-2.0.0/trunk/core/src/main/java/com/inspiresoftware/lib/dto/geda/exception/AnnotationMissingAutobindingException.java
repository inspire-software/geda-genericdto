
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
 * Denotes exception for missing {@link com.inspiresoftware.lib.dto.geda.annotations.Dto#value()} annotation.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AnnotationMissingAutobindingException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;

	/**
	 * @param className class that is missing {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} annotation. 
	 */
	public AnnotationMissingAutobindingException(
			final String className) {
		super("Dto " + className + " must be annotated with @Dto and #value parameter is specified");
		this.className = className;
	}

	/**
	 * @return class that is missing {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} annotation.
	 */
	public String getClassName() {
		return className;
	}

}
