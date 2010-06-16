
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerBatchTest {

	/**
	 * Test that AssembleDtos throws {@link IllegalArgumentException} if dtos is null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssembleDtosThrowsExceptionForNullDtoCollection() {
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleDtos(null, new ArrayList<TestEntity1Class>(), null, null);
		
	}
	
	/**
	 * Test that AssembleDtos throws {@link IllegalArgumentException} if dtos is not empty
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssembleDtosThrowsExceptionForNonEmptyDtoCollection() {
		
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>();
		dtos.add(createTestDto1(0));
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleDtos(dtos, new ArrayList<TestEntity1Class>(), null, null);
		
	}
	
	/**
	 * Test that AssembleDtos throws {@link IllegalArgumentException} if entities is null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssembleDtosThrowsExceptionForNullEntitiesCollection() {
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleDtos(new ArrayList<TestDto1Class>(), null, null, null);
		
	}
	
	/**
	 * Test that AssembleDtos assembles dtos.
	 */
	@Test
	public void testAssembleDtos() {
		
		final int totalEntities = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>(); 
		final List<TestEntity1Class> entities = new ArrayList<TestEntity1Class>(); 
		
		for (int index = 0; index < totalEntities; index++) {
			entities.add(createTestEntity1(index));
		}
		
		final long timeStart = System.currentTimeMillis();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleDtos(dtos, entities, null, null);
		
		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalEntities 
				+ " simple one level dto's in about " + deltaTime + " millis");
		
		assertEquals(totalEntities, dtos.size());
		for (int index = 0; index < totalEntities; index++) {
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).getNumber());
			assertEquals(dtos.get(index).getMyLong(), entities.get(index).getEntityId());
			assertEquals(dtos.get(index).getMyString(), entities.get(index).getName());
		}
		
	}
	
	/**
	 * Test that AssembleEntities throws {@link IllegalArgumentException} if dto is null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssembleEntitiesThrowsExceptionForNullDtoCollection() {
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleEntities(null, new ArrayList<TestEntity1Class>(), null, null);
		
	}
	
	/**
	 * Test that AssembleEntitie throws {@link IllegalArgumentException} if entities is null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssembleEntitieThrowsExceptionForNullEntitiesCollection() {
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleEntities(new ArrayList<TestDto1Class>(), null, null, null);
		
	}
	
	/**
	 * Test that AssembleEntities throws {@link IllegalArgumentException} if entities is not empty
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAssembleEntitiesThrowsExceptionForNonEmptyEntityCollection() {
		
		final List<TestEntity1Class> entities = new ArrayList<TestEntity1Class>();
		entities.add(createTestEntity1(0));
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleEntities(new ArrayList<TestDto1Class>(), entities, null, null);
		
	}
	
	/**
	 * Test that AssembleEntities assembles entities.
	 */
	@Test
	public void testAssembleEntities() {
		
		final int totalDtos = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>(); 
		final List<TestEntity1Class> entities = new ArrayList<TestEntity1Class>(); 
		
		for (int index = 0; index < totalDtos; index++) {
			dtos.add(createTestDto1(index));
		}
		
		final long timeStart = System.currentTimeMillis();
		
		final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class, TestEntity1Class.class);
		assembler.assembleEntities(dtos, entities, null, null);
		
		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalDtos 
				+ " simple one level entities in about " + deltaTime + " millis");
		
		assertEquals(totalDtos, entities.size());
		for (int index = 0; index < totalDtos; index++) {
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).getNumber());
			assertEquals(dtos.get(index).getMyLong(), entities.get(index).getEntityId());
			assertEquals(dtos.get(index).getMyString(), entities.get(index).getName());
		}
		
	}
	
	private TestEntity1Class createTestEntity1(final int counter) {
		final TestEntity1Class entity = new TestEntity1Class();
		entity.setEntityId(1L + counter);
		entity.setName("John Doe" + counter);
		entity.setNumber(2.0d + counter);
		return entity;
	}
	
	
	private TestDto1Class createTestDto1(final int counter) {
		final TestDto1Class entity = new TestDto1Class();
		entity.setMyLong(1L + counter);
		entity.setMyString("John Doe" + counter);
		entity.setMyDouble(2.0d + counter);
		return entity;
	}
	
	
}
