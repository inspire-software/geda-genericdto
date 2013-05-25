/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.osgi.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;
import com.inspiresoftware.lib.dto.geda.osgi.DTOSupportAnnotationsService;
import com.inspiresoftware.lib.dto.geda.osgi.GeDAFacade;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: denispavlov
 * Date: 13-02-18
 * Time: 3:44 PM
 */
public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private ServiceRegistration serviceRegistration;

    /** {@inheritDoc} */
    public void start(final BundleContext bundleContext) throws Exception {

        LOG.info("Starting GeDA bundle");

        LOG.info("GeDA registering OSGi service {}", DTOSupportAnnotationsService.class.getCanonicalName());

        serviceRegistration =
                bundleContext.registerService(
                        GeDAFacade.class.getCanonicalName(),
                        new GeDAFacadeImpl(bundleContext),
                        null);

        bundleContext.addServiceListener(new ServiceListener() {
            public void serviceChanged(final ServiceEvent event) {
                if (event.getType() == ServiceEvent.UNREGISTERING) {
                    final ServiceReference ref = event.getServiceReference();
                    if (ref == serviceRegistration.getReference()) {
                        // Flush all resources that might have been missed
                        final DisposableContainer facade =
                                (DisposableContainer) bundleContext.getService(ref);
                        facade.releaseResources();
                    }
                }
            }
        });

        LOG.info("GeDA bundle started");
    }

    public void stop(final BundleContext bundleContext) throws Exception {

        LOG.info("GeDA bundle stopping");

        serviceRegistration.unregister();

        LOG.info("GeDA bundle stopped");
    }

}
