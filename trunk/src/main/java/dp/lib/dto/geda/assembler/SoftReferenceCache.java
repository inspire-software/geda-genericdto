
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

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
 * @param <K> key
 * @param <V> value
 * 
 */
public class SoftReferenceCache<K, V> implements Cache<K, V> {

	private final Map<K, SoftReference<V>> cache = new ConcurrentHashMap<K, SoftReference<V>>();
	private final Map<SoftReference<V>, K> cacheKeys = new ConcurrentHashMap<SoftReference<V>, K>();
	
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
	public synchronized V get(final K key) {
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
	public synchronized void put(final K key, final V value) {
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
	
	/**
	 * Number of calls to {@link #put(Object, Object)} method before the cache is examined in order
	 * to clean up obsolete entities.
	 * 
	 * @param cleanUpCycle clean up cycle
	 */
	public void setCleanUpCycle(final int cleanUpCycle) {
		this.cleanUpCycle = cleanUpCycle;
	}

	@SuppressWarnings("unchecked")
	private void cleanUpCache() {
		Reference ref;
		while ((ref = refQueue.poll()) != null) {
			final K key = cacheKeys.remove(ref);
			if (key != null) {
				cache.remove(key);
			}
		}
	}

	
	
}
