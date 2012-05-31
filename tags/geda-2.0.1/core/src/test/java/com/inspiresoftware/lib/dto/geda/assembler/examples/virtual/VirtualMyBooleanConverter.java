
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.virtual;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import org.junit.Ignore;


/**
 * 
 * @author denispavlov
 *
 */
@Ignore
public class VirtualMyBooleanConverter implements ValueConverter {

	/** {@inheritDoc} */
	public Object convertToDto(final Object object, final BeanFactory beanFactory) {
		final TestEntity20Class entity = (TestEntity20Class) object;
		return entity.whatWasComplexDecision();
	}

	/** {@inheritDoc} */
	public Object convertToEntity(final Object object, final Object oldEntity,
			final BeanFactory beanFactory) {
		final TestEntity20Class entity = (TestEntity20Class) oldEntity;
		entity.makeComplexDecision((Boolean) object);
		return entity;
	}

}
