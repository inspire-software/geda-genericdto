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
 * DSL version of {@link com.inspiresoftware.lib.dto.geda.annotations.DtoParent}.
 *
 * @since 2.0.4
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 11:08 AM
 */
public interface DtoParentContext {

    /**
     * textual reference to {@link com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever} within
     * converters map that will do the entity retrieval.
     *
     * @param retriever retriever key
     * @return dto field context
     */
    DtoFieldContext retriever(String retriever);

}
