
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
 * Denotes exception for case when biding between dto and entity field cannot be established.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AnnotationMissingBindingException extends GeDAException {
	
	/**
	 * Type of missing binding.
	 */
	public static enum MissingBindingType { DTO_READ, DTO_WRITE, ENTITY_READ, ENTITY_WRITE, PARENT_READ, VIRTUAL_CONVERTER };

	private static final long serialVersionUID = 20110609L;

	private final MissingBindingType type;
	private final String fieldName;
	
	/**
	 * @param type missing bindig type
	 */
	public AnnotationMissingBindingException(
			final MissingBindingType type,
			final String fieldName) {
		super("Data pipe method for " + fieldName + "[" + type.name() 
				+ "] is not initialized. Please check parameter and return types of your getters/setters");
		this.type = type;
		this.fieldName = fieldName;
	}
	
	

	public String getFieldName() {
		return fieldName;
	}



	/**
	 * @return type of binding
	 */
	public MissingBindingType getType() {
		return type;
	}

}
