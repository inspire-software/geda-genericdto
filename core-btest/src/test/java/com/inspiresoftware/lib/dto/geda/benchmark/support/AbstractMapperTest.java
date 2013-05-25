/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support;

import com.inspiresoftware.lib.dto.geda.benchmark.data.DataProvider;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:56:52 AM
 */
public abstract class AbstractMapperTest {


    public PersonDTO getDto() {
        return DataProvider.providePersonDTO(false, false);
    }

    public Person getEntity() {
        return DataProvider.providePersonEntity(false);
    }

    public PersonDTO getDtoWithHistory() {
        return DataProvider.providePersonDTO(true, false);
    }

    public PersonDTO getDtoWithHistoryByCity() {
        return DataProvider.providePersonDTO(true, true);
    }

    public Person getEntityWithHistory() {
        return DataProvider.providePersonEntity(true);
    }

}
