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
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertSame;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 3:42:42 PM
 */
public class AdviceConfigStaticFactoryTest {


    @Test
    public void testGuessDtoToEntity() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.DTO_TO_ENTITY,
                        new Class[] { Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);

    }

    @Test
    public void testGuessDtoToEntityMoreParams() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.DTO_TO_ENTITY,
                        new Class[] { Object.class, Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY);

    }

    @Test
    public void testGuessDtoToEntityWithClass() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.DTO_TO_ENTITY,
                        new Class[] { Class.class, Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.DTO_BY_CLASS_TO_ENTITY);

    }

    @Test
    public void testGuessDtoToEntityWithClassMoreParams() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.DTO_TO_ENTITY,
                        new Class[] { Class.class, Object.class, Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.DTO_BY_CLASS_TO_ENTITY);

    }

    @Test
    public void testGuessDtosToEntities() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.DTO_TO_ENTITY,
                        new Class[] { Class.class, List.class, Collection.class }),
                AdviceConfig.DTOSupportMode.DTOS_BY_CLASS_TO_ENTITIES);

    }

    @Test
    public void testGuessDtosToEntitiesMoreParams() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.DTO_TO_ENTITY,
                        new Class[] { Class.class, List.class, Collection.class, Object.class }),
                AdviceConfig.DTOSupportMode.DTOS_BY_CLASS_TO_ENTITIES);

    }

    @Test
    public void testGuessEntityToDto() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.ENTITY_TO_DTO,
                        new Class[] { Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);

    }

    @Test
    public void testGuessEntityToDtoMoreParams() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.ENTITY_TO_DTO,
                        new Class[] { Object.class, Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO);

    }

    @Test
    public void testGuessEntityToDtoWithClass() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.ENTITY_TO_DTO,
                        new Class[] { Class.class, Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_CLASS);

    }

    @Test
    public void testGuessEntityToDtoWithClassMoreParams() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.ENTITY_TO_DTO,
                        new Class[] { Class.class, Object.class, Object.class, Object.class }),
                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_CLASS);

    }

    @Test
    public void testGuessEntitiesToDtos() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.ENTITY_TO_DTO,
                        new Class[] { Class.class, List.class, Collection.class }),
                AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_CLASS);

    }

    @Test
    public void testGuessEntitiesToDtosMoreParams() {

        assertSame(
                AdviceConfigStaticFactory.guessMode(Direction.ENTITY_TO_DTO,
                        new Class[] { Class.class, List.class, Collection.class, Object.class }),
                AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_CLASS);

    }

}
