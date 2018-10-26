package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestDto30Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestEntity30Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestEntity31Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.composite.TestEntity32Class;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DTOAssemblerCompositeDtoTestWithRules {

    @Test
    public void test_assembleDto() throws GeDAException {
        //given
        final TestEntity30Class e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Class e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Class e32 = new TestEntity32Class();
        e32.setField32("v32");

        final TestDto30Class dto = new TestDto30Class();

        final Assembler asm = DTOAssembler.newCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Class.class, TestEntity31Class.class, TestEntity32Class.class});

        //when
        TestPipeDataFlowRule rule = new TestPipeDataFlowRule();
        rule.addShouldBeSkipField("field30");

        asm.assembleDto(dto, new Object[] { e30, e31, e32 }, null, null, rule);

        //then
        assertNull(dto.getField30());
        assertEquals("v31", dto.getField31());
        assertEquals("v32", dto.getField32());
    }

    @Test
    public void test_assembleEntity() throws GeDAException {
        //given
        final TestDto30Class dto = new TestDto30Class();
        dto.setField30("dto30");
        dto.setField31("dto31");
        dto.setField32("dto32");

        final TestEntity30Class e30 = new TestEntity30Class();
        final TestEntity31Class e31 = new TestEntity31Class();
        final TestEntity32Class e32 = new TestEntity32Class();

        final Assembler asm = DTOAssembler.newCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Class.class, TestEntity31Class.class, TestEntity32Class.class});

        //when
        TestPipeDataFlowRule rule = new TestPipeDataFlowRule();
        rule.addShouldBeSkipField("field30");

        asm.assembleEntity(dto, new Object[] { e30, e31, e32 }, null, null, rule);

        //then
        assertNull(e30.getField30());
        assertEquals("dto31", e31.getField31());
        assertEquals("dto32", e32.getField32());
    }
}
