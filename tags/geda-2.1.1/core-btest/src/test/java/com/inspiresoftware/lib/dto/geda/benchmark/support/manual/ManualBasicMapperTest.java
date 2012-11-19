/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.manual;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.support.AbstractMapperTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:56:27 AM
 */
public class ManualBasicMapperTest extends AbstractMapperTest {

    @Test
    public void testMapper() throws Exception {

        final Person entity = getEntity();
        final PersonDTO dto = getDto();

        final Mapper mapper = new ManualBasicMapper();

        assertEquals(entity, mapper.fromDto(dto));
        assertEquals(dto, mapper.fromEntity(entity));


    }
}
