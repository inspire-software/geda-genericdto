
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
