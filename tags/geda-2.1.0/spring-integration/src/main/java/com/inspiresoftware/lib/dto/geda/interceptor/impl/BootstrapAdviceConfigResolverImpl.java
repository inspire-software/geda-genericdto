/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigRepository;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * This implementation works in pair with {@link GeDABootstrapAdvicePostProcessor}
 * bean post processor.
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 1:03:36 PM
 */
public class BootstrapAdviceConfigResolverImpl implements AdviceConfigResolver {

    private AdviceConfigRepository repository;

    public BootstrapAdviceConfigResolverImpl() {
    }

    /** {@inheritDoc} */
    public Map<Occurrence, AdviceConfig> resolve(final Method method, final Class<?> targetClass) {

        final Integer methodCacheKey = this.repository.methodCacheKey(method, targetClass);
        final Map<Integer, Map<Occurrence, AdviceConfig>> advisable = repository.getAdvisableMethodsConfigurations(targetClass);

        if (advisable.containsKey(methodCacheKey)) {
            return advisable.get(methodCacheKey);
        }

        return Collections.emptyMap();
    }

    /**
     * @param repository IoC method.
     */
    public void setRepository(final AdviceConfigRepository repository) {
        this.repository = repository;
    }
}