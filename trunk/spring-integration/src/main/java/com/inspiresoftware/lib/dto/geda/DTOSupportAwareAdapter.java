/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

/**
 * Marker interface used by {@link com.inspiresoftware.lib.dto.geda.impl.MappingAdapterRegistrar} to
 * inject dto support bean to adapters that require it.
 * <p/>
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 8:44:18 AM
 */
public interface DTOSupportAwareAdapter {

    /**
     * @param dtoSupport dto support instance
     */
    void setDtoSupport(DTOSupport dtoSupport);

}
