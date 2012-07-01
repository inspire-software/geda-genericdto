/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;

/**
 * Lightweight object to generate list of entries.
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:26:18 PM
 */
@Dto
public class BaseUserEntryDTOImpl implements BaseUserEntryDTO {

    @DtoField(readOnly = true)
    private Long entryId;
    @DtoField
    private String title;
    @DtoField
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
