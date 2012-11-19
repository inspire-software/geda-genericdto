/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Address;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.impl.AddressImpl;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.impl.PersonImpl;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.PersonDAO;

import java.util.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 11:57:49 AM
 */
public class PersonDAOImpl implements PersonDAO {

    private final Map<String, List<Person>> people = new HashMap<String, List<Person>>();
    private final Map<Person, Address> addresses = new HashMap<Person, Address>();

    public Person addPerson(final String firstName, final String lastName) {

        final Person person = new PersonImpl();
        person.setFirstName(firstName);
        person.setLastName(lastName);

        if (people.containsKey(firstName)) {
            people.get(firstName).add(person);
        } else {
            people.put(firstName, new ArrayList(Arrays.asList(person)));
        }
        return person;
    }

    public Address addAddress(final Person person, final String street, final String city, final String postCode, final String country) {

        final Address address = new AddressImpl();
        address.setAddressLine1(street);
        address.setCity(city);
        address.setPostCode(postCode);
        address.setCountry(country);

        addresses.put(person, address);

        return address;

    }

    public List<Person> findByFirstName(final String firstName) {
        return people.get(firstName);
    }

    public Address lookUpAddress(final Person person) {
        return addresses.get(person);
    }
}
