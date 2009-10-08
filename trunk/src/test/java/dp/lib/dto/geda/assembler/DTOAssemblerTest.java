/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.assembler;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.assembler.DTOAssembler;
import dp.lib.dto.geda.assembler.TestDto3Class.Decision;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
public class DTOAssemblerTest {

	/**
	 * Test that correctly mapped classes for Entity and Dto get assembled as expected.
	 */
	@Test
	public void test1stClassesMapping() {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler<TestDto1Class, TestEntity1Class> assembler = 
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleDto(dto, entity, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("Will Smith", entity.getName());
		assertEquals(Double.valueOf(1.0d), entity.getNumber());
		
	}

	private TestEntity1Interface createTestEntity1() {
		final TestEntity1Interface entity = new TestEntity1Class();
		entity.setEntityId(1L);
		entity.setName("John Doe");
		entity.setNumber(2.0d);
		return entity;
	}

	/**
	 * Test that inherited classes correctly mapped for Entity and Dto get assembled as expected.
	 */
	@Test
	public void test2ndClassesMapping() {
		
		final TestDto2Class dto = new TestDto2Class();
		final TestEntity2Class entity = createTestEntity2();
		
		final DTOAssembler<TestDto2Class, TestEntity2Class> assembler = 
			DTOAssembler.newAssembler(TestDto2Class.class, TestEntity2Class.class);
		assembler.assembleDto(dto, entity, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		assertEquals(entity.getDecision(), dto.getMyBoolean());
		
		dto.setMyLong(0L);
		dto.setMyBoolean(false);
		
		assembler.assembleEntity(dto, entity, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals(Boolean.FALSE, entity.getDecision());
		
	}

	private TestEntity2Class createTestEntity2() {
		final TestEntity2Class entity = new TestEntity2Class();
		entity.setEntityId(1L);
		entity.setName("John Doe");
		entity.setNumber(2.0d);
		entity.setDecision(true);
		return entity;
	}
	
	/**
	 * Test that Dto that has less fields that entity corretly maps them.
	 */
	@Test
	public void testDtoLessThanEntity() {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity2Class entity = createTestEntity2();
		
		final DTOAssembler<TestDto1Class, TestEntity2Class> assembler = 
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity2Class.class);
		assembler.assembleDto(dto, entity, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		
		assembler.assembleEntity(dto, entity, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals(Boolean.TRUE, entity.getDecision());
		
	}

	/**
	 * Test that Dto that has more fields that entity fails to create assembler.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDtoMoreThanEntity() {
		
		DTOAssembler.newAssembler(TestDto2Class.class, TestEntity1Class.class);
		
		
	}

	/**
	 * Test wrong classes in use with assembler give an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongObjectsInAssembleMethods1() {
		
		final TestDto1Interface dto1 = new TestDto1Class();
		final TestEntity1Interface entity1 = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity2Class.class);
		
		assembler.assembleDto(dto1, entity1, null);
		
	}

	/**
	 * Test wrong classes in use with assembler give an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongObjectsInAssembleMethods2() {
		
		final TestDto2Class dto2 = new TestDto2Class();
		final TestEntity2Class entity2 = createTestEntity2();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity2Class.class);
		
		assembler.assembleDto(dto2, entity2, null);
		
	}
	
	/**
	 * Test wrong classes in use with assembler give an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongObjectsInAssembleMethods3() {
		
		final TestDto2Class dto2 = new TestDto2Class();
		final TestEntity1Interface entity1 = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity2Class.class);

		assembler.assembleDto(dto2, entity1, null);

	}
	
	/**
	 * Test assembler that uses converter.
	 */
	@Test
	public void testWithConversion() {
		final TestDto3Class dto = new TestDto3Class();
		final TestEntity3Class entity = new TestEntity3Class();
		entity.setDecision(true);
		final ValueConverter conv3toDto = new TestConverter3();
		final Map<String, ValueConverter> converters = new HashMap<String, ValueConverter>();
		converters.put("boolToEnum", conv3toDto);
		
		final DTOAssembler<TestDto3Class, TestEntity3Class> assembler = 
			DTOAssembler.newAssembler(TestDto3Class.class, TestEntity3Class.class);
		
		assembler.assembleDto(dto, entity, converters);
		
		assertEquals(Decision.Decided, dto.getMyEnum());
		
		dto.setMyEnum(Decision.Undecided);
		
		assembler.assembleEntity(dto, entity, converters);
			
	}
	
	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 */
	@Test
	public void testWrappedProperty() {
		final TestDto4Class dto = new TestDto4Class();
		final TestEntity4Class entity = new TestEntity4Class();
		entity.setWrapper(new TestEntity4SubClass());
		entity.getWrapper().setName("Name");
		
		final DTOAssembler<TestDto4Class, TestEntity4Class> assembler = 
			DTOAssembler.newAssembler(TestDto4Class.class, TestEntity4Class.class);
		
		assembler.assembleDto(dto, entity, null);
		
		assertEquals(entity.getWrapper().getName(), dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null);
		
		assertEquals("Another Name", entity.getWrapper().getName());
		
	}
	
	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 */
	@Test
	public void testDeepWrappedProperty() {
		final TestDto5Class dto = new TestDto5Class();
		final TestEntity5Class entity = new TestEntity5Class();
		entity.setWrapper(new TestEntity4Class());
		entity.getWrapper().setWrapper(new TestEntity4SubClass());
		entity.getWrapper().getWrapper().setName("Name");
		
		final DTOAssembler<TestDto5Class, TestEntity5Class> assembler = 
			DTOAssembler.newAssembler(TestDto5Class.class, TestEntity5Class.class);

		assembler.assembleDto(dto, entity, null);
		
		assertEquals(entity.getWrapper().getWrapper().getName(), dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null);
		
		assertEquals("Another Name", entity.getWrapper().getWrapper().getName());
		
	}
	
	/**
	 * Test that read only field get copied to Dto but not back to entity.
	 */
	@Test
	public void testReadOnlyClassesMapping() {
		
		final TestDto6Class dto = new TestDto6Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler<TestDto6Class, TestEntity1Class> assembler = 
			DTOAssembler.newAssembler(TestDto6Class.class, TestEntity1Class.class);
		assembler.assembleDto(dto, entity, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("John Doe", entity.getName());
		assertEquals(Double.valueOf(2.0d), entity.getNumber());
	}
	
	/**
	 * Test that DTO as interface picks up the mapping correctly.
	 */
	@Test
	public void testDtoAsInterfaceMapping() {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(dto.getClass(), TestEntity1Class.class);
		assembler.assembleDto(dto, entity, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("Will Smith", entity.getName());
		assertEquals(Double.valueOf(1.0d), entity.getNumber());
	}

	/**
	 * Test that DTO and Entity as interface picks up the mapping correctly.
	 */
	@Test
	public void testBothAsInterfaceMapping() {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(dto.getClass(), TestEntity1Interface.class);
		assembler.assembleDto(dto, entity, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("Will Smith", entity.getName());
		assertEquals(Double.valueOf(1.0d), entity.getNumber());
	}
	
	
	
}
