/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 10:39 PM
 */
public class SimpleRunnersTest {

    @Test
    public void testInheritance() throws Exception {

        new RunSimpleInheritance().inheritanceMapping();

    }

    @Test
    public void testChain() throws Exception {

        new RunSimpleChain().deepWrappedProperty();

    }

    @Test
    public void testReadOnly() throws Exception {

        new RunSimpleReadOnly().readOnlyMapping();

    }
}
