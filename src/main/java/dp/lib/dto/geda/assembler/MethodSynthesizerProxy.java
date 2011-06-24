
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
import java.util.HashMap;
import java.util.Map;

import dp.lib.dto.geda.assembler.extension.DataReader;
import dp.lib.dto.geda.assembler.extension.DataWriter;
import dp.lib.dto.geda.assembler.extension.MethodSynthesizer;
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
	
	private static final Map<String, String> FACTORY = new HashMap<String, String>();
	static {
		FACTORY.put("javassist", 
				"dp.lib.dto.geda.assembler.extension.impl.JavassitMethodSynthesizer");
		FACTORY.put("suntools", 
				"dp.lib.dto.geda.assembler.extension.impl.SunJavaToolsMethodSynthesizer");
	}
	
	private String synthesizerImpl = FACTORY.get("javassist");
	private MethodSynthesizer synthesizer;
	
	@SuppressWarnings("unchecked")
	private MethodSynthesizer lazyGet() {
		if (this.synthesizer == null) {
			try {
				final Class clazz = Class.forName(this.synthesizerImpl);
				this.synthesizer = (MethodSynthesizer) clazz.newInstance();
			} catch (Exception exp) {
				throw new UnableToCreateInstanceException("MethodSynthesizer", 
						"Unable to create [" + this.synthesizerImpl + "] implementation: "
						+ exp.getMessage(), exp);
			}
		}
		return this.synthesizer;
	}
	
	/** {@inheritDoc} */
	public DataReader synthesizeReader(final PropertyDescriptor descriptor)
			throws InspectionPropertyNotFoundException,
			UnableToCreateInstanceException, GeDARuntimeException {
		return lazyGet().synthesizeReader(descriptor);
	}

	/** {@inheritDoc} */
	public DataWriter synthesizeWriter(final PropertyDescriptor descriptor)
			throws InspectionPropertyNotFoundException,
			UnableToCreateInstanceException {
		return lazyGet().synthesizeWriter(descriptor);
	}

	/** {@inheritDoc} */
	public boolean configure(final String configuration, final Object value) {
		if ("synthesizerImpl".equals(configuration)) {
			final String newImpl = FACTORY.get(value);
			if (newImpl != null) {
				this.synthesizerImpl = newImpl;
				return true;
			}
		}
		return lazyGet().configure(configuration, value);
	}


}
