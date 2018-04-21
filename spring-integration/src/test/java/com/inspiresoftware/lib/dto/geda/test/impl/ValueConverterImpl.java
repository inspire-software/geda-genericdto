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

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.test.ExposedValueConverter;

/**
 * .
 *
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 9:37:29 AM
 */
public class ValueConverterImpl implements ExposedValueConverter {

    private DTOSupport dtoSupport;

    public Object convertToDto(final Object object, final BeanFactory beanFactory) {
        return null;
    }

    public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
        return null;
    }

    public DTOSupport getDtoSupport() {
        return dtoSupport;
    }

    public void setDtoSupport(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }
}
