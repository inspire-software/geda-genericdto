/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi.test;

import java.math.BigDecimal;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 8:20 AM
 */
public interface SimpleDTO {

    String getString();

    void setString(String string);

    BigDecimal getDecimal();

    void setDecimal(BigDecimal decimal);

    int getInteger();

    void setInteger(int integer);
}
