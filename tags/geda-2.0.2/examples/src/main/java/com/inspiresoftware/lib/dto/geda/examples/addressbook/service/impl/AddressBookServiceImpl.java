/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Address;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.ContactDTO;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.impl.ContactDTOImpl;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.AddressBookService;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.PersonDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 12:16:10 PM
 */
public class AddressBookServiceImpl implements AddressBookService {

    private final PersonDAO personDAO;

    public AddressBookServiceImpl(final PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    public List<ContactDTO> getContactsByName(final String firstName) {

        final List<Person> people = personDAO.findByFirstName(firstName);

        final List<Object[]> entities  = new ArrayList<Object[]>();

        for (Person person : people) {

            entities.add(new Object[] {
                    person,
                    personDAO.lookUpAddress(person)
            });

        }

        final Assembler asm = DTOAssembler.newCompositeAssembler(
                ContactDTOImpl.class, new Class[] { Person.class, Address.class });

        final List<ContactDTO> dtos = new ArrayList<ContactDTO>();

        asm.assembleDtos(dtos, entities, null, null);

        return dtos;
    }
}
