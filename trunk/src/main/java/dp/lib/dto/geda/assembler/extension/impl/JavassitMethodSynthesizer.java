
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.extension.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.assembler.DTOAssembler;
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
public class JavassitMethodSynthesizer extends AbstractMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavassitMethodSynthesizer.class);
		
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

	/** {@inheritDoc} */
	protected DataReader makeReaderClass(
			final ClassLoader loader,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final Type sourceClassGetterMethodReturnType,
			final MakeContext ctx) throws UnableToCreateInstanceException, GeDARuntimeException {
		
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
		final StringBuilder readMethodCode = new StringBuilder();
		final StringBuilder getReturnTypeMethodCode = new StringBuilder();
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataReader.class.getCanonicalName()) });
			
			readMethodCode
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
				
			getReturnTypeMethodCode
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
		final StringBuilder writeMethodCode = new StringBuilder();
		final StringBuilder getParameterTypeMethodCode = new StringBuilder();
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataWriter.class.getCanonicalName()) });
			
			writeMethodCode
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

			getParameterTypeMethodCode
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
			
			return writer;
			
		} catch (CannotCompileException cce) {
			ctx.next(cce, writeMethodCode.toString() + "\n\n" + getParameterTypeMethodCode.toString());
			LOG.warn("Unable to create method in class: " + writerClassName + "... possibly class had been loaded");
			return null;
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(writerClassName, "Unable to instantiate class: " + writerClassName, ite);
		}
	}
		
	private void appendValueOf(final String primitiveTypeName, final StringBuilder toAppendTo, final String valueOf) {
		
		toAppendTo.append(PRIMITIVE_TO_WRAPPER.get(primitiveTypeName)).append(".valueOf(").append(valueOf).append(")");
		
	}
	
	private void appendPrimitiveValue(final String primitiveTypeName, final StringBuilder toAppendTo, final String valueOf) {
		
		toAppendTo.append("((").append(PRIMITIVE_TO_WRAPPER.get(primitiveTypeName)).append(") ").append(valueOf).append(")")
			.append(WRAPPER_TO_PRIMITIVE.get(primitiveTypeName));
		
	}
	
}
