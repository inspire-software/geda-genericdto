/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.service;

import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;

import java.util.List;

/**
 * .
 *
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:43:58 PM
 */
public interface UserService {

    /**
     * @return list of base user object since we do not want to load entries
     */
    List<BaseUserDTO> list();

    /**
     * @return list of user object with filtering example so we do not load everything
     */
    List<UserDTO> list(String filter);

    /**
     * @param userId PK
     * @return fully loaded user since we want to veiw use + base blog entries (but not replies)
     */
    UserDTO findUser(Long userId);

    /**
     * @param entryId PK
     * @return fully loaded entry since we want replies with base user info + base replies info
     */
    UserEntryDTO findEntry(Long entryId);

}