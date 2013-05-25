/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

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
     * @param context Assembler context
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
            AssemblerContext context,
            Class dtoClass,
            Class entityClass,
            PropertyDescriptor[] dtoPropertyDescriptors,
            PropertyDescriptor[] entityPropertyDescriptors,
            T meta,
            Pipe pipe)
        throws GeDAException;

}
