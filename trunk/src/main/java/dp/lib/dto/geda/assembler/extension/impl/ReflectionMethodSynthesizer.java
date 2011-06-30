
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.extension.impl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Reflection implementation of method synthsizer. Least efficient implementation,
 * yet the most stable one since it does not require auto generation of classes
 * during runtime.
 * 
 * @author denispavlov
 * @since 1.1.2
 *
 */
public class ReflectionMethodSynthesizer extends AbstractMethodSynthesizer
		implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReflectionMethodSynthesizer.class); 

	/** {@inheritDoc} */
	@Override
	protected DataReader makeReaderClass(
			final ClassLoader loader,
			final Method readMethod,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName,
			final Type sourceClassGetterMethodReturnType, 
			final MakeContext ctx)
			throws UnableToCreateInstanceException, GeDARuntimeException {
		
		final Class< ? > returnType;

		if (sourceClassGetterMethodReturnType instanceof Class) {
			final Class< ? > rcl = ((Class< ? >) sourceClassGetterMethodReturnType);
			if (rcl.isPrimitive()) {
				returnType = PRIMITIVE_TO_WRAPPER_CLASS.get(rcl.getCanonicalName());
			} else {
				returnType = (Class< ? >) sourceClassGetterMethodReturnType;
			}
		} else if (sourceClassGetterMethodReturnType instanceof ParameterizedType) {
			returnType = 
					(Class< ? >) ((ParameterizedType) sourceClassGetterMethodReturnType).getRawType();
		} else if (sourceClassGetterMethodReturnType instanceof TypeVariable) {
			returnType = Object.class; // generics
		} else {
			returnType = Object.class; // default
		}
		
		return new DataReader() {
			
			private final Method method = readMethod;
			private final Class< ? > type = returnType;

			public Class< ? > getReturnType() {
				return type;
			}

			public Object read(final Object source) {
				try {
					return method.invoke(source);
				} catch (Exception exp) {
					LOG.error(exp.getMessage(), exp);
				}
				return null;
			}
			
		};
	}

	/** {@inheritDoc} */
	@Override
	protected DataWriter makeWriterClass(
			final ClassLoader loader, 
			final Method writeMethod,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName,
			final Class< ? > sourceClassSetterMethodArgumentClass, 
			final MakeContext ctx)
			throws UnableToCreateInstanceException {

		final Class< ? > argType;
		if (sourceClassSetterMethodArgumentClass.isPrimitive()) {
			argType = PRIMITIVE_TO_WRAPPER_CLASS.get(sourceClassSetterMethodArgumentClass.getCanonicalName());
		} else {
			argType = sourceClassSetterMethodArgumentClass;
		}
		
		return new DataWriter() {
			
			private final Method method = writeMethod;
			private final Class< ? > type = argType;

			public Class< ? > getParameterType() {
				return type;
			}

			public void write(final Object source, final Object value) {
				final Object args = value;
				try {
					this.method.invoke(source, args);
				} catch (Exception exp) {
					LOG.error(exp.getMessage(), exp);
				}
			}
			
		};
	}

}
