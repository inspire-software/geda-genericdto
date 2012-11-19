
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;


/**
 * Common functionality for generating plain text source code.
 * 
 * @author DPavlov
 * @since 1.1.2
 */
public abstract class AbstractPlainTextMethodSynthesizer extends AbstractMethodSynthesizer implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractPlainTextMethodSynthesizer.class);
	
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
		
		LOG.debug("Creating DataReader Class: \n{}\n{}\n", readMethodCode, getReturnTypeMethodCode);
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

        LOG.debug("Creating DataWriter Class: \n{}\n{}\n", writeMethodCode, getParameterTypeMethodCode);

	}
		
	private void appendValueOf(final String primitiveTypeName, final StringBuilder toAppendTo, final String valueOf) {
		
		toAppendTo.append(PRIMITIVE_TO_WRAPPER.get(primitiveTypeName)).append(".valueOf(").append(valueOf).append(")");
		
	}
	
	private void appendPrimitiveValue(final String primitiveTypeName, final StringBuilder toAppendTo, final String valueOf) {
		
		toAppendTo.append("((").append(PRIMITIVE_TO_WRAPPER.get(primitiveTypeName)).append(") ").append(valueOf).append(")")
			.append(WRAPPER_TO_PRIMITIVE.get(primitiveTypeName));
		
	}
	
}
