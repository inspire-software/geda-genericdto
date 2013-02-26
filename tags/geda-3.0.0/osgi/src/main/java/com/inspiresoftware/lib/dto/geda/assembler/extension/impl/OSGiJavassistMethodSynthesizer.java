/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.osgi.impl.Activator;
import javassist.ClassPool;
import javassist.LoaderClassPath;

/**
 * OSGi version for the javassist synthesizer.
 *
 * User: denispavlov
 * Date: 13-02-19
 * Time: 8:40 AM
 */
public class OSGiJavassistMethodSynthesizer extends JavassistMethodSynthesizer {

    /**
     * OSGi version of Javassist synthesizer.
     *
     * @param classLoader class loader
     */
    public OSGiJavassistMethodSynthesizer(final ClassLoader classLoader) {
        super(classLoader);
    }

    /** {@inheritDoc} */
    @Override
    protected void appendClassPath(final ClassPool pool) {
        super.appendClassPath(pool);
        pool.appendClassPath(new LoaderClassPath(Activator.class.getClassLoader()));
    }

}
