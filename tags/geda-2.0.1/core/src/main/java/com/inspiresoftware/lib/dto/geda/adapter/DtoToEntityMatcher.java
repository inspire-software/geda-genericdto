
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.adapter;

/**
 * Matcher is used with Collection pipe to synchronize the DTO and Entities collection.
 * match true will allow {@link com.inspiresoftware.lib.dto.geda.assembler.Pipe} to determine whether
 * the entity has been removed, updated or DTO is to be added as new entity.
 * <p/>
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
