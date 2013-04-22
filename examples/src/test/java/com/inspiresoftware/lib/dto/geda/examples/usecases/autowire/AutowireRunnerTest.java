/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.autowire;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 6:44 PM
 */
public class AutowireRunnerTest {

    @Test
    public void testAutoBinding() throws Exception {

        new RunAutowire().autoBinding();

    }
}
