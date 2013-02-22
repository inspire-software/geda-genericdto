/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi.test;

import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;

/**
 * User: denispavlov
 * Date: 13-02-20
 * Time: 12:37 PM
 */
public class ExpectationsDTOEventListener implements DTOEventListener {

    private final Object[] expectations;

    public ExpectationsDTOEventListener(final Object[] expectations) {
        this.expectations = expectations;
    }

    public void onEvent(final Object... context) {
        if (context == null || context.length != expectations.length) {
            throw new AssertionError("Unexpected event context");
        }
        for (int index = 0; index < context.length; index++) {
            if (context[index] instanceof Comparable) {
                if (((Comparable) context[index]).compareTo(expectations[index]) != 0) {
                    throw new AssertionError("Unexpected event context at index " + index);
                }
            } else if (!context[index].equals(expectations[index])) {
                throw new AssertionError("Unexpected event context at index " + index);
            }
        }
    }
}
