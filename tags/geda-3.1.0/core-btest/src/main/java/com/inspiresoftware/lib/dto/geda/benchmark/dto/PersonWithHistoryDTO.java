/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.dto;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.AddressMatcher;

import java.util.List;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:15:42 AM
 */
@Dto
public class PersonWithHistoryDTO extends PersonDTO {

    @DtoCollection(value = "previousAddresses",
                   dtoBeanKey = "addressDto",
                   entityBeanKeys = "addressEntity",
                   entityGenericType = Address.class,
                   dtoToEntityMatcher = AddressMatcher.class)
    private List<AddressDTO> previousAddresses;


    public List<AddressDTO> getPreviousAddresses() {
        return previousAddresses;
    }

    public void setPreviousAddresses(final List<AddressDTO> previousAddresses) {
        this.previousAddresses = previousAddresses;
    }
    

}
