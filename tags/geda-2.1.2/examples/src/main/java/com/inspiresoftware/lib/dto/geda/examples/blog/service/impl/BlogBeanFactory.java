/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.service.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserEntryImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserEntryReplyImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 2:03:19 PM
 */
public class BlogBeanFactory implements BeanFactory {

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        if ("BaseUserDTO".equals(entityBeanKey)) {
            return BaseUserDTOImpl.class;
        } else if ("BaseUserEntryDTO".equals(entityBeanKey)) {
            return BaseUserEntryDTOImpl.class;
        } else if ("BaseUserEntryReplyDTO".equals(entityBeanKey)) {
            return BaseUserEntryReplyDTOImpl.class;
        } else if ("UserDTO".equals(entityBeanKey)) {
            return UserDTOImpl.class;
        } else if ("UserEntryDTO".equals(entityBeanKey)) {
            return UserEntryDTOImpl.class;
        }

        if ("User".equals(entityBeanKey)) {
            return User.class;
        } else if ("UserEntry".equals(entityBeanKey)) {
            return UserEntry.class;
        } else if ("UserEntryReply".equals(entityBeanKey)) {
            return UserEntryReply.class;
        }

        throw new IllegalArgumentException("No representative for : " + entityBeanKey);
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        if ("BaseUserDTO".equals(entityBeanKey)) {
            return new BaseUserDTOImpl();
        } else if ("BaseUserEntryDTO".equals(entityBeanKey)) {
            return new BaseUserEntryDTOImpl();
        } else if ("BaseUserEntryReplyDTO".equals(entityBeanKey)) {
            return new BaseUserEntryReplyDTOImpl();
        } else if ("UserDTO".equals(entityBeanKey)) {
            return new UserDTOImpl();
        } else if ("UserEntryDTO".equals(entityBeanKey)) {
            return new UserEntryDTOImpl();
        }

        if ("User".equals(entityBeanKey)) {
            return new UserImpl();
        } else if ("UserEntry".equals(entityBeanKey)) {
            return new UserEntryImpl();
        } else if ("UserEntryReply".equals(entityBeanKey)) {
            return new UserEntryReplyImpl();
        }

        throw new IllegalArgumentException("No entry for : " + entityBeanKey);
    }
}
