/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserService;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.BlogBeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.UserDAOImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.UserServiceImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:01:07 PM
 */
public class BlogExampleRun {

    private final UserDAO dao = new UserDAOImpl();
    private final BeanFactory bf = new BlogBeanFactory();
    private final UserService srv = new UserServiceImpl(dao, bf);

    @Test
    public void testBlogExample() {

        this.setupUsers();

        final List<BaseUserDTO> list = srv.list();
        assertNotNull(list);
        assertEquals(list.size(), 2);

        // imitate user found Bob's account in list
        final BaseUserDTO bob = selectBob(list);
        assertNotNull(bob);

        // user clicks the Bob's account entry
        final UserDTO bobFull = srv.findUser(bob.getUserId());
        assertNotNull(bobFull);

        assertNotNull(bobFull.getEntries());
        assertEquals(bobFull.getEntries().size(), 1);

        // user selects first entry
        final BaseUserEntryDTO bobEntry = bobFull.getEntries().get(0);
        assertNotNull(bobEntry);

        // user clicks bob's entry to see replies
        final UserEntryDTO bobEntryFull = srv.findEntry(bobEntry.getEntryId());
        assertNotNull(bobEntryFull);
        assertEquals(bobEntryFull.getTitle(), "GeDA");
        assertEquals(bobEntryFull.getBody(), "Hey all, This GeDA stuff really works!!!");
        assertNotNull(bobEntryFull.getReplies());
        assertEquals(bobEntryFull.getReplies().size(), 1);


        // user finds the reply
        final BaseUserEntryDTO replyToBob = bobEntryFull.getReplies().get(0);
        assertNotNull(replyToBob);
        assertEquals(replyToBob.getTitle(), "RE: GeDA");
        assertEquals(replyToBob.getBody(), "Awesome!");

        // user clicks the reply to see full details
        final UserEntryDTO replyToBobFull = srv.findEntry(replyToBob.getEntryId());
        assertNotNull(replyToBobFull);
        assertNotNull(replyToBobFull.getUser());
        assertEquals(replyToBobFull.getUser().getUsername(), "John");

        // and so on...

    }

    private BaseUserDTO selectBob(final List<BaseUserDTO> list) {
        for (final BaseUserDTO user : list) {
            if ("Bob".equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    private void setupUsers() {

        final User bob = dao.create("Bob");

        final UserEntry entry = bob.createEntry();
        entry.setTitle("GeDA");
        entry.setBody("Hey all, This GeDA stuff really works!!!");

        final User john = dao.create("John");
        final UserEntryReply reply = entry.createReply(john);
        reply.getReplyEntry().setBody("Awesome!");

    }

    @Test
    public void testBlogExampleListFilter() {

        this.setupUsers();

        // Here we load UserDTO but we will be using BaseUserDTO as
        // assembler class so we do not populate the object fully
        final List<UserDTO> list = srv.list("BaseUserDTO");
        assertNotNull(list);
        assertEquals(list.size(), 2);

    }
}
