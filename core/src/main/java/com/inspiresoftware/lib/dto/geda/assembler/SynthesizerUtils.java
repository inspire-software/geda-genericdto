/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

import java.lang.reflect.Type;

/**
 * These utils classes should expose some of the internal functions for synthesizers.
 *
 * User: denispavlov
 * Date: 13-04-20
 * Time: 12:55 PM
 */
public class SynthesizerUtils {

    /**
     * Determine the actual class for this type.
     *
     * @param type generic type
     * @return class for this generic type
     *
     * @throws GeDARuntimeException if generics tree is too complex
     */
    protected Class getClassForType(Type type) throws GeDARuntimeException {
        return PropertyInspector.getClassForType(type);
    }

}
