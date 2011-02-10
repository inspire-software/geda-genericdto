package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Javassist implementation.
 * 
 * @author DPavlov
 * @since 1.1.0
 */
public class JavassitMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavassitMethodSynthesizer.class);
	
	private static final Cache<String, DataReader> READER_CACHE = new SoftReferenceCache<String, DataReader>(100);
	private static final Cache<String, DataWriter> WRITER_CACHE = new SoftReferenceCache<String, DataWriter>(100);
	
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
	
	public void setCleanUpReaderCycle(final int cleanUpReaderCycle) {
		((SoftReferenceCache<String, DataReader>) READER_CACHE).setCleanUpCycle(cleanUpReaderCycle);
	}

	public void setCleanUpWriterCycle(final int cleanUpWriterCycle) {
		((SoftReferenceCache<String, DataWriter>) WRITER_CACHE).setCleanUpCycle(cleanUpWriterCycle);
	}
	
	/** {@inheritDoc} */
	public DataReader synthesizeReader(PropertyDescriptor descriptor) {
				
		final Method readMethod = descriptor.getReadMethod();
		
		final String sourceClassNameFull = readMethod.getDeclaringClass().getCanonicalName();
		final String sourceClassGetterMethodName = readMethod.getName();
		
		final String readerClassName = generateClassName("DataReader", sourceClassNameFull, sourceClassGetterMethodName);
		
		DataReader reader;
		
		// 1. Try to get cached instance
		reader = READER_CACHE.get(readerClassName);
		if (reader != null) {
			return reader;
		}
		
		// 2. Try to check if we have class in the class loader.
		reader = createInstanceFromClassLoader(getClassLoader(), readerClassName);
		if (reader != null) {
			return reader;
		}
		
		// 3. It cannot be found anywhere so we need to make one.
		return makeReaderClass(ClassPool.getDefault(), readerClassName, sourceClassNameFull, sourceClassGetterMethodName, readMethod.getGenericReturnType());
	}

	private DataReader makeReaderClass(final ClassPool pool, 
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final Type sourceClassGetterMethodReturnType) {
		
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
			throw new IllegalArgumentException("Uanble to determine correct return type from getter method in class: " + readerClassName);
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
			
			final DataReader reader = (DataReader) ctClass.toClass(getClassLoader()).newInstance();
			READER_CACHE.put(readerClassName, reader);
			
			return reader;
			
		} catch (NotFoundException nfe) {
			throw new IllegalArgumentException("Unable to create class: " + readerClassName, nfe);
		} catch (CannotCompileException cce) {
			throw new IllegalArgumentException("Unable to create method in class: " + readerClassName, cce);
		} catch (InstantiationException ite) {
			throw new IllegalArgumentException("Unable to instantiate class: " + readerClassName, ite);
		} catch (IllegalAccessException iae) {
			throw new IllegalArgumentException("Unable to instantiate class: " + readerClassName, iae);
		}
	}

	/** {@inheritDoc} */
	public DataWriter synthesizeWriter(PropertyDescriptor descriptor) {
		final Method writeMethod = descriptor.getWriteMethod();
		
		final String classNameFull = writeMethod.getDeclaringClass().getCanonicalName();
		final String methodName = writeMethod.getName();
		
		final String writerClassName = generateClassName("DataWriter", classNameFull, methodName);
		
		
		DataWriter writer;

		// 1. Try to get cached instance
		writer = WRITER_CACHE.get(writerClassName);
		if (writer != null) {
			return writer;
		}
		
		// 2. Try to check if we have class in the class loader.
		writer = createInstanceFromClassLoader(getClassLoader(), writerClassName);
		if (writer != null) {
			return writer;
		}
		
		// 3. It cannot be found anywhere so we need to make one.
		return makeWriterClass(ClassPool.getDefault(), writerClassName, classNameFull, methodName, writeMethod.getParameterTypes()[0]);
	}

	private DataWriter makeWriterClass(final ClassPool pool, 
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName, 
			final Class< ? > sourceClassSetterMethodArgumentClass) {
				
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
			
			final DataWriter writer = (DataWriter) ctClass.toClass(getClassLoader()).newInstance();
			WRITER_CACHE.put(writerClassName, writer);
			
			return writer;
			
		} catch (NotFoundException nfe) {
			throw new IllegalArgumentException("Unable to create class: " + writerClassName, nfe);
		} catch (CannotCompileException cce) {
			throw new IllegalArgumentException("Unable to create method in class: " + writerClassName, cce);
		} catch (InstantiationException ite) {
			throw new IllegalArgumentException("Unable to instantiate class: " + writerClassName, ite);
		} catch (IllegalAccessException iae) {
			throw new IllegalArgumentException("Unable to instantiate class: " + writerClassName, iae);
		}
	}
	
	private ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T createInstanceFromClassLoader(final ClassLoader cl, final String clazzName) {
		try {
			final Class< ? > clazz = Class.forName(clazzName, true, getClassLoader());
			return (T) clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			// That's OK we don't have it
			return null;
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Uanble to create instance of: " + clazzName, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Uanble to create instance of: " + clazzName, e);
		}
	}
	
	private String generateClassName(String prefix, String declaringClass, String methodName) {
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
