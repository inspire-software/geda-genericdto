/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 2:49:11 PM
 */
public interface DTOFactory extends BeanFactory, GeDAInfrastructure {

    /**
     * Allows to enrich bean factory with new bean mappings
     *
     * @param key string key for this class (interface name preferred)
     * @param className fully qualified string representation of java class.
     *        No check is made regarding the validity of this class and if it is invalid
     *        will cause exception during #get()
     *
     * @throws IllegalArgumentException if either parameters are null or empty, or if
     *         this key is already used.
     *
     * @deprecated use #registerDto or #registerEntity instead.
     */
    @Deprecated
    void register(final String key, final String className) throws IllegalArgumentException;

    /**
     * Allows to enrich bean factory with new bean mappings
     *
     * @param key string key for this class (interface name preferred)
     * @param className fully qualified string representation of java class.
     *        No check is made regarding the validity of this class and if it is invalid
     *        will cause exception during #get()
     *
     * @throws IllegalArgumentException if either parameters are null or empty, or if
     *         this key is already used.
     *
     * @since 2.0.4
     */
    void registerDto(final String key, final String className) throws IllegalArgumentException;


    /**
     * Allows to enrich bean factory with new bean mappings
     *
     * @param key string key for this class (interface name preferred)
     * @param className fully qualified string representation of java class.
     *        No check is made regarding the validity of this class and if it is invalid
     *        will cause exception during #get()
     * @param representative fully qualified string representation of a java interface
     *        that best describes #className class.
     *
     * @throws IllegalArgumentException if either parameters are null or empty, or if
     *         this key is already used.
     *
     * @since 2.0.4
     */
    void registerEntity(final String key, final String className, final String representative) throws IllegalArgumentException;

}
