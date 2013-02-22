/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.annotations.impl;

import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
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
public class LocalClassLoaderDtoCollectionAnnotationProxy implements AnnotationProxy {

    private final DtoCollection annotation;

    public LocalClassLoaderDtoCollectionAnnotationProxy(final DtoCollection annotation) {
        this.annotation = annotation;
    }

    /** {@inheritDoc} */
    public boolean annotationExists() {
        return true;
    }

    /** {@inheritDoc} */
    public <T> T getValue(final String property) {
        if ("value".equals(property)) {
            return (T) annotation.value();
        } else if ("readOnly".equals(property)) {
            return (T) Boolean.valueOf(annotation.readOnly());
        } else if ("entityCollectionClass".equals(property)) {
            return (T) annotation.entityCollectionClass();
        } else if ("entityCollectionClassKey".equals(property)) {
            return (T) annotation.entityCollectionClassKey();
        } else if ("dtoCollectionClass".equals(property)) {
            return (T) annotation.dtoCollectionClass();
        } else if ("dtoCollectionClassKey".equals(property)) {
            return (T) annotation.dtoCollectionClassKey();
        } else if ("entityBeanKeys".equals(property)) {
            return (T) annotation.entityBeanKeys();
        } else if ("dtoBeanKey".equals(property)) {
            return (T) annotation.dtoBeanKey();
        } else if ("entityGenericType".equals(property)) {
            return (T) annotation.entityGenericType();
        } else if ("entityGenericTypeKey".equals(property)) {
            return (T) annotation.entityGenericTypeKey();
        } else if ("dtoToEntityMatcher".equals(property)) {
            return (T) annotation.dtoToEntityMatcher();
        } else if ("dtoToEntityMatcherKey".equals(property)) {
            return (T) annotation.dtoToEntityMatcherKey();
        }
        throw new GeDARuntimeException("Invalid @DtoCollection annotation proxy access via property: " + property);
    }
}
