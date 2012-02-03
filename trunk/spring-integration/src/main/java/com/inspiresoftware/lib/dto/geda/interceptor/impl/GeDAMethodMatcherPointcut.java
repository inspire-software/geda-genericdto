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
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DynamicMethodMatcher;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Basic method matcher pointcut.
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 2:15:41 PM
 */
public class GeDAMethodMatcherPointcut extends DynamicMethodMatcher implements Pointcut {

    private final AdviceConfigResolver resolver;

    public GeDAMethodMatcherPointcut(final AdviceConfigResolver resolver) {
        this.resolver = resolver;
    }

    /** {@inheritDoc} */
    public boolean matches(final Method method, final Class<?> targetClass, final Object[] args) {
        final Map<Occurrence, AdviceConfig> cfg = this.resolver.resolve(method, targetClass);
        return !CollectionUtils.isEmpty(cfg);
    }

    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }

    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
