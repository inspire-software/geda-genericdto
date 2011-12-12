
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.exception;

/**
 * Denotes exception for missing bean factory.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class BeanFactoryNotFoundException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final boolean dto;
	private final String fieldName;
	private final String beanKey;

	/**
	 * @param fieldName field name of the dto instance
	 * @param beanKey bean key to use in bean factory
	 * @param dto true if is dto, false if is entity 
	 */
	public BeanFactoryNotFoundException(
			final String fieldName,
			final String beanKey,
			final boolean dto) {
		super("No factory provided for: " + (dto ? "dto " : "entity ")
				+ fieldName + "@key:" + beanKey);
		this.fieldName = fieldName;
		this.beanKey = beanKey;
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
	
}
