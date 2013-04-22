/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 7:07 PM
 */
public class RunWithGenerics {

    /**
     * Example showing how to use GeDA with generics. The answer is that there is no difference
     * but you do need to at least distinguish between fields, collections and maps.
     */
    public void genericMapping() throws GeDAException {

        final DtoGenericClass<String, Collection<DtoGenericItemClass<String>>, Map<String, DtoGenericItemClass<String>>> dto = new DtoClass();
        final EntityClass entity = new EntityClass();

        final Assembler assembler = DTOAssembler.newAssembler(dto.getClass(), entity.getClass());

        final DtoGenericItemClass<String> item = new DtoGenericItemClass<String>();
        item.setMyProp("item");

        dto.setMyProp("prop");
        final List<DtoGenericItemClass<String>> coll = new ArrayList<DtoGenericItemClass<String>>();
        coll.add(item);
        dto.setMyColl(coll);
        final Map<String, DtoGenericItemClass<String>> map = new HashMap<String, DtoGenericItemClass<String>>();
        map.put("m1", item);
        dto.setMyMap(map);

        assembler.assembleEntity(dto, entity, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                return new EntityGenericItemClass<String>();
            }

        });

        assertEquals("prop", entity.getMyProp());

        assertNotNull(entity.getMyColl());
        assertNotSame(dto.getMyColl(), entity.getMyColl());
        assertEquals(1, entity.getMyColl().size());
        assertEquals("item", entity.getMyColl().iterator().next().getMyProp());

        assertNotNull(entity.getMyMap());
        assertNotSame(dto.getMyMap(), entity.getMyMap());
        assertEquals(1, entity.getMyMap().size());
        assertEquals("item", entity.getMyMap().iterator().next().getMyProp());

        entity.setMyProp("e1");
        entity.getMyColl().iterator().next().setMyProp("ci1");
        entity.getMyMap().iterator().next().setMyProp("mi1");

        assembler.assembleDto(dto, entity, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                return new DtoGenericItemClass<String>();
            }

        });

        assertEquals("e1", dto.getMyProp());

        assertNotNull(dto.getMyColl());
        assertNotSame(dto.getMyColl(), entity.getMyColl());
        assertEquals(1, dto.getMyColl().size());
        assertEquals("ci1", dto.getMyColl().iterator().next().getMyProp());

        assertNotNull(entity.getMyMap());
        assertNotSame(dto.getMyMap(), entity.getMyMap());
        assertEquals(1, dto.getMyMap().size());
        assertTrue(dto.getMyMap().containsKey("mi1"));
        assertEquals("mi1", dto.getMyMap().get("mi1").getMyProp());


    }

    public static void main(String[] args) {
        new RunWithGenerics().genericMapping();
    }


}
