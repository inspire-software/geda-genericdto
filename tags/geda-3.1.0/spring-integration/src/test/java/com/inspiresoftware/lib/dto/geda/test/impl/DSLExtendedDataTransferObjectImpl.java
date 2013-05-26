/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.test.ExtendedDataTransferObject;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 2:59:25 PM
 */
public class DSLExtendedDataTransferObjectImpl extends DSLDataTransferObjectImpl implements ExtendedDataTransferObject {

    private String value2;

    public String getValue2() {
        return value2;
    }

    public void setValue2(final String value) {
        this.value2 = value;
    }

}