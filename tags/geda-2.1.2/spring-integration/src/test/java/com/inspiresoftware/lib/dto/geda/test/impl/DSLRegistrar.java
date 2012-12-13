/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.DTODSLRegistrar;
import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;

/**
 * User: denispavlov
 * Date: 12-10-03
 * Time: 1:35 PM
 */
public class DSLRegistrar implements DTODSLRegistrar {

    /** {@inheritDoc} */
    public void registerMappings(final DTOSupport dtoSupport, final Registry dslRegistry) {

        dslRegistry.dto("dslFilterKey").forEntity("entityKey")
                .withField("value")
                .and()
                .withField("timestamp").readOnly();

        dslRegistry.dto("dslDtoKey").forEntity("entityKey")
                .withField("value")
                .and()
                .withField("timestamp").readOnly()
                .and()
                .withField("value2");

    }
}
