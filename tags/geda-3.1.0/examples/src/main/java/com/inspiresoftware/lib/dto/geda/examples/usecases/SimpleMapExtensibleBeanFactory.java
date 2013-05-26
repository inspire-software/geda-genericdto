/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 3:57 PM
 */
public class SimpleMapExtensibleBeanFactory implements ExtensibleBeanFactory {

    private final Map<String, Class> classes = new HashMap<String, Class>();
    private final Map<String, Class> interfaces = new HashMap<String, Class>();

    public void registerDto(final String key, final String className) throws IllegalArgumentException {
        try {
            classes.put(key, Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void registerEntity(final String key, final String className, final String representative) throws IllegalArgumentException {
        try {
            classes.put(key, Class.forName(className));
            interfaces.put(key, Class.forName(representative));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Class getClazz(final String entityBeanKey) {
        if (interfaces.containsKey(entityBeanKey)) {
            return interfaces.get(entityBeanKey);
        } else if (classes.containsKey(entityBeanKey)) {
            return classes.get(entityBeanKey);
        }
        return null;
    }

    public Object get(final String entityBeanKey) {
        if (classes.containsKey(entityBeanKey)) {
            try {
                return classes.get(entityBeanKey).newInstance();
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    public void releaseResources() {
        // do nothing
    }
}
