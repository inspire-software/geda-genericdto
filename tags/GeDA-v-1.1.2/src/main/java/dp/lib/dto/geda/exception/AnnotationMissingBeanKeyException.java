
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
 * Denotes exception for missing bean key.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AnnotationMissingBeanKeyException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final boolean dto;
	private final String fieldName;

	/**
	 * @param fieldName field name of the dto instance
	 * @param dto true if is dto, false if is entity 
	 */
	public AnnotationMissingBeanKeyException(
			final String fieldName,
			final boolean dto) {
		super("No factory provided for: " + (dto ? "dto " : "entity ")
				+ fieldName);
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

}
