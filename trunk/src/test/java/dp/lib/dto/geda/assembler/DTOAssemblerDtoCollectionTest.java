
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;

import dp.lib.dto.geda.adapter.BeanFactory;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerDtoCollectionTest {

	private static final int I_3 = 3;

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
	 * Test to check nested collections mapping works.
	 */
	@Test
	public void testNestedCollectionMapping() {
		
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12CollectionIterface dColl = new TestDto12CollectionClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dColl.getClass(), eWrap.getClass());
		
		assembler.assembleDto(dColl, eWrap, null, new BeanFactory() {

			public Object get(String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dColl.getItems());
		assertEquals(2, dColl.getItems().size());
		Iterator<TestDto12CollectionItemIterface> iter;
		
		iter = dColl.getItems().iterator();
		final TestDto12CollectionItemIterface dto1 = iter.next();
		final TestDto12CollectionItemIterface dto2 = iter.next();
		assertEquals("itm1", dto1.getName());
		assertEquals("itm2", dto2.getName());
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dColl.getItems().add(dto3);
		
		iter = dColl.getItems().iterator();
		iter.next();
		iter.remove(); // first
		
		assembler.assembleEntity(dColl, eWrap, null, new BeanFactory() {
			
			public Object get(String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
		Iterator<TestEntity12CollectionItemInterface> eiter;
		
		eiter = eWrap.getCollectionWrapper().getItems().iterator();
		final TestEntity12CollectionItemInterface itm1 = eiter.next();
		final TestEntity12CollectionItemInterface itm2 = eiter.next();
		assertEquals("itm2", itm1.getName());
		assertEquals("itm3", itm2.getName());
		
	}
	
    /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionToNestedNullWrite() {
		final TestDto7CollectionInterface dto = new TestDto7NestedCollectionClass();
		final TestEntity7CollectionWrapperInterface entity = new TestEntity7CollectionWrapperClass();


        final BeanFactory factory = new BeanFactory() {
            public Object get(final String entityBeanKey) {
                if ("dp.lib.dto.geda.assembler.TestEntity7CollectionSubClass".equals(entityBeanKey)) {
                    return new TestEntity7iCollectionSubClass();
                } else if ("wrapper".equals(entityBeanKey)) {
                	return new TestEntity7iCollectionClass();
                }
                return null;
            }
        };

		final DTOAssembler assembler =
			DTOAssembler.newAssembler(dto.getClass(), entity.getClass());

		dto.setNestedString(new ArrayList<TestDto7CollectionSubInterface>());
		
		final TestDto7CollectionSubInterface item1 = new TestDto7CollectionSubClass();
		item1.setName("itm1");
		final TestDto7CollectionSubInterface item2 = new TestDto7CollectionSubClass();
		item2.setName("itm2");
		final TestDto7CollectionSubInterface item3 = new TestDto7CollectionSubClass();
		item3.setName("itm3");

		dto.getNestedString().add(item1);
		dto.getNestedString().add(item2);
		dto.getNestedString().add(item3);

		assembler.assembleEntity(dto, entity, null, factory);

		assertNotNull(entity.getWrapper());
		assertNotNull(entity.getWrapper().getCollection());
		assertEquals(I_3, entity.getWrapper().getCollection().size());
		
		boolean item1b = false;
		boolean item2b = false;
		boolean item3b = false;
		for(TestEntity7CollectionSubInterface item : entity.getWrapper().getCollection()) {
			if ("itm1".equals(item.getName())) {
				item1b = true;
			} else if ("itm2".equals(item.getName())) {
				item2b = true;
			} else if ("itm3".equals(item.getName())) {
				item3b = true;
			} else {
				fail("Unkown item");
			}
		}
		assertTrue(item1b && item2b && item3b);

	}
	
}
