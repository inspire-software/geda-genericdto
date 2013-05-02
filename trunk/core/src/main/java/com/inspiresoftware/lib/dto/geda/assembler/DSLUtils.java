/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * These utils classes should expose some of the internal functions for DSL contexts.
 *
 * User: denispavlov
 * Date: 13-04-24
 * Time: 8:26 AM
 */
public class DSLUtils {

    protected DSLUtils() {
        // only extension
    }

    /**
     * Return map of field names and their corresponding types.
     *
     * @param clazz class to scan for fields.
     * @return field map
     */
    protected Map<String, String> scanFieldNamesOnClass(final Class clazz) {
        final Map<String, String> map = new HashMap<String, String>();

        Class clazzMap = clazz;
        while (clazzMap != null) { // when we reach Object.class this should be null
            for (final Field field : clazz.getDeclaredFields()) {
                final Class fieldType = PropertyInspector.getClassForType(field.getGenericType());
                map.put(field.getName(), fieldType.getCanonicalName());
            }
            final Type type = clazzMap.getGenericSuperclass();
            if (type != null) {
                clazzMap = PropertyInspector.getClassForType(type);
            } else {
                clazzMap = null;
            }
        }
        return map;
    }

    /**
     * Return map of field inferred from getters on interface.
     *
     * @param clazz class to scan for fields getters.
     * @return field map
     */
    protected Map<String, String> scanGetterNamesOnClass(final Class clazz) {
        final Map<String, String> map = new HashMap<String, String>();
        for (final Method method : clazz.getMethods()) {
            final String methodName = method.getName();
            if (method.getTypeParameters().length == 0 &&
                    methodName.length() > 3 && methodName.startsWith("get")) {

                final String fieldName;
                if (methodName.length() > 4) {
                    fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                } else {
                    fieldName = Character.toLowerCase(methodName.charAt(3)) + "";
                }

                final Class fieldType = PropertyInspector.getClassForType(method.getGenericReturnType());
                map.put(fieldName, fieldType.getCanonicalName());
            }
        }
        return map;
    }

}
