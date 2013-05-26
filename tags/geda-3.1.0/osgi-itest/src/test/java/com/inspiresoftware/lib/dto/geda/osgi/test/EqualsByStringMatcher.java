/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.osgi.test;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 10:29 AM
 */
public class EqualsByStringMatcher implements DtoToEntityMatcher<SimpleDTO, SimpleEntity> {

    public boolean match(final SimpleDTO simpleDTO, final SimpleEntity simpleEntity) {
        if (simpleDTO != null && simpleEntity != null) {
            if (simpleDTO.getString() != null && simpleEntity.getString() != null) {
                return simpleDTO.getString().equals(simpleEntity.getString());
            }
        }
        return false;
    }
}
