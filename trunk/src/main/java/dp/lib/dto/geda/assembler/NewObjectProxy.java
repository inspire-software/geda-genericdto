
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Proxy object for creating new beans that will create bean only id the data for it exists.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class NewObjectProxy {

	private final BeanFactory beanFactory;
	private final String newBeanKey;
	private final Object parentObject;
	private final Method objectWrite;

    /**
	 * Constructor for proxy object that creates an entity only when it is needed.
	 * 
	 * @param beanFactory bean factory for creating new entities
	 * @param newBeanKey the key used for this entity bean class instance
	 * @param parentObject the parent of this bean (can be {@link NewObjectProxy} to allow deep nesting)
	 * @param objectWrite the method that allows to set new bean to parrent object.
	 * @throws IllegalArgumentException if bean factory is not specified.
	 */
	public NewObjectProxy(final BeanFactory beanFactory,
			final String newBeanKey,
			final Object parentObject,
			final Method objectWrite) throws IllegalArgumentException {
		if (beanFactory == null) {
			throw new IllegalArgumentException("BeanFactory must be specified in order to create bean with key: "
					+ newBeanKey);
		}
		this.beanFactory = beanFactory;
		this.newBeanKey = newBeanKey;
		this.parentObject = parentObject;
		this.objectWrite = objectWrite;
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
		if (this.parentObject instanceof NewObjectProxy) {
			final NewObjectProxy parentProxy = (NewObjectProxy) this.parentObject;
			parentObject = parentProxy.create();
		} else {
			parentObject = this.parentObject;
		}
		
		final Object newObject = this.beanFactory.get(this.newBeanKey);
		if (newObject == null) {
			throw new IllegalArgumentException("Unable to construct bean with key: " 
					+ this.newBeanKey + " using beanFactory: " + this.beanFactory);
		}
		this.objectWrite.invoke(parentObject, newObject);
		return newObject;
	}
	
}
