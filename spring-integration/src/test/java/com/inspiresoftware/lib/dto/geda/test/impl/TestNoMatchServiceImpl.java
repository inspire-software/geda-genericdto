/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Transferable;
import com.inspiresoftware.lib.dto.geda.test.DomainObject;
import com.inspiresoftware.lib.dto.geda.test.ExtendedDataTransferObject;
import com.inspiresoftware.lib.dto.geda.test.TestNoMatchService;
import com.inspiresoftware.lib.dto.geda.test.TestService;

import java.util.Collection;
import java.util.Date;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 29, 2012
 * Time: 8:45:37 PM
 */
public class TestNoMatchServiceImpl implements TestNoMatchService {

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityBeforeExact")
    public void dtoToEntityBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityBefore")
    public void dtoToEntityBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityByFilterBeforeExact")
    public void dtoToEntityByFilterBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityByFilterBefore")
    public void dtoToEntityByFilterBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtosToEntitiesBeforeExact")
    public void dtosToEntitiesBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtosToEntitiesBefore")
    public void dtosToEntitiesBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtosToEntitiesByFilterBeforeExact")
    public void dtosToEntitiesByFilterBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtosToEntitiesByFilterBefore")
    public void dtosToEntitiesByFilterBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityKeyAfterExact")
    public Object dtoToEntityKeyAfterExact(Object dto) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityKeyAfter")
    public Object dtoToEntityKeyAfter(Object dto, Object extra) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityKeyByFilterAfterExact")
    public Object dtoToEntityKeyByFilterAfterExact(Object dto) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityKeyByFilterAfter")
    public Object dtoToEntityKeyByFilterAfter(Object dto, Object extra) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityAfterExact")
    public void dtoToEntityAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityAfter")
    public void dtoToEntityAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityByFilterAfterExact")
    public void dtoToEntityByFilterAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityByFilterAfter")
    public void dtoToEntityByFilterAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtosToEntitiesByFilterAfterExact")
    public void dtosToEntitiesByFilterAfterExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtosToEntitiesByFilterAfter")
    public void dtosToEntitiesByFilterAfter(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtosToEntitiesAfterExact")
    public void dtosToEntitiesAfterExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtosToEntitiesAfter")
    public void dtosToEntitiesAfter(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoBeforeExact")
    public void entityToDtoBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoBefore")
    public void entityToDtoBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoByFilterBeforeExact")
    public void entityToDtoByFilterBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoByFilterBefore")
    public void entityToDtoByFilterBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entitiesToDtosByFilterBeforeExact")
    public void entitiesToDtosByFilterBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entitiesToDtosByFilterBefore")
    public void entitiesToDtosByFilterBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entitiesToDtosBeforeExact")
    public void entitiesToDtosBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entitiesToDtosBefore")
    public void entitiesToDtosBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoKeyAfterExact")
    public Object entityToDtoKeyAfterExact(Object entity) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoKeyAfter")
    public Object entityToDtoKeyAfter(Object entity, Object extra) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoKeyByFilterAfterExact")
    public Object entityToDtoKeyByFilterAfterExact(Object entity) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoKeyByFilterAfter")
    public Object entityToDtoKeyByFilterAfter(Object entity, Object extra) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoAfterExact")
    public void entityToDtoAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoAfter")
    public void entityToDtoAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoByFilterAfterExact")
    public void entityToDtoByFilterAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoByFilterAfter")
    public void entityToDtoByFilterAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entitiesToDtosByFilterAfterExact")
    public void entitiesToDtosByFilterAfterExact(Collection dto, Collection entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entitiesToDtosByFilterAfter")
    public void entitiesToDtosByFilterAfter(Collection dto, Collection entity, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entitiesToDtosAfterExact")
    public void entitiesToDtosAfterExact(Collection dto, Collection entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entitiesToDtosAfter")
    public void entitiesToDtosAfter(Collection dto, Collection entity, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityAndBackToDtoExact")
    public Object dtoToEntityAndBackToDtoExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityAndBackToDto")
    public Object dtoToEntityAndBackToDto(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityAndBackToDtoByFilterExact")
    public Object dtoToEntityAndBackToDtoByFilterExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityAndBackToDtoByFilter")
    public Object dtoToEntityAndBackToDtoByFilter(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityAndBackToDtoVoidExact")
    public void dtoToEntityAndBackToDtoVoidExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityAndBackToDtoVoid")
    public void dtoToEntityAndBackToDtoVoid(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityAndBackToDtoVoidByFilterExact")
    public void dtoToEntityAndBackToDtoVoidByFilterExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityAndBackToDtoVoidByFilter")
    public void dtoToEntityAndBackToDtoVoidByFilter(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoAndBackToEntityExact")
    public Object entityToDtoAndBackToEntityExact(Object targetD, Object sourceE) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoAndBackToEntity")
    public Object entityToDtoAndBackToEntity(Object targetD, Object sourceE, Object extra) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoAndBackToEntityByFilterExact")
    public Object entityToDtoAndBackToEntityByFilterExact(Object targetD, Object sourceE) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoAndBackToEntityByFilter")
    public Object entityToDtoAndBackToEntityByFilter(Object targetD, Object sourceE, Object extra) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoAndBackToEntityVoidExact")
    public void entityToDtoAndBackToEntityVoidExact(Object target, Object source) {
        swapDtoValues(target);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "entityToDtoAndBackToEntityVoid")
    public void entityToDtoAndBackToEntityVoid(Object target, Object source, Object extra) {
        swapDtoValues(target);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoAndBackToEntityVoidByFilterExact")
    public void entityToDtoAndBackToEntityVoidByFilterExact(Object target, Object source) {
        swapDtoValues(target);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "entityToDtoAndBackToEntityVoidByFilter")
    public void entityToDtoAndBackToEntityVoidByFilter(Object target, Object source, Object extra) {
        swapDtoValues(target);
    }

    private void swapEntityValues(final Object targetE) {
        final DomainObject entity = (DomainObject) targetE;
        final String temp = entity.getValue2();
        entity.setValue2(entity.getValue());
        entity.setValue(temp);
        entity.setTimestamp(new Date(System.currentTimeMillis() + 1000L));
    }

    private void swapDtoValues(final Object targetD) {
        final ExtendedDataTransferObject dto = (ExtendedDataTransferObject) targetD;
        final String temp = dto.getValue2();
        dto.setValue2(dto.getValue());
        dto.setValue(temp);
        dto.setTimestamp(new Date(System.currentTimeMillis() + 1000L));
    }


}