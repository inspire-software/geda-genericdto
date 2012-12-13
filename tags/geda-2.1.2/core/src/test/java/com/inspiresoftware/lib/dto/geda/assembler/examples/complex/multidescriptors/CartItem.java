/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors;

import java.io.Serializable;
import java.math.BigDecimal;

public interface CartItem extends Serializable {

    /**
     * @return quantity of the above sku to be purchased
     */
    BigDecimal getQty();

}