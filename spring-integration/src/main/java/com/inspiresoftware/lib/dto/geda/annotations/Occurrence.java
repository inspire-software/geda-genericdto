/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.annotations;

/**
 * When should the transfer of data from/to dto/entity should occur.
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 7:58:36 PM
 */
public enum Occurrence {

    BEFORE_METHOD_INVOCATION,
    AFTER_METHOD_INVOCATION

}
