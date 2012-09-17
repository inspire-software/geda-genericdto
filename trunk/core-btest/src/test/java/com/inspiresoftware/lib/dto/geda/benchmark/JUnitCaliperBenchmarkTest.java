/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.benchmark;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Country;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Name;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;
import com.inspiresoftware.lib.dto.geda.benchmark.support.dozer.DozerBasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.GeDABasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.manual.ManualBasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.modelmapper.ModelMapperMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.orika.OrikaMapper;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * User: denispavlov
 * Date: 12-09-17
 * Time: 5:02 PM
 */
public class JUnitCaliperBenchmarkTest extends SimpleBenchmark {

    public enum Lib {

        JAVA_MANUAL(new ManualBasicMapper()),
        GEDA(new GeDABasicMapper()),
        ORIKA(new OrikaMapper()),
        MODELMAPPER(new ModelMapperMapper()),
        DOZER(new DozerBasicMapper());

        private Mapper mapper;

        Lib(final Mapper mapper) {
            this.mapper = mapper;
        }
    }

    @Param
    private Lib lib;
    @Param({ "1", "100", "10000", "25000" })
    private int length;

    private Person personLoaded;
    private PersonDTO personDTOLoaded;

    private Mapper mapper;

    @Override
    protected void setUp() throws Exception {

        final Name name = new Name("Sherlock", "Holmes");
        final Country country = new Country("United Kingdom");
        final Address address = new Address("221B Baker Street", null, "London", country, "NW1 6XE");
        final Person entity = new Person(name, address);

        personLoaded = entity;

        final PersonDTO dto = new PersonDTO();
        dto.setFirstName("Sherlock");
        dto.setLastName("Holmes");
        final AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressLine1("221B Baker Street");
        addressDTO.setCity("London");
        addressDTO.setPostCode("NW1 6XE");
        addressDTO.setCountryName("United Kingdom");
        dto.setCurrentAddress(addressDTO);

        personDTOLoaded = dto;

        mapper = lib.mapper;
    }

    public void timeFromDTOToEntity(int reps) {
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                mapper.fromDto(personDTOLoaded);
            }
        }
    }

    public void timeFromEntityToDTO(int reps) {
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                mapper.fromEntity(personLoaded);
            }
        }
    }

    @Test
    public void testBenchmark() {
        Runner.main(CaliperBenchmark.class, new String[] { });
        assertTrue(true);
    }

}
