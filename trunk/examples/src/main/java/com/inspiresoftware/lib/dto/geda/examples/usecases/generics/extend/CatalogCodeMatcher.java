/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics.extend;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:38 AM
 */
public class CatalogCodeMatcher implements DtoToEntityMatcher<EntityCode, EntityCode> {

    public boolean match(final EntityCode testEntityCode, final EntityCode testEntityCode1) {
        if (testEntityCode == null || testEntityCode1 == null) {
            throw new IllegalArgumentException("Unable to synchronise NULLs");
        }
        if (testEntityCode.equals(testEntityCode1)) {
            return true;
        }

        if (testEntityCode != null && testEntityCode.getId().equals(testEntityCode1.getId())) {
            return true;
        }
        return false;
    }
}
