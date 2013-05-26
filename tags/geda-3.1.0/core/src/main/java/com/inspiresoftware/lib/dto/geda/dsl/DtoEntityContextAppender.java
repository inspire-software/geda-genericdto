/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.dsl;

/**
 * Interface to trace back from a specific field context to {@link com.inspiresoftware.lib.dto.geda.dsl.DtoEntityContext}.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 8:42 AM
 */
public interface DtoEntityContextAppender {
    /**
     * Continue with adding more mappings to this context.
     *
     * @return dto entity context
     */
    DtoEntityContext and();
}
