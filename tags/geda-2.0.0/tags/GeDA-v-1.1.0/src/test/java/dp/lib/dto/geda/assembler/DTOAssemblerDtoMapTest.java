
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import dp.lib.dto.geda.adapter.BeanFactory;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerDtoMapTest {

	private static final int I_3 = 3;

	/**
	 * Test that DTO map correctly maps to entity collection.
	 */
	@Test
	public void testMapToCollectionMapping() {
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
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eWrap.getClass());
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		assembler.assembleEntity(dMap, eWrap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
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
	 * Test that DTO map correctly maps to entity collection.
	 */
	@Test
	public void testMapToMapValueMapping() {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12MapInterface eMap = new TestEntity12MapClass();
		eMap.setItems(new HashMap<String, TestEntity12CollectionItemInterface>());
		eMap.getItems().put("itm1", eItem1);
		eMap.getItems().put("itm2", eItem2);
		
		final TestDto12MapIterface dMap = new TestDto12MapToMapClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eMap.getClass());
		
		assembler.assembleDto(dMap, eMap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		assembler.assembleEntity(dMap, eMap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
				if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eMap.getItems());
		assertEquals(2, eMap.getItems().size());
		
		final Set<String> ekeys = eMap.getItems().keySet();
		for (String key : ekeys) {
			if ("itm2".equals(key)) {
				assertEquals("itm2", eMap.getItems().get(key).getName());
			} else if ("itm3".equals(key)) {
				assertEquals("itm3", eMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}	
		
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 */
	@Test
	public void testMapToMapKeyMapping() {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12MapByKeyInterface eMap = new TestEntity12MapByKeyClass();
		eMap.setItems(new HashMap<TestEntity12CollectionItemInterface, String>());
		eMap.getItems().put(eItem1, "itm1");
		eMap.getItems().put(eItem2, "itm2");
		
		final TestDto12MapByKeyIterface dMap = new TestDto12MapToMapByKeyClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eMap.getClass());
		
		assembler.assembleDto(dMap, eMap, null, new BeanFactory() {

			public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<TestDto12CollectionItemIterface> keys = dMap.getItems().keySet();
		TestDto12CollectionItemIterface dItem1 = null;
		TestDto12CollectionItemIterface dItem2 = null;
		for (TestDto12CollectionItemIterface key : keys) {
			if ("itm1".equals(key.getName())) {
				assertEquals("itm1", dMap.getItems().get(key));
				dItem1 = key;
			} else if ("itm2".equals(key.getName())) {
				assertEquals("itm2", dMap.getItems().get(key));
				dItem2 = key;
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put(dto3, "itm3");
		
		dMap.getItems().put(dItem2, "itm no 2");
		
		dMap.getItems().remove(dItem1); // first

		assembler.assembleEntity(dMap, eMap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
				if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eMap.getItems());
		assertEquals(2, eMap.getItems().size());
		
		final Set<TestEntity12CollectionItemInterface> ekeys = eMap.getItems().keySet();
		for (TestEntity12CollectionItemInterface key : ekeys) {
			if ("itm2".equals(key.getName())) {
				assertEquals("itm no 2", eMap.getItems().get(key));
			} else if ("itm3".equals(key.getName())) {
				assertEquals("itm3", eMap.getItems().get(key));
			} else {
				fail("Unknown key");
			}
		}	

	}
	
	/**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionEntityToNullProperty() {
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
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eWrap.getClass());
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		
		dMap.setItems(null);
		
		assembler.assembleEntity(dMap, eWrap, null, null);

		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(0, eWrap.getCollectionWrapper().getItems().size());
	}
	
	/**
	 * Test collection of nested objects.
	 */
	@Test
	public void testMapEntityToNullProperty() {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12MapInterface eMap = new TestEntity12MapClass();
		eMap.setItems(new HashMap<String, TestEntity12CollectionItemInterface>());
		eMap.getItems().put("itm1", eItem1);
		eMap.getItems().put("itm2", eItem2);
		
		final TestDto12MapIterface dMap = new TestDto12MapToMapClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eMap.getClass());
		
		assembler.assembleDto(dMap, eMap, null, new BeanFactory() {

			public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		
		dMap.setItems(null);
		
		assembler.assembleEntity(dMap, eMap, null, null);

		assertNotNull(eMap.getItems());
		assertEquals(0, eMap.getItems().size());
	}
	
	/**
	 * Test collection of nested objects.
	 */
	@Test
	public void testCollectionNullToNullProperty() {
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eWrap.getClass());
		
		assembler.assembleDto(dMap, eWrap, null, null);
		
		assertNull(dMap.getItems());
		
		assembler.assembleEntity(dMap, eWrap, null, null);
		
		assertNull(eWrap.getCollectionWrapper().getItems());
		
	}
	
    /**
	 * Test collection of nested objects.
	 */
	@Test
	public void testMapNullToNullProperty() {
		
		final TestEntity12MapInterface eMap = new TestEntity12MapClass();
		
		final TestDto12MapIterface dMap = new TestDto12MapToMapClass();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eMap.getClass());
		
		assembler.assembleDto(dMap, eMap, null, null);

		assertNull(dMap.getItems());

		assembler.assembleEntity(dMap, eMap, null, null);

		assertNull(eMap.getItems());

	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 */
	@Test
	public void testMapToCollectionMappingNestedCreate() {

		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		dMap.setItems(new HashMap<String, TestDto12CollectionItemIterface>());
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eWrap.getClass());
				
		final TestDto12CollectionItemClass dto1 = new TestDto12CollectionItemClass();
		dto1.setName("itm1");
		final TestDto12CollectionItemClass dto2 = new TestDto12CollectionItemClass();
		dto2.setName("itm2");
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		
		dMap.getItems().put("itm1", dto1);
		dMap.getItems().put("itm2", dto2);
		dMap.getItems().put("itm3", dto3);
		
		assembler.assembleEntity(dMap, eWrap, null, new BeanFactory() {
			
			public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestEntity12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(I_3, eWrap.getCollectionWrapper().getItems().size());
		
		boolean item1 = false;
		boolean item2 = false;
		boolean item3 = false;
		for (TestEntity12CollectionItemInterface item : eWrap.getCollectionWrapper().getItems()) {
			if ("itm1".equals(item.getName())) {
				item1 = true;
			} else if ("itm2".equals(item.getName())) {
				item2 = true;
			} else if ("itm3".equals(item.getName())) {
				item3 = true;
			} else {
				fail("Unkown item");
			}
		}
		assertTrue(item1 && item2 && item3);
	}
	
}
