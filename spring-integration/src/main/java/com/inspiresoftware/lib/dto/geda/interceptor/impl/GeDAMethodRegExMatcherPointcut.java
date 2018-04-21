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

import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

import java.lang.reflect.Method;

/**
 * RegEx matcher with match the regular expression using the
 * {@link org.springframework.aop.support.JdkRegexpMethodPointcut} and then
 * will use basic {@link com.inspiresoftware.lib.dto.geda.interceptor.impl.GeDAMethodMatcherPointcut}
 * functionality.
 *
 * Please note that {@link org.springframework.aop.support.AbstractRegexpMethodPointcut} matches on union (OR)
 * of targetClass.getName() and method.getDeclaringClass().getName(), so both implementation and interfaces must
 * conform to match exclusions (if you have any).
 *
 * User: denispavlov
 * Date: May 1, 2012
 * Time: 10:00:00 AM
 */
public class GeDAMethodRegExMatcherPointcut extends GeDAMethodMatcherPointcut {

    private JdkRegexpMethodPointcut regexPointcut = new JdkRegexpMethodPointcut();

    /**
     * Uses {@link org.springframework.aop.support.JdkRegexpMethodPointcut} to work with regex
     * matching.
     *
     * @param resolver resolver that will actually match the invocations
     */
    public GeDAMethodRegExMatcherPointcut(final AdviceConfigResolver resolver) {
        super(resolver);
    }

	/**
	 * Set the regular expressions defining methods to match.
	 * Matching will be the union of all these; if any match,
	 * the pointcut matches.
	 */
	public void setPatterns(String[] patterns) {
        regexPointcut.setPatterns(patterns);
	}

	/**
	 * Set the regular expressions defining methods to match for exclusion.
	 * Matching will be the union of all these; if any match,
	 * the pointcut matches.
	 */
	public void setExcludedPatterns(String[] excludedPatterns) {
        regexPointcut.setExcludedPatterns(excludedPatterns);
    }

    /** {@inheritDoc} */
    @Override
    public boolean matches(final Method method, final Class<?> targetClass) {
        return regexPointcut.matches(method, targetClass);
    }
}
