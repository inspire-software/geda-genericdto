/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.impl;

import com.inspiresoftware.lib.dto.geda.DTOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple string key for class mapping with basic constructor reflection
 * instance creation.
 * <p/>
 * User: denispavlov
 * Date: May 25, 2011
 * Time: 6:33:52 PM
 */
public class DTOFactoryImpl implements DTOFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DTOFactoryImpl.class);

    private final Map<String, String> mapping = new ConcurrentHashMap<String, String>();

    public DTOFactoryImpl() {
    }

    public DTOFactoryImpl(final Map<String, String> mapping) {
        this.mapping.putAll(mapping);
    }

    /** {@inheritDoc} */
    public void register(final String key, final String className) throws IllegalArgumentException {
        if (StringUtils.hasText(key) && StringUtils.hasText(className)) {
            if (this.mapping.containsKey(key)) {
                throw new IllegalArgumentException("key is already used: [key=" + key + ", className=" + className + "]");
            }
            this.mapping.put(key, className);
        } else {
            throw new IllegalArgumentException("all args are mandatory: [key=" + key + ", className=" + className + "]");
        }
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        final String className = mapping.get(entityBeanKey);
        if (className != null) {
            try {
                final Class clazz = Class.forName(className);
                return clazz.newInstance();
            } catch (Exception exp) {
                LOG.error("Unable to create instance for key = " + entityBeanKey, exp);
            }
        } else {
            LOG.error("No mapping for key = " + entityBeanKey);
        }
        return null;
    }
}