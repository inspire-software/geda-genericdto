/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.dozer;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.support.AbstractMapperTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * .
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 11:27:10 AM
 */
public class DozerBasicMapperTest extends AbstractMapperTest {


    @Test
    public void testMapper() throws Exception {

        final Person entity = getEntity();
        final PersonDTO dto = getDto();

        final Mapper mapper = new DozerBasicMapper();

        assertEquals(entity, mapper.fromDto(dto));
        assertEquals(dto, mapper.fromEntity(entity));

    }

}
