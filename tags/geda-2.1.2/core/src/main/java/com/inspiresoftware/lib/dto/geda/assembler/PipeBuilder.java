/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 6:01 PM
 */
interface PipeBuilder<T extends PipeMetadata> {

    /**
     * Builds a pipe.
     *
     * @param dslRegistry DSL registry
     * @param synthesizer method synthesizer
     * @param dtoClass dto class
     * @param entityClass entity class
     * @param dtoPropertyDescriptors all DTO descriptors.
     * @param entityPropertyDescriptors all entity descriptors
     * @param meta meta data for this pipe
     * @param pipe the pipe to wrap in chain
     *
     * @return data pipe.
     *
     * @throws GeDAException in case of exceptions
     */
    Pipe build(
            Registry dslRegistry,
            MethodSynthesizer synthesizer,
            Class dtoClass, Class entityClass,
            PropertyDescriptor[] dtoPropertyDescriptors,
            PropertyDescriptor[] entityPropertyDescriptors,
            T meta, Pipe pipe)
        throws GeDAException;

}
