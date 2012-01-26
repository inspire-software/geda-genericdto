/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Occurance;
import com.inspiresoftware.lib.dto.geda.annotations.TransferableAfter;
import com.inspiresoftware.lib.dto.geda.annotations.TransferableBefore;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 1:03:36 PM
 */
public class AdviceConfigResolverImpl implements AdviceConfigResolver {

    public AdviceConfigResolverImpl() {
    }

    /** {@inheritDoc} */
    public Map<Occurance, AdviceConfig> resolve(final Method method, final Class<?> targetClass) {

        final TransferableBefore before = method.getAnnotation(TransferableBefore.class);
        final TransferableAfter after = method.getAnnotation(TransferableAfter.class);

        final Map<Occurance, AdviceConfig> cfg = new HashMap<Occurance, AdviceConfig>();
        final Class[] args = method.getParameterTypes();

        if (before != null) {
            final AdviceConfig cfgBefore =
                    AdviceConfigStaticFactory.getConfig(before.direction(), Occurance.BEFORE_METHOD_INVOCATION, args);
            if (cfgBefore != null) {
                cfg.put(Occurance.BEFORE_METHOD_INVOCATION, cfgBefore);
            }
        }
        if (after != null) {
            final AdviceConfig cfgAfter =
                    AdviceConfigStaticFactory.getConfig(after.direction(), Occurance.AFTER_METHOD_INVOCATION, args);
            if (cfgAfter != null) {
                cfg.put(Occurance.AFTER_METHOD_INVOCATION, cfgAfter);
            }
        }
        return cfg;
    }
}
