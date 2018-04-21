/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda;

import com.inspiresoftware.lib.dto.geda.config.GeDAInfrastructure;

/**
 * Acts as a visitor for dto support instance in order to get its reference
 * for value converters that need it.
 *
 * User: denispavlov
 * Date: Feb 20, 2012
 * Time: 10:21:52 PM
 */
public interface DTOAdaptersRegistrar extends GeDAInfrastructure {

    /**
     * Enhance runtime DTO converters runtime model. This method is intended to be used in
     * service bean constructors that need to provide additional implementation specific
     * conversions (such as retrievers for example).
     *
     * @param dtoSupport dto support
     */
    void registerAdapters(DTOSupport dtoSupport);

}
