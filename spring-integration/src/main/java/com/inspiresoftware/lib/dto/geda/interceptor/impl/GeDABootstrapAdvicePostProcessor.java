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

import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * This post processor will scan the methods annotated with {@link com.inspiresoftware.lib.dto.geda.annotations.Transferable}
 * on beans in order to white-list methods to be used for advice.
 *
 * User: denispavlov
 * Date: Jan 27, 2012
 * Time: 4:00:01 PM
 */
public class GeDABootstrapAdvicePostProcessor implements BeanPostProcessor, Ordered {

    private final AdviceConfigRepository repo;

    public GeDABootstrapAdvicePostProcessor() {
        this.repo = new AdviceConfigRepositoryImpl();
    }

    /** {@inheritDoc} */
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        if (bean instanceof BootstrapAdviceConfigResolverImpl) {
            ((BootstrapAdviceConfigResolverImpl) bean).setRepository(this.repo);
        } else {
            this.repo.addApplicableMethods(bean);
        }
        return bean;
    }

    /** {@inheritDoc} */
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    /** {@inheritDoc} */
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
