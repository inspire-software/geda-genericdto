/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.assembler.TestDto3Class.Decision;
import org.junit.Ignore;

/**
 * Test converter.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
class TestConverter3 implements ValueConverter {

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
