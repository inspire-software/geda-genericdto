/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
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


