/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 10, 2012
 * Time: 11:36:11 AM
 */
public class InspectionInvalidEntityInstanceExceptionTest {

    @Test
    public void testConstructorNullSafe() throws Exception {

        final InspectionInvalidEntityInstanceException exp = new InspectionInvalidEntityInstanceException(null, null);
        assertNull(exp.getClassName());
        assertNull(exp.getEntityName());
    }

    @Test
    public void testConstructor() throws Exception {

        final InspectionInvalidEntityInstanceException exp = new InspectionInvalidEntityInstanceException("java.lang.Object", new Integer(1));
        assertEquals(exp.getClassName(), "java.lang.Object");
        assertEquals(exp.getEntityName(), "java.lang.Integer");
    }
}
