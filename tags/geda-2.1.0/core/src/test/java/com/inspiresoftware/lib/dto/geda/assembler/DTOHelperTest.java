

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestDto2Class;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Helper methods tests.
 * 
 * @author DPavlov
 */
public class DTOHelperTest {

	private static final int I_4 = 4;
	private static final int I_3 = 3;
	private static final int I_5 = 5;
	private static final long L_3 = 3L;

	/**
	 * Test that loading map works.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testLoadMap() throws GeDAException {
		
		final TestDto2Class dto = new TestDto2Class();
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("myBoolean", true);
		values.put("myDouble", new Double(2));
		values.put("myLong", L_3);
		values.put("myString", null);
		
		assertNull(dto.getMyBoolean());
		assertNull(dto.getMyDouble());
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		
		DTOHelper.load(dto, values);
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(L_3), dto.getMyLong());
		assertNull(dto.getMyString());
		
		values.put("myString", "test");
		
		DTOHelper.load(dto, values);
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(L_3), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
	}
	
	/**
	 * Test that loading array works.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testLoadArray() throws GeDAException {
		
		final TestDto2Class dto = new TestDto2Class();
		
		assertNull(dto.getMyBoolean());
		assertNull(dto.getMyDouble());
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		
		DTOHelper.load(dto, "myBoolean", true, "myDouble", new Double(2), "myLong", L_3, "myString", null);
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(L_3), dto.getMyLong());
		assertNull(dto.getMyString());
		
		DTOHelper.load(dto, "myString", "test");
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(L_3), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
	}
	
	/**
	 * Test that unloading map works.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testUnloadMap() throws GeDAException {
		
		final TestDto2Class dto = new TestDto2Class();
		dto.setMyBoolean(true);
		dto.setMyDouble(new Double(2));
		dto.setMyLong(L_3);
		dto.setMyString("test");
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(L_3), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
		Map<String, Object> values;
		
		values = DTOHelper.unloadMap(dto);
		
		assertNotNull(values);
		assertEquals(TestDto2Class.class, values.get("class"));
		assertEquals(true, values.get("myBoolean"));
		assertEquals(new Double(2), values.get("myDouble"));
		assertEquals(Long.valueOf(L_3), values.get("myLong"));
		assertEquals("test", values.get("myString"));
		
		values = DTOHelper.unloadMap(dto, "myBoolean", "myLong");
		
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(true, values.get("myBoolean"));
		assertNull(values.get("myDouble"));
		assertEquals(Long.valueOf(L_3), values.get("myLong"));
		assertNull(values.get("myString"));
		
	}
	
	/**
	 * Test that unloading array works.
	 * @throws GeDAException should not be thrown
	 */
	@Test
	public void testUnloadArray() throws GeDAException {
		
		final TestDto2Class dto = new TestDto2Class();
		dto.setMyBoolean(true);
		dto.setMyDouble(new Double(2));
		dto.setMyLong(L_3);
		dto.setMyString("test");
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(L_3), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
		Object[] values;
		
		values = DTOHelper.unloadValues(dto);
		
		assertNotNull(values);
		assertEquals(I_5, values.length);
		assertEquals(TestDto2Class.class, values[0]);
        assertEquals(Long.valueOf(L_3), values[1]);
        assertEquals("test", values[2]);
        assertEquals(new Double(2), values[3]);
        assertEquals(true, values[4]);

		values = DTOHelper.unloadValues(dto, "myBoolean", "myLong");
		
		assertNotNull(values);
		assertEquals(2, values.length);
		assertEquals(true, values[0]);
		assertEquals(Long.valueOf(L_3), values[1]);
		
	}
	
}
