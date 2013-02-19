/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi.impl;

import com.inspiresoftware.lib.dto.geda.osgi.DTOSupportAnnotationsService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: denispavlov
 * Date: 13-02-18
 * Time: 3:44 PM
 */
public class Activator implements BundleActivator {

    private final Collection<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();

    /** {@inheritDoc} */
    public void start(final BundleContext bundleContext) throws Exception {

        serviceRegistrations.add(
                bundleContext.registerService(
                        DTOSupportAnnotationsService.class.getCanonicalName(),
                        new DTOSupportAnnotationsServiceImpl(),
                        null)
        );

        System.out.println("GeDA bundle started");
    }

    public void stop(final BundleContext bundleContext) throws Exception {

        for (final ServiceRegistration registration : serviceRegistrations) {
            registration.unregister();
        }

        // TODO: flush cache

        System.out.println("GeDA bundle stopped");
    }

}
