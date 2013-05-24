/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.dsl;

import com.inspiresoftware.lib.dto.geda.adapter.Adapters;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactoryProvider;
import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.impl.DefaultDSLRegistry;

/**
 * Utility class for getting default implementation of the registry
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 9:26 AM
 */
public final class Registries {

    private Registries() {
        // no instance
    }

    /**
     * Create a new registry instance with new default bean factory.
     *
     * @return new registry instance
     */
    public static Registry registry() {
        return new DefaultDSLRegistry(Adapters.beanFactory());
    }

    /**
     * Create a new registry instance with new default bean factory.
     *
     * @param classLoader class loader to use for bean factory
     *
     * @return new registry instance
     */
    public static Registry registry(ClassLoader classLoader) {
        return new DefaultDSLRegistry(Adapters.beanFactory(classLoader));
    }

    /**
     * Create a new registry instance.
     *
     * @param beanFactory extensible bean factory
     *
     * @return new registry instance
     */
    public static Registry registry(final ExtensibleBeanFactory beanFactory) {
        return new DefaultDSLRegistry(beanFactory);
    }

    /**
     * Get bean factory from DSL registry. If registry implements BeanFactory
     * then cast and return.
     *
     * @param registry registry
     *
     * @return bean factory or null (if none available)
     */
    public static BeanFactory beanFactory(final Registry registry) {
        if (registry instanceof BeanFactory) {
            return (BeanFactory) registry;
        } else if (registry instanceof BeanFactoryProvider) {
            return ((BeanFactoryProvider) registry).getBeanFactory();
        }
        return null;
    }

}
