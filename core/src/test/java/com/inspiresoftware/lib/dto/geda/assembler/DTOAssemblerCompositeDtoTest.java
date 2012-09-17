
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.examples.composite.*;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 2.0.2
 *
 */
@RunWith(value = ParameterizedSynthesizer.class)
public class DTOAssemblerCompositeDtoTest {

	private String synthesizer;

	/**
	 * @param synthesizer parameter
	 */
	public DTOAssemblerCompositeDtoTest(final String synthesizer) {
		super();
		this.synthesizer = synthesizer;
	}

	/**
	 * @return synthesizers keys
	 */
	@Parameters
	public static Collection<String[]> data() {
		final List<String[]> params = new ArrayList<String[]>();
		for (final String param : MethodSynthesizerProxy.getAvailableSynthesizers()) {
			params.add(new String[] { param });
		}
		return params;
	}

	/**
	 * Test that several entity object types can be combined to a single DTO using
     * composite assembler.
	 *
	 * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
	 */
	@Test
	public void testSimpleCompositeAssembly() throws GeDAException {

        final TestEntity30Class e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Class e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Class e32 = new TestEntity32Class();
        e32.setField32("v32");

        final TestDto30Class dto = new TestDto30Class();

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Class.class, TestEntity31Class.class, TestEntity32Class.class },
                this.synthesizer);

        asm.assembleDto(dto, new Object[] { e30, e31, e32 }, null, null);

        assertEquals(dto.getField30(), "v30");
        assertEquals(dto.getField31(), "v31");
        assertEquals(dto.getField32(), "v32");

        dto.setField30("dto30");
        dto.setField31("dto31");
        dto.setField32("dto32");

        asm.assembleEntity(dto, new Object[] { e30, e31, e32 }, null, null);

        assertEquals(e30.getField30(), "dto30");
        assertEquals(e31.getField31(), "dto31");
        assertEquals(e32.getField32(), "dto32");


	}

	/**
	 * Test that composite DTO can be assembled from few parts only and composite
     * DTO can update only few entities.
	 *
	 * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
	 */
	@Test
	public void testPartialSingleCompositeAssembly() throws GeDAException {

        final TestEntity30Class e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Class e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Class e32 = new TestEntity32Class();
        e32.setField32("v32");

        final TestDto30Class dto = new TestDto30Class();

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Class.class, TestEntity31Class.class, TestEntity32Class.class },
                this.synthesizer);

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

	/**
	 * Test that composite DTO can be assembled from few parts only and composite
     * DTO can update only few entities.
	 *
	 * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
	 */
	@Test
	public void testPartialManyCompositeAssembly() throws GeDAException {

        final TestEntity30Class e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Class e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Class e32 = new TestEntity32Class();
        e32.setField32("v32");

        final TestDto30Class dto = new TestDto30Class();

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Class.class, TestEntity31Class.class, TestEntity32Class.class },
                this.synthesizer);

        asm.assembleDto(dto, new Object[] { e31, e32 }, null, null);

        assertNull(dto.getField30());
        assertEquals(dto.getField31(), "v31");
        assertEquals(dto.getField32(), "v32");

        dto.setField30("dto30");
        dto.setField31("dto31");
        dto.setField32("dto32");

        asm.assembleEntity(dto, new Object[] { e30, e31 }, null, null);

        assertEquals(e30.getField30(), "dto30");
        assertEquals(e31.getField31(), "dto31");
        assertEquals(e32.getField32(), "v32");


	}


    /**
     * Test that several entity object types can be combined to a single DTO using
     * composite assembler.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
     */
    @Test
    public void testSimpleCompositeAssemblyOnInterface() throws GeDAException {

        final TestEntity30Interface e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Interface e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Interface e32 = new TestEntity32Class();
        e32.setField32("v32");

        final TestDto30Class dto = new TestDto30Class();

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Interface.class, TestEntity31Interface.class, TestEntity32Interface.class },
                this.synthesizer);

        asm.assembleDto(dto, new Object[] { e30, e31, e32 }, null, null);

        assertEquals(dto.getField30(), "v30");
        assertEquals(dto.getField31(), "v31");
        assertEquals(dto.getField32(), "v32");

        dto.setField30("dto30");
        dto.setField31("dto31");
        dto.setField32("dto32");

        asm.assembleEntity(dto, new Object[] { e30, e31, e32 }, null, null);

        assertEquals(e30.getField30(), "dto30");
        assertEquals(e31.getField31(), "dto31");
        assertEquals(e32.getField32(), "dto32");


    }



    /**
     * Test that several entity object types can be combined to a single DTO using
     * composite assembler.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
     */
    @Test
    public void testCollectionCompositeAssemblyOnInterface() throws GeDAException {

        final TestEntity30Interface e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Interface e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Interface e32 = new TestEntity32Class();
        e32.setField32("v32");

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Interface.class, TestEntity31Interface.class, TestEntity32Interface.class },
                this.synthesizer);

        final List<TestDto30Interface> dtos = new ArrayList<TestDto30Interface>();

        final List<Object[]> entities = new ArrayList<Object[]>();
        entities.add(new Object[] { e30, e31, e32 });

        asm.assembleDtos(dtos, entities, null, null);

        assertEquals(1, dtos.size());

        final TestDto30Interface dto = dtos.get(0);

        assertEquals(dto.getField30(), "v30");
        assertEquals(dto.getField31(), "v31");
        assertEquals(dto.getField32(), "v32");

    }

    /**
     * Test that several entity object types can be combined to a single DTO using
     * composite assembler.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
     */
    @Test
    public void testCollectionPartialCompositeAssemblyOnInterface() throws GeDAException {

        final TestEntity30Interface e30 = new TestEntity30Class();
        e30.setField30("v30");
        final TestEntity31Interface e31 = new TestEntity31Class();
        e31.setField31("v31");
        final TestEntity32Interface e32 = new TestEntity32Class();
        e32.setField32("v32");

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Interface.class, TestEntity31Interface.class, TestEntity32Interface.class },
                this.synthesizer);

        final List<TestDto30Interface> dtos = new ArrayList<TestDto30Interface>();

        final List<Object[]> entities = new ArrayList<Object[]>();
        entities.add(new Object[] { e30 });
        entities.add(new Object[] { e31 });
        entities.add(new Object[] { e30, e31, e32 });

        asm.assembleDtos(dtos, entities, null, null);

        assertEquals(3, dtos.size());

        final TestDto30Interface dto1 = dtos.get(0);

        assertEquals(dto1.getField30(), "v30");
        assertNull(dto1.getField31());
        assertNull(dto1.getField32());

        final TestDto30Interface dto2 = dtos.get(1);

        assertNull(dto2.getField30());
        assertEquals(dto2.getField31(), "v31");
        assertNull(dto2.getField32());

        final TestDto30Interface dto3 = dtos.get(2);

        assertEquals(dto3.getField30(), "v30");
        assertEquals(dto3.getField31(), "v31");
        assertEquals(dto3.getField32(), "v32");

    }

    /**
     * Test that composite dto collection to entities collection is not supported.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionToEntitiesCompositeAssemblyNotSupported() throws GeDAException {

        final Assembler asm = DTOAssembler.newCustomCompositeAssembler(
                TestDto30Class.class,
                new Class[] {TestEntity30Interface.class, TestEntity31Interface.class, TestEntity32Interface.class },
                this.synthesizer);

        final List<TestDto30Interface> dtos = new ArrayList<TestDto30Interface>();

        final TestDto30Interface dto1 = new TestDto30Class();
        dto1.setField30("a");
        dto1.setField31("b");
        dto1.setField32("c");

        dtos.add(dto1);

        final List<Object[]> entities = new ArrayList<Object[]>();

        asm.assembleEntities(dtos, entities, null, null);

    }

}