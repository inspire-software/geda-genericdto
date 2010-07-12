

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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Helper methods tests.
 * 
 * @author DPavlov
 */
public class DTOHelperTest {

	/**
	 * Test that loading map works.
	 */
	@Test
	public void testLoadMap() {
		
		final TestDto2Class dto = new TestDto2Class();
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("myBoolean", true);
		values.put("myDouble", new Double(2));
		values.put("myLong", 3L);
		values.put("myString", null);
		
		assertNull(dto.getMyBoolean());
		assertNull(dto.getMyDouble());
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		
		DTOHelper.load(dto, values);
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(3L), dto.getMyLong());
		assertNull(dto.getMyString());
		
		values.put("myString", "test");
		
		DTOHelper.load(dto, values);
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(3L), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
	}
	
	/**
	 * Test that loading array works.
	 */
	@Test
	public void testLoadArray() {
		
		final TestDto2Class dto = new TestDto2Class();
		
		assertNull(dto.getMyBoolean());
		assertNull(dto.getMyDouble());
		assertNull(dto.getMyLong());
		assertNull(dto.getMyString());
		
		DTOHelper.load(dto, "myBoolean", true, "myDouble", new Double(2), "myLong", 3L, "myString", null);
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(3L), dto.getMyLong());
		assertNull(dto.getMyString());
		
		DTOHelper.load(dto, "myString", "test");
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(3L), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
	}
	
	/**
	 * Test that unloading map works.
	 */
	@Test
	public void testUnloadMap() {
		
		final TestDto2Class dto = new TestDto2Class();
		dto.setMyBoolean(true);
		dto.setMyDouble(new Double(2));
		dto.setMyLong(3L);
		dto.setMyString("test");
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(3L), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
		Map<String, Object> values;
		
		values = DTOHelper.unloadMap(dto);
		
		assertNotNull(values);
		assertEquals(TestDto2Class.class, values.get("class"));
		assertEquals(true, values.get("myBoolean"));
		assertEquals(true, values.get("myBoolean"));
		assertEquals(new Double(2), values.get("myDouble"));
		assertEquals(Long.valueOf(3L), values.get("myLong"));
		assertEquals("test", values.get("myString"));
		
		values = DTOHelper.unloadMap(dto, "myBoolean", "myLong");
		
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(true, values.get("myBoolean"));
		assertNull(values.get("myDouble"));
		assertEquals(Long.valueOf(3L), values.get("myLong"));
		assertNull(values.get("myString"));
		
	}
	
	/**
	 * Test that unloading array works.
	 */
	@Test
	public void testUnloadArray() {
		
		final TestDto2Class dto = new TestDto2Class();
		dto.setMyBoolean(true);
		dto.setMyDouble(new Double(2));
		dto.setMyLong(3L);
		dto.setMyString("test");
		
		assertEquals(true, dto.getMyBoolean());
		assertEquals(new Double(2), dto.getMyDouble());
		assertEquals(Long.valueOf(3L), dto.getMyLong());
		assertEquals("test", dto.getMyString());
		
		Object[] values;
		
		values = DTOHelper.unloadValues(dto);
		
		assertNotNull(values);
		assertEquals(5, values.length);
		assertEquals(TestDto2Class.class, values[0]);
		assertEquals(true, values[1]);
		assertEquals(new Double(2), values[2]);
		assertEquals(Long.valueOf(3L), values[3]);
		assertEquals("test", values[4]);
		
		values = DTOHelper.unloadValues(dto, "myBoolean", "myLong");
		
		assertNotNull(values);
		assertEquals(2, values.length);
		assertEquals(true, values[0]);
		assertEquals(Long.valueOf(3L), values[1]);
		
	}
	
}
