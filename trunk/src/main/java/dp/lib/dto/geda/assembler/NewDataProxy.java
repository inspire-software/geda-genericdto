
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

import java.lang.reflect.InvocationTargetException;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.meta.PipeMetadata;

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
	private final Object parentEntity;
	private final DataWriter entityWrite;
	
	/**
	 * Constructor for proxy object that creates an entity only when it is needed.
	 * 
	 * @param beanFactory bean factory for creating new entities
	 * @param meta meta data for field
	 * @param dto true if this is proxy for dto and false if this is proxy for entity
	 * @param parentEntity the parent of this bean (can be {@link NewDataProxy} to allow deep nesting)
	 * @param entityWrite the method that allows to set new bean to parrent object.
	 * @throws IllegalArgumentException if bean factory is not specified.
	 */
	public NewDataProxy(final BeanFactory beanFactory,
			final PipeMetadata meta,
			final boolean dto,
			final Object parentEntity,
			final DataWriter entityWrite) throws IllegalArgumentException {

		this.beanFactory = beanFactory;
		this.meta = meta;
		this.dto = dto;
		this.parentEntity = parentEntity;
		this.entityWrite = entityWrite;
	}
	
	/**
	 * @return newly created entity bean for working with deeply nested objects (sets itself 
	 *         to parent object automatically).
	 * @throws IllegalArgumentException {@link java.lang.reflect.Method}
	 * @throws IllegalAccessException {@link java.lang.reflect.Method}
	 * @throws InvocationTargetException {@link java.lang.reflect.Method}
	 */
	public Object create() 
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (beanFactory == null) {
			if (dto) {
				throw new IllegalArgumentException("BeanFactory must be specified in order to create bean with key: "
						+ meta.getDtoBeanKey() + " for DTO field: " + meta.getDtoFieldName());
			} else {
				throw new IllegalArgumentException("BeanFactory must be specified in order to create bean with key: "
						+ meta.getEntityBeanKey() + " for Entity field: " + meta.getEntityFieldName());
			}
		}
		
		final Object parentObject;
		if (parentEntity instanceof NewDataProxy) {
			final NewDataProxy parentProxy = (NewDataProxy) parentEntity;
			parentObject = parentProxy.create();
		} else {
			parentObject = parentEntity;
		}
		
		final Object newObject;
		if (dto) {
			newObject = this.meta.newDtoBean(this.beanFactory);
		} else {
			newObject = this.meta.newEntityBean(this.beanFactory);
		}

		this.entityWrite.write(parentObject, newObject);
		return newObject;
	}
	
}
