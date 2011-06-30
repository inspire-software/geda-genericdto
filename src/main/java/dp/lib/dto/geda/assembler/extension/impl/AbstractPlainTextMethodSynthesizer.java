
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.exception.GeDARuntimeException;

/**
 * Common functionality for generating plain text source code.
 * 
 * @author DPavlov
 * @since 1.1.2
 */
public abstract class AbstractPlainTextMethodSynthesizer extends AbstractMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractPlainTextMethodSynthesizer.class);
	
	/**
	 * Simple object to hold plain text representation of return type of reader.
	 * 
	 * @author denispavlov
	 *
	 */
	protected static class ReturnTypeContext {
		
		private final String methodReturnType;
		private final String methodReturnTypePrimitiveName;

		/**
		 * @param methodReturnType class of object that represent return type
		 * @param methodReturnTypePrimitiveName primitive name (or null if this is not primitive)
		 */
		public ReturnTypeContext(final String methodReturnType,
				final String methodReturnTypePrimitiveName) {
			this.methodReturnType = methodReturnType;
			this.methodReturnTypePrimitiveName = methodReturnTypePrimitiveName;
		}

		/**
		 * @return class of object that represent return type
		 */
		public String getMethodReturnType() {
			return methodReturnType;
		}

		/**
		 * @return primitive name (or null if this is not primitive)
		 */
		public String getMethodReturnTypePrimitiveName() {
			return methodReturnTypePrimitiveName;
		}
		
		/**
		 * @return true if this is a primitive type
		 */
		public boolean isPrimitive() {
			return methodReturnTypePrimitiveName != null;
		}
		
	}
	
	/*
	 * @param readerClassName class name
	 * @param sourceClassGetterMethodReturnType return type
	 * @return context
	 * @throws GeDARuntimeException if unable to determine correct return type
	 */
	private ReturnTypeContext getReturnTypeContext(final String readerClassName, final Type sourceClassGetterMethodReturnType)
			throws GeDARuntimeException {

		if (sourceClassGetterMethodReturnType instanceof Class) {
			final Class< ? > rcl = ((Class< ? >) sourceClassGetterMethodReturnType);
			if (rcl.isPrimitive()) {
				return new ReturnTypeContext(
						PRIMITIVE_TO_WRAPPER.get(rcl.getCanonicalName()), rcl.getCanonicalName());
			} 
			return new ReturnTypeContext(rcl.getCanonicalName(), null);
		} else if (sourceClassGetterMethodReturnType instanceof ParameterizedType) {
			return new ReturnTypeContext(
					((Class< ? >) ((ParameterizedType) sourceClassGetterMethodReturnType).getRawType()).getCanonicalName(),
					null);
		} else if (sourceClassGetterMethodReturnType instanceof TypeVariable) {
			return new ReturnTypeContext(Object.class.getCanonicalName(), null); // generics
		}
		throw new GeDARuntimeException("Unable to determine correct return type from getter method in class: " + readerClassName);
	}
	
	/**
	 * Generates plain text source code for data reader methods.
	 * 
	 * @param readMethodCode string builder that hold source for method
	 * @param getReturnTypeMethodCode string builder that hold source for method
	 * @param readerClassName name of the reader class
	 * @param sourceClassNameFull name of the class of source object (i.e. whose getter will be invoked)
	 * @param sourceClassGetterMethodName name of the getter method to be invoked on the source object
	 * @param sourceClassGetterMethodReturnType class name of the return type to be returned
	 * @throws GeDARuntimeException any exceptions during compilation
	 */
	protected final void generateReaderMethods(
			final StringBuilder readMethodCode, 
			final StringBuilder getReturnTypeMethodCode,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final Type sourceClassGetterMethodReturnType) throws GeDARuntimeException {
		
		final ReturnTypeContext returnType = getReturnTypeContext(readerClassName, sourceClassGetterMethodReturnType);
		
		readMethodCode
			.append("public Object read(Object source) {\n")
			.append("final ").append(sourceClassNameFull).append(" clazz = (").append(sourceClassNameFull).append(") source;\n");
		if (!returnType.isPrimitive()) {
			readMethodCode
			.append("return clazz.").append(sourceClassGetterMethodName).append("();\n")
			.append("}");
		} else {
			readMethodCode
			.append("return ");
			appendValueOf(returnType.getMethodReturnTypePrimitiveName(), readMethodCode, "clazz." + sourceClassGetterMethodName + "()");
			readMethodCode
			.append(";\n}");
		}
			
		getReturnTypeMethodCode
			.append("public Class getReturnType() {\n")
			.append("return ").append(returnType.getMethodReturnType()).append(".class;\n")
			.append("}");
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating DataReader Class: \n" 
					+ readMethodCode.toString() + "\n"
					+ getReturnTypeMethodCode.toString());
		}	
	}
	
	/**
	 * Simple object to hold plain text representation of argument type of reader.
	 * 
	 * @author denispavlov
	 *
	 */
	private class ArgumentTypeContext {

		private final String methodArgType;
		private final String methodArgPrimitiveName;
		
		/**
		 * @param methodArgType object class name
		 * @param methodArgPrimitiveName primitive name (or null if this type is not primitive)
		 */
		public ArgumentTypeContext(final String methodArgType,
				final String methodArgPrimitiveName) {
			super();
			this.methodArgType = methodArgType;
			this.methodArgPrimitiveName = methodArgPrimitiveName;
		}
		
		/**
		 * @return object class name
		 */
		public String getMethodArgType() {
			return methodArgType;
		}
		
		/**
		 * @return primitive name (or null if this type is not primitive)
		 */
		public String getMethodArgPrimitiveName() {
			return methodArgPrimitiveName;
		}
		
		/**
		 * @return true if this is a primitive type
		 */
		public boolean isPrimitive() {
			return methodArgPrimitiveName != null;
		}
		
	}
	
	/*
	 * @param sourceClassSetterMethodArgumentClass class name of the argument type passed to setter
	 * @return context
	 */
	private ArgumentTypeContext getArgumentTypeContext(final Class< ? > sourceClassSetterMethodArgumentClass) {

		if (sourceClassSetterMethodArgumentClass.isPrimitive()) {
			return new ArgumentTypeContext(
					PRIMITIVE_TO_WRAPPER.get(sourceClassSetterMethodArgumentClass.getCanonicalName()), 
					sourceClassSetterMethodArgumentClass.getCanonicalName());
		} 
		return new ArgumentTypeContext(sourceClassSetterMethodArgumentClass.getCanonicalName(), null);

	}
	
	/**
	 * Generates plain text source code for data writer methods.
	 * 
	 * @param writeMethodCode writer method code
	 * @param getParameterTypeMethodCode parameter type getter code
	 * @param writerClassName name of the reader class
	 * @param sourceClassNameFull name of the class of source object (i.e. whose setter will be invoked)
	 * @param sourceClassSetterMethodName  name of the setter method to be invoked on the source object
	 * @param sourceClassSetterMethodArgumentClass  class name of the argument type passed to setter
	 */
	protected final void generateWriterMethods(
			final StringBuilder writeMethodCode,
			final StringBuilder getParameterTypeMethodCode,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName, 
			final Class< ? > sourceClassSetterMethodArgumentClass) {
		
		final ArgumentTypeContext argType = getArgumentTypeContext(sourceClassSetterMethodArgumentClass);
		
		writeMethodCode
			.append("public void write(Object source, Object value) {\n")
			.append("final ").append(sourceClassNameFull).append(" clazz = (").append(sourceClassNameFull).append(") source;\n")
			.append("clazz.").append(sourceClassSetterMethodName).append("(");
		if (!argType.isPrimitive()) {
			writeMethodCode
			.append("(").append(argType.getMethodArgType()).append(") value");
		} else {
			appendPrimitiveValue(argType.getMethodArgPrimitiveName(), writeMethodCode, "value");
		}
		writeMethodCode.append(");\n}");
	
		getParameterTypeMethodCode
			.append("public Class getParameterType() {\n")
			.append("return ").append(argType.getMethodArgType()).append(".class;\n")
			.append("}");
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating DataWriter Class: \n" 
					+ writeMethodCode.toString() + "\n"
					+ getParameterTypeMethodCode.toString());
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
