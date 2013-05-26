/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.autowire;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import static org.junit.Assert.assertEquals;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 6:40 PM
 */
public class RunAutowire {

    /**
     * Example showing that when @Dto specifies class name for entity the assembler
     * is auto created for that class or interface.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
     */
    public void autoBinding() throws GeDAException {

        final MyDtoInterface dto = new MyDtoClass();
        final MyEntityInterface entity = new MyEntityClass();

        final Assembler assembler = DTOAssembler.newAssembler(MyDtoClass.class);

        final double myDouble = 0.2d;

        dto.setMyLong(1L);
        dto.setMyDouble(myDouble);
        dto.setMyString("string");

        assembler.assembleEntity(dto, entity, null, null);

        assertEquals(Long.valueOf(1L), entity.getEntityId());
        assertEquals(Double.valueOf(myDouble), entity.getNumber());
        assertEquals("string", entity.getName());

        entity.setEntityId(2L);
        entity.setNumber(2d);
        entity.setName("name");

        assembler.assembleDto(dto, entity, null, null);

        assertEquals(Long.valueOf(2L), dto.getMyLong());
        assertEquals(Double.valueOf(2d), dto.getMyDouble());
        assertEquals("name", dto.getMyString());

    }

    public static void main(String[] args) {
        new RunAutowire().autoBinding();
    }



}
