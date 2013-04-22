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

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 3:30 PM
 */
public class BlogRun {

    private final UserDAO dao;
    private final UserService srv;

    public BlogRun(final UserDAO dao, final UserService srv) {
        this.dao = dao;
        this.srv = srv;
    }

    /*
     * Select user Bob.
     */
    public BaseUserDTO selectBob(final List<BaseUserDTO> list) {
        for (final BaseUserDTO user : list) {
            if ("Bob".equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    /*
     * Setup some dummy users with interlinked data.
     */
    public void setupUsers() {

        final User bob = dao.create("Bob");

        final UserEntry entry = bob.createEntry();
        entry.setTitle("GeDA");
        entry.setBody("Hey all, This GeDA stuff really works!!!");

        final User john = dao.create("John");
        final UserEntryReply reply = entry.createReply(john);
        reply.getReplyEntry().setBody("Awesome!");

    }

    /**
     * Running example of services that use GeDA behind the scenes to transfer data
     * between DTO and Entities.
     *
     * This example demonstrates some of the issues with recursive object references and
     * how to get around them by limiting your DTO data size.
     */
    public void assembleUsersAndBlogEntries() {

        setupUsers();

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

    /**
     * Running example of services that use GeDA behind the scenes to transfer data
     * between DTO and Entities.
     *
     * This example demonstrates how you can filter out some of the data by providing
     * parent DTO class as a filter.
     */
    public void assembleUsersAndBlogEntriesWithFilter() {

        setupUsers();

        // Here we load UserDTO but we will be using BaseUserDTO as
        // assembler class so we do not populate the object fully
        final List<UserDTO> list = srv.list("BaseUserDTO");
        assertNotNull(list);
        assertEquals(list.size(), 2);
        assertNull(list.get(0).getEntries());

        // Here we load UserDTO but we will be using BaseUserDTO as
        // assembler class so we do not populate the object fully
        final List<UserDTO> listFull = srv.list("UserDTO");
        assertNotNull(listFull);
        assertEquals(listFull.size(), 2);
        assertNotNull(listFull.get(0).getEntries());
        assertEquals(listFull.get(0).getEntries().size(), 1);


    }

    public static void main(String[] args) {

        final UserDAO dao = new UserDAOImpl();
        final BeanFactory bf = new BlogBeanFactory();
        final UserService srv = new UserServiceImpl(dao, bf);

        final BlogRun run = new BlogRun(dao, srv);

        run.setupUsers();
        run.assembleUsersAndBlogEntries();
        run.assembleUsersAndBlogEntriesWithFilter();

    }
}