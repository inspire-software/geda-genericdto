/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi.impl;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;
import com.inspiresoftware.lib.dto.geda.osgi.DTOSupportAnnotationsService;
import com.inspiresoftware.lib.dto.geda.osgi.DTOSupportDSLService;
import com.inspiresoftware.lib.dto.geda.osgi.GeDAFacade;
import org.osgi.framework.BundleContext;

import java.util.WeakHashMap;

/**
 * User: denispavlov
 * Date: 13-02-20
 * Time: 5:04 PM
 */
public class GeDAFacadeImpl implements GeDAFacade, DisposableContainer {

    private final BundleContext bundleContext;

    private final WeakHashMap<ClassLoader, DTOSupportAnnotationsService> annPool = new WeakHashMap<ClassLoader, DTOSupportAnnotationsService>();
    private final WeakHashMap<ClassLoader, DTOSupportDSLService> dslPool = new WeakHashMap<ClassLoader, DTOSupportDSLService>();

    public GeDAFacadeImpl(final BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /** {@inheritDoc} */
    public DTOSupportAnnotationsService getAnnService(final ClassLoader activator) {

        DTOSupportAnnotationsService annSrv = annPool.get(activator);
        if (annSrv == null) {
            synchronized (annPool) {
                if (annSrv == null) {
                    annSrv = new DTOSupportAnnotationsServiceImpl(activator);
                    annPool.put(activator, annSrv);
                }
            }
        }

        return annSrv;

    }

    /** {@inheritDoc} */
    public DTOSupportDSLService getDSLService(final ClassLoader activator) {

        DTOSupportDSLService dslSrv = dslPool.get(activator);
        if (dslSrv == null) {
            synchronized (dslPool) {
                if (dslSrv == null) {
                    dslSrv = new DTOSupportDSLServiceImpl(activator);
                    dslPool.put(activator, dslSrv);
                }
            }
        }

        return dslSrv;

    }

    /** {@inheritDoc} */
    public ExtensibleBeanFactory createBeanFactory(final ClassLoader activator) {
        return new OSGiBundleDTOFactoryImpl(activator);
    }

    /** {@inheritDoc} */
    public void releaseResources(final ClassLoader activator) {

        // Here we dispose of everything that is linked to this class loader

        final DTOSupportAnnotationsService annSrv = annPool.get(activator);
        if (annSrv != null) {
            ((DisposableContainer) annSrv).releaseResources();
        }
        final DTOSupportDSLService dslSrv = dslPool.get(activator);
        if (dslSrv != null) {
            ((DisposableContainer) dslSrv).releaseResources();
        }
    }

    /** {@inheritDoc} */
    public void releaseResources() {

        for (ClassLoader loader : annPool.keySet()) {
            releaseResources(loader);
        }
        for (ClassLoader loader : dslPool.keySet()) {
            releaseResources(loader);
        }

    }
}
