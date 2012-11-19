
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.Cache;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * GC friendly implementation of cache using SoftReferences.
 * 
 * @author DPavlov
 * @since 1.1.0
 * 
 * @param <V> value
 * 
 */
public class SoftReferenceCache<V> implements Cache<V> {

	private final IntHashTable<SoftReference<V>> cache = new IntHashTable<SoftReference<V>>();
	private final Map<SoftReference<V>, Integer> cacheKeys = new ConcurrentHashMap<SoftReference<V>, Integer>();
	
	private final ReferenceQueue<V> refQueue = new ReferenceQueue<V>();
	private int cleanUpCycle;
	private int currentCycle;
	
	/**
	 * @param cleanUpCycle number of put calls before we run through cache to
	 *        clean up enqueued references.
	 */
	public SoftReferenceCache(final int cleanUpCycle) {
		this.cleanUpCycle = cleanUpCycle;
		this.currentCycle = 0;
	}

	/** {@inheritDoc} */
	public synchronized V get(final int key) {
		SoftReference<V> val = cache.get(key);
		if (val != null) {
			if (val.isEnqueued()) {
				cache.remove(key);
			}
			return val.get();
		}
		return null;
	}

	/** {@inheritDoc} */
	public synchronized void put(final int key, final V value) {
		final SoftReference<V> ref = new SoftReference<V>(value, refQueue);
		cache.put(key, ref);
		cacheKeys.put(ref, key);
		if (currentCycle == cleanUpCycle) {
			currentCycle = 0;
			cleanUpCache();
		} else {
			currentCycle++;
		}
	}
	
	/*
	 * Number of calls to {@link #put(Object, Object)} method before the cache is examined in order
	 * to clean up obsolete entities.
	 * 
	 * @param cleanUpCycle clean up cycle
	 */
	private void setCleanUpCycle(final int cleanUpCycle) {
		this.cleanUpCycle = cleanUpCycle;
	}

	@SuppressWarnings("unchecked")
	private void cleanUpCache() {
		Reference ref;
		while ((ref = refQueue.poll()) != null) {
			final Integer key = cacheKeys.remove(ref);
			if (key != null) {
				cache.remove(key);
			}
		}
	}

	/** {@inheritDoc} */
	public boolean configure(final String configuration, final Object value) {
		if ("cleanUpCycle".equals(configuration) && value instanceof Number) {
			this.setCleanUpCycle(((Number) value).intValue());
			return true;
		}
		return false;
	}
	
}
