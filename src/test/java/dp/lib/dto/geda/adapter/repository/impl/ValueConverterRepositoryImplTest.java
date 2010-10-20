
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.adapter.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import dp.lib.dto.geda.adapter.repository.ValueConverterRepository;

/**
 * ValueConverterRepository test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class ValueConverterRepositoryImplTest
{

	/**
	 * Test basic methods for repository.
	 */
	@Test
	public void testConstructor() {
		
		final Object conv1 = new Object();
		final Object conv2 = new Object();
		
		final Map<String, Object> convs = new HashMap<String, Object>();
		convs.put("key1", conv1);
		convs.put("key2", conv2);
		
		final ValueConverterRepository repo = new ValueConverterRepositoryImpl(convs);
		
		final Map<String, Object> res = repo.getAll();
		
		assertNotSame(convs, res);
		assertNotNull(res);
		assertEquals(convs.size(), res.size());
		assertSame(conv1, res.get("key1"));
		assertSame(conv2, res.get("key2"));
		
		assertSame(conv1, repo.getByKey("key1"));
		
		final Map<String, Object> res2 = repo.getByKeysAsMap("key1");
		assertNotNull(res2);
		assertEquals(1, res2.size());
		assertSame(conv1, res2.get("key1"));
		
	}
	
	@Test
	public void testRegisterValueConverter() {
		
		final Object conv1 = new Object();
		final ValueConverterRepository repo = new ValueConverterRepositoryImpl();
		
		assertNotNull(repo.getAll());
		assertTrue(repo.getAll().isEmpty());
		
		repo.registerValueConverter("key1", conv1);
		
		assertNotNull(repo.getAll());
		assertEquals(1, repo.getAll().size());
		assertSame(conv1, repo.getByKey("key1"));
		
		repo.removeValueConverter("key1");
		
		assertNotNull(repo.getAll());
		assertTrue(repo.getAll().isEmpty());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterValueConverterFailForDuplicateKey() {
		
		final Object conv1 = new Object();
		final ValueConverterRepository repo = new ValueConverterRepositoryImpl();
		
		repo.registerValueConverter("key1", conv1);
		repo.registerValueConverter("key1", conv1);
		
	}
	
}
