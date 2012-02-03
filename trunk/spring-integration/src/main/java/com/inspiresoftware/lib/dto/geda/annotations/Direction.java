/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.annotations;

/**
 * Defines the direction of data transfer.
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 7:54:10 PM
 */
public enum Direction {

    /**
     * Transfer of data from DTO (source) to Entity (target)
     */
    DTO_TO_ENTITY,

    /**
     * Transfer of data from Entity (source) to DTO (target)
     */
    ENTITY_TO_DTO,

    /**
     * No transfer of data should be performed.
     */
    NONE

}
