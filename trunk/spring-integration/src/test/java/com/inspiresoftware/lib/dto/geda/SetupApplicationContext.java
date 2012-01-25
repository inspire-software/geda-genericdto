/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

import com.inspiresoftware.lib.dto.geda.utils.ApplicationContextLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:30:53 PM
 */
@ContextConfiguration("/spring.xml")
public class SetupApplicationContext extends AbstractTestNGSpringContextTests {

    private static final Logger LOG = LoggerFactory.getLogger(SetupApplicationContext.class);

    @Test
    public void testStartContext() {

        ApplicationContextLocator.setApplicationContext(this.applicationContext);

        LOG.info("Spring context is initalised and ready to use: ApplicationContextLocator.getInstance()");

    }

}