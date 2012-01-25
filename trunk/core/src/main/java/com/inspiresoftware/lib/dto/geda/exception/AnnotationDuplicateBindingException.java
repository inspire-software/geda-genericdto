
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
 * Denotes exception for duplicate binding encountered (uniqueness is determined by field name).
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AnnotationDuplicateBindingException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoClass;
	private final String fieldName;

	/**
	 * @param dtoClass dto class 
	 * @param fieldName field name of the dto instance
	 */
	public AnnotationDuplicateBindingException(
			final String dtoClass,
			final String fieldName) {
		super("Binding for '" + fieldName + "' already exists in Dto: "
                + dtoClass);
		this.fieldName = fieldName;
		this.dtoClass = dtoClass;
	}

	/**
	 * @return dto class
	 */
	public String getDtoClass() {
		return dtoClass;
	}

	/**
	 * @return field name
	 */
	public String getFieldName() {
		return fieldName;
	}

}
