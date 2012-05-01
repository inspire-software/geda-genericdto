/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl;
import com.inspiresoftware.lib.dto.geda.test.impl.ExtendedDataTransferObjectImpl;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:30:53 PM
 */
public abstract class IntegrationRegExTest extends IntegrationTest {

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityBeforeExactNoMatch() {

        final TestNoMatchService service =
                this.applicationContext.getBean("simpleTransferableNoMatchService", TestNoMatchService.class);

        assertCountersAreZero();

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityBeforeExact(dto, entity);

        assertNull(entity.getValue());
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(0, 0, 0, 0, 0, 0);

    }

}