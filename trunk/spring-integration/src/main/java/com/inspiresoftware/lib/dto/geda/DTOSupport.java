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
import java.util.List;

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
     * @param dtoClass specify the class level
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
     * @param dtoClass specify the class level
     * @param dtos dto colelction (must not be null)
     * @param entities entities collection (must not be null)
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final DTO dto = support.assembleDto(new DTO(), entity);
     */
    <T> List<T> assembleDtos(Class dtoClass, List<T> dtos, Collection entities);

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
     * Enhance runtime DTO converters runtime model. This method is intended to be used in
     * service bean constructors that need to provide additional inmplementation specific
     * conversions (such as retrievers for example).
     *
     * @param key converter key (as provided in DTO mapping annotations)
     * @param converter either conveterter of a retriever.
     */
    void registerValueConverter(final String key, final Object converter);


}
