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
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;

import java.util.List;

/**
 * Full user object + entries - prossible use is CRUD.
 * Note that entries are BaseUserEntryDTO to prevent loading of
 * user and replies info until we really need it.
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:29:34 PM
 */
@Dto
public class UserDTOImpl extends BaseUserDTOImpl implements UserDTO {

    @DtoCollection(dtoBeanKey = "BaseUserEntryDTO",
                   dtoToEntityMatcherKey = "BaseUserEntryDTOMatcher",
                   entityGenericType = UserEntry.class)
    private List<BaseUserEntryDTO> entries;

    public List<BaseUserEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(final List<BaseUserEntryDTO> entries) {
        this.entries = entries;
    }
}
