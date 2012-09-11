/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

import com.inspiresoftware.lib.dto.geda.performance.MemUtils;
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

        int available = MemUtils.availableMb();

        MemUtils.statsToStream(System.out);

        if (available < 1000) {
            System.out.println("[WARN]: Need more memory for this test...");
            System.out.println("[WARN]: use - export MAVEN_OPTS=\"-Xms512M -Xmx1024M -XX:MaxPermSize=128M\" \n\n");
        }
    }

}
