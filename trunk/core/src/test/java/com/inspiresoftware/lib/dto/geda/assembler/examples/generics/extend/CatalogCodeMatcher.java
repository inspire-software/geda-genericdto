/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.generics.extend;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:38 AM
 */
public class CatalogCodeMatcher implements DtoToEntityMatcher<TestEntityCode, TestEntityCode> {

    public boolean match(final TestEntityCode testEntityCode, final TestEntityCode testEntityCode1) {
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
