
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
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.lang.reflect.Type;


/**
 * Sun Java tools implementation of method synthesizer.
 * 
 * @author denispavlov
 * @since 1.1.2
 * 
 */
public class SunJavaToolsMethodSynthesizer extends AbstractPlainTextMethodSynthesizer 
		implements MethodSynthesizer, BaseDirectoryProvider {

	private static final Logger LOG = LoggerFactory.getLogger(SunJavaToolsMethodSynthesizer.class);
	
	private static final String JAVA_SOURCE_VERSION = "1.5";
	
	private String baseDir = "";

    /**
     * Sun Java Tools method synthesizer constructor.
     * Initializes a class loader able to define classes from auto generated
     * compiled temporary files.
     *
     * @param classLoader class loader
     */
    public SunJavaToolsMethodSynthesizer(final ClassLoader classLoader) {
        super(classLoader);
    }

    /**
	 * Manual constructor with baseDir specified.
	 * 
	 * @param baseDir base dir for creating files
	 * @throws GeDAException any exceptions during configuration
	 */
	public SunJavaToolsMethodSynthesizer(final ClassLoader classLoader, final String baseDir) throws GeDAException {
		this(classLoader);
		this.configure("baseDir", baseDir);
	}
	
	/** {@inheritDoc} */
	protected String getSynthesizerId() {
		return "suntools";
	}

    /** {@inheritDoc} */
    protected SoftReference<ClassLoader> initialiseClassLoaderWeakReference(final ClassLoader classLoader) {
        return new SoftReference<ClassLoader>(new FileClassLoader(classLoader, this));
    }

    /** {@inheritDoc} */
	public String getBaseDir(final String name) {
		return baseDir;
	}

	/**
	 * @param configuration configuration name
	 * 			  baseDir - allows to set the directory where newly generated temp files for classes
	 *                      will reside until the system exits.
	 *            readerCleanUpCycle - allows to set clean up cycle for soft cache of readers
	 *            writerCleanUpCycle - allows to set clean up cycle for soft cache of writers
	 * @param value value to set
	 * @return true if configuration was set, false if not set or invalid
	 * @throws GeDAException any exceptions during configuration
	 */
	@Override
	public boolean configure(final String configuration, final Object value) throws GeDAException {
		if ("baseDir".equals(configuration) && value instanceof String) {
			final String dir = (String) value;
			if (dir.endsWith("/")) {
				this.baseDir = dir;
			} else {
				this.baseDir = dir + "/";
			}
            LOG.info("Setting class loader base dir to: {}", this.baseDir);
            return true;
		}
		return super.configure(configuration, value);
	}

	/** {@inheritDoc} */
	protected DataReader makeReaderClass(
			final ClassLoader loader,
			final Method readMethod,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName,
			final Type sourceClassGetterMethodReturnType, 
			final MakeContext ctx)
			throws UnableToCreateInstanceException {
		
		try {
			final StringBuilder readMethodCode = new StringBuilder();
			final StringBuilder getReturnTypeMethodCode = new StringBuilder();
			
			generateReaderMethods(readMethodCode, getReturnTypeMethodCode, 
					readerClassName, sourceClassNameFull, sourceClassGetterMethodName, sourceClassGetterMethodReturnType);

			final String source = generateReaderSource(readerClassName, readMethodCode, getReturnTypeMethodCode);
			final File clazz = createSourceFile(readerClassName, source);
			if (compile(clazz) == 0) {
				final Class< ? > readerClass = getClassLoader().loadClass(readerClassName);
				return (DataReader) readerClass.newInstance();
			} else {
				ctx.next(null, source);
			}
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(readerClassName, "Unable to instantiate class: " + readerClassName, ite);
		}
		return null;
	}
	
	private String generateReaderSource(final String fullClassName, 
			final StringBuilder readMethodCode, final StringBuilder getReturnTypeMethodCode) {
		final int namePos = fullClassName.lastIndexOf('.');
		final String packageName = fullClassName.substring(0, namePos);
		final String className = fullClassName.substring(namePos + 1);

		final StringBuilder source = new StringBuilder();
		source
			.append("package ").append(packageName).append(";\n")
			.append("import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;\n")
			.append("public class ").append(className).append(" implements DataReader {\n")
			.append(getReturnTypeMethodCode)
			.append(readMethodCode)
			.append("}\n");
		return source.toString();
	}
	
	/** {@inheritDoc} */
	protected DataWriter makeWriterClass(
			final ClassLoader loader, 
			final Method writeMethod,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName,
			final Class< ? > sourceClassSetterMethodArgumentClass, 
			final MakeContext ctx)
			throws UnableToCreateInstanceException {		
		try {
			final StringBuilder writeMethodCode = new StringBuilder();
			final StringBuilder getParameterTypeMethodCode = new StringBuilder();

			generateWriterMethods(writeMethodCode, getParameterTypeMethodCode, 
					writerClassName, sourceClassNameFull, sourceClassSetterMethodName, sourceClassSetterMethodArgumentClass);
			
			final String source = generateWriterSource(writerClassName, writeMethodCode, getParameterTypeMethodCode);
			final File clazz = createSourceFile(writerClassName, source);
			if (compile(clazz) == 0) {
				final Class< ? > writerClass = getClassLoader().loadClass(writerClassName);
				return (DataWriter) writerClass.newInstance();
			} else {
				ctx.next(null, source);
			}
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(writerClassName, "Unable to instantiate class: " + writerClassName, ite);
		}
		return null;
	}

	private String generateWriterSource(final String fullClassName, 
			final StringBuilder writeMethodCode, final StringBuilder getParameterTypeMethodCode) {
		final int namePos = fullClassName.lastIndexOf('.');
		final String packageName = fullClassName.substring(0, namePos);
		final String className = fullClassName.substring(namePos + 1);

		final StringBuilder source = new StringBuilder();
		source
			.append("package ").append(packageName).append(";\n")
			.append("import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;\n")
			.append("public class ").append(className).append(" implements DataWriter {\n")
			.append(getParameterTypeMethodCode)
			.append(writeMethodCode)
			.append("}\n");
		return source.toString();
	}
	
	private File createSourceFile(final String className, final String source)
			throws UnableToCreateInstanceException {
		final String readerSimpleName = className.substring(className.lastIndexOf('.') + 1); 
		final File clazz = new File(this.baseDir + readerSimpleName + ".java");
		try {

            if (LOG.isDebugEnabled()) {
				LOG.debug("Attempt to create source file: {}", clazz.getAbsolutePath());
			}

            if (!clazz.exists()) {
                clazz.deleteOnExit();
                clazz.createNewFile();
            }
			
            LOG.debug("Source: \n{}\n", source);
			
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(clazz);
				fos.write(source.getBytes());
			} finally {
				if (fos != null) {
					fos.close();
				}
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("Successfully created source file: {}", clazz.getAbsolutePath());
			}
            
			return clazz;
		} catch (IOException ioe) {
			throw new UnableToCreateInstanceException("DataReader", 
					"Unable to create temporary file for reader source: " 
					+ clazz.getAbsolutePath(), ioe);
		}
	}
	
	private static int compile(final File source) {
		return Main.compile(new String[] { 
				"-source", JAVA_SOURCE_VERSION, 
				"-target", JAVA_SOURCE_VERSION, 
				"-verbose", 
				source.getAbsolutePath() 
			});		
	}

	
}
