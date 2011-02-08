package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

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

	private String generateClassName(String prefix, String declaringClass, String methodName) {
		return "dp.lib.dto.geda.assembler." + prefix + declaringClass + "M" + methodName;
	}
	
	/** {@inheritDoc} */
	public DataReader synthesizeReader(PropertyDescriptor descriptor) {
				
		final Method readMethod = descriptor.getReadMethod();
		
		final String className = readMethod.getDeclaringClass().getSimpleName();
		final String classNameFull = readMethod.getDeclaringClass().getCanonicalName();
		final String methodName = readMethod.getName();
		
		final String readerClassName = generateClassName("DataReader", className, methodName);
		
		final ClassPool pool = ClassPool.getDefault();
		final CtClass ctClass = pool.makeClass(readerClassName);
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataReader.class.getCanonicalName()) });
			
			final StringBuilder methodText = new StringBuilder()
				.append("public Object read(Object source) {\n")
				.append("final ").append(classNameFull).append(" clazz = (").append(classNameFull).append(") source;\n")
				.append("return clazz.").append(methodName).append("();\n")
				.append("}");
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Creating DataReader Class: \n" + methodText.toString());
			}
			
			CtMethod method = CtMethod.make(methodText.toString(), ctClass);
			ctClass.addMethod(method);
			
			return (DataReader) ctClass.toClass().newInstance();
			
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
		
		final String className = writeMethod.getDeclaringClass().getSimpleName();
		final String classNameFull = writeMethod.getDeclaringClass().getCanonicalName();
		final String methodName = writeMethod.getName();
		final String argumentClass = writeMethod.getParameterTypes()[0].getCanonicalName();
		
		final String writerClassName = generateClassName("DataWriter", className, methodName);
		
		final ClassPool pool = ClassPool.getDefault();
		final CtClass ctClass = pool.makeClass(writerClassName);
		try {
			ctClass.setInterfaces(new CtClass[] { pool.get(DataWriter.class.getCanonicalName()) });
			
			final StringBuilder methodText = new StringBuilder()
				.append("public void write(Object source, Object value) {\n")
				.append("final ").append(classNameFull).append(" clazz = (").append(classNameFull).append(") source;\n")
				.append("return clazz.").append(methodName).append("((" + argumentClass + ") value);\n")
				.append("}");
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Creating DataWriter Class: \n" + methodText.toString());
			}
			
			CtMethod method = CtMethod.make(methodText.toString(), ctClass);
			ctClass.addMethod(method);
			
			return (DataWriter) ctClass.toClass().newInstance();
			
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

}
