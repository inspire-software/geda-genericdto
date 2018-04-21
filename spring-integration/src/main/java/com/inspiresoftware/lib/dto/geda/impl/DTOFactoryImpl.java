/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.impl;

import com.inspiresoftware.lib.dto.geda.DTOFactory;
import com.inspiresoftware.lib.dto.geda.adapter.impl.ClassLoaderBeanFactory;

import java.util.Map;

/**
 * Simple string key for class mapping with basic constructor reflection
 * instance creation.
 *
 * User: denispavlov
 * Date: May 25, 2011
 * Time: 6:33:52 PM
 */
public class DTOFactoryImpl extends ClassLoaderBeanFactory implements DTOFactory {

    public DTOFactoryImpl() {
        super(DTOFactoryImpl.class.getClassLoader());
    }

    /**
     * @param mappingClasses simple key to class mapping
     */
    public DTOFactoryImpl(final Map<String, String> mappingClasses) {
        super(DTOFactoryImpl.class.getClassLoader(), mappingClasses, mappingClasses);
    }

    /**
     * @param mappingClasses simple key to class mapping
     * @param mappingEntityRepresentatives simple key to interface mapping for entities
     */
    public DTOFactoryImpl(final Map<String, String> mappingClasses,
                          final Map<String, String> mappingEntityRepresentatives) {
        super(DTOFactoryImpl.class.getClassLoader(), mappingClasses, mappingEntityRepresentatives);
    }

}