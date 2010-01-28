/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import org.junit.Ignore;

/**
 * Test bean factory.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestBeanFactory implements BeanFactory {

	/** {@inheritDoc} */
	public Object get(final String entityBeanKey) {
		if ("wrapper.wrapper.key".equals(entityBeanKey)) {
			return new TestEntity4SubClass();
		} else if ("wrapper.key".equals(entityBeanKey)) {
			return new TestEntity4Class();
		}
		return null;
	}

}
