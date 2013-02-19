/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;

import java.util.Collection;

/**
 * User: denispavlov
 * Date: 13-02-18
 * Time: 6:11 PM
 */
public interface DTOSupportAnnotationsService {

    /**
     * Assemble dto from entity object
     *
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final DTO dto = support.assembleDto(new DTO(), entity);
     */
    <T> T assembleDto(T dto, Object entity, BeanFactory beanFactory, String context);

    /**
     * Assemble new dto from entity object
     *
     * @param dtoKey dto key (must not be null)
     * @param entity entity (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return DTO object with data
     */
    <T> T assembleDtoByKey(String dtoKey, Object entity, BeanFactory beanFactory, String context);

    /**
     * Assemble dto from entity object
     *
     * @param dtoFilter specify the class level (can be used as a filter if there are DTO hierarchies)
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return convenience return object (same as dto param). Used for the
     *         following: final DTO dto = support.assembleDto(new DTO(), entity);
     */
    <T> T assembleDto(String dtoFilter, T dto, Object entity, BeanFactory beanFactory, String context);

    /**
     * Assemble dto from entity object
     *
     * @param dtoFilter specify the class level (can be used as a filter if there are DTO hierarchies)
     * @param dtoKey dto key (must not be null)
     * @param entity entity (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return DTO object with data
     */
    <T> T assembleDtoByKey(String dtoFilter, String dtoKey, Object entity, BeanFactory beanFactory, String context);

    /**
     * Assemble dto from entity object
     *
     * @param dtoKey dto object key
     * @param dtos dto collection (must not be null)
     * @param entities entities collection (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     */
    <T> void assembleDtos(String dtoKey, Collection<T> dtos, Collection entities, BeanFactory beanFactory, String context);

    /**
     * Assemble dto from entity object
     *
     * @param dtoFilter specify the class level (can be used as a filter if there are DTO hierarchies)
     * @param dtoKey dto object key
     * @param dtos dto collection (must not be null)
     * @param entities entities collection (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     */
    <T> void assembleDtos(String dtoFilter, String dtoKey, Collection<T> dtos, Collection entities, BeanFactory beanFactory, String context);

    /**
     * Assemble entity from dto object
     *
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return convenience return object (same as entity param). Used for the
     *         following: final Object dto = support.assembleEntity(dto, new Object());
     */
    <T> T assembleEntity(Object dto, T entity, BeanFactory beanFactory, String context);

    /**
     * Assemble entity from dto object
     *
     * @param dto dto (must not be null)
     * @param entityKey entity key (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return new entity
     */
    <T> T assembleEntityByKey(Object dto, String entityKey, BeanFactory beanFactory, String context);

    /**
     * Assemble entity from dto object
     *
     * @param dtoFilter specify the class level
     * @param dto dto (must not be null)
     * @param entity entity (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return convenience return object (same as entity param). Used for the
     *         following: final Object dto = support.assembleEntity(dto, new Object());
     */
    <T> T assembleEntity(String dtoFilter, Object dto, T entity, BeanFactory beanFactory, String context);

    /**
     * Assemble entity from dto object
     *
     * @param dtoFilter specify the class level
     * @param dto dto (must not be null)
     * @param entityKey entity key (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     *
     * @return new entity
     */
    <T> T assembleEntityByKey(String dtoFilter, Object dto, String entityKey, BeanFactory beanFactory, String context);

    /**
     * Assemble entities from dto objects
     *
     * @param entityKey entity object key
     * @param dtos dto collection (must not be null)
     * @param entities entities collection (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     */
    <T> void assembleEntities(String entityKey, Collection dtos, Collection<T> entities, BeanFactory beanFactory, String context);

    /**
     * Assemble entities from dto objects
     *
     * @param dtoFilter specify the class level
     * @param entityKey entity object key
     * @param dtos dto collection (must not be null)
     * @param entities entities collection (must not be null)
     * @param beanFactory bean factory
     * @param context transfer context
     */
    <T> void assembleEntities(String dtoFilter, String entityKey, Collection dtos, Collection<T> entities, BeanFactory beanFactory, String context);

    /**
     * Enhance runtime DTO converters runtime model. This method is intended to be used in
     * service bean constructors that need to provide additional implementation specific
     * conversions (such as retrievers for example).
     *
     * @param key converter key (as provided in DTO mapping annotations)
     * @param adapter either converter of a retriever.
     */
    void registerAdapter(final String key, final Object adapter);

    /**
     * Event listener.
     *
     * @param dtoEvent event name
     * @param eventListener listener to add
     */
    void addListener(String dtoEvent, DTOEventListener eventListener);

    /**
     * Remove event listener.
     *
     * @param eventListener listener to remove
     */
    void removeListener(DTOEventListener eventListener);

}
