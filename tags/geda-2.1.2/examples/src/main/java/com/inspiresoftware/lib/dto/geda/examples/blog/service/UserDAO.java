/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.service;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;

import java.util.List;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:43:58 PM
 */
public interface UserDAO {

    /**
     * Simple user entity creation for testing.
     *
     * @param username username
     * @return entity
     */
    User create(String username);

    /**
     * @return list of all users
     */
    List<User> list();

    /**
     * @param userId PK
     * @return user by id
     */
    User findUser(Long userId);

    /**
     * @param entryId PK
     * @return entry by id
     */
    UserEntry findEntry(Long entryId);

}
