/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.test.ExtendedDataTransferObject;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 2:59:25 PM
 */
@Dto
public class ExtendedDataTransferObjectImpl extends DataTransferObjectImpl implements ExtendedDataTransferObject {

    @DtoField
    private String value2;

    public String getValue2() {
        return value2;
    }

    public void setValue2(final String value) {
        this.value2 = value;
    }

}