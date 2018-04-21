
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */


package com.inspiresoftware.lib.dto.geda.adapter;

/**
 * Matcher is used with Collection pipe to synchronize the DTO and Entities collection.
 * match true will allow assembler pipe to determine whether the entity has been removed,
 * updated or DTO is to be added as new entity.
 *
 * 
 * @param <DTO> dto class
 * @param <Entity> entity class
 * 
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 1:05:45 PM
 */
public interface DtoToEntityMatcher<DTO, Entity> extends Adapter {

    /**
     * @param dto DTO to match
     * @param entity Entity to match
     * @return if this DTO matches to Entity
     */
    boolean match(final DTO dto, final Entity entity);

}
