/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.benchmark.dto;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Country;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Name;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.GeDABeanFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 8:50:03 AM
 */
public class GeDAMappingTest {

    @Test
    public void testMapping() throws Exception {


        final BeanFactory bf = new GeDABeanFactory();

        final Name name = new Name("Sherlock", "Holmes");
        final Country country = new Country("United Kingdom");
        final Address address = new Address("221B Baker Street", null, "London", country, "NW1 6XE");
        final Person entity = new Person(name, address);

        final PersonDTO dto = new PersonDTO();

        final Assembler asm = DTOAssembler.newAssembler(PersonDTO.class, Person.class);

        asm.assembleDto(dto, entity, null, bf);

        assertEquals(dto.getFirstName(), "Sherlock");
        assertEquals(dto.getLastName(), "Holmes");

        final AddressDTO addressDTO = dto.getCurrentAddress();
        assertNotNull(addressDTO);
        assertEquals(addressDTO.getAddressLine1(), "221B Baker Street");
        assertNull(addressDTO.getAddressLine2());
        assertEquals(addressDTO.getCity(), "London");
        assertEquals(addressDTO.getCountryName(), "United Kingdom");
        assertEquals(addressDTO.getPostCode(), "NW1 6XE");


    }


}
