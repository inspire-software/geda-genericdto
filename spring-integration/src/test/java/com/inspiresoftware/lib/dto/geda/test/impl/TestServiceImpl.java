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
public class TestServiceImpl implements TestService {

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtoToEntityBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtoToEntityBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtoToEntityByFilterBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtoToEntityByFilterBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtosToEntitiesBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtosToEntitiesBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtosToEntitiesByFilterBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtosToEntitiesByFilterBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object dtoToEntityKeyAfterExact(Object dto) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object dtoToEntityKeyAfter(Object dto, Object extra) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object dtoToEntityKeyByFilterAfterExact(Object dto) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object dtoToEntityKeyByFilterAfter(Object dto, Object extra) {
        return null;
    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtoToEntityAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtoToEntityAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtoToEntityByFilterAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtoToEntityByFilterAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtosToEntitiesByFilterAfterExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtosToEntitiesByFilterAfter(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtosToEntitiesAfterExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtosToEntitiesAfter(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entityToDtoBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entityToDtoBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entityToDtoByFilterBeforeExact(Object dto, Object entity) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entityToDtoByFilterBefore(Object dto, Object entity, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entitiesToDtosByFilterBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entitiesToDtosByFilterBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entitiesToDtosBeforeExact(Collection dto, Collection blankCollection) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entitiesToDtosBefore(Collection dto, Collection blankCollection, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object entityToDtoKeyAfterExact(Object entity) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object entityToDtoKeyAfter(Object entity, Object extra) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object entityToDtoKeyByFilterAfterExact(Object entity) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object entityToDtoKeyByFilterAfter(Object entity, Object extra) {
        return null;
    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entityToDtoAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entityToDtoAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entityToDtoByFilterAfterExact(Object dto, Object entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entityToDtoByFilterAfter(Object dto, Object entity, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entitiesToDtosByFilterAfterExact(Collection dto, Collection entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entitiesToDtosByFilterAfter(Collection dto, Collection entity, Object extra) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entitiesToDtosAfterExact(Collection dto, Collection entity) {

    }

    @Transferable(after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entitiesToDtosAfter(Collection dto, Collection entity, Object extra) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object dtoToEntityAndBackToDtoExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object dtoToEntityAndBackToDto(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object dtoToEntityAndBackToDtoByFilterExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object dtoToEntityAndBackToDtoByFilter(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtoToEntityAndBackToDtoVoidExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey")
    public void dtoToEntityAndBackToDtoVoid(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtoToEntityAndBackToDtoVoidByFilterExact(Object sourceD, Object targetE) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void dtoToEntityAndBackToDtoVoidByFilter(Object sourceD, Object targetE, Object extra) {
        swapEntityValues(targetE);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object entityToDtoAndBackToEntityExact(Object targetD, Object sourceE) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public Object entityToDtoAndBackToEntity(Object targetD, Object sourceE, Object extra) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object entityToDtoAndBackToEntityByFilterExact(Object targetD, Object sourceE) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public Object entityToDtoAndBackToEntityByFilter(Object targetD, Object sourceE, Object extra) {
        swapDtoValues(targetD);
        return null;
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entityToDtoAndBackToEntityVoidExact(Object target, Object source) {
        swapDtoValues(target);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey")
    public void entityToDtoAndBackToEntityVoid(Object target, Object source, Object extra) {
        swapDtoValues(target);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
    public void entityToDtoAndBackToEntityVoidByFilterExact(Object target, Object source) {
        swapDtoValues(target);
    }

    @Transferable(before = Direction.ENTITY_TO_DTO, after = Direction.DTO_TO_ENTITY, entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey")
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
