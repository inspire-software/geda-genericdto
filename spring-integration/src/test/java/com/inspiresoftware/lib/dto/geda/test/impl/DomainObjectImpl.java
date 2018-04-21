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

import com.inspiresoftware.lib.dto.geda.test.DomainObject;

import java.util.Date;

/**
 * .
 *
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 2:59:25 PM
 */
public class DomainObjectImpl implements DomainObject {

    private String value;
    private String value2;
    private Date timestamp;

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(final String value2) {
        this.value2 = value2;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
}