/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.blog;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserService;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.BlogBeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.UserDAOImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.UserServiceImpl;
import org.junit.Test;

/**
 * .
 *
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 1:01:07 PM
 */
public class BlogRunnerTest {

    private final UserDAO dao = new UserDAOImpl();
    private final BeanFactory bf = new BlogBeanFactory();
    private final UserService srv = new UserServiceImpl(dao, bf);

    @Test
    public void testBlogExample() {

        final UserDAO dao = new UserDAOImpl();
        final BeanFactory bf = new BlogBeanFactory();
        final UserService srv = new UserServiceImpl(dao, bf);

        final BlogRun run = new BlogRun(dao, srv);

        run.assembleUsersAndBlogEntries();

    }

    @Test
    public void testBlogExampleListFilter() {


        final UserDAO dao = new UserDAOImpl();
        final BeanFactory bf = new BlogBeanFactory();
        final UserService srv = new UserServiceImpl(dao, bf);

        final BlogRun run = new BlogRun(dao, srv);

        run.assembleUsersAndBlogEntriesWithFilter();

    }
}
