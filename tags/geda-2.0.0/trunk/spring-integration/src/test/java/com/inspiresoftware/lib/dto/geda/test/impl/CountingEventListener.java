/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.test.DTOCountingEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 5:12:43 PM
 */
public class CountingEventListener implements DTOCountingEventListener, BeanNameAware {

    private static final Logger LOG = LoggerFactory.getLogger(CountingEventListener.class);

    private int count = 0;
    private String name;

    public void onEvent(final Object... context) {
        count++;
        LOG.debug(name + " is called " + count + " times");
    }

    public int getCount() {
        return count;
    }

    public void reset() {
        count = 0;
    }

    public void setBeanName(final String name) {
        this.name = name;
    }
}
