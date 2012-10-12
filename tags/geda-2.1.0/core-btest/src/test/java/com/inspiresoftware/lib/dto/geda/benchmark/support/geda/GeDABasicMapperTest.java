/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.geda;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.support.AbstractMapperTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Ignored tests are just loop versions of regular tests so that we have some
 * time to exhibit what is going on using profiling tools.
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:53:37 AM
 */
public class GeDABasicMapperTest extends AbstractMapperTest {

    @Test
    public void testMapper() throws Exception {

        final Person entity = getEntity();
        final PersonDTO dto = getDto();

        final Mapper mapper = new GeDABasicMapper();

        assertEquals(entity, mapper.fromDto(dto));
        assertEquals(dto, mapper.fromEntity(entity));

    }

    private static final int PERFORMANCE_TEST_CYCLES = 1000000;

    @Test
    @Ignore
    public void testMapperPerformance() throws Exception {

        for (int i = 0; i < PERFORMANCE_TEST_CYCLES; i++) {
            testMapper();
        }
    }

}
