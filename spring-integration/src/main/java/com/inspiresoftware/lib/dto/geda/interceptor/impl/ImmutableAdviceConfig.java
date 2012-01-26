/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Occurance;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;

/**
 * Basic immutable config.
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 12:38:09 PM
 */
public class ImmutableAdviceConfig implements AdviceConfig {

    private final Direction direction;
    private final Occurance occurance;
    private final DTOSupportMode dtoSupportMode;

    public ImmutableAdviceConfig(final Direction direction,
                                 final Occurance occurance,
                                 final DTOSupportMode dtoSupportMode) {
        this.direction = direction;
        this.occurance = occurance;
        this.dtoSupportMode = dtoSupportMode;
    }

    /** {@inheritDoc} */
    public Direction getDirection() {
        return direction;
    }

    /** {@inheritDoc} */
    public Occurance getOccurance() {
        return occurance;
    }

    /** {@inheritDoc} */
    public DTOSupportMode getDtoSupportMode() {
        return dtoSupportMode;
    }
}
