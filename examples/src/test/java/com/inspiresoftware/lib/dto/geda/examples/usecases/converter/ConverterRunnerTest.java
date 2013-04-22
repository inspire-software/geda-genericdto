/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.converter;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 7:03 PM
 */
public class ConverterRunnerTest {

    @Test
    public void testWithConversion() throws Exception {

        new RunWithConverter().withConversion();

    }
}
