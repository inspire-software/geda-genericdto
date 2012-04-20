/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

import org.junit.Test;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Feb 18, 2012
 * Time: 7:31:08 PM
 */
public class MemoryCheckTest {

    @Test
    public void testSufficientMemory() {
        final Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory() / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024;
        long freeMemory = runtime.freeMemory()  / 1024;

        long available = freeMemory + (maxMemory - allocatedMemory);

        System.out.println("free memory: " + freeMemory);
        System.out.println("allocated memory: " + allocatedMemory);
        System.out.println("max memory: " + maxMemory);
        System.out.println("total free memory: " + available);

        if (available < 1024) {
            System.out.println("Need more memory for this test...");
        }
    }

}
