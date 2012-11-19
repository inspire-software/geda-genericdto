
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
public class BeanFactoryUnableToCreateInstanceException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String factoryToString;
	private final boolean dto;
	private final String fieldName;
	private final String beanKey;

	/**
	 * @param factoryToString factory to string representation
	 * @param fieldName field name of the dto instance
	 * @param beanKey bean key to use in bean factory
	 * @param dto true if is dto, false if is entity 
	 */
	public BeanFactoryUnableToCreateInstanceException(
			final String factoryToString,
			final String fieldName,
			final String beanKey,
			final boolean dto) {
		super("Unable to construct " + (dto ? "dto " : "entity ") 
				+ " bean with key: " + beanKey 
				+ " using beanFactory: " + factoryToString + " for: "
				+ fieldName);
		this.beanKey = beanKey;
		this.factoryToString = factoryToString;
		this.fieldName = fieldName;
		this.dto = dto;
	}
	
	/**
	 * @return true if cause by dto mapping (false if by entity mapping)
	 */
	public boolean isDto() {
		return dto;
	}

	/**
	 * @return field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return bean key
	 */
	public String getBeanKey() {
		return beanKey;
	}	

	/**
	 * @return factory to string representation
	 */
	public String getFactoryToString() {
		return factoryToString;
	}

}
