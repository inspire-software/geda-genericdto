/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.service.impl;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:50:08 PM
 */
public class UserDAOImpl implements UserDAO {

    private final Map<Long, User> db = new HashMap<Long, User>();

    public User create(final String username) {
        final User user = new UserImpl();
        user.setUsername(username);
        db.put(user.getUserId(), user);
        return user;
    }

    public List<User> list() {
        return new ArrayList<User>(db.values());
    }

    public User findUser(final Long userId) {
        return db.get(userId);
    }

    public UserEntry findEntry(final Long entryId) {
        for (final User user : db.values()) {
            for (final UserEntry entry : user.getEntries()) {
                if (entry.getEntryId().equals(entryId)) {
                    return entry;
                }
            }
        }
        return null;
    }
}
