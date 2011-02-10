
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.assembler;

/**
 * Cache interface that allows to handle basic caching in GeDA.
 * 
 * @author DPavlov
 * @since 1.1.0
 */
public interface Cache<K, V> {

	/**
	 * @param key cache key
	 * @return value or <code>null</code>
	 */
	V get(K key);
	
	/**
	 * @param key
	 * @param value
	 */
	void put(K key, V value);
	
}
