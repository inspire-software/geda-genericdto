/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import com.inspiresoftware.lib.dto.geda.test.impl.TestServiceImpl;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static com.inspiresoftware.lib.dto.geda.interceptor.impl.TransferableUtils.NO_INDEX;
import static com.inspiresoftware.lib.dto.geda.interceptor.impl.TransferableUtils.RETURN_INDEX;
import static org.testng.Assert.assertEquals;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 29, 2012
 * Time: 7:52:54 PM
 */
public class TransferableUtilsTest {

    private Method getMethod(Class testClass, String method) {
        final Method[] methods = testClass.getMethods();
        for (final Method meth : methods) {
            if (meth.getName().equals(method)) {
                return meth;
            }
        }
        return null;
    }

    @Test
    public void testResolveDtoToEntityBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityBeforeExact"), TestServiceImpl.class);

        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityBefore"), TestServiceImpl.class);

        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityByFilterBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityByFilterBeforeExact"), TestServiceImpl.class);

        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityByFilterBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityByFilterBefore"), TestServiceImpl.class);

        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesBeforeExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "entityKey");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesBefore"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "entityKey");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesByFilterBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesByFilterBeforeExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "entityKey");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesByFilterBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesByFilterBefore"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "entityKey");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityKeyAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityKeyAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveDtoToEntityKeyAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityKeyAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveDtoToEntityKeyByFilterAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityKeyByFilterAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveDtoToEntityKeyByFilterAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityKeyByFilterAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityByFilterAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityByFilterAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtoToEntityByFilterAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityByFilterAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesByFilterAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesByFilterAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES_BY_FILTER);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesByFilterAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesByFilterAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES_BY_FILTER);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveDtosToEntitiesAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtosToEntitiesAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveEntityToDtoBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoBeforeExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoBefore"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoByFilterBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoByFilterBeforeExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoByFilterBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoByFilterBefore"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosByFilterBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosByFilterBefore"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_FILTER);
        assertEquals(before.getDtoKey(), "dtoKey");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosByFilterBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosByFilterBeforeExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_FILTER);
        assertEquals(before.getDtoKey(), "dtoKey");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosBeforeExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosBeforeExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS);
        assertEquals(before.getDtoKey(), "dtoKey");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosBefore() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosBefore"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS);
        assertEquals(before.getDtoKey(), "dtoKey");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoKeyAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoKeyAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 0);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoKeyAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoKeyAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 0);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoKeyByFilterAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoKeyByFilterAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 0);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoKeyByFilterAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoKeyByFilterAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 0);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoByFilterAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoByFilterAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoByFilterAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoByFilterAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosByFilterAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosByFilterAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_FILTER);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosByFilterAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosByFilterAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_FILTER);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosAfterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosAfterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntitiesToDtosAfter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entitiesToDtosAfter"), TestServiceImpl.class);


        assertEquals(map.size(), 1);
        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDtoExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);
        
        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDto() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDto"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDtoByFilterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoByFilterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDtoByFilter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoByFilter"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER);
        assertEquals(after.getDtoKey(), "dtoKey");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), RETURN_INDEX);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDtoVoidExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoVoidExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDtoVoid() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoVoid"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveDtoToEntityAndBackToDtoVoidByFilterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoVoidByFilterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }


    @Test
    public void testResolveDtoToEntityAndBackToDtoVoidByFilter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "dtoToEntityAndBackToDtoVoidByFilter"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), 0);
        assertEquals(before.getDtoTargetIndex(), NO_INDEX);
        assertEquals(before.getEntitySourceIndex(), NO_INDEX);
        assertEquals(before.getEntityTargetIndex(), 1);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), NO_INDEX);
        assertEquals(after.getDtoTargetIndex(), 0);
        assertEquals(after.getEntitySourceIndex(), 1);
        assertEquals(after.getEntityTargetIndex(), NO_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntity() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntity"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityByFilterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityByFilterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityByFilter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityByFilter"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "entityKey");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), RETURN_INDEX);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityVoidExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityVoidExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityVoid() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityVoid"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityVoidByFilterExact() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityVoidByFilterExact"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }

    @Test
    public void testResolveEntityToDtoAndBackToEntityVoidByFilter() throws Exception {

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(TestServiceImpl.class, "entityToDtoAndBackToEntityVoidByFilter"), TestServiceImpl.class);


        assertEquals(map.size(), 2);

        final AdviceConfig before = map.get(Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDirection(), Direction.ENTITY_TO_DTO);
        assertEquals(before.getOccurrence(), Occurrence.BEFORE_METHOD_INVOCATION);
        assertEquals(before.getDtoSupportMode(), AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER);
        assertEquals(before.getDtoKey(), "");
        assertEquals(before.getEntityKey(), "");
        assertEquals(before.getDtoFilterKey(), "filterKey");
        assertEquals(before.getDtoSourceIndex(), NO_INDEX);
        assertEquals(before.getDtoTargetIndex(), 0);
        assertEquals(before.getEntitySourceIndex(), 1);
        assertEquals(before.getEntityTargetIndex(), NO_INDEX);

        final AdviceConfig after = map.get(Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDirection(), Direction.DTO_TO_ENTITY);
        assertEquals(after.getOccurrence(), Occurrence.AFTER_METHOD_INVOCATION);
        assertEquals(after.getDtoSupportMode(), AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY);
        assertEquals(after.getDtoKey(), "");
        assertEquals(after.getEntityKey(), "");
        assertEquals(after.getDtoFilterKey(), "filterKey");
        assertEquals(after.getDtoSourceIndex(), 0);
        assertEquals(after.getDtoTargetIndex(), NO_INDEX);
        assertEquals(after.getEntitySourceIndex(), NO_INDEX);
        assertEquals(after.getEntityTargetIndex(), 1);

    }
}
