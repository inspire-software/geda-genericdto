
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
 * Denotes exception for case when biding between dto and entity field cannot be established.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class InspectionBindingNotFoundException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoClassName;
	private final String dtoFieldName;
	private final String entityClassName;
	private final String entityFieldName;

	/**
	 * @param dtoClassName dto class name
	 * @param dtoFieldName field name of the dto instance
	 * @param entityClassName dto class name
	 * @param entityFieldName field name of the dto instance
	 */
	public InspectionBindingNotFoundException(
			final String dtoClassName,
			final String dtoFieldName,
			final String entityClassName,
			final String entityFieldName) {
		super("Unable to bind Dto field '" + dtoClassName + "#" 
				+ dtoFieldName + "' to '" 
				+ entityClassName + "#" + entityFieldName + "'");
		this.dtoClassName = dtoClassName;
		this.dtoFieldName = dtoFieldName;
		this.entityClassName = entityClassName;
		this.entityFieldName = entityFieldName;
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
	public String getEntityClassName() {
		return entityClassName;
	}

	/**
	 * @return field name
	 */
	public String getEntityFieldName() {
		return entityFieldName;
	}

}
