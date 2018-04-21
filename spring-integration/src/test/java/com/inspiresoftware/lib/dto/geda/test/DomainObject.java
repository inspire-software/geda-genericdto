/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.test;

import java.util.Date;

/**
 * .
 *
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 2:56:58 PM
 */
public interface DomainObject {

    String getValue();

    void setValue(String value);

    String getValue2();

    void setValue2(String value);

    Date getTimestamp();

    void setTimestamp(Date timestamp);

}