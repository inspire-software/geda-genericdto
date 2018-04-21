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

import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.AddressBookService;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.PersonDAO;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl.AddressBookServiceImpl;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.service.impl.PersonDAOImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * .
 *
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 12:49:58 PM
 */
public class AddressBookRunnerTest {

    private PersonDAO personDAO;
    private AddressBookService service;


    @Before
    public void setupPeopleAndAddresses() {

        personDAO = new PersonDAOImpl();
        service = new AddressBookServiceImpl(personDAO);

        new AddressBookRun(personDAO, service).runSetupData();

    }

    @Test
    public void testAssembleContactsWithAddress() {

        new AddressBookRun(personDAO, service).assembleContactsWithAddress();

    }

    @Test
    public void testAssembleContactsWithoutAddress() {

        new AddressBookRun(personDAO, service).assembleContactsWithoutAddress();

    }

}
