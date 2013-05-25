/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics.extend;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 13-04-23
 * Time: 3:06 PM
 */
public class GenericsExtendRunnerTest {

    @Test
    public void testGenericsMapping() throws Exception {

        new RunWithGenericsExtend().genericsExtendMapping();

    }


}
