
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
 * Denotes exception for assembler being used with unsupported DTO instance.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class InspectionInvalidDtoInstanceException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;
	private final String dtoName;

	/**
	 * @param className bean class name
	 * @param dto dto instance
	 */
	public InspectionInvalidDtoInstanceException(
			final String className,
			final Object dto) {
		super("This assembler is only applicable for dto: " + className 
				+ (dto != null ? ", found: " + dto.getClass().getCanonicalName() : ""));
		this.className = className;
		this.dtoName = dto.getClass().getCanonicalName();
	}

	/**
	 * @return entity name
	 */
	public String getDtoName() {
		return dtoName;
	}

	/**
	 * @return supported dto class name
	 */
	public String getClassName() {
		return className;
	}

	
}
