/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 29, 2012
 * Time: 2:30:29 PM
 */
public interface DTOValueConverterRegister {

    /**
     * Enhance runtime DTO converters runtime model. This method is intended to be used in
     * service bean constructors that need to provide additional implementation specific
     * conversions (such as retrievers for example).
     *
     * @param key converter key (as provided in DTO mapping annotations)
     * @param converter either converter of a retriever.
     */
    void registerValueConverter(final String key, final Object converter);
    
}
