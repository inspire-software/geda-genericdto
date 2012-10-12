
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
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.BeanFactoryUnableToCreateInstanceException;

/**
 * Proxy object for creating new beans that will create bean only id the data for it exists.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class NewDataProxy {

	private final BeanFactory beanFactory;
	private final PipeMetadata meta;
	private final boolean dto;
	private final Object parentDataObject;
	private final DataWriter parentDataWriter;
	
	/**
	 * Constructor for proxy object that creates an entity only when it is needed.
	 * 
	 * @param beanFactory bean factory for creating new entities
	 * @param meta meta data for field
	 * @param dto true if this is proxy for dto and false if this is proxy for entity
	 * @param parentDataObject the parent of this bean (can be {@link NewDataProxy} to allow deep nesting)
	 * @param parentDataWriter the method that allows to set new bean to parent object.
	 */
	public NewDataProxy(final BeanFactory beanFactory,
			final PipeMetadata meta,
			final boolean dto,
			final Object parentDataObject,
			final DataWriter parentDataWriter) {

		this.beanFactory = beanFactory;
		this.meta = meta;
		this.dto = dto;
		this.parentDataObject = parentDataObject;
		this.parentDataWriter = parentDataWriter;
	}

    /**
     * @return newly created entity bean for working with deeply nested objects (sets itself
     *         to parent object automatically).
     * @throws BeanFactoryNotFoundException if bean factory annotation is missing
     * @throws BeanFactoryUnableToCreateInstanceException if bean factory is unable to create dot/entity instance
     */
	public Object create() 
		throws BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException {
		
		if (beanFactory == null) {
			if (dto) {
				throw new BeanFactoryNotFoundException(meta.getDtoFieldName(), meta.getDtoBeanKey(), true);
			} else {
				throw new BeanFactoryNotFoundException(meta.getEntityFieldName(), meta.getEntityBeanKey(), false);
			}
		}
		
		final Object parentObject;
		if (parentDataObject instanceof NewDataProxy) {
			parentObject = ((NewDataProxy) parentDataObject).create();
		} else {
			parentObject = parentDataObject;
		}
		
		final Object newObject;
		if (dto) {
			newObject = this.meta.newDtoBean(this.beanFactory);
		} else {
			newObject = this.meta.newEntityBean(this.beanFactory);
		}

		this.parentDataWriter.write(parentObject, newObject);
		return newObject;
	}
	
}
