/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOAdaptersRegistrar;

import java.util.Map;

/**
 * Helps test the binding mechanism.
 * <p/>
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 8:57:14 AM
 */
public interface ExposedDTOAdaptersRegistrar extends DTOAdaptersRegistrar {

    /**
     * @return all converters.
     */
    Map<String, Object> getAdapters();

}
