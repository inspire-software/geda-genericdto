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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This factory encapsulates basic static logic for creating an advice configuration object.
 * Also includes simple permanent caching to stop poluting memory with same advice configuration
 * objects. 
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 12:21:48 PM
 */
public final class AdviceConfigStaticFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceConfigStaticFactory.class);

    private static final ConcurrentHashMap<String, AdviceConfig> CACHE = new ConcurrentHashMap<String, AdviceConfig>();

    private AdviceConfigStaticFactory() {
        // no instance
    }

    /**
     * Static utility method that allows to extract advice configuration from
     * the annotation and the method argument types.
     *
     * @param direction direction of transfer
     * @param occurance occurance of transfer
     * @param args invocation method arguments
     * @return advice.
     */
    public static AdviceConfig getConfig(final Direction direction,
                                         final Occurance occurance,
                                         final Class[] args) {

        if (args == null || args.length < 2) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Method must have at least two arguments... skipping GeDA advice");
            }
            return null;
        }

        final AdviceConfig.DTOSupportMode mode = guessMode(direction, args);
        final String cacheKey = cacheKey(occurance, mode);

        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(cacheKey);
        }

        final AdviceConfig cfg = new ImmutableAdviceConfig(direction, occurance, mode);
        CACHE.put(cacheKey, cfg);

        return cfg;

    }

    static AdviceConfig.DTOSupportMode guessMode(final Direction direction,
                                                         final Class[] args) {

        final boolean toDto = direction == Direction.ENTITY_TO_DTO;
        final boolean firstArgIsClass = args[0] == Class.class;

        if (firstArgIsClass) {

            final boolean secondClassIsCollection = Collection.class.isAssignableFrom(args[1]);
            if (toDto) {
                if (secondClassIsCollection) {
                    return AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_CLASS;
                }
                return AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_CLASS;
            }
            if (secondClassIsCollection) {
                return AdviceConfig.DTOSupportMode.DTOS_BY_CLASS_TO_ENTITIES;
            }
            return AdviceConfig.DTOSupportMode.DTO_BY_CLASS_TO_ENTITY;


        } else {
            if (toDto) {
                return AdviceConfig.DTOSupportMode.ENTITY_TO_DTO;
            }
            return AdviceConfig.DTOSupportMode.DTO_TO_ENTITY;
        }


    }

    private static String cacheKey(final Occurance occurance, final AdviceConfig.DTOSupportMode mode) {
        return occurance.name() + "_" + mode.name();
    }

}
