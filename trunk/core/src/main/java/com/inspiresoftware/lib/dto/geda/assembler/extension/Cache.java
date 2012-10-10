
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler.extension;


/**
 * Cache interface that allows to handle basic caching in GeDA.
 * 
 * @author DPavlov
 * @since 1.1.0
 * 
 * @param <V> value
 * 
 */
public interface Cache<V> extends Configurable {

	/**
	 * @param key cache key
	 * @return value or <code>null</code>
	 */
	V get(int key);
	
	/**
	 * @param key cache key
	 * @param value cache value
	 */
	void put(int key, V value);
	
}
