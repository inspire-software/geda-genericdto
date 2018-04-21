/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark;

/**
 * Simple adapter to unify transfer process.
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:22:37 AM
 */
public interface Mapper {

    /**
     * Convert entity to DTO.
     *
     * @param entity entity object
     * @return dto
     */
    Object fromEntity(Object entity);

    /**
     * Assemble entity from DTO
     *
     * @param dto dto
     * @return entity with data
     */
    Object fromDto(Object dto);

}
