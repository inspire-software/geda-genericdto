
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */


package com.inspiresoftware.lib.dto.geda.adapter;

/**
 * Adapter for data to resolve incompatibilities between DTO and Entities.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface ValueConverter extends Adapter {

	/**
	 * Convert one object into another to resolve incompatibilities between DTO and Entities.
	 *
     * @param object the object to convert
     * @param beanFactory DTO bean factory
     *
	 * @return converted object.
	 */
	Object convertToDto(final Object object, final BeanFactory beanFactory);

	/**
	 * Convert one object into another to resolve incompatibilities between DTO and Entities.
	 *
     * @param object the object to convert
     * @param oldEntity the old value that was in the entity
     * @param beanFactory entity bean factory
     *
	 * @return converted object.
	 */
	Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory);
	
}
