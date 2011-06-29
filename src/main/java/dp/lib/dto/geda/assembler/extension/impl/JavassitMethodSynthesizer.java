
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.extension.impl;

import java.lang.reflect.Type;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Javassist implementation.
 * 
 * @author DPavlov
 * @since 1.1.0
 */
public class JavassitMethodSynthesizer extends AbstractPlainTextMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavassitMethodSynthesizer.class);
	
	private final ClassPool pool = new ClassPool(true);
	
	/**
	 * Default constructor that adds GeDA path to pool for generating files.
	 */
	public JavassitMethodSynthesizer() {
		pool.appendClassPath(new LoaderClassPath(super.getClassLoader()));
	}

	/** {@inheritDoc} */
	protected DataReader makeReaderClass(
			final ClassLoader loader,
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
			LOG.warn("Unable to create method in class: " + readerClassName + "... posibly class already loaded");
			return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(readerClassName, "Unable to instantiate class: " + readerClassName, ite);
		}
	}

	/** {@inheritDoc} */
	protected DataWriter makeWriterClass(
			final ClassLoader loader,
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
			LOG.warn("Unable to create method in class: " + writerClassName + "... possibly class had been loaded");
			return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(writerClassName, "Unable to instantiate class: " + writerClassName, ite);
		}
	}
	
}
