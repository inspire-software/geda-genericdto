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
import com.inspiresoftware.lib.dto.geda.benchmark.data.DataProvider;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.GeDABasicMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.orika.OrikaBasicMapper;

/**
 * Caliper powered benchmark.
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 8:35:29 AM
 */
public class CaliperBenchmark extends SimpleBenchmark {


    public enum Lib {

        //JAVA_MANUAL(new ManualBasicMapper()),
        GEDA(new GeDABasicMapper()),
        ORIKA(new OrikaBasicMapper()); //,
        //MODELMAPPER(new ModelMapperBasicMapper()),
        //DOZER(new DozerBasicMapper());

        private Mapper mapper;

        Lib(final Mapper mapper) {
            this.mapper = mapper;
        }
    }

    @Param
    private Lib lib;
    @Param({ "1", "100", "10000", "25000" })
    private int length;

    private Object dto;
    private Object entity;

    private Mapper mapper;

    @Override
    protected void setUp() throws Exception {
        dto = DataProvider.providePersonDTO(false, false);
        entity = DataProvider.providePersonEntity(false);
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

    public static void main(String[] args) throws Exception {
        Runner.main(CaliperBenchmark.class, args);
    }

}
