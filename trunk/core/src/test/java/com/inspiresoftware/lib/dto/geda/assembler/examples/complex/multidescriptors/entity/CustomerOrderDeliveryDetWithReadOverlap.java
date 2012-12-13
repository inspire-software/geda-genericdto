/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.entity;

import com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.CartItem;

import java.math.BigDecimal;

public interface CustomerOrderDeliveryDetWithReadOverlap extends CartItem {

    /**
     * Get quantity of SKU.
     *
     * @return qty
     */
    BigDecimal getQty();

    /**
     * Set  quantity of sku.
     *
     * @param qty quantity of sku.
     */
    void setQty(BigDecimal qty);

}