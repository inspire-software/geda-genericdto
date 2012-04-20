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
public interface DTOFactory extends BeanFactory {

    /**
     * Allows to enrich bean factory with new bean mappings
     *
     * @param key string key for this class (interface name prefferred)
     * @param className fully qualified string representation of java class.
     *        No check is made regarding the validity of this class and if it is invalid
     *        will cause exception during #get()
     *
     * @throws IllegalArgumentException if either parameters are null or empty, or if
     *         this key is already used.
     */
    void register(final String key, final String className) throws IllegalArgumentException;

}
