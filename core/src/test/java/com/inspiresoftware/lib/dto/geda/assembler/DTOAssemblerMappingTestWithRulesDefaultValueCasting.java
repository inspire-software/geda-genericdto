package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestDto16Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestDto17Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestEntity16Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestEntity17Class;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DTOAssemblerMappingTestWithRulesDefaultValueCasting {

	@Test
	public void testDefaultValueTypeCasting() {
		//given
		TestDto16Class dto = new TestDto16Class();
		TestEntity16Class entity = new TestEntity16Class();

		dto.setMyByte(new Byte("45"));
		dto.setMyShort(new Short("46"));
		dto.setMyInteger(47);
		dto.setMyLong(48L);
		dto.setMyDouble(49.2);
		dto.setMyFloat(49.2f);
		dto.setMyCharacter('Z');
		dto.setMyString("my string");
		dto.setMyBoolean(false);
		dto.setMyDate(null);

		//when
		TestPipeDataFlowRule rule = new TestPipeDataFlowRule();
		rule.addShouldBeSkipField("myByte");
		rule.addShouldBeSkipField("myShort");
		rule.addShouldBeSkipField("myInteger");
		rule.addShouldBeSkipField("myLong");
		rule.addShouldBeSkipField("myString");
		rule.addShouldBeSkipField("myDouble");
		rule.addShouldBeSkipField("myBoolean");
		rule.addShouldBeSkipField("myFloat");
		rule.addShouldBeSkipField("myDate");

		rule.addDefaultValue("myByte", "1");
		rule.addDefaultValue("myShort", "2");
		rule.addDefaultValue("myInteger", "3");
		rule.addDefaultValue("myLong", "4");
		rule.addDefaultValue("myString", "new string");
		rule.addDefaultValue("myDouble", "45.68");
		rule.addDefaultValue("myBoolean", "T");
		rule.addDefaultValue("myFloat", "52.23");
		rule.addDefaultValue("myDate", "sysdate");

		final Assembler assembler = DTOAssembler.newAssembler(TestDto16Class.class, TestEntity16Class.class);
		assembler.assembleEntity(dto, entity, null, null, rule);

		//then
		assertEquals(new Byte("1"), entity.getMyByte());
		assertEquals(new Short("2"), entity.getMyShort());
		assertEquals(new Integer("3"), entity.getMyInteger());
		assertEquals(new Long("4"), entity.getMyLong());
		assertEquals("new string", entity.getMyString());
		assertEquals(new Double(45.68), entity.getMyDouble());
		assertEquals(Boolean.TRUE, entity.getMyBoolean());
		assertEquals(new Float(52.23), entity.getMyFloat());
		assertNotNull(entity.getMyDate());
	}

	@Test
	public void testConverterWithDefaultValue() {
		// given
		TestDto17Class dto = new TestDto17Class();
		dto.setMyBoolean(null);
		TestEntity17Class entity = new TestEntity17Class();

		// when
        final Map<String, Object> converters = new HashMap<>();
        converters.put(TestDto17Class.MY_INTEGER_CONVERTER, new MyIntegerToBooleanConverter());

		TestPipeDataFlowRule rule = new TestPipeDataFlowRule();
        rule.addShouldBeSkipField("myBoolean");

        rule.addDefaultValue("myBoolean", "Y");

		final Assembler assembler = DTOAssembler.newAssembler(TestDto17Class.class, TestEntity17Class.class);
		assembler.assembleEntity(dto, entity, converters, null, rule);

		// then
        assertEquals(new Integer(1), entity.getMyInteger());
	}

    @Test
    public void testConverterWithoutDefaultValueWorksCorrectly() {
        // given
        TestDto17Class dto = new TestDto17Class();
        dto.setMyBoolean(Boolean.TRUE);
        TestEntity17Class entity = new TestEntity17Class();

        // when
        final Map<String, Object> converters = new HashMap<>();
        converters.put(TestDto17Class.MY_INTEGER_CONVERTER, new MyIntegerToBooleanConverter());

        TestPipeDataFlowRule rule = new TestPipeDataFlowRule();

        final Assembler assembler = DTOAssembler.newAssembler(TestDto17Class.class, TestEntity17Class.class);
        assembler.assembleEntity(dto, entity, converters, null, rule);

        // then
        assertEquals(new Integer(1), entity.getMyInteger());
    }

	private static class MyIntegerToBooleanConverter implements ValueConverter {

        @Override
        public Object convertToDto(Object object, BeanFactory beanFactory) {
            if (object == null) {
                return null;
            }

            if (((Integer)object) > 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        @Override
        public Object convertToEntity(Object object, Object oldEntity, BeanFactory beanFactory) {
            if (object == null) {
                return null;
            }
            if (Boolean.TRUE.equals(object)) {
                return 1;
            }
            return 0;
        }
    }
}
