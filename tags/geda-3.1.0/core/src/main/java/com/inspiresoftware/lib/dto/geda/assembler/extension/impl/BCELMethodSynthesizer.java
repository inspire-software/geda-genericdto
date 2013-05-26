
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import static org.apache.bcel.Constants.ACC_PUBLIC;
import static org.apache.bcel.Constants.ACC_SUPER;


/**
 * CGLib implementation.
 * 
 * @author DPavlov
 * @since 1.1.0
 */
public class BCELMethodSynthesizer extends AbstractMethodSynthesizer 
		implements MethodSynthesizer, BaseDirectoryProvider {
	
	private static final int MAJOR = Constants.MAJOR_1_5;
	private static final int MINOR = Constants.MINOR_1_5;

	private static final Logger LOG = LoggerFactory.getLogger(BCELMethodSynthesizer.class);
	
	private String baseDir = null;
	
	
	/**
	 * Primitive to wrapper conversion map.
	 */
	protected static final Map<String, org.apache.bcel.generic.Type> PRIMITIVE_TO_TYPE = new HashMap<String, org.apache.bcel.generic.Type>();
	static {
		PRIMITIVE_TO_TYPE.put("byte", 	org.apache.bcel.generic.Type.BYTE);
		PRIMITIVE_TO_TYPE.put("short", 	org.apache.bcel.generic.Type.SHORT);
		PRIMITIVE_TO_TYPE.put("int", 	org.apache.bcel.generic.Type.INT);
		PRIMITIVE_TO_TYPE.put("long", 	org.apache.bcel.generic.Type.LONG);
		PRIMITIVE_TO_TYPE.put("float", 	org.apache.bcel.generic.Type.FLOAT);
		PRIMITIVE_TO_TYPE.put("double", 	org.apache.bcel.generic.Type.DOUBLE);
		PRIMITIVE_TO_TYPE.put("boolean", org.apache.bcel.generic.Type.BOOLEAN);
		PRIMITIVE_TO_TYPE.put("char", 	org.apache.bcel.generic.Type.CHAR);
	}

	
	/**
	 * Default constructor that adds GeDA path to pool for generating files.
     *
     * @param classLoader class loader
	 */
	public BCELMethodSynthesizer(ClassLoader classLoader) {
		super(classLoader);
	}

    /**
     * Manual BCEL synthesizer constructor that enables generated file dumps.
     *
     * @param classLoader class loader
     * @param baseDir base directory
     */
    public BCELMethodSynthesizer(final ClassLoader classLoader, final String baseDir) {
        this(classLoader);
        this.configure("baseDir", baseDir);
    }

    /** {@inheritDoc} */
    protected SoftReference<ClassLoader> initialiseClassLoaderWeakReference(final ClassLoader classLoader) {
        if (baseDir == null) {
            return new SoftReference<ClassLoader>(new ByteClassLoader(classLoader));
        }
        return new SoftReference<ClassLoader>(new FileClassLoader(classLoader, this));
    }

	/** {@inheritDoc} */
	@Override
	protected String getSynthesizerId() {
		return "bcel";
	}
	
	/** {@inheritDoc} */
	public String getBaseDir(final String name) {
		return baseDir;
	}
	
	/*
	 * Load class as byte array.
	 */
	@SuppressWarnings("unchecked")
	private <T> T loadClass(final ClassLoader loader, final String className, final ClassGen cg)
			throws IOException, InstantiationException, IllegalAccessException {
		if (this.baseDir == null) {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			cg.getJavaClass().dump(out);
			final Class< ? > clazzB = ((ByteClassLoader) loader).loadClass(className, out.toByteArray());
			return (T) clazzB.newInstance();
		}
		
		final String readerSimpleName = className.substring(className.lastIndexOf('.') + 1); 
		final File clazz = new File(this.baseDir + readerSimpleName + ".class");
		clazz.deleteOnExit();

        LOG.debug("Attempt to create source file: {}", clazz.getAbsolutePath());

		clazz.createNewFile();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(clazz);
			cg.getJavaClass().dump(fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

        LOG.debug("Successfully created source file: {}", clazz.getAbsolutePath());

		try {
			final Class< ? > clazzF = loader.loadClass(className);
			return (T) clazzF.newInstance();
		} catch (ClassNotFoundException cnfe) {
			throw new IOException(cnfe.getMessage());
		}
	}
	
	private String enhanceInnerClassName(final String clazz, final boolean inner) {
		if (inner) {
			final int pos = clazz.lastIndexOf('.');
			return clazz.substring(0, pos) + '$' + clazz.substring(pos + 1);
		}
		return clazz;
	}

	/** {@inheritDoc} */
	protected DataReader makeReaderClass(
			final ClassLoader loader,
			final java.lang.reflect.Method readMethod,
			final String readerClassName, 
			final String sourceClassNameFull,
			final String sourceClassGetterMethodName, 
			final java.lang.reflect.Type sourceClassGetterMethodReturnType,
			final MakeContext ctx) throws UnableToCreateInstanceException, GeDARuntimeException {
		
		try {

            LOG.debug("Generating DataReader: {}", readerClassName);

			final ReturnTypeContext rtc = getReturnTypeContext(readerClassName, sourceClassGetterMethodReturnType);
			
			final ClassGen cg = new ClassGen(readerClassName, "java.lang.Object",
	                "<generated>", ACC_PUBLIC | ACC_SUPER,
	                new String[] { DataReader.class.getCanonicalName() });
			final ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
			cg.setMajor(MAJOR);
			cg.setMinor(MINOR);
			
			cg.addEmptyConstructor(ACC_PUBLIC);

            if (LOG.isDebugEnabled()) {
				LOG.debug("<init> method:\n{}", cg.getMethods()[0].getCode().toString(true));
			}
			
			final InstructionList il = new InstructionList();
			final InstructionFactory factory = new InstructionFactory(cg);
			
			// read method
			final MethodGen read = new MethodGen(
					ACC_PUBLIC, 
					org.apache.bcel.generic.Type.OBJECT,
					new org.apache.bcel.generic.Type[] {             // argument types
			                  org.apache.bcel.generic.Type.OBJECT 
	                 },
	                 new String[] { "source" },
	                 "read", readerClassName, il, cp);
			
			final org.apache.bcel.generic.Type sourceClassType = new ObjectType(sourceClassNameFull);
			
			il.append(new ALOAD(1));
			il.append(factory.createCast(read.getArgumentType(0), sourceClassType));
			final LocalVariableGen clazz = read.addLocalVariable("clazz", sourceClassType, null, null);
			final int clazzIndex = clazz.getIndex();
			clazz.setStart(il.append(new ASTORE(clazzIndex)));
			il.append(new ALOAD(clazzIndex));
			il.append(factory.createInvoke(sourceClassNameFull, 
									sourceClassGetterMethodName, 
									rtc.isPrimitive() 
										? PRIMITIVE_TO_TYPE.get(rtc.getMethodReturnTypePrimitiveName())
										: new ObjectType(enhanceInnerClassName(rtc.getMethodReturnType(), rtc.getClazz().isMemberClass())), 
									org.apache.bcel.generic.Type.NO_ARGS, 
									readMethod.getDeclaringClass().isInterface()
										? Constants.INVOKEINTERFACE
										: Constants.INVOKEVIRTUAL));
			if (rtc.isPrimitive()) {
				il.append(factory.createInvoke(rtc.getMethodReturnType(), 
										"valueOf", 
										new ObjectType(rtc.getMethodReturnType()), 
										new org.apache.bcel.generic.Type[] {             // argument types
											PRIMITIVE_TO_TYPE.get(rtc.getMethodReturnTypePrimitiveName())
										}, Constants.INVOKESTATIC));
			}
			clazz.setEnd(il.append(InstructionFactory.ARETURN));
			read.setMaxStack();
			cg.addMethod(read.getMethod());

            if (LOG.isDebugEnabled()) {
				LOG.debug("read method:\n{}", read.getMethod().getCode().toString(true));
			}

            il.dispose();
			
			// return type method
			final MethodGen returnType = new MethodGen(
					ACC_PUBLIC, 
					org.apache.bcel.generic.Type.CLASS,
					org.apache.bcel.generic.Type.NO_ARGS,
	                 null,
	                 "getReturnType", readerClassName, il, cp);
			il.append(new LDC_W(cp.addClass(enhanceInnerClassName(rtc.getMethodReturnType(), rtc.getClazz().isMemberClass()))));
			il.append(InstructionFactory.ARETURN);
			returnType.setMaxStack();
			cg.addMethod(returnType.getMethod());

            if (LOG.isDebugEnabled()) {
				LOG.debug("return type method:\n{}", returnType.getMethod().getCode().toString(true));
			}

            il.dispose();
				
			return loadClass(loader, readerClassName, cg);
			
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(readerClassName, "Unable to instantiate class: " + readerClassName, ite);
		}
	}

	/** {@inheritDoc} */
	protected DataWriter makeWriterClass(
			final ClassLoader loader,
			final java.lang.reflect.Method writeMethod,
			final String writerClassName, 
			final String sourceClassNameFull,
			final String sourceClassSetterMethodName, 
			final Class< ? > sourceClassSetterMethodArgumentClass,
			final MakeContext ctx) throws UnableToCreateInstanceException {
		
		try {

            LOG.error("Generating WriterClass: {}", writerClassName);

			final ArgumentTypeContext atc = getArgumentTypeContext(sourceClassSetterMethodArgumentClass);		
			
			final ClassGen cg = new ClassGen(writerClassName, "java.lang.Object",
	                "<generated>", ACC_PUBLIC | ACC_SUPER,
	                new String[] { DataWriter.class.getCanonicalName() });
			final ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
			cg.setMajor(MAJOR);
			cg.setMinor(MINOR);
			
			cg.addEmptyConstructor(ACC_PUBLIC);

            if (LOG.isDebugEnabled()) {
				LOG.debug("<init> method:\n{}", cg.getMethods()[0].getCode().toString(true));
			}
			
			final InstructionList il = new InstructionList();
			final InstructionFactory factory = new InstructionFactory(cg);
			
			// write method
			final MethodGen write = new MethodGen(
					ACC_PUBLIC, 
					org.apache.bcel.generic.Type.VOID,
					new org.apache.bcel.generic.Type[] {             // argument types
							org.apache.bcel.generic.Type.OBJECT, 
			                org.apache.bcel.generic.Type.OBJECT, 
	                 },
	                 new String[] { "source", "value" },
	                 "write", writerClassName, il, cp);
			
			
			final org.apache.bcel.generic.Type sourceClassType = new ObjectType(sourceClassNameFull);
			
			il.append(new ALOAD(1)); // source
			il.append(factory.createCast(write.getArgumentType(0), sourceClassType));
			final LocalVariableGen clazz = write.addLocalVariable("clazz", sourceClassType, null, null);
			final int clazzIndex = clazz.getIndex();
			clazz.setStart(il.append(new ASTORE(clazzIndex)));
			il.append(new ALOAD(clazzIndex));
			il.append(new ALOAD(2)); // value
			il.append(factory.createCast(write.getArgumentType(1), 
					new ObjectType(enhanceInnerClassName(atc.getMethodArgType(), atc.getClazz().isMemberClass()))));
			if (atc.isPrimitive()) {
				final String textMethCall = WRAPPER_TO_PRIMITIVE.get(atc.getMethodArgPrimitiveName()); 
				il.append(factory.createInvoke(atc.getMethodArgType(), 
									textMethCall.substring(1, textMethCall.length() - 2), 
									PRIMITIVE_TO_TYPE.get(atc.getMethodArgPrimitiveName()), 
									org.apache.bcel.generic.Type.NO_ARGS, 
									Constants.INVOKEVIRTUAL));
			}
			il.append(factory.createInvoke(sourceClassNameFull, 
									sourceClassSetterMethodName, 
									org.apache.bcel.generic.Type.VOID,
									new org.apache.bcel.generic.Type[] {
										atc.isPrimitive() 
											? PRIMITIVE_TO_TYPE.get(atc.getMethodArgPrimitiveName())
											: new ObjectType(enhanceInnerClassName(atc.getMethodArgType(), atc.getClazz().isMemberClass()))
									}, writeMethod.getDeclaringClass().isInterface()
										? Constants.INVOKEINTERFACE
										: Constants.INVOKEVIRTUAL));

			clazz.setEnd(il.append(InstructionFactory.RETURN));
			write.setMaxStack();
			cg.addMethod(write.getMethod());

            if (LOG.isDebugEnabled()) {
				LOG.debug("write method:\n{}", write.getMethod().getCode().toString(true));
			}

            il.dispose();
			
			// arg type method
			final MethodGen paramType = new MethodGen(
					ACC_PUBLIC, 
					org.apache.bcel.generic.Type.CLASS,
					org.apache.bcel.generic.Type.NO_ARGS,
	                 null,
	                 "getParameterType", writerClassName, il, cp);
			il.append(new LDC_W(cp.addClass(enhanceInnerClassName(atc.getMethodArgType(), atc.getClazz().isMemberClass()))));
			il.append(InstructionFactory.ARETURN);
			paramType.setMaxStack();
			cg.addMethod(paramType.getMethod());

            if (LOG.isDebugEnabled()) {
				LOG.debug("return type method:\n{}", paramType.getMethod().getCode().toString(true));
			}

            il.dispose();
	
			return loadClass(loader, writerClassName, cg);
			
		} catch (Exception ite) {
			throw new UnableToCreateInstanceException(writerClassName, "Unable to instantiate class: " + writerClassName, ite);
		}

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
            enhanceClassLoader();
			return true;
		}
		return super.configure(configuration, value);
	}


}
