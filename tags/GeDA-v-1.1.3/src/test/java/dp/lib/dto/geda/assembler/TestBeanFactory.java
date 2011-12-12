
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.assembler.examples.nested.TestEntity4Class;
import dp.lib.dto.geda.assembler.examples.nested.TestEntity4SubClass;

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
