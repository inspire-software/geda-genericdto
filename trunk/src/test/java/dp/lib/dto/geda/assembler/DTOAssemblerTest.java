
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.assembler.TestDto3Class.Decision;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerTest {

	private static final int I_3 = 3;

	/**
	 * Test that correctly mapped classes for Entity and Dto get assembled as expected.
	 */
	@Test
	public void test1stClassesMapping() {
		
		final TestDto1Class dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
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
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto2Class.class, TestEntity2Class.class);
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
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto1Class.class, TestEntity2Class.class);
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
		
		assembler.assembleDto(dto1, entity1, null, null);
		
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
		
		assembler.assembleDto(dto2, entity2, null, null);
		
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

		assembler.assembleDto(dto2, entity1, null, null);

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
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto3Class.class, TestEntity3Class.class);
		
		assembler.assembleDto(dto, entity, converters, null);
		
		assertEquals(Decision.Decided, dto.getMyEnum());
		
		dto.setMyEnum(Decision.Undecided);
		
		assembler.assembleEntity(dto, entity, converters, null);
			
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
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto4Class.class, TestEntity4Class.class);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals(entity.getWrapper().getName(), dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertEquals("Another Name", entity.getWrapper().getName());
		
	}

    /**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 */
	@Test
	public void testWrappedNullDtoProperty() {
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
			DTOAssembler.newAssembler(TestDto4ComplexClass.class, TestEntity4Class.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertEquals(entity.getWrapper().getName(), dto.getNestedString().getNestedName());

		dto.getNestedString().setNestedName("Another Name");

		assembler.assembleEntity(dto, entity, null, null);

		assertEquals("Another Name", entity.getWrapper().getName());

	}

    /**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 */
	@Test
	public void testWrappedNullEntityProperty() {
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
			DTOAssembler.newAssembler(TestDto4ComplexClass.class, TestEntity4Class.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertNull(dto.getNestedString());

        dto.setNestedString(new TestDto4ComplexSubClass());
		dto.getNestedString().setNestedName("Another Name");

		assembler.assembleEntity(dto, entity, null, factory);

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
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto5Class.class, TestEntity5Class.class);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals(entity.getWrapper().getWrapper().getName(), dto.getNestedString());
		
		dto.setNestedString("Another Name");
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertEquals("Another Name", entity.getWrapper().getWrapper().getName());
		
	}

	/**
	 * Test that wrapper (nested) dto property mapping get resolved correctly.
	 * Test shows that second level entity is created on the fly.
	 */
	@Test
	public void testDeepWrappedNullProperty() {
		final TestDto5Class dto = new TestDto5Class();
		final TestEntity5Class entity = new TestEntity5Class();
		entity.setWrapper(new TestEntity4Class());
		entity.getWrapper().setWrapper(null);
		final BeanFactory beanFactory = new TestBeanFactory();
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto5Class.class, TestEntity5Class.class);
		
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
	 */
	@Test
	public void testDeepWrappedDoubleNullProperty() {
		final TestDto5Class dto = new TestDto5Class();
		final TestEntity5Class entity = new TestEntity5Class();
		entity.setWrapper(null);
		final BeanFactory beanFactory = new TestBeanFactory();
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto5Class.class, TestEntity5Class.class);

		assembler.assembleDto(dto, entity, null, null);
		
		assertNull(dto.getNestedString());
		
		dto.setNestedString("Another Deep Name");
		
		assembler.assembleEntity(dto, entity, null, beanFactory);

		assertEquals("Another Deep Name", entity.getWrapper().getWrapper().getName());

	}
	
	/**
	 * Test that read only field get copied to Dto but not back to entity.
	 */
	@Test
	public void testReadOnlyClassesMapping() {
		
		final TestDto6Class dto = new TestDto6Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto6Class.class, TestEntity1Class.class);
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
	 */
	@Test
	public void testDtoAsInterfaceMapping() {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(dto.getClass(), TestEntity1Class.class);
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
	 */
	@Test
	public void testBothAsInterfaceMapping() {
		
		final TestDto1Interface dto = new TestDto1Class();
		final TestEntity1Interface entity = createTestEntity1();
		
		final DTOAssembler assembler = 
			DTOAssembler.newAssembler(dto.getClass(), TestEntity1Interface.class);
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
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionProperty() {
		final TestDto7CollectionClass dto = new TestDto7CollectionClass();
		final TestEntity7CollectionClass entity = new TestEntity7CollectionClass();
        entity.setCollection(new HashSet<TestEntity7CollectionSubClass>());

        final TestEntity7CollectionSubClass item1 = new TestEntity7CollectionSubClass();
        item1.setName("1");
        final TestEntity7CollectionSubClass item2 = new TestEntity7CollectionSubClass();
        item2.setName("2");
        final TestEntity7CollectionSubClass item3 = new TestEntity7CollectionSubClass();
        item3.setName("3");
        entity.getCollection().add(item1);
        entity.getCollection().add(item2);
        entity.getCollection().add(item3);

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestDto7CollectionSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7CollectionSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto7CollectionClass.class, TestEntity7CollectionClass.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertNotNull(dto.getNestedString());
        assertEquals(I_3, dto.getNestedString().size());

        Iterator<TestDto7CollectionSubClass> it = dto.getNestedString().iterator();
        for (int index = 0; it.hasNext(); index++) {
            it.next().setName("sameName" + index);
        }

		assembler.assembleEntity(dto, entity, null, factory);

		assertNotNull(entity.getCollection());
		assertEquals(I_3, entity.getCollection().size());

        Iterator<TestEntity7CollectionSubClass> itr = entity.getCollection().iterator();
        while (itr.hasNext()) {
            final TestEntity7CollectionSubClass next = itr.next();

            assertNotNull(next.getName());
            assertTrue(next.getName().startsWith("sameName"));
        }

	}

    /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionEntityToNullProperty() {
		final TestDto7CollectionClass dto = new TestDto7CollectionClass();
		final TestEntity7CollectionClass entity = new TestEntity7CollectionClass();
        entity.setCollection(new HashSet<TestEntity7CollectionSubClass>());

        final TestEntity7CollectionSubClass item1 = new TestEntity7CollectionSubClass();
        item1.setName("1");
        final TestEntity7CollectionSubClass item2 = new TestEntity7CollectionSubClass();
        item2.setName("2");
        final TestEntity7CollectionSubClass item3 = new TestEntity7CollectionSubClass();
        item3.setName("3");
        entity.getCollection().add(item1);
        entity.getCollection().add(item2);
        entity.getCollection().add(item3);

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestDto7CollectionSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7CollectionSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto7CollectionClass.class, TestEntity7CollectionClass.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertNotNull(dto.getNestedString());
        assertEquals(I_3, dto.getNestedString().size());

        dto.setNestedString(null);

		assembler.assembleEntity(dto, entity, null, factory);

		assertNotNull(entity.getCollection());
		assertEquals(0, entity.getCollection().size());

	}
	

    /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionNullToNullProperty() {
		final TestDto7CollectionClass dto = new TestDto7CollectionClass();
		final TestEntity7CollectionClass entity = new TestEntity7CollectionClass();
        entity.setCollection(null);

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestDto7CollectionSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7CollectionSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto7CollectionClass.class, TestEntity7CollectionClass.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertNull(dto.getNestedString());

		assembler.assembleEntity(dto, entity, null, factory);

		assertNull(entity.getCollection());

	}

        /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionPropertyWithInterfaces() {
		final TestDto7CollectionInterface dto = new TestDto7iCollectionClass();
		final TestEntity7CollectionInterface entity = new TestEntity7iCollectionClass();
        entity.setCollection(new HashSet<TestEntity7CollectionSubInterface>());

        final TestEntity7CollectionSubInterface item1 = new TestEntity7iCollectionSubClass();
        item1.setName("1");
        final TestEntity7CollectionSubInterface item2 = new TestEntity7iCollectionSubClass();
        item2.setName("2");
        final TestEntity7CollectionSubInterface item3 = new TestEntity7iCollectionSubClass();
        item3.setName("3");
        entity.getCollection().add(item1);
        entity.getCollection().add(item2);
        entity.getCollection().add(item3);

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto7iCollectionSubClass".equals(entityBeanKey)) {
                    return new TestDto7iCollectionSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity7iCollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7iCollectionSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(dto.getClass(), TestEntity7CollectionInterface.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertNotNull(dto.getNestedString());
        assertEquals(I_3, dto.getNestedString().size());

        Iterator<TestDto7CollectionSubInterface> it = dto.getNestedString().iterator();
        for (int index = 0; it.hasNext(); index++) {
            it.next().setName("sameName" + index);
        }

		assembler.assembleEntity(dto, entity, null, factory);

		assertNotNull(entity.getCollection());
		assertEquals(I_3, entity.getCollection().size());

        Iterator<TestEntity7CollectionSubInterface> itr = entity.getCollection().iterator();
        while (itr.hasNext()) {
            final TestEntity7CollectionSubInterface next = itr.next();

            assertNotNull(next.getName());
            assertTrue(next.getName().startsWith("sameName"));
        }

	}

    /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionEntityToNullPropertyWithInterfaces() {
		final TestDto7CollectionInterface dto = new TestDto7iCollectionClass();
		final TestEntity7CollectionInterface entity = new TestEntity7iCollectionClass();
        entity.setCollection(new HashSet<TestEntity7CollectionSubInterface>());

        final TestEntity7CollectionSubInterface item1 = new TestEntity7iCollectionSubClass();
        item1.setName("1");
        final TestEntity7CollectionSubInterface item2 = new TestEntity7iCollectionSubClass();
        item2.setName("2");
        final TestEntity7CollectionSubInterface item3 = new TestEntity7iCollectionSubClass();
        item3.setName("3");
        entity.getCollection().add(item1);
        entity.getCollection().add(item2);
        entity.getCollection().add(item3);

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto7iCollectionSubClass".equals(entityBeanKey)) {
                    return new TestDto7iCollectionSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity7iCollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7iCollectionSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(dto.getClass(), TestEntity7CollectionInterface.class);

		assembler.assembleDto(dto, entity, null, factory);

				assertNotNull(dto.getNestedString());
        assertEquals(I_3, dto.getNestedString().size());

        dto.setNestedString(null);

		assembler.assembleEntity(dto, entity, null, factory);

		assertNotNull(entity.getCollection());
		assertEquals(0, entity.getCollection().size());

	}

        /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionNullToNullPropertyWithInterfaces() {
		final TestDto7CollectionInterface dto = new TestDto7iCollectionClass();
		final TestEntity7CollectionInterface entity = new TestEntity7iCollectionClass();
        entity.setCollection(null);

        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestDto7iCollectionSubClass".equals(entityBeanKey)) {
                    return new TestDto7iCollectionSubClass();
                } else if ("dp.lib.dto.geda.assembler.TestEntity7iCollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7iCollectionSubClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(dto.getClass(), TestEntity7CollectionInterface.class);

		assembler.assembleDto(dto, entity, null, factory);

		assertNull(dto.getNestedString());

		assembler.assembleEntity(dto, entity, null, factory);

		assertNull(entity.getCollection());

	}
	
	/**
	 * Test that names are extracted from field name if binding is not specified.
	 */
	@Test
	public void testAutowireNames() {
		
		final String name = "testName";
		final String name2 = "testAnotherName";
		
		final TestDto8AutowireNameClass dto = new TestDto8AutowireNameClass();
		final TestEntity8AutowireNameClass entity = new TestEntity8AutowireNameClass();
		entity.setName(name);
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto8AutowireNameClass.class, TestEntity8AutowireNameClass.class);

		assembler.assembleDto(dto, entity, null, null);

		assertNotNull(dto.getName());
		assertEquals(name, dto.getName());
		
		dto.setName(name2);

		assembler.assembleEntity(dto, entity, null, null);

		assertNotNull(entity.getName());
		assertEquals(name2, entity.getName());

		
	}
	
	/**
	 * Test the inheritance of DtoField does not break data pipes.
	 */
	@Test
	public void testInheritanceOfDtoFields() {
		
		final String name = "name";
		final String nameChild = "nameChild";
		final String name2 = "name2";
		final String nameChild2 = "nameChild2";
		
		final TestDto9InheritanceChildClass dto = new TestDto9InheritanceChildClass();
		final TestEntity9InheritanceClass entity = new TestEntity9InheritanceClass();
		entity.setName(name);
		entity.setNameChild(nameChild);
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto9InheritanceChildClass.class, TestEntity9InheritanceClass.class);

		assembler.assembleDto(dto, entity, null, null);

		assertNotNull(dto.getName());
		assertEquals(name, dto.getName());
		assertNotNull(dto.getNameChild());
		assertEquals(nameChild, dto.getNameChild());
		
		dto.setName(name2);
		dto.setNameChild(nameChild2);

		assembler.assembleEntity(dto, entity, null, null);

		assertNotNull(entity.getName());
		assertEquals(name2, entity.getName());
		assertNotNull(entity.getNameChild());
		assertEquals(nameChild2, entity.getNameChild());
		
	}
	
	/**
	 * Test to ensure null property of DTO correctly maps on Entity.
	 */
	@Test
	public void testNestedNullPropertyOnEntity() {
		
		final TestDto4ComplexClass dto = new TestDto4ComplexClass();
		final TestEntity4Class entity = new TestEntity4Class();
		entity.setWrapper(new TestEntity4SubClass());
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto4ComplexClass.class, TestEntity4Class.class);

		assertNull(dto.getNestedString());
		assertNotNull(entity.getWrapper());
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertNull(entity.getWrapper());
		
	}

	
}
