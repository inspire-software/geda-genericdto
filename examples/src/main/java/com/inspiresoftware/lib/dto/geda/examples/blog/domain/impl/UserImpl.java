/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * .
 *
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:26:52 PM
 */
public class UserImpl implements User {

    private static Long dbCounter = 0L;
    
    private Long userId;
    private String username;
    private Collection<UserEntry> entries = new ArrayList<UserEntry>();

    public UserImpl() {
        this.userId = ++dbCounter;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Collection<UserEntry> getEntries() {
        return entries;
    }

    public void setEntries(final Collection<UserEntry> entries) {
        this.entries = entries;
    }

    public UserEntry createEntry() {
        final UserEntry entry = new UserEntryImpl(this);
        entries.add(entry);
        return entry;
    }
}
