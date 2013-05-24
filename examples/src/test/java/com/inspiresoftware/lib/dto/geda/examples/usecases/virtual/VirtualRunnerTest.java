/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.virtual;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 11:02 PM
 */
public class VirtualRunnerTest {

    @Test
    public void testVirtual() throws Exception {

        new RunVirtual().virtualMapping();

    }
}