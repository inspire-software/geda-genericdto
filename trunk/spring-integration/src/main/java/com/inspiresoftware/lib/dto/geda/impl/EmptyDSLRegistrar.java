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

import com.inspiresoftware.lib.dto.geda.DTODSLRegistrar;
import com.inspiresoftware.lib.dto.geda.DTOSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Empty implementation that does not register any mappings.
 *
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-10-03
 * Time: 1:04 PM
 */
public class EmptyDSLRegistrar implements DTODSLRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(EmptyDSLRegistrar.class);

    /** {@inheritDoc} */
    public void registerMappings(final DTOSupport dtoSupport, final com.inspiresoftware.lib.dto.geda.dsl.Registry dslRegistry) {
        LOG.warn("Using empty DSL Registrar - check that you have specified registrar bean");
    }

}
