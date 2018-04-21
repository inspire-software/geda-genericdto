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
import com.inspiresoftware.lib.dto.geda.benchmark.support.dozer.DozerListMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.geda.GeDAListMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.manual.ManualListMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.modelmapper.ModelMapperListMapper;
import com.inspiresoftware.lib.dto.geda.benchmark.support.orika.OrikaListMapper;

/**
 * Caliper powered benchmark.
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 8:35:29 AM
 */
public class CaliperBenchmark2Temp extends SimpleBenchmark {


    public enum Lib {

        JAVA_MANUAL(new ManualListMapper()),
        GEDA(new GeDAListMapper()),
        ORIKA(new OrikaListMapper()),
        MODELMAPPER(new ModelMapperListMapper()),
        DOZER(new DozerListMapper());

        private Mapper mapper;

        Lib(final Mapper mapper) {
            this.mapper = mapper;
        }
    }

    @Param
    private Lib lib;
    @Param({ "100" /*, "100", "10000", "25000" */})
    private int length;

    private Object dto;
    private Object entity;

    private Mapper mapper;

    @Override
    protected void setUp() throws Exception {
        dto = DataProvider.providePersonDTO(true, false);
        entity = DataProvider.providePersonEntity(true);
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
        Runner.main(CaliperBenchmark2Temp.class, args);
    }

}
