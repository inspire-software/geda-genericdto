

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException;


/**
 * Abstract pipe meta contains information on the metadata of the pipe.
 * 
 * @author DPavlov
 */
class BasePipeMetadata implements PipeMetadata {

	private final String dtoFieldName;
	private final String entityFieldName;
    private final boolean readOnly;
    private final String dtoBeanKey;
    private final String entityBeanKey;
    
    /**
     * @param dtoFieldName key for accessing field on DTO object
     * @param entityFieldName key for accessing field on Entity bean
     * @param dtoBeanKey key for constructing DTO bean
     * @param entityBeanKey key for constructing Entity bean
     * @param readOnly read only marker (true then write to entity is omitted)
     */
	public BasePipeMetadata(final String dtoFieldName,
								final String entityFieldName,
								final String dtoBeanKey, 
								final String entityBeanKey, 
								final boolean readOnly) {
		this.dtoFieldName = dtoFieldName;
		this.entityFieldName = entityFieldName;
		this.dtoBeanKey = dtoBeanKey;
		this.entityBeanKey = entityBeanKey;
		this.readOnly = readOnly;
	}

	/** {@inheritDoc} */
	public String getDtoFieldName() {
		return dtoFieldName;
	}

	/** {@inheritDoc} */
	public String getEntityFieldName() {
		return entityFieldName;
	}

	/** {@inheritDoc} */
	public boolean isReadOnly() {
		return readOnly;
	}

	/** {@inheritDoc} */
	public String getDtoBeanKey() {
		return dtoBeanKey;
	}
	
	/** {@inheritDoc} */
	public Object newDtoBean(final BeanFactory factory) 
			throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException {
		return newBean(this.getDtoBeanKey(), factory, true);
	}

	/** {@inheritDoc} */
	public String getEntityBeanKey() {
		return entityBeanKey;
	}
	
	/** {@inheritDoc} */
	public Object newEntityBean(final BeanFactory factory) 
			throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException {
		return newBean(this.getEntityBeanKey(), factory, false);
	}
	
	private Object newBean(final String beanKey, final BeanFactory factory, final boolean isDto) 
			throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException {
		if (factory == null) {
			if (isDto) {
				throw new BeanFactoryNotFoundException(
						dtoFieldName, dtoBeanKey, true);
			} else {
				throw new BeanFactoryNotFoundException(
					entityFieldName, entityBeanKey, false);
			}
		}
		final Object newObject = factory.get(beanKey);
		if (newObject == null) {
			if (isDto) {
				throw new BeanFactoryUnableToCreateInstanceException(factory.toString(), 
						dtoFieldName, dtoBeanKey, true);
			} else {
				throw new BeanFactoryUnableToCreateInstanceException(factory.toString(), 
						entityFieldName, entityBeanKey, false);
			}
		}
		return newObject;
	}
			
}
