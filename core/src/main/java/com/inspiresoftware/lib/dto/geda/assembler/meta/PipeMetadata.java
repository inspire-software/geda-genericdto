
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.meta;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException;

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
	 * @throws BeanFactoryNotFoundException if no factory is specified for creating bean instance
	 * @throws BeanFactoryUnableToCreateInstanceException if bean cannot be create using factory provided
	 */
	Object newDtoBean(BeanFactory factory) 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException;

	/**
	 * @param factory entity bean factory
	 * @return new instance of Entity bean
	 * @throws BeanFactoryNotFoundException if no factory is specified for creating bean instance
	 * @throws BeanFactoryUnableToCreateInstanceException if bean cannot be create using factory provided
	 */
	Object newEntityBean(BeanFactory factory) 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException;
	
}
