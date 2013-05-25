
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */


package com.inspiresoftware.lib.dto.geda.adapter.repository;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;
import com.inspiresoftware.lib.dto.geda.exception.DuplicateValueConverterKeyException;

import java.io.Serializable;
import java.util.Map;


/**
 * A convenience repository that can be mapped into IoC container.
 *
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 4:11:13 PM
 */
public interface AdaptersRepository extends DisposableContainer, Serializable {

    /**
     * @return all converters mapped for this repository.
     */
    Map<String, Object> getAll();

    /**
     * Register a new adapter in this repository or override old one.
     *
     * @param key converter key
     * @param adapter adapter to register converter to register (ValueCnverter, EntityRetriever, DtoToEntityMatcher)
     */
    void registerAdapterForced(String key, Object adapter);

    /**
     * Register a new adapter in this repository.
     *
     * @param key adapter key
     * @param adapter adapter to register (ValueCnverter, EntityRetriever, DtoToEntityMatcher)
     * @throws com.inspiresoftware.lib.dto.geda.exception.DuplicateValueConverterKeyException in case if key is already used in this repository.
     */
    void registerAdapter(String key, Object adapter) throws DuplicateValueConverterKeyException;

    /**
     * Removes adapter under specified key. If no adapter uses this key in this repository
     * silently does nothing.
     *
     * @param key adapter key
     */
    void removeAdapter(String key);

    /**
     * @param keys keys for adapters.
     *
     * @return all converters mapped for this repository.
     */
    Map<String, Object> getByKeysAsMap(final String ... keys);

    /**
     * @param <T> convenience type coercing
     * @param key adapter key.
     * @return adapter for given key.
     */
    <T> T getByKey(final String key);

}