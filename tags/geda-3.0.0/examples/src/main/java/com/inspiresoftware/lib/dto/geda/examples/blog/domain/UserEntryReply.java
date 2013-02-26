/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.domain;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:35:19 PM
 */
public interface UserEntryReply {
    
    UserEntry getOriginalEntry();

    UserEntry getReplyEntry();
}
