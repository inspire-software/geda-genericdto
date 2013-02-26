/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi.impl;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DisposableContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple string key for class mapping with basic constructor reflection
 * instance creation.
 *
 * This is disposable throw away container - once it had been disposed
 * it is no longer should be used as we loose class loader reference.
 * <p/>
 * User: denispavlov
 * Date: May 25, 2011
 * Time: 6:33:52 PM
 */
public class OSGiBundleDTOFactoryImpl implements ExtensibleBeanFactory, DisposableContainer {

    private static final Logger LOG = LoggerFactory.getLogger(OSGiBundleDTOFactoryImpl.class);

    private final Map<String, String> mappingClasses = new ConcurrentHashMap<String, String>();
    private final Map<String, String> mappingReps = new ConcurrentHashMap<String, String>();
    private final Map<String, Class> cacheClasses = new ConcurrentHashMap<String, Class>();
    private final Map<String, Class> cacheReps = new ConcurrentHashMap<String, Class>();

    private final Reference<ClassLoader> classLoader;

    public OSGiBundleDTOFactoryImpl(final ClassLoader classLoader) {
        this.classLoader = new SoftReference<ClassLoader>(classLoader);
    }

    /** {@inheritDoc} */
    public void register(final String key, final String className) throws IllegalArgumentException {
        registerDto(key, className);
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
        if (key != null && key.length() > 0 && className != null && className.length() > 0) {
            if (this.mappingClasses.containsKey(key)) {
                throw new IllegalArgumentException("key is already used: [key=" + key + ", className=" + className + "]");
            }
            this.mappingClasses.put(key, className);
            this.mappingReps.put(key, representative);
        } else {
            throw new IllegalArgumentException("all args are mandatory: [key=" + key + ", className=" + className + "]");
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