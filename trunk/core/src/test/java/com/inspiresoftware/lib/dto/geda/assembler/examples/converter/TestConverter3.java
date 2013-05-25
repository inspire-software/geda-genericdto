
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.converter;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.examples.converter.TestDto3Class.Decision;
import org.junit.Ignore;


/**
 * Test converter.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestConverter3 implements ValueConverter {

	/** {@inheritDoc} */
	public Object convertToDto(final Object object, final BeanFactory beanFactory) {
		final Boolean value = (Boolean) object;
		if (value != null && value) {
			return Decision.Decided;
		}
		return Decision.Undecided;
	}
	
	/** {@inheritDoc} */
	public Object convertToEntity(final Object object, final Object oldValue, final BeanFactory beanFactory) {
		final Decision value = (Decision) object;
		return (value != null && Decision.Decided.equals(value));
	}

}
