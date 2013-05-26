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
 * Time: 10:34 PM
 */
public class RunSimpleChain {

    /**
     * Example showing how object graph can be simplified by flattening
     * Entities by mapping.
     */
    public void deepWrappedProperty() throws GeDAException {
        final DtoChainClass dto = new DtoChainClass();
        final EntityChainLv1Class entity = new EntityChainLv1Class();
        entity.setWrapper(new EntityChainLv2Class());
        entity.getWrapper().setWrapper(new EntityChainLv3Class());
        entity.getWrapper().getWrapper().setName("Name");

        final Assembler assembler =
                DTOAssembler.newAssembler(DtoChainClass.class, EntityChainLv1Class.class);

        assembler.assembleDto(dto, entity, null, null);

        assertEquals(entity.getWrapper().getWrapper().getName(), dto.getNestedString());

        dto.setNestedString("Another Name");

        assembler.assembleEntity(dto, entity, null, null);

        assertEquals("Another Name", entity.getWrapper().getWrapper().getName());

    }

    public static void main(String[] args) {
        new RunSimpleChain().deepWrappedProperty();
    }


}
