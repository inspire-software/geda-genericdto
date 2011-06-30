
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
import dp.lib.dto.geda.exception.GeDAException;
import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Proxy for method synthesizer implementation.
 * 
 * @author denispavlov
 * @since 1.1.2
 *
 */
class MethodSynthesizerProxy implements MethodSynthesizer {
	
	private static final Logger LOG = LoggerFactory.getLogger(MethodSynthesizerProxy.class);
	
	private static final Map<String, String> FACTORY = new HashMap<String, String>();
	static {
		FACTORY.put("javassist", 
				"dp.lib.dto.geda.assembler.extension.impl.JavassitMethodSynthesizer");
		FACTORY.put("suntools", 
				"dp.lib.dto.geda.assembler.extension.impl.SunJavaToolsMethodSynthesizer");
		FACTORY.put("reflection", 
				"dp.lib.dto.geda.assembler.extension.impl.ReflectionMethodSynthesizer");
	}
	
	private final Lock lock = new ReentrantLock();
	
	private String synthesizerImpl = FACTORY.get("javassist");
	private MethodSynthesizer synthesizer;
	
	/**
	 * Configurable instance.	
	 */
	public MethodSynthesizerProxy() {
		super();
	}

	/**
	 * Preconfigured instance.
	 * @param value synthesizer instance
	 * @throws UnableToCreateInstanceException when instance cannot be configured
	 */
	public MethodSynthesizerProxy(final Object value) throws UnableToCreateInstanceException {
		this();
		lazyGet(value);
	}
	
	/**
	 * @return keys for tests.
	 */
	static Collection<String> getAvailableSynthesizers() {
		return FACTORY.keySet();
	}
	
	/**
	 * @return instance retrieval for test
	 */
	MethodSynthesizer getSynthesizer() {
		return synthesizer;
	}

	@SuppressWarnings("unchecked")
	private MethodSynthesizer lazyGet(final Object value) throws UnableToCreateInstanceException {
		if (this.synthesizer == null) {
			try {
				lock.lock();
				if (this.synthesizer == null) {
					if (value == null) {
						// default impl
						final Class clazz = Class.forName(this.synthesizerImpl);
						this.synthesizer = (MethodSynthesizer) clazz.newInstance();						
					
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
							this.synthesizer = (MethodSynthesizer) clazz.newInstance();
						} else if (LOG.isWarnEnabled()) {
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
		} else if (value != null && LOG.isWarnEnabled()) {
			LOG.warn("Synthesizer is already set to: " 
					+ this.synthesizer.getClass().getCanonicalName() 
					+ (value == null ? "" : ", configuration [" + value + "] ignored"));
		}
		return this.synthesizer;
	}
	
	/** {@inheritDoc} */
	public DataReader synthesizeReader(final PropertyDescriptor descriptor)
			throws InspectionPropertyNotFoundException,
			UnableToCreateInstanceException, GeDARuntimeException {
		final MethodSynthesizer syn = lazyGet(null);
		return syn.synthesizeReader(descriptor);
	}

	/** {@inheritDoc} */
	public DataWriter synthesizeWriter(final PropertyDescriptor descriptor)
			throws InspectionPropertyNotFoundException,
			UnableToCreateInstanceException, GeDARuntimeException {
		final MethodSynthesizer syn = lazyGet(null);
		return syn.synthesizeWriter(descriptor);
	}

	/** {@inheritDoc} */
	public boolean configure(final String configuration, final Object value) throws GeDAException {
		final MethodSynthesizer syn;
		if ("synthesizerImpl".equals(configuration)) {
			syn = lazyGet(value);
			return true;
		} else {
			syn = lazyGet(null);
		}
		return syn.configure(configuration, value);
	}


}
