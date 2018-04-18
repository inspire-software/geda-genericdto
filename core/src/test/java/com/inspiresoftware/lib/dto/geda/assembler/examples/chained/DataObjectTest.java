
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.chained;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import org.junit.Assert;
import org.junit.Test;

public class DataObjectTest {

    @Test
    public void testPassingClass(){
        //GIVEN
        PassingClass passingClass = new PassingClass();
        passingClass.setId(42);
        passingClass.setDescription("The Answer to the Question of Life, the Universe, and Everything.");

        BusinessDTO myDTO = new BusinessDTO();

        //WHEN
        DTOAssembler.newAssembler(myDTO.getClass(), passingClass.getClass())
                .assembleDto(myDTO, passingClass, null, null);

        //THEN
        Assert.assertEquals(passingClass.getId(), myDTO.getId());
        Assert.assertEquals(passingClass.getDescription(), myDTO.getText());
    }

    @Test
    public void testFailingClass(){
        //GIVEN
        ChainedSetterClass chainedSetterClass = new ChainedSetterClass()
                .setId(36)
                .setDescription("Not The Answer to the Question of Life, the Universe, and Everything.");

        BusinessDTO myDTO = new BusinessDTO();

        //WHEN
        DTOAssembler.newAssembler(myDTO.getClass(), chainedSetterClass.getClass())
                .assembleDto(myDTO, chainedSetterClass, null, null);

        //THEN
        Assert.assertEquals(chainedSetterClass.getId(), myDTO.getId());
        Assert.assertEquals(chainedSetterClass.getDescription(), myDTO.getText());
    }
}
