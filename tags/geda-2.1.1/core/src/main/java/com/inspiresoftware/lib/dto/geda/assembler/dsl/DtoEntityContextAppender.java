/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl;

/**
 * Interface to trace back from a specific field context to {@link DtoEntityContext}.
 *
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 12:32 PM
 */
public interface DtoEntityContextAppender {

    /**
     * Continue with adding more mappings to this context.
     *
     * @return dto entity context
     */
    DtoEntityContext and();

}
