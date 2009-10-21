/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.assembler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dp.lib.dto.geda.adapter.BeanFactory;

/**
 * Proxy object for creating new beans that will create bean only id the data for it exists.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class NewEntityProxy {

	private final BeanFactory beanFactory;
	private final String newEntityBeanKey;
	private final Object parentEntity;
	private final Method entityWrite;
	
	/**
	 * Constructor for proxy object that creates an entity only when it is needed.
	 * 
	 * @param beanFactory bean factory for creating new entities
	 * @param newEntityBeanKey the key used for this entity bean class instance
	 * @param parentEntity the parent of this bean (can be {@link NewEntityProxy} to allow deep nesting)
	 * @param entityWrite the method that allows to set new bean to parrent object.
	 * @throws IllegalArgumentException if bean factory is not specified.
	 */
	public NewEntityProxy(final BeanFactory beanFactory,
			final String newEntityBeanKey,
			final Object parentEntity,
			final Method entityWrite) throws IllegalArgumentException {
		if (beanFactory == null) {
			throw new IllegalArgumentException("BeanFactory must be specified in order to create bean with key: "
					+ newEntityBeanKey);
		}
		this.beanFactory = beanFactory;
		this.newEntityBeanKey = newEntityBeanKey;
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
		
		final Object parentObject;
		if (parentEntity instanceof NewEntityProxy) {
			final NewEntityProxy parentProxy = (NewEntityProxy) parentEntity;
			parentObject = parentProxy.create();
		} else {
			parentObject = parentEntity;
		}
		
		final Object newEntity = this.beanFactory.get(this.newEntityBeanKey);
		if (newEntity == null) {
			throw new IllegalArgumentException("Unable to construct bean with key: " 
					+ this.newEntityBeanKey + " using beanFactory: " + this.beanFactory);
		}
		this.entityWrite.invoke(parentObject, newEntity);
		return newEntity;
	}
	
}
