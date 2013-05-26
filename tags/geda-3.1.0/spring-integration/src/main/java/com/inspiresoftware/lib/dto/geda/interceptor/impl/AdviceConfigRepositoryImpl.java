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
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 27, 2012
 * Time: 4:44:13 PM
 */
public class AdviceConfigRepositoryImpl implements AdviceConfigRepository {

    private static final Logger LOG = LoggerFactory.getLogger(AdviceConfigRepositoryImpl.class);

    private final Map<String, Map<Integer, Map<Occurrence, AdviceConfig>>> cache
            = new ConcurrentHashMap<String, Map<Integer, Map<Occurrence, AdviceConfig>>>();


    /** {@inheritDoc} */
    public void addApplicableMethods(final Object bean) {
        final Method[] methods = bean.getClass().getMethods();
        for (final Method method : methods) {
            if (Modifier.isPublic(method.getModifiers())) {
                addMethodIfApplicable(method, bean.getClass());                
            }
        }
    }

    public void addMethodIfApplicable(final Method method, final Class<?> targetClass) {

        Map<Integer, Map<Occurrence, AdviceConfig>> cMap = getAdvisableMethodsConfigurations(targetClass);
        if (cMap.isEmpty()) {
            cMap = new ConcurrentHashMap<Integer, Map<Occurrence, AdviceConfig>>();
            cache.put(targetClass.getCanonicalName(), cMap);
        }

        final Integer key = methodCacheKey(method, targetClass);
        if (!cMap.containsKey(key)) {
            final Map<Occurrence, AdviceConfig> methodCfg = resolveConfiguration(method, targetClass);
            if (methodCfg.isEmpty()) {
                return;
            }
            cMap.put(key, methodCfg);
            if (LOG.isInfoEnabled()) {
                int methCount = 0;
                for (Map meths : cache.values()) {
                    methCount += meths.size();
                }
                LOG.info("Added GeDA configuration for method: {}.{}[p={}]... {} total mappings so far",
                        new Object[] {
                                targetClass.getCanonicalName(),
                                method.getName(),
                                method.getParameterTypes().length,
                                methCount });
            }
        }

    }

    Map<Occurrence, AdviceConfig> resolveConfiguration(final Method method,
                                                      final Class<?> targetClass) {

        return TransferableUtils.resolveConfiguration(method, targetClass);
        
    }

    /** {@inheritDoc} */
    public Map<Integer, Map<Occurrence, AdviceConfig>> getAdvisableMethodsConfigurations(final Class<?> targetClass) {
        if (targetClass == null) {
            return Collections.emptyMap();
        }
        final Map<Integer, Map<Occurrence, AdviceConfig>> cMap = cache.get(targetClass.getCanonicalName());
        if (cMap == null) {
            return Collections.emptyMap();
        }
        return cMap;
    }

    /**
     * This key generation algorithm uses plain java.lang.reflect.Method.toString() to create a
     * unique method signature that can be used.
     *
     * @param method potentially advisable method
     * @param targetClass bean class on which it is invoked
     * @return key for this method/class pair
     */
    public Integer methodCacheKey(final Method method, final Class<?> targetClass) {

        final StringBuilder signature = new StringBuilder(method.getName()).append('(');
        final Class[] args = method.getParameterTypes();
        if (args.length > 0) {
            for (Class arg : args) {
                signature.append(arg.getCanonicalName()).append(',');
            }
            signature.deleteCharAt(signature.length() - 1);
        }
        signature.append(')');

        return signature.toString().hashCode();

        /*
        int hashKey = method.getName().hashCode();
        final Class[] args = method.getParameterTypes();
        if (args.length > 0) {
            for (Class arg : args) {
                hashKey = 31 * hashKey + arg.hashCode();
            }
        }

        return Integer.valueOf(hashKey);
        */
    }
}
