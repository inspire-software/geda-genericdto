
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
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
public interface Cache<V> extends Configurable, DisposableContainer {

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
