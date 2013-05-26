/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import static org.junit.Assert.assertEquals;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 10:48 PM
 */
public class RunSimpleReadOnly {

    /**
     * Example showing immutable entity mapping that allows loading the data to
     * DTO but prevent from writing field back to entity on per field basis.
     */
    public void readOnlyMapping() throws GeDAException {
        final ReadOnlyDtoClass dto = new ReadOnlyDtoClass();

        final ReadOnlyEntityClass entity = new ReadOnlyEntityClass("name", "desc");

        final Assembler assembler =
                DTOAssembler.newAssembler(ReadOnlyDtoClass.class, ReadOnlyEntityClass.class);

        assembler.assembleDto(dto, entity, null, null);

        assertEquals("name", dto.getName());
        assertEquals("desc", dto.getDesc());

        dto.setName("Name DTO");
        dto.setDesc("Desc DTO");

        assembler.assembleEntity(dto, entity, null, null);

        assertEquals("name", entity.getName());
        assertEquals("desc", entity.getDesc());

    }


    public static void main(String[] args) {
        new RunSimpleReadOnly().readOnlyMapping();
    }



}
