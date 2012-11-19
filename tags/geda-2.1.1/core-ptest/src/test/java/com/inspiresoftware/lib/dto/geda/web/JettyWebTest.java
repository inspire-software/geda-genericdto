/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.web;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Feb 18, 2012
 * Time: 7:04:58 PM
 */
public class JettyWebTest {


    private static final Logger LOG = LoggerFactory.getLogger(GeDAServlet.class);

    private static ServletTester tester;

    @BeforeClass
    public static void initServletContainer () throws Exception {
        tester = new ServletTester();
        tester.setContextPath("/");
        tester.addServlet(GeDAServlet.class, "/");
        tester.start();
    }

    @Test
    public void testPost () throws Exception {

        final HttpTester request = new HttpTester();
        final HttpTester response = new HttpTester();

        request.setMethod("GET");
        request.setHeader("Host", "tester");
        request.setVersion("HTTP/1.0");
        request.setURI("/");

        response.parse(tester.getResponses(request.generate()));
        final String stats = response.getContent();

        System.out.println(stats);

    }

    @AfterClass
    public static void cleanupServletContainer () throws Exception {
        tester.stop();
    }


}
