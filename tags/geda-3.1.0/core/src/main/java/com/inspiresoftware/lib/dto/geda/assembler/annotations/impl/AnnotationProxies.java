/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.annotations.impl;

import com.inspiresoftware.lib.dto.geda.annotations.*;
import com.inspiresoftware.lib.dto.geda.assembler.annotations.AnnotationProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotation proxy factory that hides some of the complexity by providing static
 * wrappers for same classloader annotations and reflection proxies for external
 * class loaders.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-21
 * Time: 11:41 AM
 */
public final class AnnotationProxies {

    private static final AnnotationProxy NULL = new NullAnnotationProxy();

    private AnnotationProxies() {
        // no instance
    }

    /**
     * Some annotations are proxied. This method attempt to detect this.
     *
     * @param annotation annotation or proxy
     *
     * @return annotation class
     */
    private static Class getAnnotationTrueClass(Annotation annotation) {
        if (annotation instanceof Proxy) {
            return annotation.getClass().getInterfaces()[0];
        }
        return annotation.getClass();
    }

    /**
     * Get @Dto proxy for this class.
     *
     * @param clazz class to discover @Dto annotation for
     *
     * @return annotation proxy (never null)
     */
    public static AnnotationProxy getClassAnnotationProxy(Class clazz) {
        for (final Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (annotation instanceof Dto) {
                return new LocalClassLoaderDtoAnnotationProxy((Dto) annotation);
            } else if ("com.inspiresoftware.lib.dto.geda.annotations.Dto"
                            .equals(getAnnotationTrueClass(annotation).getCanonicalName())) {
                return new ReflectionAnnotationProxy(annotation, "value");
            }
        }
        return NULL;
    }

    private static final Map<String, String[]> REFLECTIVE_PROPERTIES = new HashMap<String, String[]>();
    static {
        REFLECTIVE_PROPERTIES.put(
                "com.inspiresoftware.lib.dto.geda.annotations.DtoField",
                new String[] { "value", "converter", "readOnly", "entityBeanKeys", "dtoBeanKey" }
        );
        REFLECTIVE_PROPERTIES.put(
                "com.inspiresoftware.lib.dto.geda.annotations.DtoParent",
                new String[] { "value", "retriever" }
        );
        REFLECTIVE_PROPERTIES.put(
                "com.inspiresoftware.lib.dto.geda.annotations.DtoVirtualField",
                new String[] { "converter", "readOnly", "entityBeanKeys", "dtoBeanKey" }
        );
        REFLECTIVE_PROPERTIES.put(
                "com.inspiresoftware.lib.dto.geda.annotations.DtoCollection",
                new String[] { "value", "readOnly", "entityCollectionClass", "entityCollectionClassKey",
                        "dtoCollectionClass", "dtoCollectionClassKey", "entityBeanKeys", "dtoBeanKey",
                        "entityGenericType", "entityGenericTypeKey", "dtoToEntityMatcher", "dtoToEntityMatcherKey" }
        );
        REFLECTIVE_PROPERTIES.put(
                "com.inspiresoftware.lib.dto.geda.annotations.DtoMap",
                new String[] { "value", "readOnly", "entityMapOrCollectionClass", "entityMapOrCollectionClassKey",
                        "dtoMapClass", "dtoMapClassKey", "entityBeanKeys", "dtoBeanKey", "entityGenericType",
                        "entityGenericTypeKey", "entityCollectionMapKey", "useEntityMapKey",
                        "dtoToEntityMatcher", "dtoToEntityMatcherKey" }
        );
    }

    /**
     * Get @DtoField (+@DtoParent), @DtoVirtualField, @DtoCollection, @DtoMap proxy for this field.
     *
     * @param field field to discover annotation for
     *
     * @return annotation proxy map (never null)
     */
    public static Map<String, AnnotationProxy> getFieldAnnotationProxy(Field field) {
        final Map<String, AnnotationProxy> anns = new HashMap<String, AnnotationProxy>();
        for (final Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation instanceof DtoField) {
                anns.put("DtoField", new LocalClassLoaderDtoFieldAnnotationProxy((DtoField) annotation));
            } else if (annotation instanceof DtoParent) {
                anns.put("DtoParent", new LocalClassLoaderDtoParentAnnotationProxy((DtoParent) annotation));
            } else if (annotation instanceof DtoVirtualField) {
                anns.put("DtoVirtualField", new LocalClassLoaderDtoVirtualFieldAnnotationProxy((DtoVirtualField) annotation));
            } else if (annotation instanceof DtoCollection) {
                anns.put("DtoCollection", new LocalClassLoaderDtoCollectionAnnotationProxy((DtoCollection) annotation));
            } else if (annotation instanceof DtoMap) {
                anns.put("DtoMap", new LocalClassLoaderDtoMapAnnotationProxy((DtoMap) annotation));
            } else {
                final Class annClass = getAnnotationTrueClass(annotation);
                final String[] properties = REFLECTIVE_PROPERTIES.get(annClass.getCanonicalName());
                if (properties != null) {
                    anns.put(annClass.getSimpleName(),
                        new ReflectionAnnotationProxy(annotation,properties));
                }
            }
        }
        return anns;
    }

}
