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
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;

import java.util.ArrayList;
import java.util.Collection;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:29:39 PM
 */
public class UserEntryImpl implements UserEntry {

    private static Long dbCounter = 0L;

    private Long entryId;
    private User user;
    private String title;
    private String body;
    private Collection<UserEntryReply> replies = new ArrayList<UserEntryReply>();

    public UserEntryImpl() {
        this.entryId = ++dbCounter;
    }

    public UserEntryImpl(final User user) {
        this();
        this.user = user;
    }

    public Long getEntryId() {
        return entryId;
    }

    public User getUser() {
        return user;
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

    public Collection<UserEntryReply> getReplies() {
        return replies;
    }

    public void setReplies(final Collection<UserEntryReply> replies) {
        this.replies = replies;
    }

    public UserEntryReply createReply(final User replier) {
        final UserEntryReply reply = new UserEntryReplyImpl(this, replier);
        replies.add(reply);
        return reply;
    }
}
