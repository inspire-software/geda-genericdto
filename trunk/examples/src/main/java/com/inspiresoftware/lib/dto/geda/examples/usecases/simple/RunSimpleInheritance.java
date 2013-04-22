/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import static org.junit.Assert.assertEquals;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 10:26 PM
 */
public class RunSimpleInheritance {

    /**
     * Example showing how DTO copes with entity inheritance
     */
    public void inheritanceMapping() throws GeDAException {

        final SimpleDtoClass dto = new SimpleDtoClass();
        final SimpleEntityClass entity = createTestEntity2();

        final Assembler assembler =
                DTOAssembler.newAssembler(SimpleDtoClass.class, SimpleEntityClass.class);
        assembler.assembleDto(dto, entity, null, null);
        assertEquals(entity.getEntityId(), dto.getMyLong());
        assertEquals(entity.getName(), dto.getMyString());
        assertEquals(entity.getNumber(), dto.getMyDouble());
        assertEquals(entity.getDecision(), dto.getMyBoolean());

        dto.setMyLong(0L);
        dto.setMyBoolean(false);

        assembler.assembleEntity(dto, entity, null, null);
        assertEquals(Long.valueOf(0L), entity.getEntityId());
        assertEquals(Boolean.FALSE, entity.getDecision());

    }


    private SimpleEntityClass createTestEntity2() throws GeDAException {
        final SimpleEntityClass entity = new SimpleEntityClass();
        entity.setEntityId(1L);
        entity.setName("John Doe");
        entity.setNumber(2.0d);
        entity.setDecision(true);
        return entity;
    }

    public static void main(String[] args) {
        new RunSimpleInheritance().inheritanceMapping();
    }


}
