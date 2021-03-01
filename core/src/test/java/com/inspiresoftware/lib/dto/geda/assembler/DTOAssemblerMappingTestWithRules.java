
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestDto2Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestEntity2Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.virtual.TestDto21Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.virtual.TestEntity21Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.virtual.VirtualMyBooleanConverter;
import com.inspiresoftware.lib.dto.geda.assembler.examples.virtual.VirtualMyLongConverter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class DTOAssemblerMappingTestWithRules {

	@Test
	public void testMappingNullifyAllFields() {
		//given
		final TestDto2Class dto = new TestDto2Class();
		final TestEntity2Class entity = new TestEntity2Class();
		entity.setEntityId(1L);
		entity.setName("John Doe");
		entity.setNumber(2.0d);
		entity.setDecision(true);

		//when
		TestPipeDataFlowRule rule = new TestPipeDataFlowRule();
		rule.addShouldBeSkipField("myLong");
		rule.addShouldBeSkipField("myString");
		rule.addShouldBeSkipField("myDouble");
		rule.addShouldBeSkipField("myBoolean");

		rule.addDefaultValue("myLong", "1");
		rule.addDefaultValue("myString", "my string");
		rule.addDefaultValue("myDouble", "15.2");
		rule.addDefaultValue("myBoolean", "true");

		final Assembler assembler = DTOAssembler.newAssembler(TestDto2Class.class, TestEntity2Class.class);
		assembler.assembleDto(dto, entity, null, null, rule);

		//then
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		assertNull(dto.getMyDouble());
		assertNull(dto.getMyBoolean());
	}

	@Test
	public void testNullifyNotAllFields() {
		//given
		final TestDto2Class dto = new TestDto2Class();
		final TestEntity2Class entity = new TestEntity2Class();
		entity.setEntityId(1L);
		entity.setName("John Doe");
		entity.setNumber(2.0d);
		entity.setDecision(true);

		//when
		TestPipeDataFlowRule rules = new TestPipeDataFlowRule();
		rules.addShouldBeSkipField("myLong");
		rules.addShouldBeSkipField("myString");

		final Assembler assembler = DTOAssembler.newAssembler(TestDto2Class.class, TestEntity2Class.class);
		assembler.assembleDto(dto, entity, null, null, rules);

		//then
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		assertEquals(Double.valueOf(2.0), dto.getMyDouble());
		assertTrue(dto.getMyBoolean());
	}

	@Test
	public void testAssembleEntityWithDtoVirtualField() {
		//given
		final TestDto21Class dto = new TestDto21Class();
		final TestEntity21Class entity = new TestEntity21Class();
		entity.setPk(1L);
		entity.makeComplexDecision(true);
		entity.setMyString("raaaka");

		final Map<String, Object> converters = new HashMap<>();
		converters.put("VirtualMyBoolean", new VirtualMyBooleanConverter());
		converters.put("VirtualMyLong", new VirtualMyLongConverter());

		final Assembler assembler = DTOAssembler.newAssembler(TestDto21Class.class, TestEntity21Class.class);
		assembler.assembleDto(dto, entity, converters, null);

		//when
		TestPipeDataFlowRule rule = new TestPipeDataFlowRule();
		rule.addShouldBeSkipField("myString");
		rule.addShouldBeSkipField("pk");
		rule.addShouldBeSkipField("decided");

		rule.addDefaultValue("myString", "my test");
		rule.addDefaultValue("pk", "1");
		rule.addDefaultValue("decided", "F");

		TestEntity21Class resultEntity = new TestEntity21Class();
		assembler.assembleEntity(dto, resultEntity, converters, null, rule);

		//then
		assertEquals(1L, resultEntity.getPk());
		assertTrue(resultEntity.isDecided());
		assertEquals("my test", resultEntity.getMyString());
	}
}
