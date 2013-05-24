/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.adapter;

import com.inspiresoftware.lib.dto.geda.adapter.impl.ClassLoaderBeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.repository.AdaptersRepository;
import com.inspiresoftware.lib.dto.geda.adapter.repository.impl.AdaptersRepositoryImpl;

/**
 * Utility class for getting default implementations
 *
 * @since 3.0.1
 *
 * User: denispavlov
 * Date: 13-05-24
 * Time: 8:44 AM
 */
public class Adapters {

    private Adapters() {
        // no instance
    }

    /**
     * Return default bean factory implementation bound to current class loader.
     *
     * @return bean factory
     */
    public static ExtensibleBeanFactory beanFactory() {
        return new ClassLoaderBeanFactory(Adapters.class.getClassLoader());
    }

    /**
     * Return default bean factory implementation bound to current class loader.
     *
     * @param classLoader class loader for instantiation of classes.
     *
     * @return bean factory
     */
    public static ExtensibleBeanFactory beanFactory(final ClassLoader classLoader) {
        return new ClassLoaderBeanFactory(classLoader);
    }

    /**
     * Default implementation of adapters repository
     *
     * @return new adapter repository instance.
     */
    public static AdaptersRepository adaptersRepository() {
        return new AdaptersRepositoryImpl();
    }

}
