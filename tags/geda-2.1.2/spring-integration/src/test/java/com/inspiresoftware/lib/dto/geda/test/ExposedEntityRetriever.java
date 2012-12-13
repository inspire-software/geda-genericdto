/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.DTOSupportAwareAdapter;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 9:47:23 AM
 */
public interface ExposedEntityRetriever extends EntityRetriever, DTOSupportAwareAdapter {

    /**
     * @return support bean
     */
    DTOSupport getDtoSupport();

}
