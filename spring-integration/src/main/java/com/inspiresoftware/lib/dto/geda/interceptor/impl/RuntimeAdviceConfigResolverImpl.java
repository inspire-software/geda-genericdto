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

import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic implementation of the resolved that examines method annotations in order to
 * produce configuration objects. Provides method signature cache to store the
 * configuration information in a permanent cache. 
 *
 * Additionally this resolver keeps track of the methods that are not applicable and
 * thus are blacklisted.
 *
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 1:03:36 PM
 */
public class RuntimeAdviceConfigResolverImpl implements AdviceConfigResolver {

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeAdviceConfigResolverImpl.class);

    private final Map<Integer, Boolean> blacklist = new ConcurrentHashMap<Integer, Boolean>();
    private final Map<Integer, Map<Occurrence, AdviceConfig>> cache = new ConcurrentHashMap<Integer, Map<Occurrence, AdviceConfig>>();

    public RuntimeAdviceConfigResolverImpl() {
    }

    /** {@inheritDoc} */
    public Map<Occurrence, AdviceConfig> resolve(final Method method, final Class<?> targetClass) {

        final Integer methodCacheKey = methodCacheKey(method, targetClass);
        if (isBlacklisted(methodCacheKey)) {
            return Collections.emptyMap();
        }

        if (isCached(methodCacheKey)) {
            return this.cache.get(methodCacheKey);
        }

        final Map<Occurrence, AdviceConfig> cfg = resolveConfiguration(method, targetClass);

        if (cfg.isEmpty()) {
            this.blacklist.put(methodCacheKey, Boolean.TRUE);
        } else {
            cache.put(methodCacheKey, cfg);
            if (LOG.isInfoEnabled()) {
                LOG.info("Added GeDA configuration for method: {}.{}[p={}]... {} total mappings so far",
                        new Object[] {
                                targetClass == null ? method.getDeclaringClass().getCanonicalName() : targetClass.getCanonicalName(),
                                method.getName(),
                                method.getParameterTypes().length,
                                cache.size() });
            }
        }

        return cfg;
    }

    boolean isCached(final Integer methodCacheKey) {
        return this.cache.containsKey(methodCacheKey);
    }

    boolean isBlacklisted(final Integer methodCacheKey) {
        return this.blacklist.containsKey(methodCacheKey);
    }

    Map<Occurrence, AdviceConfig> resolveConfiguration(final Method method,
                                                      final Class<?> targetClass) {

        return TransferableUtils.resolveConfiguration(method, targetClass);

    }

    Integer methodCacheKey(final Method method, final Class<?> targetClass) {

        int hashKey = targetClass != null ? targetClass.getCanonicalName().hashCode() : method.getDeclaringClass().getCanonicalName().hashCode();
        hashKey = 31 * hashKey + method.getName().hashCode();
        final Class[] args = method.getParameterTypes();
        if (args.length > 0) {
            for (Class arg : args) {
                hashKey = 31 * hashKey + arg.hashCode();
            }
        }

        return Integer.valueOf(hashKey);

    }
}
