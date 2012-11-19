/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.orika;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonWithHistoryDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.support.AbstractMapperTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 12:07:57 PM
 */
public class OrikaListMapperTest extends AbstractMapperTest {

    @Test
    public void testMapper() throws Exception {

        final Person entity = getEntityWithHistory();
        final PersonDTO dto = getDtoWithHistory();

        final Mapper mapper = new OrikaListMapper();


        final Person fromDto = (Person) mapper.fromDto(dto);
        assertEquals(entity, fromDto);
        assertNotNull(fromDto.getPreviousAddresses());
        assertEquals(2, fromDto.getPreviousAddresses().size());
        assertEquals(entity.getPreviousAddresses().get(0), fromDto.getPreviousAddresses().get(0));

        final PersonWithHistoryDTO fromEntity = (PersonWithHistoryDTO) mapper.fromEntity(entity);
        assertEquals(dto, fromEntity);
        assertNotNull(fromEntity.getPreviousAddresses());
        assertEquals(2, fromEntity.getPreviousAddresses().size());
        assertEquals(((PersonWithHistoryDTO) dto).getPreviousAddresses().get(0), fromEntity.getPreviousAddresses().get(0));

    }

}
