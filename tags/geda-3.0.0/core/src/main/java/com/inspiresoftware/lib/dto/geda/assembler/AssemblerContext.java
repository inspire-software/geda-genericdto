/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.exception.GeDARuntimeException;

/**
 * Internal interface to pass though Assembler contexts.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-22
 * Time: 12:06 PM
 */
interface AssemblerContext {

    /**
     * Creates new DTO assembler.
     *
     * @param dto dto class
     * @param entity entity class
     *
     * @return new assmbler instance
     */
    Assembler newAssembler(final Class< ? > dto, final Class< ? > entity);

    /**
     * Return method synthesizer used for this Assembler graph.
     *
     * @return method synthesizer
     */
    MethodSynthesizer getMethodSynthesizer();

    /**
     * Returns DSL registry used for this Assembler graph.
     *
     * @return DSL registry
     */
    Registry getDslRegistry();

    /**
     * Class loader used for this Assembler graph.
     *
     * @return class loader
     *
     * @throws GeDARuntimeException SoftReferences are used to keep GC tidy, so
     *         this may throw a GeDARuntimeException.
     */
    ClassLoader getClassLoader() throws GeDARuntimeException;

}
