/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.domain;

import java.util.Collection;

/**
 * .
 *
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:25:35 PM
 */
public interface UserEntry {


    Long getEntryId();

    User getUser();

    String getTitle();

    void setTitle(String title);

    String getBody();

    void setBody(String body);

    Collection<UserEntryReply> getReplies();

    void setReplies(Collection<UserEntryReply> replies);

    UserEntryReply createReply(User replier);
}
