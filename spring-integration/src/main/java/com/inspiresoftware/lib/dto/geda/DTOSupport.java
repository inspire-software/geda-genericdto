/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

import java.util.Collection;

/**
 * Facade for working with DTO to entity conversion that is application aware and
 * uses the GeDA assembler.
 * <p/>
 * User: denispavlov
 * Date: Sep 27, 2011
 * Time: 8:29:02 AM
 */
public interface DTOSupport {

    /**
     * Assemble dto from entity object
     *
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final DTO dto = support.assembleDto(new DTO(), entity);
     */
    <T> T assembleDto(T dto, Object entity);

    /**
     * Assemble dto from entity object
     *
     * @param dtoClass specify the class level (can be used as a filter if there are DTO hierarchies)
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final DTO dto = support.assembleDto(new DTO(), entity);
     */
    <T> T assembleDto(Class dtoClass, T dto, Object entity);

    /**
     * Assemble dto from entity object
     *
     * @param dtoClass specify the class level (can be used as a filter if there are DTO hierarchies)
     * @param dtos dto collection (must not be null)
     * @param entities entities collection (must not be null)
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final Collection<DTO> dtos = support.assembleDtos(DTO.class, dtos, entities);
     */
    <T> Collection<T> assembleDtos(Class dtoClass, Collection<T> dtos, Collection entities);

    /**
     * Assemble entity from dto object
     *
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     *
     * @return convenience return object (same as entity param). Used for the
     *         following: final Object dto = support.assembleEntity(dto, new Object());
     */
    <T> T assembleEntity(Object dto, T entity);

    /**
     * Assemble entity from dto object
     *
     * @param dtoClass specify the class level
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     *
     * @return convenience return object (same as entity param). Used for the
     *         following: final Object dto = support.assembleEntity(dto, new Object());
     */
    <T> T assembleEntity(Class dtoClass, Object dto, T entity);

    /**
     * Assemble entities from dto objects
     *
     * @param entityClass specify the class level
     * @param dtos dto collection (must not be null)
     * @param entities entities collection (must not be null)
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final Collection<DTO> dtos = support.assembleDtos(DTO.class, dtos, entities);
     */
    <T> Collection<T> assembleEntities(Class entityClass, Collection dtos, Collection<T> entities);

    /**
     * Enhance runtime DTO converters runtime model. This method is intended to be used in
     * service bean constructors that need to provide additional implementation specific
     * conversions (such as retrievers for example).
     *
     * @param key converter key (as provided in DTO mapping annotations)
     * @param converter either converter of a retriever.
     */
    void registerValueConverter(final String key, final Object converter);


}
