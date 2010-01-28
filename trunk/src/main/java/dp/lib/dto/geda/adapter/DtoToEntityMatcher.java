/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.adapter;

/**
 * Matcher is used with Collection pipe to synchronize the DTO and Entities collection.
 * match true will allow {@link dp.lib.dto.geda.assembler.Pipe} to determine whether
 * the entity has been removed, updated or DTO is to be added as new entity.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 1:05:45 PM
 */
public interface DtoToEntityMatcher<DTO, Entity> {

    /**
     * @param dto DTO to match
     * @param entity Entity to match
     * @return if this DTO matches to Entity
     */
    boolean match(final DTO dto, final Entity entity);

}
