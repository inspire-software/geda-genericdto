/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.adapter.repository.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dp.lib.dto.geda.adapter.repository.ValueConverterRepository;

/**
 * Repository for all converters and entity retrievers.
 * Rationale - default implementation to provide basic support for value conversions.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 4:13:40 PM
 */
public class ValueConverterRepositoryImpl implements ValueConverterRepository {

    private static final long serialVersionUID = 20100126L;

    private final Map<String, Object> repository = new HashMap<String, Object>();

    /**
     * Empty constructor.
     * 
     * use {@link #registerValueConverter(String, Object)} to add elements dynamically.
     */
    public ValueConverterRepositoryImpl() {
    	// do nothing       
    }
    
    /**
     * IoC constructor.
     *
     * @param repository initial repository.
     */
    public ValueConverterRepositoryImpl(final Map<String, Object> repository) {
        this.repository.putAll(repository);        
    }

    /** {@inheritDoc} */
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(repository);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
	public <T> T getByKey(final String key) {
        return (T) repository.get(key);
    }

    /** {@inheritDoc} */
    public Map<String, Object> getByKeysAsMap(final String... keys) {
        final Map<String, Object> repo = new HashMap<String, Object>();

        if (keys != null) {
            for (String key : keys) {

                final Object conv = getByKey(key);
                if (conv != null) {
                    repo.put(key, conv);
                }

            }
        }

        return repo;
    }

    /** {@inheritDoc} */
	public void registerValueConverter(String key, Object converter) throws IllegalArgumentException {
		if (repository.containsKey(key)) {
			throw new IllegalArgumentException("Key [" + key + "] is already assined a converter.");
		}
		repository.put(key, converter);
	}

	/** {@inheritDoc} */
	public void removeValueConverter(String key) {
		repository.remove(key);		
	}
    
    
}
