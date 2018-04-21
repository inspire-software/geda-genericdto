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
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonWithHistoryDTO;
import org.dozer.DozerBeanMapper;

import java.util.Arrays;

/**
 * .
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 11:24:11 AM
 */
public class DozerListMapper implements Mapper {

    private DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozer-list-mapping.xml"));

    public Object fromEntity(final Object entity) {
        return mapper.map(entity, PersonWithHistoryDTO.class);
    }

    public Object fromDto(final Object dto) {
        return mapper.map(dto, Person.class);
    }
}
