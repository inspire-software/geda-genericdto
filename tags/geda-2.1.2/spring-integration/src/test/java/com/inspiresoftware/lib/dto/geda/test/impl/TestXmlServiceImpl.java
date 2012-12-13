/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.test.TestXmlService;

/**
 * User: denispavlov
 * Date: 12-11-14
 * Time: 5:21 PM
 */
public class TestXmlServiceImpl implements TestXmlService {

    private DTOSupport dtoSupport;

    public <T> T transfer(final Object from) {
        return dtoSupport.assembleDtoByKey("annDtoKey", from, "manual");
    }

    public void setDtoSupportBean(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }

    public void setDtoSupport(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }

}
