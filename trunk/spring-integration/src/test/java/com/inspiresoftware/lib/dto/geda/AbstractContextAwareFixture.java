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
import org.springframework.context.ApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:27:40 PM
 */
public class AbstractContextAwareFixture {

    /** Access to current Spring context. */
    protected ApplicationContext ctx;



    @BeforeClass(alwaysRun = true)
    public void setUp() {
        ctx = ApplicationContextLocator.getInstance();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        ctx = null;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest() {
        // clean up?
    }


}
