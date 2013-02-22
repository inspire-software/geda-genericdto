/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.annotations.impl;

import com.inspiresoftware.lib.dto.geda.assembler.annotations.AnnotationProxy;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Reflection enabled proxy so that we do not need to cast annotations loaded
 * by different class loaders.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-21
 * Time: 11:40 AM
 */
public class ReflectionAnnotationProxy implements AnnotationProxy {

    private final String annotationClass;
    private final Map<String, Object> properties = new HashMap<String, Object>();

    public ReflectionAnnotationProxy(final Object annotation, final String ... availableProperties) {
        annotationClass = annotation.getClass().getSimpleName();
        for (final String property : availableProperties) {
            try {
                properties.put(property, annotation.getClass().getDeclaredMethod(property).invoke(annotation));
            } catch (Exception exp) {
                throw new GeDARuntimeException("Invalid @" + annotationClass
                        + " annotation proxy access via property: " + property);
            }
        }
    }

    /** {@inheritDoc} */
    public boolean annotationExists() {
        return true;
    }

    /** {@inheritDoc} */
    public <T> T getValue(final String property) {
        if (properties.containsKey(property)) {
            return (T) properties.get(property);
        }
        throw new GeDARuntimeException("Invalid @" + annotationClass
                + " annotation proxy access via property: " + property);

    }
}
