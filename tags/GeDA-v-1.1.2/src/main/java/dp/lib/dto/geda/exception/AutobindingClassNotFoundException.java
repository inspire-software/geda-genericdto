
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
 * Denotes exception for missing {@link dp.lib.dto.geda.annotations.Dto#value()} annotation.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AutobindingClassNotFoundException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;
	private final String autobinding;

	/**
	 * @param className class that is missing {@link dp.lib.dto.geda.annotations.Dto} annotation. 
	 * @param autobinding auto binding class
	 */
	public AutobindingClassNotFoundException(
			final String className,
			final String autobinding) {
		super("Specified entity class " + autobinding + " for Dto " + className + " cannot be loaded...");
		this.className = className;
		this.autobinding = autobinding;
	}

	/**
	 * @return class that is missing {@link dp.lib.dto.geda.annotations.Dto} annotation.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return auto binding class name
	 */
	public String getAutobinding() {
		return autobinding;
	}

}
