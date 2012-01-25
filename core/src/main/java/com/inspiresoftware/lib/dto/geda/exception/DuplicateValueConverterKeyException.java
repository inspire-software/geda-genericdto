
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
 * Denotes exception for inappropriate use of value converter keys.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class DuplicateValueConverterKeyException extends GeDAException {

	private static final long serialVersionUID = 20110609L;

	/**
	 * @param key duplicated key
	 */
	public DuplicateValueConverterKeyException(final String key) {
		super("Key [" + key + "] is already assined a converter.");
	}
	
}
