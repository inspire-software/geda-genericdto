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
import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;

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
        ENTITY_TO_DTO_KEY,
        ENTITY_TO_DTO_BY_FILTER,
        ENTITY_TO_DTO_KEY_BY_FILTER,
        ENTITIES_TO_DTOS,
        ENTITIES_TO_DTOS_BY_FILTER,
        DTO_TO_ENTITY,
        DTO_TO_ENTITY_KEY,
        DTO_BY_FILTER_TO_ENTITY,
        DTO_BY_FILTER_TO_ENTITY_KEY,
        DTOS_TO_ENTITIES,
        DTOS_TO_ENTITIES_BY_FILTER
    }

    /**
     * @return how to perform information transfer.
     */
    Direction getDirection();

    /**
     * @return when to perform information transfer.
     */
    Occurrence getOccurrence();

    /**
     * @return dto filter key for bean factory.
     */
    String getDtoFilterKey();

    /**
     * @return dto key for bean factory.
     */
    String getDtoKey();

    /**
     * @return entity key for bean factory.
     */
    String getEntityKey();

    /**
     * @return resolved method name of the {@link com.inspiresoftware.lib.dto.geda.DTOSupport}
     *         that will be invoked.
     */
    DTOSupportMode getDtoSupportMode();

    /**
     * @return parameter index for the DTO that contains data to transfer to Entity.
     */
    int getDtoSourceIndex();

    /**
     * @return parameter index for the DTO that will receive data from Entity (-1 for return).
     */
    int getDtoTargetIndex();

    /**
     * @return parameter index for the Entity that contains data to transfer to DTO.
     */
    int getEntitySourceIndex();

    /**
     * @return parameter index for the Entity that will receive data from DTO (-1 for return).
     */
    int getEntityTargetIndex();

}
