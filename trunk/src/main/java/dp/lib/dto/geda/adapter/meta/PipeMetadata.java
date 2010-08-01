
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.adapter.meta;

import dp.lib.dto.geda.adapter.BeanFactory;

/**
 * Basic Meta data for pipes.
 * 
 * @author DPavlov
 */
public interface PipeMetadata {

	/**
	 * @return dtoFieldName key for accessing field on DTO object
	 */
	String getDtoFieldName();

	/**
	 * @return entityFieldName key for accessing field on Entity bean
	 */
	String getEntityFieldName();

	/**
	 * @return read only meta
	 */
	boolean isReadOnly();

	/**
	 * @return dtoBeanKey key for constructing DTO bean
	 */
	String getDtoBeanKey();

	/**
	 * @return entityBeanKey key for constructing Entity bean
	 */
	String getEntityBeanKey();

	/**
	 * @param factory dto bean factory
	 * @return new instance of DTO bean
	 * @throws IllegalArgumentException if bean cannot be create using factory provided
	 */
	Object newDtoBean(BeanFactory factory) throws IllegalArgumentException;

	/**
	 * @param factory entity bean factory
	 * @return new instance of Entity bean
	 * @throws IllegalArgumentException if bean cannot be create using factory provided
	 */
	Object newEntityBean(BeanFactory factory) throws IllegalArgumentException;
	
}
