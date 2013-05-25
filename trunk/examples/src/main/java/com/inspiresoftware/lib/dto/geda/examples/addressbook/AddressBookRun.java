/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.addressbook;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.ContactDTO;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.AddressBookService;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.PersonDAO;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl.AddressBookServiceImpl;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl.PersonDAOImpl;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 3:24 PM
 */
public class AddressBookRun {

    private PersonDAO personDAO;
    private AddressBookService service;

    public AddressBookRun(final PersonDAO personDAO, final AddressBookService service) {
        this.personDAO = personDAO;
        this.service = service;
    }

    /**
     * Running example of services that use GeDA behind the scenes to transfer data
     * between DTO and Entities.
     */
    public void runSetupData() {
        final Person irene = personDAO.addPerson("Irene", "Adler");

        final Person mycroft = personDAO.addPerson("Mycroft", "Holmes");

        final Person sherlock = personDAO.addPerson("Sherlock", "Holmes");

        personDAO.addAddress(sherlock, "221B Baker str", "London", "NW1 6XE", "GB");

        final Person john = personDAO.addPerson("John H.", "Watson");

        personDAO.addAddress(john, "221B Baker str", "London", "NW1 6XE", "GB");
    }

    public void assembleContactsWithAddress() {

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

    public void assembleContactsWithoutAddress() {

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



    public static void main(String[] args) {

        final PersonDAO personDAO = new PersonDAOImpl();
        final AddressBookService service = new AddressBookServiceImpl(personDAO);

        final AddressBookRun run = new AddressBookRun(personDAO, service);

        run.runSetupData();
        run.assembleContactsWithAddress();
        run.assembleContactsWithoutAddress();
    }

}
