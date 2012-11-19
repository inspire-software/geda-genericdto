
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.TestDto1Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.autowire.TestEntity1Class;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.exception.InvalidDtoCollectionException;
import com.inspiresoftware.lib.dto.geda.exception.InvalidEntityCollectionException;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@RunWith(value = ParameterizedSynthesizer.class)
public class DTOAssemblerBatchTest {
	
	private String synthesizer;
	
	/**
	 * @param synthesizer parameter
	 */
	public DTOAssemblerBatchTest(final String synthesizer) {
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
	 * Test that AssembleDtos throws {@link Exception} if dtos is null.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InvalidDtoCollectionException.class)
	public void testAssembleDtosThrowsExceptionForNullDtoCollection() throws GeDAException {
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleDtos(null, new ArrayList<TestEntity1Class>(), null, null);
		
	}
	
	/**
	 * Test that AssembleDtos throws {@link Exception} if dtos is not empty.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InvalidDtoCollectionException.class)
	public void testAssembleDtosThrowsExceptionForNonEmptyDtoCollection() throws GeDAException {
		
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>();
		dtos.add(createTestDto1(0));
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleDtos(dtos, new ArrayList<TestEntity1Class>(), null, null);
		
	}
	
	/**
	 * Test that AssembleDtos throws {@link Exception} if entities is null.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InvalidDtoCollectionException.class)
	public void testAssembleDtosThrowsExceptionForNullEntitiesCollection() throws GeDAException {
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleDtos(new ArrayList<TestDto1Class>(), null, null, null);
		
	}
	
	/**
	 * Test that AssembleDtos assembles dtos.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testAssembleDtos() throws GeDAException {
		
		final int totalEntities = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>(); 
		final List<TestEntity1Class> entities = new ArrayList<TestEntity1Class>(); 
		
		for (int index = 0; index < totalEntities; index++) {
			entities.add(createTestEntity1(index));
		}
		
		final long timeStart = System.currentTimeMillis();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleDtos(dtos, entities, null, null);
		
		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalEntities 
				+ " simple one level dto's from entities in about " + deltaTime + " millis");
		
		assertEquals(totalEntities, dtos.size());
		for (int index = 0; index < totalEntities; index++) {
            assertNotNull(entities.get(index).getNumber());
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).getNumber());
            assertNotNull(entities.get(index).getEntityId());
			assertEquals(dtos.get(index).getMyLong(), entities.get(index).getEntityId());
            assertNotNull(entities.get(index).getName());
			assertEquals(dtos.get(index).getMyString(), entities.get(index).getName());
		}
		
	}

	/**
	 * Test that AssembleDtos assembles dtos.
	 *
	 * @throws GeDAException exception
	 */
	@Test
	public void testAssembleDtosAsMaps() throws GeDAException {

		final int totalEntities = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>();
		final List<Map> entities = new ArrayList<Map>();

		for (int index = 0; index < totalEntities; index++) {
			entities.add(createTestEntity1Map(index));
		}

		final long timeStart = System.currentTimeMillis();

		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, Map.class, synthesizer);
		assembler.assembleDtos(dtos, entities, null, null);

		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalEntities
				+ " simple one level dto's from map in about " + deltaTime + " millis");

		assertEquals(totalEntities, dtos.size());
		for (int index = 0; index < totalEntities; index++) {
            assertNotNull(entities.get(index).get("myDouble"));
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).get("myDouble"));
            assertNotNull(entities.get(index).get("myLong"));
            assertEquals(dtos.get(index).getMyLong(), entities.get(index).get("myLong"));
            assertNotNull(entities.get(index).get("myString"));
            assertEquals(dtos.get(index).getMyString(), entities.get(index).get("myString"));
		}

	}

	/**
	 * Test that AssembleDtos assembles dtos.
	 *
	 * @throws GeDAException exception
	 */
	@Test
	public void testAssembleDtosAsLists() throws GeDAException {

		final int totalEntities = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>();
		final List<List> entities = new ArrayList<List>();

		for (int index = 0; index < totalEntities; index++) {
			entities.add(createTestEntity1List(index));
		}

		final long timeStart = System.currentTimeMillis();

		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, List.class, synthesizer);
		assembler.assembleDtos(dtos, entities, null, null);

		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalEntities
				+ " simple one level dto's from list in about " + deltaTime + " millis");

		assertEquals(totalEntities, dtos.size());
		for (int index = 0; index < totalEntities; index++) {
            assertNotNull(entities.get(index).get(5));
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).get(5));
            assertNotNull(entities.get(index).get(1));
            assertEquals(dtos.get(index).getMyLong(), entities.get(index).get(1));
            assertNotNull(entities.get(index).get(3));
            assertEquals(dtos.get(index).getMyString(), entities.get(index).get(3));
		}

	}

	/**
	 * Test that AssembleEntities throws {@link Exception} if dto is null.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InvalidEntityCollectionException.class)
	public void testAssembleEntitiesThrowsExceptionForNullDtoCollection() throws GeDAException {
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleEntities(null, new ArrayList<TestEntity1Class>(), null, null);
		
	}
	
	/**
	 * Test that AssembleEntitie throws {@link Exception} if entities is null.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InvalidEntityCollectionException.class)
	public void testAssembleEntitieThrowsExceptionForNullEntitiesCollection() throws GeDAException {
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleEntities(new ArrayList<TestDto1Class>(), null, null, null);
		
	}
	
	/**
	 * Test that AssembleEntities throws {@link Exception} if entities is not empty.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = InvalidEntityCollectionException.class)
	public void testAssembleEntitiesThrowsExceptionForNonEmptyEntityCollection() throws GeDAException {
		
		final List<TestEntity1Class> entities = new ArrayList<TestEntity1Class>();
		entities.add(createTestEntity1(0));
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleEntities(new ArrayList<TestDto1Class>(), entities, null, null);
		
	}
	
	/**
	 * Test that AssembleEntities assembles entities.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testAssembleEntities() throws GeDAException {
		
		final int totalDtos = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>(); 
		final List<TestEntity1Class> entities = new ArrayList<TestEntity1Class>(); 
		
		for (int index = 0; index < totalDtos; index++) {
			dtos.add(createTestDto1(index));
		}
		
		final long timeStart = System.currentTimeMillis();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, TestEntity1Class.class, synthesizer);
		assembler.assembleEntities(dtos, entities, null, null);
		
		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalDtos 
				+ " simple one level entities in about " + deltaTime + " millis");
		
		assertEquals(totalDtos, entities.size());
		for (int index = 0; index < totalDtos; index++) {
            assertNotNull(entities.get(index).getNumber());
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).getNumber());
            assertNotNull(entities.get(index).getEntityId());
            assertEquals(dtos.get(index).getMyLong(), entities.get(index).getEntityId());
            assertNotNull(entities.get(index).getName());
            assertEquals(dtos.get(index).getMyString(), entities.get(index).getName());
		}
		
	}

	/**
	 * Test that AssembleEntities assembles entities.
	 *
	 * @throws GeDAException exception
	 */
	@Test
	public void testAssembleEntitiesAsMaps() throws GeDAException {

		final int totalDtos = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>();
		final List<Map> entities = new ArrayList<Map>();

		for (int index = 0; index < totalDtos; index++) {
			dtos.add(createTestDto1(index));
		}

		final long timeStart = System.currentTimeMillis();

		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, HashMap.class, synthesizer);
		assembler.assembleEntities(dtos, entities, null, null);

		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalDtos
				+ " simple one level maps in about " + deltaTime + " millis");

		assertEquals(totalDtos, entities.size());
		for (int index = 0; index < totalDtos; index++) {
            assertNotNull(entities.get(index).get("myDouble"));
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).get("myDouble"));
            assertNotNull(entities.get(index).get("myLong"));
            assertEquals(dtos.get(index).getMyLong(), entities.get(index).get("myLong"));
            assertNotNull(entities.get(index).get("myString"));
            assertEquals(dtos.get(index).getMyString(), entities.get(index).get("myString"));
		}

	}

	/**
	 * Test that AssembleEntities assembles entities.
	 *
	 * @throws GeDAException exception
	 */
	@Test
	public void testAssembleEntitiesAsLists() throws GeDAException {

		final int totalDtos = 100000;
		final List<TestDto1Class> dtos = new ArrayList<TestDto1Class>();
		final List<List> entities = new ArrayList<List>();

		for (int index = 0; index < totalDtos; index++) {
			dtos.add(createTestDto1(index));
		}

		final long timeStart = System.currentTimeMillis();

		final Assembler assembler = DTOAssembler.newCustomAssembler(TestDto1Class.class, ArrayList.class, synthesizer);
		assembler.assembleEntities(dtos, entities, null, null);

		final long deltaTime = System.currentTimeMillis() - timeStart;
		System.out.println("Assembled " + totalDtos
				+ " simple one level lists in about " + deltaTime + " millis");

		assertEquals(totalDtos, entities.size());
		for (int index = 0; index < totalDtos; index++) {
            assertNotNull(entities.get(index).get(5));
			assertEquals(dtos.get(index).getMyDouble(), entities.get(index).get(5));
            assertNotNull(entities.get(index).get(1));
            assertEquals(dtos.get(index).getMyLong(), entities.get(index).get(1));
            assertNotNull(entities.get(index).get(3));
            assertEquals(dtos.get(index).getMyString(), entities.get(index).get(3));
		}

	}

	private TestEntity1Class createTestEntity1(final int counter) {
		final TestEntity1Class entity = new TestEntity1Class();
		entity.setEntityId(1L + counter);
		entity.setName("John Doe" + counter);
		entity.setNumber(2.0d + counter);
		return entity;
	}

	private Map<String, Object> createTestEntity1Map(final int counter) {
		final Map<String, Object> entity = new HashMap<String, Object>();
		entity.put("myLong", 1L + counter);
		entity.put("myString", "John Doe" + counter);
		entity.put("myDouble", 2.0d + counter);
		return entity;
	}

	private List<Object> createTestEntity1List(final int counter) {
		final List<Object> entity = new ArrayList<Object>();
        entity.add("myLong");
        entity.add(1L + counter);
		entity.add("myString");
        entity.add("John Doe" + counter);
		entity.add("myDouble");
        entity.add(2.0d + counter);
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
