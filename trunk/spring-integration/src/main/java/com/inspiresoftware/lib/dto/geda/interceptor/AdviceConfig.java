/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Occurance;

import java.io.Serializable;

/**
 * Advice config is a basic storate for the annotation data
 * so that we do not keep reference of it.
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 7:54:26 AM
 */
public interface AdviceConfig extends Serializable {

    /**
     * Simple enum of available methods on the
     * {@link com.inspiresoftware.lib.dto.geda.DTOSupport}.
     */
    public enum DTOSupportMode {
        ENTITY_TO_DTO,
        ENTITY_TO_DTO_BY_CLASS,
        ENTITIES_TO_DTOS_BY_CLASS,
        DTO_TO_ENTITY,
        DTO_BY_CLASS_TO_ENTITY,
        DTOS_BY_CLASS_TO_ENTITIES
    }

    /**
     * @return how to perform information transfer.
     */
    Direction getDirection();

    /**
     * @return when to perform information transfer.
     */
    Occurance getOccurance();

    /**
     * @return resolved method name of the {@link com.inspiresoftware.lib.dto.geda.DTOSupport}
     *         that will be invoked.
     */
    DTOSupportMode getDtoSupportMode();

}
