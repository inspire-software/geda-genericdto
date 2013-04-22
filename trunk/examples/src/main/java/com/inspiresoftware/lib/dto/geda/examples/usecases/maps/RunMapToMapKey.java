/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.maps;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 11:49 PM
 */
public class RunMapToMapKey {

    /**
     * Example of alternative map setup where entity map key are sub entities and map
     * entity values are associated values.
     */
    public void mapToMapKeyMapping() {
        final EntityItemInterface eItem1 = new EntityItemClass();
        eItem1.setName("itm1");
        final EntityItemInterface eItem2 = new EntityItemClass();
        eItem2.setName("itm2");

        final EntityMapByKeyInterface eMap = new EntityMapByKeyClass();
        eMap.setItems(new HashMap<EntityItemInterface, String>());
        eMap.getItems().put(eItem1, "itm1");
        eMap.getItems().put(eItem2, "itm2");

        final DtoMapByKeyIterface dMap = new DtoMapToMapByKeyClass();

        final Assembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eMap.getClass());

        assembler.assembleDto(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return DtoItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return new DtoItemClass();
                }
                return null;
            }

        });

        assertNotNull(dMap.getItems());
        assertEquals(2, dMap.getItems().size());
        final Set<DtoItemIterface> keys = dMap.getItems().keySet();
        DtoItemIterface dItem1 = null;
        DtoItemIterface dItem2 = null;
        for (DtoItemIterface key : keys) {
            if ("itm1".equals(key.getName())) {
                assertEquals("itm1", dMap.getItems().get(key));
                dItem1 = key;
            } else if ("itm2".equals(key.getName())) {
                assertEquals("itm2", dMap.getItems().get(key));
                dItem2 = key;
            } else {
                fail("Unknown key");
            }
        }

        final DtoItemClass dto3 = new DtoItemClass();
        dto3.setName("itm3");
        dMap.getItems().put(dto3, "itm3");

        dMap.getItems().put(dItem2, "itm no 2");

        dMap.getItems().remove(dItem1); // first

        assembler.assembleEntity(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("entityItem".equals(entityBeanKey)) {
                    return EntityItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("entityItem".equals(entityBeanKey)) {
                    return new EntityItemClass();
                }
                return null;
            }

        });

        assertNotNull(eMap.getItems());
        assertEquals(2, eMap.getItems().size());

        final Set<EntityItemInterface> ekeys = eMap.getItems().keySet();
        for (EntityItemInterface key : ekeys) {
            if ("itm2".equals(key.getName())) {
                assertEquals("itm no 2", eMap.getItems().get(key));
            } else if ("itm3".equals(key.getName())) {
                assertEquals("itm3", eMap.getItems().get(key));
            } else {
                fail("Unknown key");
            }
        }

    }


    public static void main(String[] args) {
        new RunMapToMapKey().mapToMapKeyMapping();
    }


}
