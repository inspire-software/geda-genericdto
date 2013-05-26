
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.Cache;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;


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

    private final ReferenceQueue<V> cacheQueue = new ReferenceQueue<V>();

	/**
	 * Simple soft references cache that allows efficient access via int hash key.
     * Hash should be well defined as bad hashes will degrade performance.
	 */
	public SoftReferenceCache() {

	}

	/** {@inheritDoc} */
	public V get(final int key) {
        SoftReference<V> val = cache.get(key);
        if (val != null) {
            final V obj = val.get();
            if (obj == null) {
                synchronized (this) {
                    cache.remove(key);
                }
                return null;
            }
            return obj;
        }
        return null;
	}

	/** {@inheritDoc} */
	public void put(final int key, final V value) {
        synchronized (this) {
            final SoftReference<V> ref = new SoftReference<V>(value, cacheQueue);
            cache.put(key, ref);
        }
	}

	/** {@inheritDoc} */
	public boolean configure(final String configuration, final Object value) {
		return false;
	}

    /** {@inheritDoc} */
    public void releaseResources() {
        synchronized (this) {
            int[] keys = cache.keysArray();
            for (int index = 0; index < keys.length; index++) {
                int key = keys[index];
                if (key != 0) {
                    final SoftReference<V> ref = cache.remove(key);
                    final Object obj = ref.get();
                    if (obj instanceof DisposableContainer) {
                        ((DisposableContainer) obj).releaseResources();
                    }
                    ref.clear();
                }
            }
            while (cacheQueue.poll() != null) {
                // Nothing for now
            }
        }
    }
}
