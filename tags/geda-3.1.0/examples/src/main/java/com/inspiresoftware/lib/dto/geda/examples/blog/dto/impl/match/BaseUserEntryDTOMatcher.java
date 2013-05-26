/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl.match;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:41:32 PM
 */
public class BaseUserEntryDTOMatcher implements DtoToEntityMatcher<BaseUserEntryDTO, Object> {

    public boolean match(final BaseUserEntryDTO baseUserEntryDTO, final Object o) {
        if (o instanceof UserEntry) {
            return baseUserEntryDTO.getEntryId().equals(((UserEntry) o).getEntryId());
        } else if (o instanceof UserEntryReply) {
            return baseUserEntryDTO.getEntryId().equals(((UserEntryReply) o).getReplyEntry().getEntryId());
        }
        throw new IllegalArgumentException("Invalid use");
    }
}
