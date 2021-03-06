/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.geda;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;

/**
 * .
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 9:38:38 AM
 */
public class AddressMatcher implements DtoToEntityMatcher<AddressDTO, Address>{

    public boolean match(final AddressDTO addressDTO, final Address address) {
        return addressDTO.getPostCode().equals(address.getPostCode());
    }
}
