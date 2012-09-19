/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.benchmark.dto;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.AddressMatcher;

import java.util.Map;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:15:42 AM
 */
@Dto
public class PersonWithHistoryByCityDTO extends PersonDTO {

    @DtoMap(value = "previousAddresses",
            dtoBeanKey = "addressDto",
            entityBeanKeys = "addressEntity",
            entityCollectionMapKey = "city",
            entityGenericType = Address.class,
            dtoToEntityMatcher = AddressMatcher.class)
    private Map<String, AddressDTO> previousAddresses;


    public Map<String, AddressDTO> getPreviousAddresses() {
        return previousAddresses;
    }

    public void setPreviousAddresses(final Map<String, AddressDTO> previousAddresses) {
        this.previousAddresses = previousAddresses;
    }
    

}
