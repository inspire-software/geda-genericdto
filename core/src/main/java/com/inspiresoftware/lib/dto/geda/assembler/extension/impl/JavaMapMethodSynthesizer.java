/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Map class method synthesizer.
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-09-18
 * Time: 10:10 AM
 */
public class JavaMapMethodSynthesizer implements MethodSynthesizer {

    /** {@inheritDoc} */
    public DataReader synthesizeReader(final PropertyDescriptor descriptor) throws InspectionPropertyNotFoundException, UnableToCreateInstanceException, GeDARuntimeException {

        final String propName = descriptor.getName();
        final Class returnType = descriptor.getReadMethod().getReturnType();

        return new DataReader() {

            private String property = propName;
            private Class type = returnType;

            public Object read(final Object source) {
                final Map<String, Object> mapSource = (Map) source;
                return mapSource.get(property);
            }

            public Class<?> getReturnType() {
                return type;
            }
        };
    }

    /** {@inheritDoc} */
    public DataWriter synthesizeWriter(final PropertyDescriptor descriptor) throws InspectionPropertyNotFoundException, UnableToCreateInstanceException, GeDARuntimeException {

        final String propName = descriptor.getName();
        final Class paramType = descriptor.getWriteMethod().getParameterTypes()[0];

        return new DataWriter() {

            private String property = propName;
            private Class type = paramType;

            public void write(final Object source, final Object value) {
                final Map<String, Object> mapSource = (Map) source;
                mapSource.put(property, value);
            }

            public Class<?> getParameterType() {
                return type;
            }
        };
    }

    /** {@inheritDoc} */
    public boolean configure(final String configuration, final Object value) throws GeDAException {
        return false;
    }

    /** {@inheritDoc} */
    public void releaseResources() {
        // none
    }
}
