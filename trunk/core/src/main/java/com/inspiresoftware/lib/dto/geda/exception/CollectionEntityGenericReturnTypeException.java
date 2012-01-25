
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
 * Denotes exception for invalid retriever.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class CollectionEntityGenericReturnTypeException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoName;
	private final String fieldName;
	private final String entityGenericType;	

	/**
	 * @param dtoName class name of the dto instance
	 * @param fieldName field name of the dto instance
	 * @param entityGenericType class of the entity instance
	 */
	public CollectionEntityGenericReturnTypeException(
			final String dtoName,
			final String fieldName,
			final String entityGenericType) {
		super("A mismatch in return type of entity is detected," 
        		+ "please check @DtoCollection.entityGenericType(" + entityGenericType 
        		+ ") of " + fieldName + "[" + dtoName + "]");
		this.dtoName = dtoName;
		this.fieldName = fieldName;
		this.entityGenericType = entityGenericType;
	}

	/**
	 * @return dto class
	 */
	public String getDtoName() {
		return dtoName;
	}

	/**
	 * @return field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return entity collection item generic type declaration
	 */
	public String getEntityGenericType() {
		return entityGenericType;
	}
	
}
