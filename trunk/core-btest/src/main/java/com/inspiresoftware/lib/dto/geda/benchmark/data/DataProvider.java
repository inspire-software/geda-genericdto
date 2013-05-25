/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.data;

import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Country;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Name;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonWithHistoryByCityDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonWithHistoryDTO;

import java.util.*;

/**
 * User: denispavlov
 * Date: 12-09-19
 * Time: 10:38 AM
 */
public final class DataProvider {

    public static Person providePersonEntity(final boolean withHistory) {

        final Name name = new Name("Sherlock", "Holmes");
        final Country country = new Country("United Kingdom");
        final Address home = new Address("221B Baker Street", null, "London", country, "NW1 6XE");
        final Person entity = new Person(123456789012L, name, home);

        if (withHistory) {
            final Address baskervilleHall = new Address("Baskerville Hall", null, "Hay-on-Wye", country, "HR3 5LE");
            entity.setPreviousAddresses(new ArrayList<Address>(Arrays.asList(home, baskervilleHall)));
        }

        return entity;

    }

    public static PersonDTO providePersonDTO(final boolean withHistory, final boolean historyByCity) {

        final PersonDTO dto;
        if (withHistory) {
            if (historyByCity) {
                dto = new PersonWithHistoryByCityDTO();
            } else {
                dto = new PersonWithHistoryDTO();
            }
        } else {
            dto = new PersonDTO();
        }

        dto.setId(1234567890123L);
        dto.setFirstName("Sherlock");
        dto.setLastName("Holmes");

        final AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressLine1("221B Baker Street");
        addressDTO.setCity("London");
        addressDTO.setPostCode("NW1 6XE");
        addressDTO.setCountryName("United Kingdom");
        dto.setCurrentAddress(addressDTO);

        if (withHistory) {
            final AddressDTO addressPrevDTO = new AddressDTO();
            addressPrevDTO.setAddressLine1("Baskerville Hall");
            addressPrevDTO.setCity("Hay-on-Wye");
            addressPrevDTO.setPostCode("HR3 5LE");
            addressPrevDTO.setCountryName("United Kingdom");

            if (historyByCity) {
                final Map<String, AddressDTO> prev = new HashMap<String, AddressDTO>();
                prev.put("NW1 6XE", addressDTO);
                prev.put("HR3 5LE", addressPrevDTO);
                ((PersonWithHistoryByCityDTO) dto).setPreviousAddresses(prev);
            } else {
                final List<AddressDTO> prev = new ArrayList<AddressDTO>();
                prev.add(addressDTO);
                prev.add(addressPrevDTO);
                ((PersonWithHistoryDTO) dto).setPreviousAddresses(prev);
            }
        }
        return dto;
    }

}
