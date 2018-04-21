/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.event;

/**
 * Simple event interface that allows to act upon events produced by the DTOSupport
 * implementor.
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 3:07:55 PM
 */
public interface DTOEventListener {

    /**
     * This method will be invoked by the DTOSupport implementor depending on the settings
     * defined in the context.xml.
     *
     * As a special case with handling erroneous assembly this listener allows to swallow
     * the exception and return null (if the exception passed as context is not re-thrown
     * from onEvent() method)
     *
     * @param context context is DTO, DO (Domain Object) and Exception if any
     */
    void onEvent(Object... context);

}
