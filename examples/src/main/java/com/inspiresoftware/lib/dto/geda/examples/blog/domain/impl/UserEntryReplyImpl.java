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

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:37:53 PM
 */
public class UserEntryReplyImpl implements UserEntryReply {

    private UserEntry originalEntry;
    private UserEntry replyEntry;

    public UserEntryReplyImpl() {
    }

    public UserEntryReplyImpl(final UserEntry originalEntry, final User replier) {
        this();
        this.originalEntry = originalEntry;
        this.replyEntry = replier.createEntry();
        this.replyEntry.setTitle("RE: " + this.originalEntry.getTitle());
    }

    public UserEntry getOriginalEntry() {
        return originalEntry;
    }

    public UserEntry getReplyEntry() {
        return replyEntry;
    }

}
