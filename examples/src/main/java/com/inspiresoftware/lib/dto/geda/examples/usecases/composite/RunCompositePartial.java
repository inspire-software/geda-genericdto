/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.composite;

import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 6:50 PM
 */
public class RunCompositePartial {

    /**
     * Example that shows how several entities can be combined into single DTO 
     * using composite assembler but not full set specified in the mapping.
     */
    public void compositePartialAssembly() {


        final EntityPart1Class e30 = new EntityPart1Class();
        e30.setField30("v30");
        final EntityPart2Class e31 = new EntityPart2Class();
        e31.setField31("v31");
        final EntityPart3Class e32 = new EntityPart3Class();
        e32.setField32("v32");

        final CompositeDtoClass dto = new CompositeDtoClass();

        final Assembler asm = DTOAssembler.newCompositeAssembler(
                CompositeDtoClass.class,
                new Class[]{EntityPart1Class.class, EntityPart2Class.class, EntityPart3Class.class});

        asm.assembleDto(dto, e31, null, null);

        assertNull(dto.getField30());
        assertEquals(dto.getField31(), "v31");
        assertNull(dto.getField32());

        dto.setField30("dto30");
        dto.setField31("dto31");
        dto.setField32("dto32");

        asm.assembleEntity(dto, e31, null, null);

        assertEquals(e30.getField30(), "v30");
        assertEquals(e31.getField31(), "dto31");
        assertEquals(e32.getField32(), "v32");


    }

    public static void main(String[] args) {
        new RunCompositePartial().compositePartialAssembly();
    }


}
