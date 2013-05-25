
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

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.*;
import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.dto.CustomerOrderDeliveryDetailDTO;
import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.dto.CustomerOrderDeliveryDetailDTOImpl;
import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.entity.CustomerOrderDeliveryDetEnrichWithSet;
import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.entity.CustomerOrderDeliveryDetEnrichWithSetEntity;
import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.entity.CustomerOrderDeliveryDetWithReadOverlap;
import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.entity.CustomerOrderDeliveryDetWithReadOverlapEntity;
import com.inspiresoftware.lib.dto.geda.assembler.examples.generics.TestDto18Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.generics.TestDto18aClass;
import com.inspiresoftware.lib.dto.geda.assembler.examples.generics.TestEntity18Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.generics.TestEntity18aClass;
import com.inspiresoftware.lib.dto.geda.assembler.examples.generics.extend.*;
import com.inspiresoftware.lib.dto.geda.assembler.examples.nested.*;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.*;
import com.inspiresoftware.lib.dto.geda.assembler.examples.virtual.*;
import com.inspiresoftware.lib.dto.geda.exception.*;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;


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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
	 * Test that Dto that has less fields that entity correctly maps them.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoLessThanEntity() throws GeDAException {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity2Class entity = createTestEntity2();
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("com.inspiresoftware.lib.dto.geda.assembler.TestDto4ComplexSubClass".equals(entityBeanKey)) {
                    return new TestDto4ComplexSubClass();
                }
                return null;
            }
        };

		final Assembler assembler =
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

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("com.inspiresoftware.lib.dto.geda.assembler.TestDto4ComplexSubClass".equals(entityBeanKey)) {
                    return new TestDto4ComplexSubClass();
                } else if ("com.inspiresoftware.lib.dto.geda.assembler.TestEntity4SubClass".equals(entityBeanKey)) {
                    return new TestEntity4SubClass();
                }
                return null;
            }
        };

		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler =
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
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, synthesizer);
		
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
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dto.getClass(), entity.getClass(), synthesizer);

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

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

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

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

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

    /**
     * Test that assembler copes with generic DTO and Entity types that
     * are extending other generic types.
     *
     * @throws GeDAException exception
     */
    @Test
    public void testDtoEntityClassGenericExtendMapping() throws Exception {

        final TestDtoCatalogClass<TestDtoCodeClass> dto = new TestDtoCatalogClass<TestDtoCodeClass>();
        final TestEntityCatalog<TestEntityCatalogCode> entity = new TestEntityCatalogClass<TestEntityCatalogCode>();
        final TestEntityCatalogCode entityCodePrime = new TestEntityCatalogCodeClass();
        final TestEntityCatalogCode entityCodeAdditional = new TestEntityCatalogCodeClass();


        entity.setId("ID-ABC");
        entityCodePrime.setCatalog(entity);
        entityCodePrime.setSectionName("AdvancedGenerics");
        entityCodePrime.setCode("CODE-AG1");
        entityCodePrime.setId("ID-123");
        entityCodeAdditional.setCatalog(entity);
        entityCodeAdditional.setSectionName("DtoTrandformations");
        entityCodeAdditional.setCode("CODE-DT1");
        entityCodeAdditional.setId("ID-235");
        entity.setType(entityCodePrime);
        entity.setCodes(new ArrayList<TestEntityCatalogCode>(Arrays.asList(entityCodePrime, entityCodeAdditional)));

        final Assembler assembler = DTOAssembler.newCustomAssembler(dto.getClass(), entity.getClass(), synthesizer);

        final Map<String, Object> adapters = new HashMap<String, Object>();
        adapters.put("CatalogCodeMatcher", new CatalogCodeMatcher());

        assembler.assembleDto(dto, entity, adapters, new BeanFactory() {
            public Class getClazz(final String entityBeanKey) {
                if ("DtoCatalogCode".equals(entityBeanKey)) {
                    return TestDtoCatalogCodeClass.class;
                } else if ("DtoCode".equals(entityBeanKey)) {
                    return TestDtoCodeClass.class;
                } else if ("DtoCatalog".equals(entityBeanKey)) {
                    return TestDtoCatalogClass.class;
                }
                fail("Unknown DTO key: " + entityBeanKey);
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("DtoCatalogCode".equals(entityBeanKey)) {
                    return new TestDtoCatalogCodeClass();
                } else if ("DtoCode".equals(entityBeanKey)) {
                    return new TestDtoCodeClass();
                } else if ("DtoCatalog".equals(entityBeanKey)) {
                    return new TestDtoCatalogClass();
                }
                fail("Unknown DTO key: " + entityBeanKey);
                return null;
            }
        });

        assertEquals("ID-ABC", dto.getId());

        final TestDtoCodeClass dtoType = dto.getType();
        assertNotNull(dtoType);
        assertEquals("ID-123", dtoType.getId());
        assertEquals("CODE-AG1", dtoType.getCode());

        final Collection<TestDtoCodeClass> dtoCodes = dto.getCodes();
        assertNotNull(dtoCodes);
        assertFalse(dtoCodes.isEmpty());
        // The collection is actually an ArrayList, so we will cheat a little
        final List<TestDtoCodeClass> dtoCodesAsList = (List) dtoCodes;
        assertEquals(2, dtoCodesAsList.size());

        final TestDtoCodeClass dtoCode1 = dtoCodesAsList.get(0);
        assertNotNull(dtoCode1);
        assertEquals("ID-123", dtoCode1.getId());
        assertEquals("CODE-AG1", dtoCode1.getCode());

        final TestDtoCodeClass dtoCode2 = dtoCodesAsList.get(1);
        assertNotNull(dtoCode2);
        assertEquals("ID-235", dtoCode2.getId());
        assertEquals("CODE-DT1", dtoCode2.getCode());

        final TestEntityCatalog<TestEntityCatalogCode> entityCopy = new TestEntityCatalogClass<TestEntityCatalogCode>();

        assembler.assembleEntity(dto, entityCopy, adapters, new BeanFactory() {
            public Class getClazz(final String entityBeanKey) {
                if ("CatalogCode".equals(entityBeanKey)) {
                    return TestEntityCatalogCodeClass.class;
                } else if ("Catalog".equals(entityBeanKey)) {
                    return TestEntityCatalogClass.class;
                }
                fail("Unknown Entity key: " + entityBeanKey);
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("CatalogCode".equals(entityBeanKey)) {
                    return new TestEntityCatalogCodeClass();
                } else if ("Catalog".equals(entityBeanKey)) {
                    return new TestEntityCatalogClass();
                }
                fail("Unknown Entity key: " + entityBeanKey);
                return null;
            }
        });

        assertEquals("ID-ABC", entityCopy.getId());

        final TestEntityCatalogCode entityCopyType = entityCopy.getType();
        assertNotNull(entityCopyType);

        assertEquals("ID-123", entityCopyType.getId());
        assertEquals("CODE-AG1", entityCopyType.getCode());
        assertNull(entityCopyType.getSectionName()); // This property is not part of mapping to prevent recursion
        assertNull(entityCopyType.getCatalog()); // This property is not part of mapping to prevent recursion

        final Collection<TestEntityCatalogCode> entityCopyCodes = entityCopy.getCodes();
        assertNotNull(entityCopyCodes);
        assertFalse(entityCopyCodes.isEmpty());
        // The collection is actually an ArrayList, so we will cheat a little
        final List<TestEntityCatalogCode> entityCopyCodesAsList = (List) entityCopyCodes;
        assertEquals(2, entityCopyCodesAsList.size());

        final TestEntityCatalogCode entityCopyCode1 = entityCopyCodesAsList.get(0);
        assertEquals("ID-123", entityCopyCode1.getId());
        assertEquals("CODE-AG1", entityCopyCode1.getCode());
        assertNull(entityCopyCode1.getSectionName()); // This property is not part of mapping to prevent recursion
        assertNull(entityCopyCode1.getCatalog()); // This property is not part of mapping to prevent recursion

        final TestEntityCatalogCode entityCopyCode2 = entityCopyCodesAsList.get(1);
        assertEquals("ID-235", entityCopyCode2.getId());
        assertEquals("CODE-DT1", entityCopyCode2.getCode());
        assertNull(entityCopyCode2.getSectionName()); // This property is not part of mapping to prevent recursion
        assertNull(entityCopyCode2.getCatalog()); // This property is not part of mapping to prevent recursion

    }

    /**
	 * Test that assembler copes with virtual field mapping.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testVirtualFieldMapping() throws GeDAException {
		
		final TestDto20Class dto = new TestDto20Class();
		final TestEntity20Class entity = new TestEntity20Class();
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("VirtualMyBoolean", new VirtualMyBooleanConverter());
		converters.put("VirtualMyLong", new VirtualMyLongConverter());
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dto.getClass(), entity.getClass(), synthesizer);
		
		assembler.assembleDto(dto, entity, converters, null);
		
		assertTrue(dto.getMyBoolean());
		assertEquals(Long.valueOf(0L), dto.getMyLong());
		dto.setMyBoolean(true);
		dto.setMyLong(2L);
		assertFalse(entity.isDecided());
		
		assembler.assembleEntity(dto, entity, converters, null);
		
		assertTrue(entity.isDecided());
		assertEquals(2L, entity.getPk());
		
	}
	
	/**
	 * Test that assembler copes with virtual field mapping.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = AnnotationMissingBindingException.class)
	public void testVirtualFieldMappingNoConverter() throws GeDAException {
		
		final TestDto20ncClass dto = new TestDto20ncClass();
		final TestEntity20Class entity = new TestEntity20Class();
		
		DTOAssembler.newCustomAssembler(dto.getClass(), entity.getClass(), synthesizer);
		
	}


    /**
     * Test that two interfaces with one defining only getter and the other both getter and setter
     * create a valid single full descriptor.
     *
     * @throws GeDAException exception
     */
    @Test
    public void testComplexOverlappingInterfacesWithReadOverlap() throws Exception {

        final CustomerOrderDeliveryDetailDTO dto = new CustomerOrderDeliveryDetailDTOImpl();
        final CustomerOrderDeliveryDetWithReadOverlap entity = new CustomerOrderDeliveryDetWithReadOverlapEntity();

        entity.setQty(BigDecimal.TEN);

        final Assembler asm =
                DTOAssembler.newCustomAssembler(dto.getClass(), CustomerOrderDeliveryDetWithReadOverlap.class, synthesizer);

        asm.assembleDto(dto, entity, null, null);

        assertEquals(BigDecimal.TEN, dto.getQty());

    }

    /**
     * Test that two interfaces with one defining getter and the other setter create a valid
     * single full descriptor.
     *
     * @throws GeDAException exception
     */
    @Test
    public void testComplexOverlappingInterfacesWithSeparateGetSetOverlap() throws Exception {

        final CustomerOrderDeliveryDetailDTO dto = new CustomerOrderDeliveryDetailDTOImpl();
        final CustomerOrderDeliveryDetEnrichWithSet entity = new CustomerOrderDeliveryDetEnrichWithSetEntity();

        entity.setQty(BigDecimal.TEN);

        final Assembler asm =
                DTOAssembler.newCustomAssembler(dto.getClass(), CustomerOrderDeliveryDetEnrichWithSet.class, synthesizer);

        asm.assembleDto(dto, entity, null, null);

        assertEquals(BigDecimal.TEN, dto.getQty());

    }
}
