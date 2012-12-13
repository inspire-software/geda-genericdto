
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
 * Denotes exception for case when dto property value cannot be retrieved.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class GetFieldValueException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoClassName;
	private final String dtoFieldName;

	/**
	 * @param dtoClassName dto class name
	 * @param dtoFieldName field name of the dto instance
	 */
	public GetFieldValueException(
			final String dtoClassName,
			final String dtoFieldName) {
		super("Unable to get [" + dtoClassName + "@" + dtoFieldName + "]");
		this.dtoClassName = dtoClassName;
		this.dtoFieldName = dtoFieldName;
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

}
