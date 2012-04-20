
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
 * Denotes exception for case when dto property cannot be set with a value.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class SetFieldValueException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoClassName;
	private final String dtoFieldName;
	private final Object value;

	/**
	 * @param dtoClassName dto class name
	 * @param dtoFieldName field name of the dto instance
	 * @param value value
	 */
	public SetFieldValueException(
			final String dtoClassName,
			final String dtoFieldName,
			final Object value) {
		super("Unable to set [" + dtoClassName + "@" + dtoFieldName + "] to [" + value + "]");
		this.dtoClassName = dtoClassName;
		this.dtoFieldName = dtoFieldName;
		this.value = value;
	}
	
	/**
	 * @return bean class name
	 */
	public String getDtoClassName() {
		return dtoClassName;
	}

	/**
	 * @return field name
	 */
	public String getDtoFieldName() {
		return dtoFieldName;
	}

	/**
	 * @return bean class name
	 */
	public Object getValue() {
		return value;
	}

}
