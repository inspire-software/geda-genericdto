/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.DTOFactory;
import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import com.inspiresoftware.lib.dto.geda.test.impl.TestServiceImpl;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static com.inspiresoftware.lib.dto.geda.interceptor.impl.TransferableUtils.NO_INDEX;
import static com.inspiresoftware.lib.dto.geda.interceptor.impl.TransferableUtils.RETURN_INDEX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

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
        assertEquals(before.getContext(), "dtoToEntityBeforeExact");

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
        assertEquals(before.getContext(), "dtoToEntityBefore");

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
        assertEquals(before.getContext(), "dtoToEntityByFilterBeforeExact");

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
        assertEquals(before.getContext(), "dtoToEntityByFilterBefore");

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
        assertEquals(before.getContext(), "dtosToEntitiesBeforeExact");

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
        assertEquals(before.getContext(), "dtosToEntitiesBefore");

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
        assertEquals(before.getContext(), "dtosToEntitiesByFilterBeforeExact");

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
        assertEquals(before.getContext(), "dtosToEntitiesByFilterBefore");

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
        assertEquals(after.getContext(), "dtoToEntityKeyAfterExact");

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
        assertEquals(after.getContext(), "dtoToEntityKeyAfter");

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
        assertEquals(after.getContext(), "dtoToEntityKeyByFilterAfterExact");

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
        assertEquals(after.getContext(), "dtoToEntityKeyByFilterAfter");

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
        assertEquals(after.getContext(), "dtoToEntityAfterExact");

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
        assertEquals(after.getContext(), "dtoToEntityAfter");

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
        assertEquals(after.getContext(), "dtoToEntityByFilterAfterExact");

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
        assertEquals(after.getContext(), "dtoToEntityByFilterAfter");

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
        assertEquals(after.getContext(), "dtosToEntitiesByFilterAfterExact");

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
        assertEquals(after.getContext(), "dtosToEntitiesByFilterAfter");

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
        assertEquals(after.getContext(), "dtosToEntitiesAfterExact");

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
        assertEquals(after.getContext(), "dtosToEntitiesAfter");

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
        assertEquals(before.getContext(), "entityToDtoBeforeExact");

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
        assertEquals(before.getContext(), "entityToDtoBefore");

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
        assertEquals(before.getContext(), "entityToDtoByFilterBeforeExact");

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
        assertEquals(before.getContext(), "entityToDtoByFilterBefore");

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
        assertEquals(before.getContext(), "entitiesToDtosByFilterBefore");

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
        assertEquals(before.getContext(), "entitiesToDtosByFilterBeforeExact");

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
        assertEquals(before.getContext(), "entitiesToDtosBeforeExact");

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
        assertEquals(before.getContext(), "entitiesToDtosBefore");

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
        assertEquals(after.getContext(), "entityToDtoKeyAfterExact");

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
        assertEquals(after.getContext(), "entityToDtoKeyAfter");

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
        assertEquals(after.getContext(), "entityToDtoKeyByFilterAfterExact");

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
        assertEquals(after.getContext(), "entityToDtoKeyByFilterAfter");

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
        assertEquals(after.getContext(), "entityToDtoAfterExact");

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
        assertEquals(after.getContext(), "entityToDtoAfter");

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
        assertEquals(after.getContext(), "entityToDtoByFilterAfterExact");

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
        assertEquals(after.getContext(), "entityToDtoByFilterAfter");

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
        assertEquals(after.getContext(), "entitiesToDtosByFilterAfterExact");

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
        assertEquals(after.getContext(), "entitiesToDtosByFilterAfter");

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
        assertEquals(after.getContext(), "entitiesToDtosAfterExact");

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
        assertEquals(after.getContext(), "entitiesToDtosAfter");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoExact");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoExact");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDto");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDto");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoByFilterExact");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoByFilterExact");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoByFilter");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoByFilter");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoVoidExact");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoVoidExact");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoVoid");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoVoid");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoVoidByFilterExact");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoVoidByFilterExact");

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
        assertEquals(before.getContext(), "dtoToEntityAndBackToDtoVoidByFilter");

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
        assertEquals(after.getContext(), "dtoToEntityAndBackToDtoVoidByFilter");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityExact");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityExact");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntity");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntity");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityByFilterExact");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityByFilterExact");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityByFilter");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityByFilter");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityVoidExact");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityVoidExact");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityVoid");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityVoid");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityVoidByFilterExact");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityVoidByFilterExact");

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
        assertEquals(before.getContext(), "entityToDtoAndBackToEntityVoidByFilter");

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
        assertEquals(after.getContext(), "entityToDtoAndBackToEntityVoidByFilter");

    }


    @Test
    public void testNotUsingInfrastructureBeansDtoToEntityMatcher() throws Exception {

        class DtoToEntityMatcherAdapter implements DtoToEntityMatcher {

            public boolean match(final Object o, final Object o1) {
                return false;
            }
        }

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(DtoToEntityMatcherAdapter.class, "match"), DtoToEntityMatcherAdapter.class);

        assertSame(map, Collections.<Object, Object>emptyMap());

    }

    @Test
    public void testNotUsingInfrastructureBeansEntityRetriever() throws Exception {

        class EntityRetrieverAdapter implements EntityRetriever {

            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                return null;
            }
        }

        final Map<Occurrence, AdviceConfig> map =
                TransferableUtils.resolveConfiguration(getMethod(EntityRetrieverAdapter.class, "retrieveByPrimaryKey"), EntityRetrieverAdapter.class);

        assertSame(map, Collections.<Object, Object>emptyMap());

    }

    @Test
    public void testNotUsingInfrastructureBeansValueConverter() throws Exception {

        class ValueConverterAdapter implements ValueConverter {

            public Object convertToDto(final Object object, final BeanFactory beanFactory) {
                return null;
            }

            public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
                return null;
            }
        }

        final Map<Occurrence, AdviceConfig> mapToDto =
                TransferableUtils.resolveConfiguration(getMethod(ValueConverterAdapter.class, "convertToDto"), ValueConverterAdapter.class);

        assertSame(mapToDto, Collections.<Object, Object>emptyMap());

        final Map<Occurrence, AdviceConfig> mapToEntity =
                TransferableUtils.resolveConfiguration(getMethod(ValueConverterAdapter.class, "convertToEntity"), ValueConverterAdapter.class);

        assertSame(mapToEntity, Collections.<Object, Object>emptyMap());

    }

    @Test
    public void testNotUsingInfrastructureBeansGeDAInfrastructure() throws Exception {

        final Map<Occurrence, AdviceConfig> mapToDto =
                TransferableUtils.resolveConfiguration(getMethod(DTOFactory.class, "register"), DTOFactory.class);

        assertSame(mapToDto, Collections.<Object, Object>emptyMap());


    }


}
