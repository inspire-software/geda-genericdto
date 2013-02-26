/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.addressbook.service;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Address;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;

import java.util.List;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Aug 30, 2012
 * Time: 11:56:22 AM
 */
public interface PersonDAO {

    Person addPerson(String firstName, String lastName);

    Address addAddress(Person person, String street, String city, String postCode, String country);

    List<Person> findByFirstName(String firstName);

    Address lookUpAddress(Person person);

}
