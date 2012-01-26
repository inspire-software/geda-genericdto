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
import com.inspiresoftware.lib.dto.geda.annotations.Transferable;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic implementation of the resolved that examines method annotations in order to
 * produce configuration objects. Provides method signature cache to store the
 * configuration information in a permanent cache. 
 *
 * Additionally this resolver keeps track of the methods that are not applicable and
 * thus are blacklisted.
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 1:03:36 PM
 */
public class AdviceConfigResolverImpl implements AdviceConfigResolver {

    private final Map<String, Boolean> blacklist = new ConcurrentHashMap<String, Boolean>();
    private final Map<String, Map<Occurance, AdviceConfig>> cache = new ConcurrentHashMap<String, Map<Occurance, AdviceConfig>>();

    public AdviceConfigResolverImpl() {
    }

    /** {@inheritDoc} */
    public Map<Occurance, AdviceConfig> resolve(final Method method, final Class<?> targetClass) {

        final String methodCacheKey = methodCacheKey(method, targetClass);
        if (isBlacklisted(methodCacheKey)) {
            return Collections.emptyMap();
        }

        if (isCached(methodCacheKey)) {
            return this.cache.get(methodCacheKey);
        }

        final Map<Occurance, AdviceConfig> cfg = resolveConfiguration(method, targetClass, true);

        if (cfg.isEmpty()) {
            this.blacklist.put(methodCacheKey, Boolean.TRUE);
        } else {
            cache.put(methodCacheKey, cfg);
        }

        return cfg;
    }

    boolean isCached(final String methodCacheKey) {
        return this.cache.containsKey(methodCacheKey);
    }

    boolean isBlacklisted(final String methodCacheKey) {
        return this.blacklist.containsKey(methodCacheKey);
    }

    Map<Occurance, AdviceConfig> resolveConfiguration(final Method method,
                                                      final Class<?> targetClass,
                                                      final boolean trySpecific) {
        
        final Transferable annotation = method.getAnnotation(Transferable.class);

        final Map<Occurance, AdviceConfig> cfg = new HashMap<Occurance, AdviceConfig>();
        final Class[] args = method.getParameterTypes();

        if (annotation != null) {

            if (annotation.before() != Direction.NONE) {
                final AdviceConfig cfgBefore =
                        AdviceConfigStaticFactory.getConfig(annotation.before(), Occurance.BEFORE_METHOD_INVOCATION, args);
                if (cfgBefore != null) {
                    cfg.put(Occurance.BEFORE_METHOD_INVOCATION, cfgBefore);
                }
            }
            if (annotation.after() != Direction.NONE) {
                final AdviceConfig cfgAfter =
                        AdviceConfigStaticFactory.getConfig(annotation.after(), Occurance.AFTER_METHOD_INVOCATION, args);
                if (cfgAfter != null) {
                    cfg.put(Occurance.AFTER_METHOD_INVOCATION, cfgAfter);
                }
            }

        } else if (trySpecific) {
            
            // The method may be on an interface, but we need attributes from the target class.
            // If the target class is null, the method will be unchanged.
            Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
            // If we are dealing with method with generic parameters, find the original method.
            specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

            return resolveConfiguration(specificMethod, targetClass, false);

        }

        return cfg;
    }

    String methodCacheKey(final Method method, final Class<?> targetClass) {

        /*
           As per java documentation:
           "Returns a string describing this Method. The string is formatted as the method
            access modifiers, if any, followed by the method return type, followed by a
            space, followed by the class declaring the method, followed by a period, followed
            by the method name, followed by a parenthesized, comma-separated list of the
            method's formal parameter types. If the method throws checked exceptions, the
            parameter list is followed by a space, followed by the word throws followed by
            a comma-separated list of the thrown exception types. For example:

                public boolean java.lang.Object.equals(java.lang.Object)

            "  - see documentation on java.lang.reflect.Method.toString();

           This suggests that full method signature must be present in toString() output and
           therefore it should provide sufficient caching key for the method.

         */

        final String signature = method.toString();
        final String objectSignature = "java.lang.Object." + method.getName() + "(";

        if (signature.indexOf(objectSignature) == -1) {
            return (targetClass != null ? targetClass.getCanonicalName() : "") + signature;
        }
        return signature; // do not distinguish between targetClass for java.lang.Object.* methods.
    }
}
