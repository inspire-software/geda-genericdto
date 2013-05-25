
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.exception;

/**
 * Denotes exception for missing property in a class mapping.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class InspectionPropertyNotFoundException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;
	private final String fieldName;

	/**
	 * @param className bean class name
	 * @param fieldName field name of the dto instance
	 */
	public InspectionPropertyNotFoundException(
			final String className,
			final String fieldName) {
		super("Unable to locate field '" + className + "#" 
				+ fieldName + "'");
		this.className = className;
		this.fieldName = fieldName;
	}

	/**
	 * @return field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return bean class name
	 */
	public String getClassName() {
		return className;
	}

	
}
