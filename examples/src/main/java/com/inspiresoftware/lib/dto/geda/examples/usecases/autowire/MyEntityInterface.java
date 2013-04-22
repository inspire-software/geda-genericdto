
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.autowire;

/**
 * Test interface for entity.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface MyEntityInterface {

	/**
	 * @return a long property.
	 */
	Long getEntityId();

	/**
	 * @param entityId a long property.
	 */
	void setEntityId(final Long entityId);

	/**
	 * @return  a string property.
	 */
	String getName();

	/**
	 * @param name a string property.
	 */
	void setName(final String name);

	/**
	 * @return a double property.
	 */
	Double getNumber();

	/**
	 * @param number a double property.
	 */
	void setNumber(final Double number);

}