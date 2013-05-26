/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import com.inspiresoftware.lib.dto.geda.benchmark.data.DataProvider;
import com.inspiresoftware.lib.dto.geda.benchmark.support.dozer.DozerBasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.GeDABasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.manual.ManualBasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.modelmapper.ModelMapperBasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.orika.OrikaBasicMapper;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Benchmarking takes a while so remove Ignore to run it manually.
 * User: denispavlov
 * Date: 12-09-17
 * Time: 5:02 PM
 */
@Ignore
public class JUnitCaliperBenchmarkTest extends SimpleBenchmark {

    public enum Lib {

        JAVA_MANUAL(new ManualBasicMapper()),
        GEDA(new GeDABasicMapper()),
        ORIKA(new OrikaBasicMapper()),
        MODELMAPPER(new ModelMapperBasicMapper()),
        DOZER(new DozerBasicMapper());

        private Mapper mapper;

        Lib(final Mapper mapper) {
            this.mapper = mapper;
        }
    }


    public enum Mode {

        BASIC(
                DataProvider.providePersonDTO(false, false),
                DataProvider.providePersonEntity(false)
        ),
        COLL(
                DataProvider.providePersonDTO(true, false),
                DataProvider.providePersonEntity(true)
        ),
        MAP(
                DataProvider.providePersonDTO(true, true),
                DataProvider.providePersonEntity(true)
        );

        private Object dto;
        private Object entity;

        private Mode(final Object dto, final Object entity) {
            this.dto = dto;
            this.entity = entity;
        }
    }

    @Param
    private Lib lib;
    @Param({ "1", "100", "10000", "25000" })
    private int length;
    @Param
    private Mode mode;

    private Object dto;
    private Object entity;

    private Mapper mapper;

    @Override
    protected void setUp() throws Exception {
        dto = mode.dto;
        entity = mode.entity;
        mapper = lib.mapper;
    }

    public void timeFromDTOToEntity(int reps) {
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                mapper.fromDto(dto);
            }
        }
    }

    public void timeFromEntityToDTO(int reps) {
        for (int i = 0; i < reps; i++) {
            for (int ii = 0; ii < length; ii++) {
                mapper.fromEntity(entity);
            }
        }
    }

    @Test
    public void testBenchmark() {
        Runner.main(CaliperBenchmark.class, new String[] { });
        assertTrue(true);
    }

}
