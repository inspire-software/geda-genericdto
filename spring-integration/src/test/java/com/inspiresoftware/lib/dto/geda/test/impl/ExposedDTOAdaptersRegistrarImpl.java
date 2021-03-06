/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.impl.MappingAdapterRegistrar;
import com.inspiresoftware.lib.dto.geda.test.ExposedDTOAdaptersRegistrar;

import java.util.Map;

/**
 * .
 *
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 9:34:06 AM
 */
public class ExposedDTOAdaptersRegistrarImpl extends MappingAdapterRegistrar implements ExposedDTOAdaptersRegistrar {

    public ExposedDTOAdaptersRegistrarImpl(final Map<String, Object> converters) {
        super(converters);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAdapters() {
        return super.getAdapters();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
