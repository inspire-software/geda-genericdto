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

import com.inspiresoftware.lib.dto.geda.annotations.DtoVirtualField;
import com.inspiresoftware.lib.dto.geda.assembler.annotations.AnnotationProxy;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

/**
 * DtoField wrapper.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-21
 * Time: 1:13 PM
 */
public class LocalClassLoaderDtoVirtualFieldAnnotationProxy implements AnnotationProxy {

    private final DtoVirtualField annotation;

    public LocalClassLoaderDtoVirtualFieldAnnotationProxy(final DtoVirtualField annotation) {
        this.annotation = annotation;
    }

    /** {@inheritDoc} */
    public boolean annotationExists() {
        return true;
    }

    /** {@inheritDoc} */
    public <T> T getValue(final String property) {
        if ("converter".equals(property)) {
            return (T) annotation.converter();
        } else if ("readOnly".equals(property)) {
            return (T) Boolean.valueOf(annotation.readOnly());
        } else if ("entityBeanKeys".equals(property)) {
            return null;
        } else if ("dtoBeanKey".equals(property)) {
            return null;
        }
        throw new GeDARuntimeException("Invalid @DtoVirtualField annotation proxy access via property: " + property);
    }
}
