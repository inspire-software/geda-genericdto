package com.inspiresoftware.lib.dto.geda.examples.blog.dto;

import java.util.List;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:32:42 PM
 */
public interface UserDTO extends BaseUserDTO {
    List<BaseUserEntryDTO> getEntries();

    void setEntries(List<BaseUserEntryDTO> entries);
}
