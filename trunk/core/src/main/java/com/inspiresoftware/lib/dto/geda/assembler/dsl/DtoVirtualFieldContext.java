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
 * DSL version of {@link com.inspiresoftware.lib.dto.geda.annotations.DtoVirtualField}.
 *
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 12:23 PM
 */
public interface DtoVirtualFieldContext {

    /**
     * Textual reference to a converter to use when assembling DTO's and Entities. This reference is
     * used to lookup converter in adapters map passed into assembleDto and assembleEntity methods.
     * This converter must implement {@link com.inspiresoftware.lib.dto.geda.adapter.ValueConverter}.
     *
     * @param converter converter key
     * @return dto field context
     */
    DtoFieldContext converter(String converter);

}
