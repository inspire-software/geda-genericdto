/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.osgi;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;

/**
 * GeDA OSGi facade that provide a clean way of working with GeDA to
 * minimise memory leaks.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 4:40 PM
 */
public interface GeDAFacade {

    /**
     * Annotations driven DTO service.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     *
     * @return DTO service
     */
    DTOSupportAnnotationsService getAnnService(ClassLoader activator);

    /**
     * DSL driven DTO service.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     *
     * @return DTO service
     */
    DTOSupportDSLService getDSLService(ClassLoader activator);

    /**
     * Create BeanFactory adapter for activators bundle. Activators class
     * loader will be used for Class.forName() invocations.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     *
     * @return bean factory instance.
     */
    ExtensibleBeanFactory createBeanFactory(ClassLoader activator);

    /**
     * Release resources for given activator.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     */
    void releaseResources(ClassLoader activator);

}
