/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.converter;

import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 7:00 PM
 */
public class RunWithConverter {

    /**
     * Example showing how a converter can be passed to assembler.
     * Please note that there is {@link com.inspiresoftware.lib.dto.geda.adapter.repository.AdaptersRepository} 
     * wrapper available in core where you can store your adapter instances (including converters).
     * IMPORTANT: Keep converters stateless!
     */
    public void withConversion() {
        final DtoClass dto = new DtoClass();
        final EntityClass entity = new EntityClass();
        entity.setDecision(true);
        final ValueConverter conv3toDto = new Converter();
        final Map<String, Object> converters = new HashMap<String, Object>();
        converters.put("boolToEnum", conv3toDto);

        final Assembler assembler =
                DTOAssembler.newAssembler(DtoClass.class, EntityClass.class);

        assembler.assembleDto(dto, entity, converters, null);

        assertEquals(DtoClass.Decision.Decided, dto.getMyEnum());

        dto.setMyEnum(DtoClass.Decision.Undecided);

        assembler.assembleEntity(dto, entity, converters, null);

    }

    public static void main(String[] args) {
        new RunWithConverter().withConversion();
    }



}
