/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.dsl;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 8:30 AM
 */
public class DSLRunnersTest {

    @Test
    public void testByReference() throws Exception {

        new RunDSLByReference().byReference();

    }

    @Test
    public void testByInterface() throws Exception {

        new RunDSLByInterface().byInterface();

    }

    @Test
    public void testByClassWithAlias() throws Exception {

        new RunDSLByClassWithAlias().byClassWithAlias();

    }

    @Test
    public void testGeneric() throws Exception {

        new RunDSLGeneric().generic();

    }


    @Test
    public void testReuseMapping() throws Exception {

       new RunDSLReuseMapping().reuseMapping();

    }

    @Test
    public void testWithFieldsAuto() throws Exception {

        new RunDSLWithSameFields().withSameFieldsByKey();

    }

    @Test
    public void testWithFieldsAutoAndExclusions() throws Exception {

        new RunDSLWithSameFields().withSameFieldsByKeyExcluding();

    }
}
