/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOAdaptersRegistrar;

import java.util.Map;

/**
 * Helps test the binding mechanism.
 *
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
