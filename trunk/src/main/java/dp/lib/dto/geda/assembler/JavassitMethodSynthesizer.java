package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Javassist implementation.
 * 
 * @author DPavlov
 * @since 1.1.0
 */
public class JavassitMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavassitMethodSynthesizer.class);
	
	private final Lock readLock = new ReentrantLock();
	private final Lock writeLock = new ReentrantLock();
	
	private static final Cache<String, Object> READER_CACHE = new SoftReferenceCache<String, Object>(100);
	private static final Cache<String, Object> WRITER_CACHE = new SoftReferenceCache<String, Object>(100);
	
	private static final Map<String, String> PRIMITIVE_TO_WRAPPER = new HashMap<String, String>();
	static {
		PRIMITIVE_TO_WRAPPER.put("byte", 	Byte.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("short", 	Short.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("int", 	Integer.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("long", 	Long.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("float", 	Float.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("double", 	Double.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("boolean", Boolean.class.getCanonicalName());
		PRIMITIVE_TO_WRAPPER.put("char", 	Character.class.getCanonicalName());
	}

	private static final Map<String, String> WRAPPER_TO_PRIMITIVE = new HashMap<String, String>();
	static {
		WRAPPER_TO_PRIMITIVE.put("byte", 	".byteValue()");
		WRAPPER_TO_PRIMITIVE.put("short", 	".shortValue()");
		WRAPPER_TO_PRIMITIVE.put("int", 	".intValue()");
		WRAPPER_TO_PRIMITIVE.put("long", 	".longValue()");
		WRAPPER_TO_PRIMITIVE.put("float", 	".floatValue()");
		WRAPPER_TO_PRIMITIVE.put("double", 	".doubleValue()");
		WRAPPER_TO_PRIMITIVE.put("boolean", ".booleanValue()");
		WRAPPER_TO_PRIMITIVE.put("char", 	".charValue()");
	}
	
	private final ClassPool pool = new ClassPool(true);
	
	/**
	 * Default constructor that adds GeDA path to pool for generating files.
	 */
	public JavassitMethodSynthesizer() {
		pool.appendClassPath(new LoaderClassPath(DTOAssembler.class.getClassLoader()));
	}
	
	/**
	 * @param cleanUpReaderCycle reader cache clean up cycle
	 */
	public void setCleanUpReaderCycle(final int cleanUpReaderCycle) {
		((SoftReferenceCache<String, Object>) READER_CACHE).setCleanUpCycle(cleanUpReaderCycle);
	}

	/**
	 * @param cleanUpWriterCycle writer cache clean up cycle
	 */
	public void setCleanUpWriterCycle(final int cleanUpWriterCycle) {
		((SoftReferenceCache<String, Object>) WRITER_CACHE).setCleanUpCycle(cleanUpWriterCycle);
	}
	
	/** {@inheritDoc} */
	public DataReader synthesizeReader(final PropertyDescriptor descriptor) 
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
			try {
				do {
					reader = makeReaderClass(pool, getClassLoader(), 
							readerClassName, sourceClassNameFull, sourceClassGetterMethodName, readMethod.getGenericReturnType());
					if (reader == null) {
						reader = getFromCacheOrCreateFromClassLoader(readerClassName, READER_CACHE, getClassLoader());
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

	private DataReader makeReaderClass(final ClassPool pool, 
			final ClassLoader loader,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final Type sourceClassGetterMethodReturnType) throws UnableToCreateInstanceException, GeDARuntimeException {
		
		final String methodReturnType;
		final String methodReturnTypePrimitiveName;
		if (sourceClassGetterMethodReturnType instanceof Class) {
			final Class< ? > rcl = ((Class< ? >) sourceClassGetterMethodReturnType);
			if (rcl.isPrimitive()) {
				methodReturnType = PRIMITIVE_TO_WRAPPER.get(rcl.getCanonicalName());
				methodReturnTypePrimitiveName = rcl.getCanonicalName();
			} else {
				methodReturnType = rcl.getCanonicalName();
				methodReturnTypePrimitiveName = null;
			}
		} else if (sourceClassGetterMethodReturnType instanceof ParameterizedType) {
			methodReturnType = ((Class< ? >) ((ParameterizedType) sourceClassGetterMethodReturnType).getRawType()).getCanonicalName();
			methodReturnTypePrimitiveName = null;
		} else if (sourceClassGetterMethodReturnType instanceof TypeVariable) {
			methodReturnType = Object.class.getCanonicalName(); // generics
			methodReturnTypePrimitiveName = null;
		} else {
			throw new GeDARuntimeException("Unable to determine correct return type from getter method in class: " + readerClassName);
		}
		
		final CtClass ctClass = pool.makeClass(readerClassName);
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataReader.class.getCanonicalName()) });
			
			final StringBuilder readMethodCode = new StringBuilder()
				.append("public Object read(Object source) {\n")
				.append("final ").append(sourceClassNameFull).append(" clazz = (").append(sourceClassNameFull).append(") source;\n");
			if (methodReturnTypePrimitiveName == null) {
				readMethodCode
				.append("return clazz.").append(sourceClassGetterMethodName).append("();\n")
				.append("}");
			} else {
				readMethodCode
				.append("return ");
				appendValueOf(methodReturnTypePrimitiveName, readMethodCode, "clazz." + sourceClassGetterMethodName + "()");
				readMethodCode
				.append(";\n}");
			}
				
			final StringBuilder getReturnTypeMethodCode = new StringBuilder()
				.append("public Class getReturnType() {\n")
				.append("return ").append(methodReturnType).append(".class;\n")
				.append("}");
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Creating DataReader Class: \n" 
						+ readMethodCode.toString() + "\n"
						+ getReturnTypeMethodCode.toString());
			}
			
			CtMethod methodRead = CtMethod.make(readMethodCode.toString(), ctClass);
			ctClass.addMethod(methodRead);

			CtMethod methodGetReturnType = CtMethod.make(getReturnTypeMethodCode.toString(), ctClass);
			ctClass.addMethod(methodGetReturnType);
			ctClass.detach();
			
			final DataReader reader = (DataReader) ctClass.toClass(
					loader, DataReader.class.getProtectionDomain()).newInstance();
			READER_CACHE.put(readerClassName, reader);
			
			return reader;
			
		} catch (CannotCompileException cce) {
			LOG.warn("Unable to create method in class: " + readerClassName + "... posibly class already loaded");
			return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(readerClassName, "Unable to instantiate class: " + readerClassName, ite);
		}
	}

	/** {@inheritDoc} */
	public DataWriter synthesizeWriter(final PropertyDescriptor descriptor) 
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
			try {
				do {
					writer = makeWriterClass(pool, getClassLoader(), 
							writerClassName, classNameFull, methodName, writeMethod.getParameterTypes()[0]);
					if (writer == null) {
						writer = getFromCacheOrCreateFromClassLoader(writerClassName, WRITER_CACHE, getClassLoader());
					}
				} while (writer == null);
			} finally {
				writeLock.unlock();
			}
		}
		return writer;
	}

	private DataWriter makeWriterClass(final ClassPool pool,
			final ClassLoader loader,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName, 
			final Class< ? > sourceClassSetterMethodArgumentClass) throws UnableToCreateInstanceException {
				
		final String methodArgType;
		final String methodArgPrimitiveName;
		if (sourceClassSetterMethodArgumentClass.isPrimitive()) {
			methodArgType = PRIMITIVE_TO_WRAPPER.get(sourceClassSetterMethodArgumentClass.getCanonicalName());
			methodArgPrimitiveName = sourceClassSetterMethodArgumentClass.getCanonicalName();
		} else {
			methodArgType = sourceClassSetterMethodArgumentClass.getCanonicalName();
			methodArgPrimitiveName = null;
		}
		
		final CtClass ctClass = pool.makeClass(writerClassName);
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataWriter.class.getCanonicalName()) });
			
			final StringBuilder writeMethodCode = new StringBuilder()
				.append("public void write(Object source, Object value) {\n")
				.append("final ").append(sourceClassNameFull).append(" clazz = (").append(sourceClassNameFull).append(") source;\n")
				.append("clazz.").append(sourceClassSetterMethodName).append("(");
			if (methodArgPrimitiveName == null) {
				writeMethodCode
				.append("(").append(methodArgType).append(") value");
			} else {
				appendPrimitiveValue(methodArgPrimitiveName, writeMethodCode, "value");
			}
			writeMethodCode.append(");\n}");

			final StringBuilder getParameterTypeMethodCode = new StringBuilder()
				.append("public Class getParameterType() {\n")
				.append("return ").append(methodArgType).append(".class;\n")
				.append("}");
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Creating DataWriter Class: \n" 
						+ writeMethodCode.toString() + "\n"
						+ getParameterTypeMethodCode.toString());
			}
			
			CtMethod methodWrite = CtMethod.make(writeMethodCode.toString(), ctClass);
			ctClass.addMethod(methodWrite);

			CtMethod methodGetParameterType = CtMethod.make(getParameterTypeMethodCode.toString(), ctClass);
			ctClass.addMethod(methodGetParameterType);
			ctClass.detach();
			
			final DataWriter writer = (DataWriter) ctClass.toClass(
					loader, DataWriter.class.getProtectionDomain()).newInstance();
			WRITER_CACHE.put(writerClassName, writer);
			
			return writer;
			
		} catch (CannotCompileException cce) {
			LOG.warn("Unable to create method in class: " + writerClassName + "... possibly class had been loaded");
			return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(writerClassName, "Unable to instantiate class: " + writerClassName, ite);
		}
	}
	
	private ClassLoader getClassLoader() {
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
	
	private void appendValueOf(final String primitiveTypeName, final StringBuilder toAppendTo, final String valueOf) {
		
		toAppendTo.append(PRIMITIVE_TO_WRAPPER.get(primitiveTypeName)).append(".valueOf(").append(valueOf).append(")");
		
	}
	
	private void appendPrimitiveValue(final String primitiveTypeName, final StringBuilder toAppendTo, final String valueOf) {
		
		toAppendTo.append("((").append(PRIMITIVE_TO_WRAPPER.get(primitiveTypeName)).append(") ").append(valueOf).append(")")
			.append(WRAPPER_TO_PRIMITIVE.get(primitiveTypeName));
		
	}

}
