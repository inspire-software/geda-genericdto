/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.manual;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Country;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Name;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;

/**
 * .
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:29:18 AM
 */
public class ManualBasicMapper implements Mapper {

    public Object fromEntity(final Object entity) {

        final Person person = (Person) entity;
        final PersonDTO dto = new PersonDTO();

        dto.setId(person.getId());
        if (person.getName() != null) {
            dto.setFirstName(person.getName().getFirstname());
            dto.setLastName(person.getName().getSurname());
        }
        if (person.getCurrentAddress() != null) {
            final Address address = person.getCurrentAddress();
            final AddressDTO addressDTO = fromAddress(address);
            dto.setCurrentAddress(addressDTO);
        }
        return dto;
    }

    private AddressDTO fromAddress(final Address address) {
        final AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressLine1(address.getAddressLine1());
        addressDTO.setAddressLine2(address.getAddressLine2());
        addressDTO.setCity(address.getCity());
        addressDTO.setPostCode(address.getPostCode());
        if (address.getCountry() != null) {
            addressDTO.setCountryName(address.getCountry().getName());
        }
        return addressDTO;
    }

    public Object fromDto(final Object dto) {

        final Person person = new Person();
        final PersonDTO personDTO = (PersonDTO) dto;

        person.setId(personDTO.getId());
        person.setName(new Name(personDTO.getFirstName(), personDTO.getLastName()));

        if (personDTO.getCurrentAddress() != null) {
            final AddressDTO addressDTO = personDTO.getCurrentAddress();
            final Address address = fromAddressDTO(addressDTO);

            person.setCurrentAddress(address);
        }
        return person;
    }

    private Address fromAddressDTO(final AddressDTO addressDTO) {
        final Address address = new Address();

        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setPostCode(addressDTO.getPostCode());
        address.setCountry(new Country(addressDTO.getCountryName()));
        return address;
    }
}
