/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.addressbook;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.ContactDTO;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.AddressBookService;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.PersonDAO;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl.AddressBookServiceImpl;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl.PersonDAOImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 12:49:58 PM
 */
public class AddressBookExampleRun {

    private PersonDAO personDAO;
    private AddressBookService service;


    @Before
    public void setupPeopleAndAddresses() {

        personDAO = new PersonDAOImpl();
        service = new AddressBookServiceImpl(personDAO);


        final Person irene = personDAO.addPerson("Irene", "Adler");

        final Person mycroft = personDAO.addPerson("Mycroft", "Holmes");

        final Person sherlock = personDAO.addPerson("Sherlock", "Holmes");

        personDAO.addAddress(sherlock, "221B Baker str", "London", "NW1 6XE", "GB");

        final Person john = personDAO.addPerson("John H.", "Watson");

        personDAO.addAddress(john, "221B Baker str", "London", "NW1 6XE", "GB");

    }

    @Test
    public void testAssembleContactsWithAddress() {

        final List<ContactDTO> contacts = service.getContactsByName("John H.");
        assertNotNull(contacts);
        assertEquals(1, contacts.size());

        final ContactDTO john = contacts.get(0);

        assertEquals("John H.", john.getFirstName());
        assertEquals("Watson", john.getLastName());
        assertEquals("221B Baker str", john.getAddressLine1());
        assertEquals("London", john.getCity());
        assertEquals("NW1 6XE", john.getPostCode());
        assertEquals("GB", john.getCountry());


    }

    @Test
    public void testAssembleContactsWithoutAddress() {

        final List<ContactDTO> contacts = service.getContactsByName("Irene");
        assertNotNull(contacts);
        assertEquals(1, contacts.size());

        final ContactDTO irene = contacts.get(0);

        assertEquals("Irene", irene.getFirstName());
        assertEquals("Adler", irene.getLastName());
        assertNull(irene.getAddressLine1());
        assertNull(irene.getCity());
        assertNull(irene.getPostCode());
        assertNull(irene.getCountry());


    }

}
