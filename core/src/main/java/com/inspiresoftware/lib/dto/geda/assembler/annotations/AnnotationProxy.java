/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.annotations;

/**
 * Annotation proxy allows to wrap the annotation, so that we can extract
 * data event from annotations provided by different class loaders.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-21
 * Time: 11:35 AM
 */
public interface AnnotationProxy {

    /**
     * Helper flag to see if this proxy actually has access to a valid
     * annotation. This is to enforce NullObject pattern to annotation
     * discovery mechanism.
     *
     * @return true if annotation actually defined (false otherwise)
     */
    boolean annotationExists();

    /**
     * Return value of the annotation property.
     *
     * @param property property of a GeDA annotation.
     *
     * @return value
     */
    public <T> T getValue(String property);

}
