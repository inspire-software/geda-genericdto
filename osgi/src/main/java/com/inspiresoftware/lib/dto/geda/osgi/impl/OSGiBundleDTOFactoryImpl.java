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

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.impl.ClassLoaderBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;

/**
 * Simple string key for class mapping with basic constructor reflection
 * instance creation.
 *
 * This is disposable throw away container - once it had been disposed
 * it is no longer should be used as we loose class loader reference.
 *
 * User: denispavlov
 * Date: May 25, 2011
 * Time: 6:33:52 PM
 */
public class OSGiBundleDTOFactoryImpl extends ClassLoaderBeanFactory implements ExtensibleBeanFactory, DisposableContainer {

    public OSGiBundleDTOFactoryImpl(final ClassLoader classLoader) {
        super(classLoader);
    }

}