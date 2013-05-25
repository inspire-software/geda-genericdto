/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.virtual;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 10:54 PM
 */
public class RunVirtual {

    /**
     * Test that assembler copes with virtual field mapping.
     */
    public void virtualMapping() {

        final DtoClass dto = new DtoClass();
        final EntityClass entity = new EntityClass();

        final Map<String, Object> converters = new HashMap<String, Object>();
        converters.put("VirtualMyBoolean", new VirtualBooleanConverter());
        converters.put("VirtualMyLong", new VirtualLongConverter());

        final Assembler assembler = DTOAssembler.newAssembler(dto.getClass(), entity.getClass());

        assembler.assembleDto(dto, entity, converters, null);

        assertTrue(dto.getMyBoolean());
        assertEquals(Long.valueOf(0L), dto.getMyLong());
        dto.setMyBoolean(true);
        dto.setMyLong(2L);
        assertFalse(entity.isDecided());

        assembler.assembleEntity(dto, entity, converters, null);

        assertTrue(entity.isDecided());
        assertEquals(2L, entity.getPk());

    }


    public static void main(String[] args) {
        new RunVirtual().virtualMapping();
    }


}
