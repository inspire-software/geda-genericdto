
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

import org.junit.Test;

import dp.lib.dto.geda.adapter.BeanFactory;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerDtoFieldTest {

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
	 * Test that if mapping is specified with a readOnly property with a nesting, 
	 * then when write to entity is invoked the higher level object is not created via proxy. 
	 */
	@Test
	public void testNullObjectWithReadOnlyMappingAndLayeredNestingDoesNotRequireBeanFactory() {
		final TestDto4DelegatingReadOnlyClass dto = new TestDto4DelegatingReadOnlyClass();
		dto.setNestedString("ReadOnly");
		
		final TestEntity4Class entity = new TestEntity4Class(); // entity with nested string null.
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto4DelegatingReadOnlyClass.class, TestEntity4Class.class);
		
		assertNotNull(dto.getNestedString());
		assertNull(entity.getWrapper());
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertNull(entity.getWrapper());
		
	}
	
	/**
	 * Test that if mapping is specified without a readOnly property with a nesting, 
	 * then when write to entity is invoked the higher level object is created via proxy
	 * and exception is thrown if no beanFactory exists. 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullObjectWithoutReadOnlyMappingAndLayeredNestingDoesNotRequireBeanFactory() {
		final TestDto4DelegatingWritableClass dto = new TestDto4DelegatingWritableClass();
		dto.setNestedString("ReadOnly");
		
		final TestEntity4Class entity = new TestEntity4Class(); // entity with nested string null.
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto4DelegatingWritableClass.class, TestEntity4Class.class);

		assertNotNull(dto.getNestedString());
		assertNull(entity.getWrapper());
		
		assembler.assembleEntity(dto, entity, null, null);
		
	}
	
	/**
	 * Refer to Test*10* classes to see setup for a nested entityt property that is
	 * exposed as an interface that is a composite of serveral interfaces including
	 * generics. This test shows that such mapping is correctly handled and properties 
	 * are resolved.
	 */
	@Test
	public void testMultiInheritaceInInterfacesForNestedProperties() {
		final TestDto10Class dto = new TestDto10Class();
		
		final TestEntity10Class entity = new TestEntity10Class();
		final TestEntity10Interface subEntity = new TestEntity10SubClass();
		subEntity.setIm1("im1");
		subEntity.setIm2("im2");
		subEntity.setIm3("im3");
		entity.setNested(subEntity);
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto10Class.class, TestEntity10Class.class);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals("im1", dto.getIm1());
		assertEquals("im2", dto.getIm2());
		assertEquals("im3", dto.getIm3());
		
		dto.setIm1("dto1");
		dto.setIm2("dto2");
		dto.setIm3("dto3");
		
		assembler.assembleEntity(dto, entity, null, new BeanFactory() {
			public Object get(final String entityBeanKey) {
				return new TestEntity10SubClass();
			}
			
		});
		
		assertEquals("dto1", entity.getNested().getIm1());
		assertEquals("dto2", entity.getNested().getIm2());
		assertEquals("dto3", entity.getNested().getIm3());
		
		entity.setNested(null);
		
		assembler.assembleEntity(dto, entity, null, new BeanFactory() {
			public Object get(final String entityBeanKey) {
				return new TestEntity10SubClass();
			}
			
		});
		
		assertEquals("dto1", entity.getNested().getIm1());
		assertEquals("dto2", entity.getNested().getIm2());
		assertEquals("dto3", entity.getNested().getIm3());
		
		
	}
	
	/**
	 * Refer to Test*14* classes to see setup for a nested interface inheritance 
	 * in entity.
	 */
	@Test
	public void testVerticalMultiInheritaceInInterfaces() {
		final TestDto14IfaceDescriptable dto = new TestDto14Class();
		
		final TestEntity14IfaceDescriptable entity = new TestEntity14Class();
		entity.setName("name");
		entity.setDesc("desc");
	
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(dto.getClass(), TestEntity14IfaceDescriptable.class);
		
		assembler.assembleDto(dto, entity, null, null);
		
		assertEquals("name", dto.getName());
		assertEquals("desc", dto.getDesc());
		
		dto.setName("Name DTO");
		dto.setDesc("Desc DTO");
		
		assembler.assembleEntity(dto, entity, null, null);
		
		assertEquals("Name DTO", entity.getName());
		assertEquals("Desc DTO", entity.getDesc());

	
	}
	
}
