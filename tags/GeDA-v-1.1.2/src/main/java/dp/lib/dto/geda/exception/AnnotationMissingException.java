
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
 * Denotes exception for missing {@link dp.lib.dto.geda.annotations.Dto} annotation.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AnnotationMissingException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;

	/**
	 * @param className class that is missing {@link dp.lib.dto.geda.annotations.Dto} annotation. 
	 */
	public AnnotationMissingException(
			final String className) {
		super("Dto " + className + " must be annotated with @Dto");
		this.className = className;
	}

	/**
	 * @return class that is missing {@link dp.lib.dto.geda.annotations.Dto} annotation.
	 */
	public String getClassName() {
		return className;
	}

}
