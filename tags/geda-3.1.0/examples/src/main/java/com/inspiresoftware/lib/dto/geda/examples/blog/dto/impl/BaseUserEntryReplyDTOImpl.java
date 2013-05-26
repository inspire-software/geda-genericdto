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
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;

/**
 * The reply DTO object is in essence the same as base user entry,
 * so we can reuse the interface but provide a "carry over" mapping
 * for replyEntry property of domain entity.
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:35:10 PM
 */
@Dto
public class BaseUserEntryReplyDTOImpl implements BaseUserEntryDTO {

    @DtoField(value = "replyEntry.entryId", readOnly = true)
    private Long entryId;
    @DtoField(value = "replyEntry.title", readOnly = true)
    private String title;
    @DtoField(value = "replyEntry.body", readOnly = true)
    private String body;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(final Long entryId) {
        this.entryId = entryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }
}
