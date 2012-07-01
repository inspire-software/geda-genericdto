/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.domain;

import java.util.Collection;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:24:07 PM
 */
public interface User {


    Long getUserId();

    String getUsername();

    void setUsername(String username);

    Collection<UserEntry> getEntries();

    void setEntries(Collection<UserEntry> entries);

    UserEntry createEntry();
}
