
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.adapter.repository;

import java.io.Serializable;
import java.util.Map;

/**
 * A convenience repository that can be mapped into IoC container.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 4:11:13 PM
 */
public interface ValueConverterRepository extends Serializable {

    /**
     * @return all converters mapped for this repository.
     */
    Map<String, Object> getAll();
    
    /**
     * Register a new converter in this repository.
     * 
     * @param key converter key
     * @param converter converter to register
     * @throws IllegalArgumentException in case if key is already used in this repository.
     */
    void registerValueConverter(String key, Object converter) throws IllegalArgumentException;

    /**
     * Removes converter under specified key. If no converter uses this key in this repository
     * silently does nothing.
     * 
     * @param key converter key
     */
    void removeValueConverter(String key);
    
    /**
     * @param keys keys for converters.
     * 
     * @return all converters mapped for this repository.
     */
    Map<String, Object> getByKeysAsMap(final String ... keys);

    /**
     * @param key value converter key.
     * @return value converter for given key.
     */
    <T> T getByKey(final String key);

}
