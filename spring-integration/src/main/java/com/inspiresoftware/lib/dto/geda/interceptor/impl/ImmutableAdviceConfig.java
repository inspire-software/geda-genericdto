/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;

/**
 * Basic immutable config.
 *
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 12:38:09 PM
 */
public class ImmutableAdviceConfig implements AdviceConfig {

    private final Direction direction;
    private final Occurrence occurrence;
    private final DTOSupportMode dtoSupportMode;

    private final String dtoFilterKey;
    private final String dtoKey;
    private final String entityKey;

    private final int dtoSourceIndex;
    private final int dtoTargetIndex;
    private final int entitySourceIndex;
    private final int entityTargetIndex;

    private final String context;


    public ImmutableAdviceConfig(final Direction direction,
                                 final Occurrence occurrence,
                                 final DTOSupportMode dtoSupportMode,
                                 final String dtoFilterKey,
                                 final String dtoKey,
                                 final String entityKey,
                                 final int dtoSourceIndex,
                                 final int dtoTargetIndex,
                                 final int entitySourceIndex,
                                 final int entityTargetIndex,
                                 final String context) {
        this.direction = direction;
        this.occurrence = occurrence;
        this.dtoSupportMode = dtoSupportMode;
        this.dtoKey = dtoKey;
        this.entityKey = entityKey;
        this.dtoFilterKey = dtoFilterKey;
        this.dtoSourceIndex = dtoSourceIndex;
        this.dtoTargetIndex = dtoTargetIndex;
        this.entitySourceIndex = entitySourceIndex;
        this.entityTargetIndex = entityTargetIndex;
        this.context = context;
    }

    /** {@inheritDoc} */
    public Direction getDirection() {
        return direction;
    }

    /** {@inheritDoc} */
    public Occurrence getOccurrence() {
        return occurrence;
    }

    /** {@inheritDoc} */
    public DTOSupportMode getDtoSupportMode() {
        return dtoSupportMode;
    }

    /** {@inheritDoc} */
    public String getDtoFilterKey() {
        return dtoFilterKey;
    }

    /** {@inheritDoc} */
    public String getDtoKey() {
        return dtoKey;
    }

    /** {@inheritDoc} */
    public String getEntityKey() {
        return entityKey;
    }

    /** {@inheritDoc} */
    public int getDtoSourceIndex() {
        return dtoSourceIndex;
    }

    /** {@inheritDoc} */
    public int getDtoTargetIndex() {
        return dtoTargetIndex;
    }

    /** {@inheritDoc} */
    public int getEntitySourceIndex() {
        return entitySourceIndex;
    }

    /** {@inheritDoc} */
    public int getEntityTargetIndex() {
        return entityTargetIndex;
    }

    /** {@inheritDoc} */
    public String getContext() {
        return context;
    }
}
