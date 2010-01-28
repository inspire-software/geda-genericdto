/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.adapter.repository.impl;

import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.adapter.repository.ValueConverterRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 4:13:40 PM
 */
public class ValueConverterRepositoryImpl implements ValueConverterRepository {

    private static final long serialVersionUID = 20100126L;

    private final Map<String, ValueConverter> repository = new HashMap<String, ValueConverter>();

    /**
     * IoC constructor.
     *
     * @param repository initial repository.
     */
    public ValueConverterRepositoryImpl(final Map<String, ValueConverter> repository) {
        this.repository.putAll(repository);        
    }

    /** {@inheritDoc} */
    public Map<String, ValueConverter> getAll() {
        return Collections.unmodifiableMap(repository);
    }

    /** {@inheritDoc} */
    public ValueConverter getByKey(final String key) {
        return repository.get(key);
    }

    /** {@inheritDoc} */
    public Map<String, ValueConverter> getByKeysAsMap(final String... keys) {
        final Map<String, ValueConverter> repo = new HashMap<String, ValueConverter>();

        if (keys != null) {
            for (String key : keys) {

                final ValueConverter conv = getByKey(key);
                if (conv != null) {
                    repo.put(key, conv);
                }

            }
        }

        return repo;
    }
}
