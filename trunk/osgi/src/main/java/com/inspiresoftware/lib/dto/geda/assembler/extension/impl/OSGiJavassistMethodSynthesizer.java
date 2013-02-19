/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import com.inspiresoftware.lib.dto.geda.osgi.impl.Activator;
import javassist.ClassPool;
import javassist.LoaderClassPath;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * OSGi version for the javassist synthesizer.
 *
 * User: denispavlov
 * Date: 13-02-19
 * Time: 8:40 AM
 */
public class OSGiJavassistMethodSynthesizer extends JavassistMethodSynthesizer {

    /** {@inheritDoc} */
    @Override
    protected void appendClassPath(final ClassPool pool) {
        pool.appendClassPath(new LoaderClassPath(Activator.class.getClassLoader()));
        super.appendClassPath(pool);
    }

    /** {@inheritDoc} */
    @Override
    protected DataReader makeReaderClass(final ClassLoader loader,
                                         final Method readMethod,
                                         final String readerClassName,
                                         final String sourceClassNameFull,
                                         final String sourceClassGetterMethodName,
                                         final Type sourceClassGetterMethodReturnType,
                                         final MakeContext ctx) throws UnableToCreateInstanceException, GeDARuntimeException {
        // Need to add class path for incoming classes since they are in a different cl
        getClassPool().appendClassPath(new LoaderClassPath(readMethod.getDeclaringClass().getClassLoader()));
        return super.makeReaderClass(loader, readMethod, readerClassName, sourceClassNameFull, sourceClassGetterMethodName, sourceClassGetterMethodReturnType, ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /** {@inheritDoc} */
    @Override
    protected DataWriter makeWriterClass(final ClassLoader loader,
                                         final Method writeMethod,
                                         final String writerClassName,
                                         final String sourceClassNameFull,
                                         final String sourceClassSetterMethodName,
                                         final Class<?> sourceClassSetterMethodArgumentClass,
                                         final MakeContext ctx) throws UnableToCreateInstanceException {
        // Need to add class path for incoming classes since they are in a different cl
        getClassPool().appendClassPath(new LoaderClassPath(writeMethod.getDeclaringClass().getClassLoader()));
        return super.makeWriterClass(loader, writeMethod, writerClassName, sourceClassNameFull, sourceClassSetterMethodName, sourceClassSetterMethodArgumentClass, ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
