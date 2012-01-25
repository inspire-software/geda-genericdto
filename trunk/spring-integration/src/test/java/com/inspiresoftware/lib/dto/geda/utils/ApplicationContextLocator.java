/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.utils;

import org.springframework.context.ApplicationContext;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:28:50 PM
 */
public class ApplicationContextLocator {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(final ApplicationContext context) {
       APPLICATION_CONTEXT = context;
    }

    /**
     * @return current spring context singleton
     */
    public static final ApplicationContext getInstance() {
        return APPLICATION_CONTEXT;
    }

}
