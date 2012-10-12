
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.adapter.repository.impl;

import com.inspiresoftware.lib.dto.geda.adapter.repository.AdaptersRepository;
import com.inspiresoftware.lib.dto.geda.exception.DuplicateValueConverterKeyException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * AdapterRepositoryImpl test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class AdapterRepositoryImplTest {

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
		
		final AdaptersRepository repo = new AdaptersRepositoryImpl(convs);
		
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
	
	/**
	 * Test register methods for repository.
	 * 
	 * @throws com.inspiresoftware.lib.dto.geda.exception.DuplicateValueConverterKeyException duplicate exception 
	 */
	@Test
	public void testRegisterAdapters() throws DuplicateValueConverterKeyException {
		
		final Object conv1 = new Object();
		final AdaptersRepository repo = new AdaptersRepositoryImpl();
		
		assertNotNull(repo.getAll());
		assertTrue(repo.getAll().isEmpty());
		
		repo.registerAdapter("key1", conv1);
		
		assertNotNull(repo.getAll());
		assertEquals(1, repo.getAll().size());
		assertSame(conv1, repo.getByKey("key1"));
		
		repo.removeAdapter("key1");
		
		assertNotNull(repo.getAll());
		assertTrue(repo.getAll().isEmpty());
		
	}
	
	/**
	 * Test duplicate keys.
	 * 
	 * @throws com.inspiresoftware.lib.dto.geda.exception.DuplicateValueConverterKeyException duplicate exception
	 */
	@Test(expected = DuplicateValueConverterKeyException.class)
	public void testRegisterAdaptersFailForDuplicateKey() throws DuplicateValueConverterKeyException {
		
		final Object conv1 = new Object();
		final AdaptersRepository repo = new AdaptersRepositoryImpl();
		
		repo.registerAdapter("key1", conv1);
		repo.registerAdapter("key1", conv1);
		
	}
	
	/**
	 * Test duplicate keys.
	 */
	@Test
	public void testRegisterAdaptersForcedDoesNotFailForDuplicateKey() {
		
		final Object conv1 = new Object();
		final AdaptersRepository repo = new AdaptersRepositoryImpl();
		
		repo.registerAdapterForced("key1", conv1);
		repo.registerAdapterForced("key1", conv1);
		
	}
	
}