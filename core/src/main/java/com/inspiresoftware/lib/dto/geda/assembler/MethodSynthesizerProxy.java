
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Proxy for method synthesizer implementation.
 * 
 * @author denispavlov
 * @since 1.1.2
 *
 */
class MethodSynthesizerProxy implements MethodSynthesizer {
	
	private static final String DEFAULT = "javassist";

	private static final Logger LOG = LoggerFactory.getLogger(MethodSynthesizerProxy.class);
	
	private static final Map<String, String> FACTORY = new HashMap<String, String>();
	static {
		FACTORY.put("javassist", 
				"com.inspiresoftware.lib.dto.geda.assembler.extension.impl.JavassistMethodSynthesizer");
		FACTORY.put("reflection",
				"com.inspiresoftware.lib.dto.geda.assembler.extension.impl.ReflectionMethodSynthesizer");
		FACTORY.put("bcel", 
				"com.inspiresoftware.lib.dto.geda.assembler.extension.impl.BCELMethodSynthesizer");
	}
	
	private final Lock lock = new ReentrantLock();
	
	private String synthesizerImpl = FACTORY.get(DEFAULT);
	private MethodSynthesizer synthesizer;

    private final Reference<ClassLoader> clRef;
	
	/**
	 * Configurable instance.
     *
     * @param classLoader class loader namespace
	 */
	public MethodSynthesizerProxy(final ClassLoader classLoader) {
		clRef = new SoftReference<ClassLoader>(classLoader);
	}

	/**
	 * Pre-configured instance.
     *
     * @param classLoader class loader namespace
	 * @param value synthesizer instance
     *
	 * @throws UnableToCreateInstanceException when instance cannot be configured
	 */
	public MethodSynthesizerProxy(final ClassLoader classLoader, final Object value) throws UnableToCreateInstanceException {
		this(classLoader);
		if (value instanceof String) {
			final String[] configs = ((String) value).split(";");
			final String syn = configs[0];
			final MethodSynthesizer synth = lazyGet(classLoader, syn);
			for (int i = 1; i < configs.length; i++) {
				final String config = configs[i];
				final String name = config.substring(0, config.indexOf('='));
				final String val = config.substring(name.length() + 1);
				try {
					synth.configure(name, val);
				} catch (GeDAException geda) {
					throw new UnableToCreateInstanceException(synth.getClass().getCanonicalName(), 
							"Unable to configure with: " + value, geda);
				}
			}
		} else {
			lazyGet(classLoader, value);
		}
	}
	
	/**
	 * @return keys for tests.
	 */
	static Collection<String> getAvailableSynthesizers() {
		return FACTORY.keySet();
	}
	
	/**
	 * @return default key for testing.
	 */
	static String getDefaultImpl() {
		return FACTORY.get(DEFAULT);
	}
	
	/**
	 * @return instance retrieval for test
	 */
	MethodSynthesizer getSynthesizer() {
		return synthesizer;
	}

	@SuppressWarnings("unchecked")
	private MethodSynthesizer lazyGet(final ClassLoader classLoader, final Object value) throws UnableToCreateInstanceException {
		if (this.synthesizer == null) {
			try {
				lock.lock();
				if (this.synthesizer == null) {
					if (value == null) {
						// default impl
						final Class clazz = Class.forName(this.synthesizerImpl);
						this.synthesizer = (MethodSynthesizer) clazz.getConstructor(ClassLoader.class).newInstance(classLoader);
					
					} else {
						if (value instanceof MethodSynthesizer) {
							this.synthesizer = (MethodSynthesizer) value;
							this.synthesizerImpl = value.getClass().getCanonicalName();
						} else if (value instanceof String) {
							final String newImpl = FACTORY.get(value);
							if (newImpl != null) {
								this.synthesizerImpl = newImpl;
							} else {
								this.synthesizerImpl = value.toString();
							}
							final Class clazz = Class.forName(this.synthesizerImpl);
							this.synthesizer = (MethodSynthesizer) clazz.getConstructor(ClassLoader.class).newInstance(classLoader);
						} else {
							throw new UnableToCreateInstanceException("MethodSynthesizer", 
									"Unable to create [" + value + "] implementation: "
									+ "configuration not recognizer (Must be either: " 
									+ "MethodSynthesizer instance, or full class name as string, " 
									+ "or one of the following markers: " 
									+ new ArrayList<String>(FACTORY.keySet()).toString() + ")", null);
						}
					}
				}
			} catch (UnableToCreateInstanceException rethrow) {
				throw rethrow;
			} catch (Exception exp) {
				throw new UnableToCreateInstanceException("MethodSynthesizer", 
						"Unable to create [" + this.synthesizerImpl + "] implementation: "
						+ exp.getMessage(), exp);
			} finally {
				lock.unlock();
			}
            if (this.clRef != null) {
                this.clRef.clear(); // release class loader as soon as we do not need it anymore.
            }
        } else if (value != null) {
			LOG.warn("Synthesizer is already set to: {}, configuration [{}] ignored",
					this.synthesizer.getClass().getCanonicalName(), value);
		}
		return this.synthesizer;
	}
	
	/** {@inheritDoc} */
	public DataReader synthesizeReader(final PropertyDescriptor descriptor)
			throws InspectionPropertyNotFoundException,
			UnableToCreateInstanceException, GeDARuntimeException {
		final MethodSynthesizer syn = lazyGet(clRef.get(), null);
		return syn.synthesizeReader(descriptor);
	}

	/** {@inheritDoc} */
	public DataWriter synthesizeWriter(final PropertyDescriptor descriptor)
			throws InspectionPropertyNotFoundException,
			UnableToCreateInstanceException, GeDARuntimeException {
		final MethodSynthesizer syn = lazyGet(clRef.get(), null);
		return syn.synthesizeWriter(descriptor);
	}

	/** {@inheritDoc} */
	public boolean configure(final String configuration, final Object value) throws GeDAException {
		final MethodSynthesizer syn;
		if ("synthesizerImpl".equals(configuration)) {
			syn = lazyGet(clRef.get(), value);
			return true;
		} else {
			syn = lazyGet(clRef.get(), null);
		}
		return syn.configure(configuration, value);
	}

	/**
	 * @return {@link MethodSynthesizerProxy#synthesizerImpl} - full class name.
	 */
	@Override
	public String toString() {
		return synthesizerImpl;
	}

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || synthesizer == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            // match proxy against source
            return  (o instanceof MethodSynthesizer) && synthesizer.equals(o);

        }

        return synthesizer.equals(((MethodSynthesizerProxy) o).synthesizer);

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return synthesizer != null ? synthesizer.hashCode() : super.hashCode();
    }

    /** {@inheritDoc} */
    public void releaseResources() {
        if (synthesizer != null) {
            synthesizer.releaseResources();
        }
        clRef.clear();
    }
}
