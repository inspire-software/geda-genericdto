/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.adapter.impl;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: denispavlov
 * Date: 13-05-24
 * Time: 8:50 AM
 */
public class ClassLoaderBeanFactory implements ExtensibleBeanFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderBeanFactory.class);

    private final Map<String, String> mappingClasses = new ConcurrentHashMap<String, String>();
    private final Map<String, String> mappingReps = new ConcurrentHashMap<String, String>();
    private final Map<String, Class> cacheClasses = new ConcurrentHashMap<String, Class>();
    private final Map<String, Class> cacheReps = new ConcurrentHashMap<String, Class>();

    private final Reference<ClassLoader> classLoader;

    /**
     * @param classLoader class loader for loading classes
     */
    public ClassLoaderBeanFactory(final ClassLoader classLoader) {
        this.classLoader = new SoftReference<ClassLoader>(classLoader);
    }

    /**
     * @param classLoader class loader for loading classes
     * @param mappingClasses simple key to class mapping
     * @param mappingEntityRepresentatives simple key to interface mapping for entities
     */
    public ClassLoaderBeanFactory(final ClassLoader classLoader,
                                  final Map<String, String> mappingClasses,
                                  final Map<String, String> mappingEntityRepresentatives) {
        this(classLoader);
        // put all classes
        this.mappingClasses.putAll(mappingClasses);
        // by default all representatives are implementation classes
        this.mappingReps.putAll(mappingClasses);
        // override all entity ones
        this.mappingReps.putAll(mappingEntityRepresentatives);
    }


    /** {@inheritDoc} */
    public void registerDto(final String key, final String className) throws IllegalArgumentException {
        if (key != null && key.length() > 0 && className != null && className.length() > 0) {
            if (this.mappingClasses.containsKey(key)) {
                throw new IllegalArgumentException("key is already used: [key=" + key + ", className=" + className + "]");
            }
            this.mappingClasses.put(key, className);
            this.mappingReps.put(key, className);
        } else {
            throw new IllegalArgumentException("all args are mandatory: [key=" + key + ", className=" + className + "]");
        }
    }

    /** {@inheritDoc} */
    public void registerEntity(final String key, final String className, final String representative) throws IllegalArgumentException {
        if (key != null && key.length() > 0 && className != null && className.length() > 0 && representative != null && representative.length() > 0) {
            if (this.mappingClasses.containsKey(key)) {
                throw new IllegalArgumentException("key is already used: [key=" + key + ", className=" + className + "]");
            }
            this.mappingClasses.put(key, className);
            this.mappingReps.put(key, representative);
        } else {
            throw new IllegalArgumentException("all args are mandatory: [key=" + key + ", className=" + className + ", rep=" + representative + "]");
        }
    }

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        return getClassFromMapping(entityBeanKey, mappingReps, cacheReps);
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        final Class clazz = getClassFromMapping(entityBeanKey, mappingClasses, cacheClasses);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception exp) {
                LOG.error("Unable to create instance for key = {}", entityBeanKey);
                LOG.error(exp.getMessage(), exp);
            }
        }
        return null;
    }

    private Class getClassFromMapping(final String entityBeanKey,
                                      final Map<String, String> mapping,
                                      final Map<String, Class> cache) {

        if (entityBeanKey != null && entityBeanKey.length() > 0) {
            if (cache.containsKey(entityBeanKey)) {
                return cache.get(entityBeanKey);
            }
            final String className = mapping.get(entityBeanKey);
            if (className != null) {
                try {
                    Class clazz = Class.forName(className, true, classLoader.get());
                    cache.put(entityBeanKey, clazz);
                    return clazz;
                } catch (Exception exp) {
                    LOG.error("Unable to create class for key = {}", entityBeanKey);
                    LOG.error(exp.getMessage(), exp);
                }
            } else {
                LOG.error("No mapping for key = {}", entityBeanKey);
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    public void releaseResources() {
        synchronized (this) {
            mappingClasses.clear();
            mappingReps.clear();
            cacheClasses.clear();
            cacheReps.clear();
            classLoader.clear();
        }
    }

}
