
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.EntityRetriever;
import dp.lib.dto.geda.exception.GeDAException;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerDtoParentTest {

	private static final long L_3 = 3L;
	
	/**
	 * Test that when dto field is additionally annotated with dto parent when writing
	 * back data from dto to entity the assembler does not copy the values but uses
	 * {@link dp.lib.dto.geda.adapter.EntityRetriever} to delegate the setting of value.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoParentAnnotation() throws GeDAException {
		
		final TestEntity11ParentInterface parentEntity = new TestEntity11ParentClass();
		final String parentName = "parent with id 3";
		parentEntity.setEntityId(L_3);
		parentEntity.setName(parentName);
		
		final TestEntity11ChildInterface childEntity = new TestEntity11ChildClass();
		final String childName = "child of parent with id 3";
		childEntity.setParent(parentEntity);
		childEntity.setName(childName);
		
		final TestDto11ChildInterface childDto = new TestDto11ChildClass();
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(childDto.getClass(), childEntity.getClass());
		
		assembler.assembleDto(childDto, childEntity, null, createDtoBeanFactory());
		
		assertEquals(childName, childDto.getName());
		assertNotNull(childDto.getParent());
		assertEquals(parentName, childDto.getParent().getName());
		assertEquals(Long.valueOf(L_3), Long.valueOf(childDto.getParent().getEntityId()));
		
		final TestEntity11ParentInterface parentEntity2 = new TestEntity11ParentClass();
		final String parentName2 = "parent with id 0";
		parentEntity2.setEntityId(0);
		parentEntity2.setName(parentName2);
		
		// change dto parent.
		childDto.getParent().setEntityId(0L);
		childDto.setName("child with changed parent");
		
		final Map<String, Object> conv = createMapWithEntityRetriever(parentEntity2, Long.valueOf(0));
		
		assembler.assembleEntity(childDto, childEntity, conv, createEntityBeanFactory());
		
		assertNotNull(childEntity.getParent());
		assertSame(parentEntity2, childEntity.getParent());
		assertEquals("child with changed parent", childEntity.getName());
		
	}

	private BeanFactory createEntityBeanFactory() {
		return new BeanFactory() {

			public Object get(final String entityBeanKey) {
				if (entityBeanKey.equals("dp.lib.dto.geda.assembler.TestEntity11ParentClass")) {
					return new TestEntity11ParentClass();
				}
				return null;
			}
			
		};
	}

	private BeanFactory createDtoBeanFactory() {
		return new BeanFactory() {

			public Object get(final String entityBeanKey) {
				if (entityBeanKey.equals("dp.lib.dto.geda.assembler.TestDto11ParentClass")) {
					return new TestDto11ParentClass();
				}
				return null;
			}
			
		};
	}

	private Map<String, Object> createMapWithEntityRetriever(final TestEntity11ParentInterface parentEntity2, final Object idExpectations) {
		final EntityRetriever retriever = new EntityRetriever() {
			
			@SuppressWarnings("unchecked")
			public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
				assertEquals(idExpectations, primaryKey);
				assertTrue(entityInterface.equals(TestEntity11ParentInterface.class));
				assertTrue(entityClass.equals(TestEntity11ParentClass.class));
				return parentEntity2;
			}
			
		};
		
		final Map<String, Object> conv = new HashMap<String, Object>();
		conv.put("retriever", retriever);
		return conv;
	}

	/**
	 * Test to make sure that if dto parent field is null it is set to null in entity.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testDtoParentIsSetToNullIfDtoDataIsNull() throws GeDAException {
		
		final TestEntity11ParentInterface parentEntity = new TestEntity11ParentClass();
		final String parentName = "parent with id 3";
		parentEntity.setEntityId(L_3);
		parentEntity.setName(parentName);
		
		final TestEntity11ChildInterface childEntity = new TestEntity11ChildClass();
		final String childName = "child of parent with id 3";
		childEntity.setParent(parentEntity);
		childEntity.setName(childName);
		
		final TestDto11ChildInterface childDto = new TestDto11ChildClass();
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(childDto.getClass(), childEntity.getClass());
		
		assembler.assembleEntity(childDto, childEntity, null, null);
		
		assertNotNull(childEntity);
		assertNull(childEntity.getParent());
		
	}
	
}
