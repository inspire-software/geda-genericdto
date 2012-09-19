
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.adapter;

/**
 * Bean Factory for generating Domain Entity bean instances.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface BeanFactory {

    /**
     * Return a class or interface that is applicable for given bean key.
     *
     * NOTE: this is not a convenience method for #get(entityBeanKey).getClass()
     *       this method should return a class for DTO instances and an interface
     *       for entity key whenever possible to keep IoC at maximum and make
     *       assemblers as generic as possible.
     *
     * @param entityBeanKey string key reference to the bean class or interface required
     * @return class or interface that best represent bean objects by given key
     *
     * @since 2.0.4
     */
    Class getClazz(String entityBeanKey);

	/**
     * Returns object instance for given class. Default assumption is
     * that if no mapping is present for this key the factory will return null,
     * which will cause Assembler to throw GeDAException.
     *
     * However it is up to implementor of this interface if an exception is to be thrown
     * in such cases.
     *
	 * @param entityBeanKey string key reference to the bean required
	 * @return new domain entity instance
	 */
	Object get(String entityBeanKey);
	
}
