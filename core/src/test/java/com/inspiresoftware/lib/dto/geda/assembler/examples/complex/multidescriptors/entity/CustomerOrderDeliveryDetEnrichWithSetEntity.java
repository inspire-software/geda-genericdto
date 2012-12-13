/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.complex.multidescriptors.entity;


import java.math.BigDecimal;

public class CustomerOrderDeliveryDetEnrichWithSetEntity implements CustomerOrderDeliveryDetEnrichWithSet, java.io.Serializable {


    private BigDecimal qty;

    public CustomerOrderDeliveryDetEnrichWithSetEntity() {
    }


    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}


