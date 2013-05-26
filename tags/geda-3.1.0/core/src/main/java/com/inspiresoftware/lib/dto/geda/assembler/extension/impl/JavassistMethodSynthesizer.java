
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;


/**
 * Javassist implementation.
 * 
 * @author DPavlov
 * @since 1.1.0
 */
public class JavassistMethodSynthesizer extends AbstractPlainTextMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavassistMethodSynthesizer.class);
	
	private ClassPool pool = new ClassPool(true);
	
	/**
	 * Default constructor that adds GeDA path to pool for generating files.
     *
     * @param classLoader class loader
	 */
	public JavassistMethodSynthesizer(final ClassLoader classLoader) {
        super(classLoader);
        appendClassPath(pool);
	}

    /**
     * Hook for javassist pool classpath alterations.
     *
     * @param pool this synthesizer's pool
     */
    protected void appendClassPath(final ClassPool pool) {
        pool.appendClassPath(new LoaderClassPath(getClassLoader()));
    }

    /**
     * Access to javassist pool for sub classes.
     *
     * @return this synthesizer's pool
     */
    protected ClassPool getClassPool() {
        return pool;
    }
	
	/** {@inheritDoc} */
	@Override
	protected String getSynthesizerId() {
		return "javassist";
	}

	/** {@inheritDoc} */
	protected DataReader makeReaderClass(
			final ClassLoader loader,
			final Method readMethod,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final Type sourceClassGetterMethodReturnType,
			final MakeContext ctx) throws UnableToCreateInstanceException, GeDARuntimeException {
		
		
		final CtClass ctClass = pool.makeClass(readerClassName);
		final StringBuilder readMethodCode = new StringBuilder();
		final StringBuilder getReturnTypeMethodCode = new StringBuilder();
		try {
			
			ctClass.setInterfaces(new CtClass[] { pool.get(DataReader.class.getCanonicalName()) });
			
			generateReaderMethods(readMethodCode, getReturnTypeMethodCode, 
					readerClassName, sourceClassNameFull, sourceClassGetterMethodName, sourceClassGetterMethodReturnType);
						
			CtMethod methodRead = CtMethod.make(readMethodCode.toString(), ctClass);
			ctClass.addMethod(methodRead);

			CtMethod methodGetReturnType = CtMethod.make(getReturnTypeMethodCode.toString(), ctClass);
			ctClass.addMethod(methodGetReturnType);
			ctClass.detach();
			
			final DataReader reader = (DataReader) ctClass.toClass(
					loader, DataReader.class.getProtectionDomain()).newInstance();
			
			return reader;
			
		} catch (CannotCompileException cce) {
			ctx.next(cce, readMethodCode.toString() + "\n\n" + getReturnTypeMethodCode.toString());

            LOG.warn("Unable to create method in class: {}... possibly class already loaded", readerClassName);

            return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(readerClassName, "Unable to instantiate class: " + readerClassName, ite);
		}
	}

	/** {@inheritDoc} */
	protected DataWriter makeWriterClass(
			final ClassLoader loader,
			final Method writeMethod,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName, 
			final Class< ? > sourceClassSetterMethodArgumentClass,
			final MakeContext ctx) throws UnableToCreateInstanceException {
						
		final CtClass ctClass = pool.makeClass(writerClassName);
		final StringBuilder writeMethodCode = new StringBuilder();
		final StringBuilder getParameterTypeMethodCode = new StringBuilder();
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataWriter.class.getCanonicalName()) });
			
			generateWriterMethods(writeMethodCode, getParameterTypeMethodCode, 
					writerClassName, sourceClassNameFull, sourceClassSetterMethodName, sourceClassSetterMethodArgumentClass);
			
			CtMethod methodWrite = CtMethod.make(writeMethodCode.toString(), ctClass);
			ctClass.addMethod(methodWrite);

			CtMethod methodGetParameterType = CtMethod.make(getParameterTypeMethodCode.toString(), ctClass);
			ctClass.addMethod(methodGetParameterType);
			ctClass.detach();
			
			final DataWriter writer = (DataWriter) ctClass.toClass(
					loader, DataWriter.class.getProtectionDomain()).newInstance();
			
			return writer;
			
		} catch (CannotCompileException cce) {
			ctx.next(cce, writeMethodCode.toString() + "\n\n" + getParameterTypeMethodCode.toString());

            LOG.warn("Unable to create method in class: {}... possibly class had been loaded", writerClassName);

            return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(writerClassName, "Unable to instantiate class: " + writerClassName, ite);
		}
	}

    /** {@inheritDoc} */
    public void releaseResources() {
        super.releaseResources();
        pool = null;
    }

}
