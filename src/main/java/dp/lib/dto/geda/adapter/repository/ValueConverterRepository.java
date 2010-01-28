/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.adapter.repository;

import dp.lib.dto.geda.adapter.ValueConverter;

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
    Map<String, ValueConverter> getAll();

    /**
     * @param keys keys for converters.
     * 
     * @return all converters mapped for this repository.
     */
    Map<String, ValueConverter> getByKeysAsMap(final String ... keys);

    /**
     * @param key value converter key.
     * @return value converter for given key.
     */
    ValueConverter getByKey(final String key);

}
