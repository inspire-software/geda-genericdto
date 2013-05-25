/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.dto;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.benchmark.data.DataProvider;
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
    public void testBasicMapping() throws Exception {


        final BeanFactory bf = new GeDABeanFactory();

        final Person entity = DataProvider.providePersonEntity(false);

        final PersonDTO dto = new PersonDTO();

        final Assembler asm = DTOAssembler.newAssembler(PersonDTO.class, Person.class, this.getClass().getClassLoader());

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

    @Test
    public void testCollectionsMapping() throws Exception {


        final BeanFactory bf = new GeDABeanFactory();

        final Person entity = DataProvider.providePersonEntity(true);

        final PersonWithHistoryDTO dto = new PersonWithHistoryDTO();

        final Assembler asm = DTOAssembler.newAssembler(PersonWithHistoryDTO.class, Person.class, this.getClass().getClassLoader());

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

        assertNotNull(dto.getPreviousAddresses());
        assertEquals(2, dto.getPreviousAddresses().size());

        final AddressDTO prev1 = dto.getPreviousAddresses().get(0);
        assertNotNull(prev1);
        assertEquals(prev1.getAddressLine1(), "221B Baker Street");
        assertNull(prev1.getAddressLine2());
        assertEquals(prev1.getCity(), "London");
        assertEquals(prev1.getCountryName(), "United Kingdom");
        assertEquals(prev1.getPostCode(), "NW1 6XE");

        final AddressDTO prev2 = dto.getPreviousAddresses().get(1);
        assertNotNull(prev2);
        assertEquals(prev2.getAddressLine1(), "Baskerville Hall");
        assertNull(prev2.getAddressLine2());
        assertEquals(prev2.getCity(), "Hay-on-Wye");
        assertEquals(prev2.getCountryName(), "United Kingdom");
        assertEquals(prev2.getPostCode(), "HR3 5LE");

    }

    @Test
    public void testMapsMapping() throws Exception {


        final BeanFactory bf = new GeDABeanFactory();

        final Person entity = DataProvider.providePersonEntity(true);

        final PersonWithHistoryByCityDTO dto = new PersonWithHistoryByCityDTO();

        final Assembler asm = DTOAssembler.newAssembler(PersonWithHistoryByCityDTO.class, Person.class, this.getClass().getClassLoader());

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

        assertNotNull(dto.getPreviousAddresses());
        assertEquals(2, dto.getPreviousAddresses().size());

        final AddressDTO prev1 = dto.getPreviousAddresses().get("London");
        assertNotNull(prev1);
        assertEquals(prev1.getAddressLine1(), "221B Baker Street");
        assertNull(prev1.getAddressLine2());
        assertEquals(prev1.getCity(), "London");
        assertEquals(prev1.getCountryName(), "United Kingdom");
        assertEquals(prev1.getPostCode(), "NW1 6XE");

        final AddressDTO prev2 = dto.getPreviousAddresses().get("Hay-on-Wye");
        assertNotNull(prev2);
        assertEquals(prev2.getAddressLine1(), "Baskerville Hall");
        assertNull(prev2.getAddressLine2());
        assertEquals(prev2.getCity(), "Hay-on-Wye");
        assertEquals(prev2.getCountryName(), "United Kingdom");
        assertEquals(prev2.getPostCode(), "HR3 5LE");

    }


}
