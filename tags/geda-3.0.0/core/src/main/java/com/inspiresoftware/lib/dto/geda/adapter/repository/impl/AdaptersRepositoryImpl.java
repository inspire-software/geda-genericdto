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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Repository for all adapters.
 * Rationale - default implementation to provide basic support for value transfer.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 4:13:40 PM
 */
public class AdaptersRepositoryImpl implements AdaptersRepository {

    private static final long serialVersionUID = 20100126L;

    private final Map<String, Object> repository = new ConcurrentHashMap<String, Object>();

    /**
     * Empty constructor.
     *
     * use {@link #registerAdapter(String, Object)} to add elements dynamically.
     */
    public AdaptersRepositoryImpl() {
    	// do nothing
    }

    /**
     * IoC constructor.
     *
     * @param repository initial repository.
     */
    public AdaptersRepositoryImpl(final Map<String, Object> repository) {
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
    public void registerAdapterForced(final String key, final Object adapter) {
    	repository.put(key, adapter);
    }

    /** {@inheritDoc} */
	public void registerAdapter(final String key, final Object adapter)
			throws DuplicateValueConverterKeyException  {
		if (repository.containsKey(key)) {
			throw new DuplicateValueConverterKeyException(key);
		}
		registerAdapterForced(key, adapter);
	}

	/** {@inheritDoc} */
	public void removeAdapter(final String key) {
		repository.remove(key);
	}

    /** {@inheritDoc} */
    public void releaseResources() {
        repository.clear();
    }
}