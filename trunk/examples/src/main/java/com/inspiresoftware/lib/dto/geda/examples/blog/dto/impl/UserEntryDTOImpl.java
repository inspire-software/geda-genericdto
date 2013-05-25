/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;

import java.util.List;

/**
 * Full loaded entry object so we may examine replies.
 * This one contains BaseUserDTO (so we know who this entry belongs to if we
 * need to look up used record) and replies as BaseUserEntryDTO as we do not want
 * to load replies of replies until we need them.
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:39:15 PM
 */
@Dto
public class UserEntryDTOImpl extends BaseUserEntryDTOImpl implements UserEntryDTO {

    @DtoField(dtoBeanKey = "BaseUserDTO", readOnly = true)
    private BaseUserDTO user;

    @DtoCollection(dtoBeanKey = "BaseUserEntryReplyDTO",
                   dtoToEntityMatcherKey = "BaseUserEntryDTOMatcher",
                   entityGenericType = UserEntryReply.class, readOnly = true)
    private List<BaseUserEntryDTO> replies;


    public BaseUserDTO getUser() {
        return user;
    }

    public void setUser(final BaseUserDTO user) {
        this.user = user;
    }

    public List<BaseUserEntryDTO> getReplies() {
        return replies;
    }

    public void setReplies(final List<BaseUserEntryDTO> replies) {
        this.replies = replies;
    }
}
