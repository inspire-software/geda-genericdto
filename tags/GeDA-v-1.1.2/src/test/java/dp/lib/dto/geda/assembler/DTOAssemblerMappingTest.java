
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.exception.AnnotationMissingAutobindingException;
import dp.lib.dto.geda.exception.AutobindingClassNotFoundException;
import dp.lib.dto.geda.exception.GeDAException;
import dp.lib.dto.geda.exception.InspectionBindingNotFoundException;
import dp.lib.dto.geda.exception.InspectionInvalidDtoInstanceException;
import dp.lib.dto.geda.exception.InspectionInvalidEntityInstanceException;
import dp.lib.dto.geda.utils.ParameterizedSynthesizer;
import dp.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@RunWith(value = ParameterizedSynthesizer.class)
public class DTOAssemblerMappingTest {
	
	private String synthesizer;
	
	/**
	 * @param synthesizer parameter
	 */
	public DTOAssemblerMappingTest(final String synthesizer) {
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
	 * Test that correctly mapped classes for Entity and Dto get assembled as expected.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void test1stClassesMapping() throws GeDAException {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleDto(dto, entity, null, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("Will Smith", entity.getName());
		assertEquals(Double.valueOf(1.0d), entity.getNumber());
		
	}

	private TestEntity1Interface createTestEntity1() throws GeDAException {
		final TestEntity1Interface entity = new TestEntity1Class();
		entity.setEntityId(1L);
		entity.setName("John Doe");
		entity.setNumber(2.0d);
		return entity;
	}

	/**
	 * Test that inherited classes correctly mapped for Entity and Dto get assembled as expected.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void test2ndClassesMapping() throws GeDAException {
		
		final TestDto2Class dto = new TestDto2Class();
		final TestEntity2Class entity = createTestEntity2();
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto2Class.class, TestEntity2Class.class, synthesizer);
		assembler.assembleDto(dto, entity, null, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		assertEquals(entity.getDecision(), dto.getMyBoolean());
		
		dto.setMyLong(0L);
		dto.setMyBoolean(false);
		
		assembler.assembleEntity(dto, entity, null, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals(Boolean.FALSE, entity.getDecision());
		
	}

	private TestEntity2Class createTestEntity2() throws GeDAException {
		final TestEntity2Class entity = new TestEntity2Class();
		entity.setEntityId(1L);
		entity.setName("John Doe");
		entity.setNumber(2.0d);
		entity.setDecision(true);
		return entity;
	}
	
	/**
	 * Test that Dto that has less fields that entity corretly maps them.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoLessThanEntity() throws GeDAException {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity2Class entity = createTestEntity2();
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity2Class.class, synthesizer);
		assembler.assembleDto(dto, entity, null, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		
		assembler.assembleEntity(dto, entity, null, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals(Boolean.TRUE, entity.getDecision());
		
	}

	/**
	 * Test that Dto that has more fields than entity fails to create assembler.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InspectionBindingNotFoundException.class)
	public void testDtoMoreThanEntity() throws GeDAException {
		
		DTOAssembler.newCustomAssembler(TestDto2Class.class, TestEntity1Class.class, synthesizer);
		
		
	}

	/**
	 * Test wrong classes in use with assembler give an exception.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InspectionInvalidEntityInstanceException.class)
	public void testWrongObjectsInAssembleMethods1() throws GeDAException {
		
		final TestDto1Interface dto1 = new TestDto1Class();
		final TestEntity1Interface entity1 = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity2Class.class, synthesizer);
		
		assembler.assembleDto(dto1, entity1, null, null);
		
	}

	/**
	 * Test wrong classes in use with assembler give an exception.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InspectionInvalidDtoInstanceException.class)
	public void testWrongObjectsInAssembleMethods2() throws GeDAException {
		
		final TestDto2Class dto2 = new TestDto2Class();
		final TestEntity2Class entity2 = createTestEntity2();
		
		final DTOAssembler assembler = 
			DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity2Class.class, synthesizer);
		
		assembler.assembleDto(dto2, entity2, null, null);
		
	}
	
	/**
	 * Test wrong classes in use with assembler give an exception.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InspectionInvalidDtoInstanceException.class)
	public void testWrongObjectsInAssembleMethods3() throws GeDAException {
		
		final TestDto2Class dto2 = new TestDto2Class();
		final TestEntity1Interface entity1 = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity2Class.class, synthesizer);

		assembler.assembleDto(dto2, entity1, null, null);

	}
	
	/**
	 * Test that read only field get copied to Dto but not back to entity.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testReadOnlyClassesMapping() throws GeDAException {
		
		final TestDto6Class dto = new TestDto6Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto6Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleDto(dto, entity, null, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("John Doe", entity.getName());
		assertEquals(Double.valueOf(2.0d), entity.getNumber());
	}
	
	/**
	 * Test that DTO as interface picks up the mapping correctly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoAsInterfaceMapping() throws GeDAException {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newCustomAssembler(dto.getClass(), TestEntity1Class.class, synthesizer);
		assembler.assembleDto(dto, entity, null, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("Will Smith", entity.getName());
		assertEquals(Double.valueOf(1.0d), entity.getNumber());
	}

	/**
	 * Test that DTO and Entity as interface picks up the mapping correctly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testBothAsInterfaceMapping() throws GeDAException {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newCustomAssembler(dto.getClass(), TestEntity1Interface.class, synthesizer);
		assembler.assembleDto(dto, entity, null, null);
		assertEquals(entity.getEntityId(), dto.getMyLong());
		assertEquals(entity.getName(), dto.getMyString());
		assertEquals(entity.getNumber(), dto.getMyDouble());
		
		dto.setMyLong(0L);
		dto.setMyString("Will Smith");
		dto.setMyDouble(1.0d);
		
		assembler.assembleEntity(dto, entity, null, null);
		assertEquals(Long.valueOf(0L), entity.getEntityId());
		assertEquals("Will Smith", entity.getName());
		assertEquals(Double.valueOf(1.0d), entity.getNumber());
	}
	
	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testWrappedProperty() throws GeDAException {
		final TestDto4Class dto = new TestDto4Class();
		final TestEntity4Class entity = new TestEntity4Class();
		entity.setWrapper(new TestEntity4SubClass());
		entity.getWrapper().setName("Name");
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto4Class.class, TestEntity4Class.class, synthesizer);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals(entity.getWrapper().getName(), dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertEquals("Another Name", entity.getWrapper().getName());
		
	}

    /**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testWrappedNullDtoProperty() throws GeDAException {
		final TestDto4ComplexClass dto = new TestDto4ComplexClass();
		final TestEntity4Class entity = new TestEntity4Class();
		entity.setWrapper(new TestEntity4SubClass());
		entity.getWrapper().setName("Name");

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto4ComplexSubClass".equals(entityBeanKey)) {
                    return new TestDto4ComplexSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto4ComplexClass.class, TestEntity4Class.class, synthesizer);

		assembler.assembleDto(dto, entity, null, factory);

		assertEquals(entity.getWrapper().getName(), dto.getNestedString().getNestedName());

		dto.getNestedString().setNestedName("Another Name");

		assembler.assembleEntity(dto, entity, null, null);

		assertEquals("Another Name", entity.getWrapper().getName());

	}

    /**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testWrappedNullEntityProperty() throws GeDAException {
		final TestDto4ComplexClass dto = new TestDto4ComplexClass();
		final TestEntity4Class entity = new TestEntity4Class();

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto4ComplexSubClass".equals(entityBeanKey)) {
                    return new TestDto4ComplexSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity4SubClass".equals(entityBeanKey)) {
                    return new TestEntity4SubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto4ComplexClass.class, TestEntity4Class.class, synthesizer);

		assembler.assembleDto(dto, entity, null, factory);

		assertNull(dto.getNestedString());

        dto.setNestedString(new TestDto4ComplexSubClass());
		dto.getNestedString().setNestedName("Another Name");

		assembler.assembleEntity(dto, entity, null, factory);

		assertEquals("Another Name", entity.getWrapper().getName());

	}

	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDeepWrappedProperty() throws GeDAException {
		final TestDto5Class dto = new TestDto5Class();
		final TestEntity5Class entity = new TestEntity5Class();
		entity.setWrapper(new TestEntity4Class());
		entity.getWrapper().setWrapper(new TestEntity4SubClass());
		entity.getWrapper().getWrapper().setName("Name");
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto5Class.class, TestEntity5Class.class, synthesizer);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals(entity.getWrapper().getWrapper().getName(), dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertEquals("Another Name", entity.getWrapper().getWrapper().getName());
		
	}

	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * Test shows that second level entity is created on the fly.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDeepWrappedNullProperty() throws GeDAException {
		final TestDto5Class dto = new TestDto5Class();
		final TestEntity5Class entity = new TestEntity5Class();
		entity.setWrapper(new TestEntity4Class());
		entity.getWrapper().setWrapper(null);
		final BeanFactory beanFactory = new TestBeanFactory();
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto5Class.class, TestEntity5Class.class, synthesizer);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertNull(dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null, beanFactory);
		
		assertEquals("Another Name", entity.getWrapper().getWrapper().getName());
		
	}

	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * At the moment there is no way to create null domain nested bean. This issue in the
	 * process of design decision.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDeepWrappedDoubleNullProperty() throws GeDAException {
		final TestDto5Class dto = new TestDto5Class();
		final TestEntity5Class entity = new TestEntity5Class();
		entity.setWrapper(null);
		final BeanFactory beanFactory = new TestBeanFactory();
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto5Class.class, TestEntity5Class.class, synthesizer);

		assembler.assembleDto(dto, entity, null, null);
		
		assertNull(dto.getNestedString());
		
		dto.setNestedString("Another Deep Name");
		
		assembler.assembleEntity(dto, entity, null, beanFactory);

		assertEquals("Another Deep Name", entity.getWrapper().getWrapper().getName());

	}

	
	/**
	 * Test that null in entity property does not cause NPE.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testNullPropertyInEntity() throws GeDAException {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity1Class entity = new TestEntity1Class();
		
		dto.setMyDouble(1d);
		dto.setMyLong(1L);
		dto.setMyString("1");
		
		assertNotNull(dto.getMyDouble());
		assertNotNull(dto.getMyLong());
		assertNotNull(dto.getMyString());
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertNull(dto.getMyDouble());
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		
		
	}
	
	/**
	 * Test to ensure null property of DTO correctly maps on Entity.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testNestedNullPropertyOnEntity() throws GeDAException {
		
		final TestDto4ComplexClass dto = new TestDto4ComplexClass();
		final TestEntity4Class entity = new TestEntity4Class();
		entity.setWrapper(new TestEntity4SubClass());
		
		final DTOAssembler assembler =
			DTOAssembler.newCustomAssembler(TestDto4ComplexClass.class, TestEntity4Class.class, synthesizer);

		assertNull(dto.getNestedString());
		assertNotNull(entity.getWrapper());
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertNull(entity.getWrapper());
		
	}
	
	/**
	 * Test that if @Dto does not specify class name for entity constructor fails with 
	 * {@link Exception}.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = AnnotationMissingAutobindingException.class)
	public void testDtoEntityClassAutoBindingWhenNotSpecified() throws GeDAException {
		
		DTOAssembler.newCustomAssembler(TestDto10Class.class, synthesizer);
		
	}
	
	/**
	 * Test that if @Dto specifies class name for entity that cannot be loaded the 
	 * constructor fails with {@link Exception}.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = AutobindingClassNotFoundException.class)
	public void testDtoEntityClassAutoBindingWhenBadClassName() throws GeDAException {
		
		DTOAssembler.newCustomAssembler(TestDto13Class.class, synthesizer);
		
	}
	
	/**
	 * Test that if @Dto specifies class name for entity the assembler is auto created
	 * for that class or interface.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoEntityClassAutoBinding() throws GeDAException {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = new TestEntity1Class();
		
		final DTOAssembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, synthesizer);
		
		final double myDouble = 0.2d;
		
		dto.setMyLong(1L);
		dto.setMyDouble(myDouble);
		dto.setMyString("string");
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertEquals(Long.valueOf(1L), entity.getEntityId());
		assertEquals(Double.valueOf(myDouble), entity.getNumber());
		assertEquals("string", entity.getName());
		
		entity.setEntityId(2L);
		entity.setNumber(2d);
		entity.setName("name");
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals(Long.valueOf(2L), dto.getMyLong());
		assertEquals(Double.valueOf(2d), dto.getMyDouble());
		assertEquals("name", dto.getMyString());
		
	}
	
	/**
	 * Test that assembler copes with generic DTO and Entity types.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoEntityClassGenericMapping() throws GeDAException {
		
		final TestDto18Class dto = new TestDto18Class();
		final TestEntity18Class entity = new TestEntity18Class();
		
		final DTOAssembler assembler = DTOAssembler.newCustomAssembler(dto.getClass(), entity.getClass(), synthesizer);

		final TestDto18aClass<String> item = new TestDto18aClass<String>();
		item.setMyProp("item");

		dto.setMyProp("prop");
		final List<TestDto18aClass<String>> coll = new ArrayList<TestDto18aClass<String>>();
		coll.add(item);
		dto.setMyColl(coll);
		final Map<String, TestDto18aClass<String>> map = new HashMap<String, TestDto18aClass<String>>();
		map.put("m1", item);
		dto.setMyMap(map);
		
		assembler.assembleEntity(dto, entity, null, new BeanFactory() {

			public Object get(final String entityBeanKey) {
				return new TestEntity18aClass<String>();
			}
			
		});
		
		assertEquals("prop", entity.getMyProp());
		
		assertNotNull(entity.getMyColl());
		assertNotSame(dto.getMyColl(), entity.getMyColl());
		assertEquals(1, entity.getMyColl().size());
		assertEquals("item", entity.getMyColl().iterator().next().getMyProp());
		
		assertNotNull(entity.getMyMap());
		assertNotSame(dto.getMyMap(), entity.getMyMap());
		assertEquals(1, entity.getMyMap().size());
		assertEquals("item", entity.getMyMap().iterator().next().getMyProp());
		
		entity.setMyProp("e1");
		entity.getMyColl().iterator().next().setMyProp("ci1");
		entity.getMyMap().iterator().next().setMyProp("mi1");
		
		assembler.assembleDto(dto, entity, null, new BeanFactory() {

			public Object get(final String entityBeanKey) {
				return new TestDto18aClass<String>();
			}
			
		});
		
		assertEquals("e1", dto.getMyProp());
		
		assertNotNull(dto.getMyColl());
		assertNotSame(dto.getMyColl(), entity.getMyColl());
		assertEquals(1, dto.getMyColl().size());
		assertEquals("ci1", dto.getMyColl().iterator().next().getMyProp());
		
		assertNotNull(entity.getMyMap());
		assertNotSame(dto.getMyMap(), entity.getMyMap());
		assertEquals(1, dto.getMyMap().size());
		assertTrue(dto.getMyMap().containsKey("mi1"));
		assertEquals("mi1", dto.getMyMap().get("mi1").getMyProp());
		
		
	}
	
	
}
