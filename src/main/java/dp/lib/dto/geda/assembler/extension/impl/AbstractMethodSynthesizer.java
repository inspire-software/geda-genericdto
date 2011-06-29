
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.extension.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.assembler.DTOAssembler;
import dp.lib.dto.geda.assembler.extension.Cache;
import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Template for method synthesizer.
 * 
 * @author DPavlov
 * @since 1.1.2
 */
public abstract class AbstractMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractMethodSynthesizer.class);
	
	private final Lock readLock = new ReentrantLock();
	private final Lock writeLock = new ReentrantLock();
	private static final int MAX_COMPILE_TRIES = 3; 
	
	/** DataReaders instances cache. */
	private static final Cache<String, Object> READER_CACHE = new SoftReferenceCache<String, Object>(100);
	/** DataWriters instances cache. */
	private static final Cache<String, Object> WRITER_CACHE = new SoftReferenceCache<String, Object>(100);
	
	
	/**
	 * Default constructor that adds GeDA path to pool for generating files.
	 */
	public AbstractMethodSynthesizer() {
		super();
	}
	
	
	/**
	 * @param configuration configuration name
	 *            readerCleanUpCycle - allows to set clean up cycle for soft cache of readers
	 *            writerCleanUpCycle - allows to set clean up cycle for soft cache of writers
	 * @param value value to set
	 * @return true if configuration was set, false if not set or invalid
	 */
	public boolean configure(final String configuration, final Object value) {
		if ("readerCleanUpCycle".equals(configuration)) {
			LOG.info("Setting reader cleanup cycle to " + value);
			return this.setCleanUpReaderCycle(value);
		} else if ("writerCleanUpCycle".equals(configuration)) {
			LOG.info("Setting writer cleanup cycle to " + value);
			return this.setCleanUpWriterCycle(value);
		}
		return false;
	}

	/*
	 * @param cleanUpReaderCycle reader cache clean up cycle
	 */
	private boolean setCleanUpReaderCycle(final Object cleanUpReaderCycle) {
		return READER_CACHE.configure("cleanUpCycle", cleanUpReaderCycle);
	}

	/*
	 * @param cleanUpWriterCycle writer cache clean up cycle
	 */
	private boolean setCleanUpWriterCycle(final Object cleanUpWriterCycle) {
		return WRITER_CACHE.configure("cleanUpCycle", cleanUpWriterCycle);
	}
	
	/** {@inheritDoc} */
	public final DataReader synthesizeReader(final PropertyDescriptor descriptor) 
		throws InspectionPropertyNotFoundException, UnableToCreateInstanceException, GeDARuntimeException {
				
		final Method readMethod = descriptor.getReadMethod();
        if (readMethod == null) {
            throw new InspectionPropertyNotFoundException("No read method for: ", descriptor.getName());
        }
		
		final String sourceClassNameFull = readMethod.getDeclaringClass().getCanonicalName();
		final String sourceClassGetterMethodName = readMethod.getName();
		
		final String readerClassName = generateClassName("DataReader", sourceClassNameFull, sourceClassGetterMethodName);
		
		DataReader reader;
		
		reader = getFromCacheOrCreateFromClassLoader(readerClassName, READER_CACHE, getClassLoader());
		
		if (reader == null) {
			readLock.lock();
			final MakeContext ctx = new MakeContext(DataReader.class.getCanonicalName());
			try {
				do {
					reader = makeReaderClass(getClassLoader(), 
							readerClassName, sourceClassNameFull, sourceClassGetterMethodName, readMethod.getGenericReturnType(), ctx);
					if (reader == null) {
						reader = getFromCacheOrCreateFromClassLoader(readerClassName, READER_CACHE, getClassLoader());
					} else {
						READER_CACHE.put(readerClassName, reader);
					}
				} while (reader == null);
			} finally {
				readLock.unlock();
			}
		}
		return reader;
	}

	@SuppressWarnings("unchecked")
	private <T> T getFromCacheOrCreateFromClassLoader(final String readerClassName, 
													  final Cache<String, Object> cache, 
													  final ClassLoader classLoader) 
			throws UnableToCreateInstanceException {
		Object instance;

		instance = cache.get(readerClassName);
		if (instance != null) {
			return (T) instance;
		}
		
		instance = createInstanceFromClassLoader(classLoader, readerClassName);
		if (instance != null) {
			return (T) instance;
		}
		return null;
	}

	/**
	 * Method to be overridden by specific synthesizer - contains the low level code 
	 * to actually generating Class object.
	 *
	 * @param loader class loader
	 * @param readerClassName name of the reader class
	 * @param sourceClassNameFull name of the class of source object (i.e. whose getter will be invoked)
	 * @param sourceClassGetterMethodName name of the getter method to be invoked on the source object
	 * @param sourceClassGetterMethodReturnType class name of the return type to be returned
	 * @param ctx compilation context. Need to invoke .next() for every unsuccessful compilation attempt. 
	 * 
	 * @return data reader instance.
	 * 
	 * @throws UnableToCreateInstanceException whenever there is a problem creating an instance of the generated class
	 */
	protected abstract DataReader makeReaderClass(
			final ClassLoader loader,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final Type sourceClassGetterMethodReturnType,
			final MakeContext ctx) throws UnableToCreateInstanceException;

	/** {@inheritDoc} */
	public final DataWriter synthesizeWriter(final PropertyDescriptor descriptor) 
			throws InspectionPropertyNotFoundException, UnableToCreateInstanceException {
		final Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            throw new InspectionPropertyNotFoundException("No write method for: ", descriptor.getName());
        }

		final String classNameFull = writeMethod.getDeclaringClass().getCanonicalName();
		final String methodName = writeMethod.getName();
		
		final String writerClassName = generateClassName("DataWriter", classNameFull, methodName);
		
		
		DataWriter writer;

		writer = getFromCacheOrCreateFromClassLoader(writerClassName, WRITER_CACHE, getClassLoader());
		if (writer == null) {
			writeLock.lock();
			final MakeContext ctx = new MakeContext(DataWriter.class.getCanonicalName());
			try {
				do {
					writer = makeWriterClass(getClassLoader(), 
							writerClassName, classNameFull, methodName, writeMethod.getParameterTypes()[0], ctx);
					if (writer == null) {
						writer = getFromCacheOrCreateFromClassLoader(writerClassName, WRITER_CACHE, getClassLoader());
					} else {
						WRITER_CACHE.put(writerClassName, writer);
					}
				} while (writer == null);
			} finally {
				writeLock.unlock();
			}
		}
		return writer;
	}

	/**
	 * Method to be overridden by specific synthesizer - contains the low level code 
	 * to actually generating Class object.
	 * 
	 * @param loader class loader
	 * @param writerClassName name of the reader class
	 * @param sourceClassNameFull name of the class of source object (i.e. whose setter will be invoked)
	 * @param sourceClassSetterMethodName  name of the setter method to be invoked on the source object
	 * @param sourceClassSetterMethodArgumentClass  class name of the argument type passed to setter
	 * @param ctx  compilation context. Need to invoke .next() for every unsuccessful compilation attempt.
	 * 
	 * @return DataWriter instance
	 * 
	 * @throws UnableToCreateInstanceException  whenever there is a problem creating an instance of the generated class
	 */
	protected abstract DataWriter makeWriterClass(
			final ClassLoader loader,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName, 
			final Class< ? > sourceClassSetterMethodArgumentClass,
			final MakeContext ctx) throws UnableToCreateInstanceException;
	
	/**
	 * Default class loader. This is very tricky since this is the class loader which will
	 * load the auto generated classes. Hopefully the one that loaded GeDA is the less likely 
	 * one to cause problems.
	 * 
	 * @return dp.lib.dto.geda.assembler.DTOAssembler.class.getClassLoader();
	 */
	protected ClassLoader getClassLoader() {
		return DTOAssembler.class.getClassLoader();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T createInstanceFromClassLoader(final ClassLoader cl, final String clazzName) 
			throws UnableToCreateInstanceException {
		try {
			final Class< ? > clazz = Class.forName(clazzName, true, cl);
			return (T) clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			// That's OK we don't have it
			return null;
		} catch (Exception exp) {
			throw new UnableToCreateInstanceException(clazzName, "Uanble to create instance of: " + clazzName, exp);
		}
	}
	
	private String generateClassName(final String prefix, final String declaringClass, final String methodName) {
		return declaringClass + prefix + "M" + methodName;
	}
			
	/**
	 * Inner class to keep track of recursive attempts to compile a class.
	 * 
	 * @author denispavlov
	 *
	 */
	public static final class MakeContext {
		private int tryNo;
		private final String classType;
		
		/**
		 * @param classType class type (reader/writer)
		 */
		public MakeContext(final String classType) {
			this.classType = classType;
			this.tryNo = 0;
		}
		
		/**
		 * To be used to attemp recovery through classloader instance creation.
		 * If next counter exceed the number of tries and exception is thrown,
		 * otherwise another cycle is attempted.
		 * 
		 * @param exp exception which occured whilst auto generating a class
		 * @param source source code.
		 * @throws UnableToCreateInstanceException thrown after number of tries is exceeded. 
		 *         wraps the original exception.
		 */
		public void next(final Exception exp, final String source) throws UnableToCreateInstanceException {
			this.tryNo++;
			if (this.tryNo > MAX_COMPILE_TRIES) {
				throw new UnableToCreateInstanceException(classType, 
						"Unable to create class type [" + classType + "]\n" 
						+ "with source:\n============>" + source + "\n<=============", exp);
			}
		}
	}

}
