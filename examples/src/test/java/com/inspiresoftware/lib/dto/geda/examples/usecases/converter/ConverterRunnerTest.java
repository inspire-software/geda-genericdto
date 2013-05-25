/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
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
