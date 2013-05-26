/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.annotations.impl;

import com.inspiresoftware.lib.dto.geda.assembler.annotations.AnnotationProxy;

/**
 * Null Object proxy.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-21
 * Time: 11:47 AM
 */
public class NullAnnotationProxy implements AnnotationProxy {

    /** {@inheritDoc} */
    public boolean annotationExists() {
        return false;
    }

    /** {@inheritDoc} */
    public <T> T getValue(final String property) {
        return null;
    }
}
