/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 31, 2012
 * Time: 5:13:36 PM
 */
public interface DTOCountingEventListener extends DTOEventListener {

    /**
     * @return count event calls
     */
    public int getCount();

    /**
     * reser the counter.
     */
    public void reset();

}
