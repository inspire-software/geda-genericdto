package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Transferable;

import java.util.Collection;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Feb 2, 2012
 * Time: 2:04:08 PM
 */
public interface TestService {

    void dtoToEntityBeforeExact(Object dto, Object entity);

    void dtoToEntityBefore(Object dto, Object entity, Object extra);

    void dtoToEntityByFilterBeforeExact(Object dto, Object entity);

    void dtoToEntityByFilterBefore(Object dto, Object entity, Object extra);

    void dtosToEntitiesBeforeExact(Collection dto, Collection blankCollection);

    void dtosToEntitiesBefore(Collection dto, Collection blankCollection, Object extra);

    void dtosToEntitiesByFilterBeforeExact(Collection dto, Collection blankCollection);

    void dtosToEntitiesByFilterBefore(Collection dto, Collection blankCollection, Object extra);

    Object dtoToEntityKeyAfterExact(Object dto);

    Object dtoToEntityKeyAfter(Object dto, Object extra);

    Object dtoToEntityKeyByFilterAfterExact(Object dto);

    Object dtoToEntityKeyByFilterAfter(Object dto, Object extra);

    void dtoToEntityAfterExact(Object dto, Object entity);

    void dtoToEntityAfter(Object dto, Object entity, Object extra);

    void dtoToEntityByFilterAfterExact(Object dto, Object entity);

    void dtoToEntityByFilterAfter(Object dto, Object entity, Object extra);

    void dtosToEntitiesByFilterAfterExact(Collection dto, Collection blankCollection);

    void dtosToEntitiesByFilterAfter(Collection dto, Collection blankCollection, Object extra);

    void dtosToEntitiesAfterExact(Collection dto, Collection blankCollection);

    void dtosToEntitiesAfter(Collection dto, Collection blankCollection, Object extra);

    void entityToDtoBeforeExact(Object dto, Object entity);

    void entityToDtoBefore(Object dto, Object entity, Object extra);

    void entityToDtoByFilterBeforeExact(Object dto, Object entity);

    void entityToDtoByFilterBefore(Object dto, Object entity, Object extra);

    void entitiesToDtosByFilterBeforeExact(Collection dto, Collection blankCollection);

    void entitiesToDtosByFilterBefore(Collection dto, Collection blankCollection, Object extra);

    void entitiesToDtosBeforeExact(Collection dto, Collection blankCollection);

    void entitiesToDtosBefore(Collection dto, Collection blankCollection, Object extra);

    Object entityToDtoKeyAfterExact(Object entity);

    Object entityToDtoKeyAfter(Object entity, Object extra);

    Object entityToDtoKeyByFilterAfterExact(Object entity);

    Object entityToDtoKeyByFilterAfter(Object entity, Object extra);

    void entityToDtoAfterExact(Object dto, Object entity);

    void entityToDtoAfter(Object dto, Object entity, Object extra);

    void entityToDtoByFilterAfterExact(Object dto, Object entity);

    void entityToDtoByFilterAfter(Object dto, Object entity, Object extra);

    void entitiesToDtosByFilterAfterExact(Collection dto, Collection entity);

    void entitiesToDtosByFilterAfter(Collection dto, Collection entity, Object extra);

    void entitiesToDtosAfterExact(Collection dto, Collection entity);

    void entitiesToDtosAfter(Collection dto, Collection entity, Object extra);

    Object dtoToEntityAndBackToDtoExact(Object sourceD, Object targetE);

    Object dtoToEntityAndBackToDto(Object sourceD, Object targetE, Object extra);

    Object dtoToEntityAndBackToDtoByFilterExact(Object sourceD, Object targetE);

    Object dtoToEntityAndBackToDtoByFilter(Object sourceD, Object targetE, Object extra);

    void dtoToEntityAndBackToDtoVoidExact(Object sourceD, Object targetE);

    void dtoToEntityAndBackToDtoVoid(Object sourceD, Object targetE, Object extra);

    void dtoToEntityAndBackToDtoVoidByFilterExact(Object sourceD, Object targetE);

    void dtoToEntityAndBackToDtoVoidByFilter(Object sourceD, Object targetE, Object extra);

    Object entityToDtoAndBackToEntityExact(Object targetD, Object sourceE);

    Object entityToDtoAndBackToEntity(Object targetD, Object sourceE, Object extra);

    Object entityToDtoAndBackToEntityByFilterExact(Object targetD, Object sourceE);

    Object entityToDtoAndBackToEntityByFilter(Object targetD, Object sourceE, Object extra);

    void entityToDtoAndBackToEntityVoidExact(Object target, Object source);

    void entityToDtoAndBackToEntityVoid(Object target, Object source, Object extra);

    void entityToDtoAndBackToEntityVoidByFilterExact(Object target, Object source);

    void entityToDtoAndBackToEntityVoidByFilter(Object target, Object source, Object extra);
}
