
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
 * Parameter checking exception.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class NullParametersNotAllowedException extends GeDAException {

	private static final long serialVersionUID = 20110609L;

	/**
	 * @param message message
	 */
	public NullParametersNotAllowedException(
			final String message) {
		super(message);
	}

}
