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
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 11:55 PM
 */
public class RunMapToCollection {

    /**
     * Example of advanced conversion from entity collection to DTO map.
     */
    public void mapToCollectionMapping() throws GeDAException {
        final EntityItemInterface eItem1 = new EntityItemClass();
        eItem1.setName("itm1");
        final EntityItemInterface eItem2 = new EntityItemClass();
        eItem2.setName("itm2");

        final EntityCollectionInterface eColl = new EntityCollectionClass();
        eColl.setItems(new ArrayList<EntityItemInterface>());
        eColl.getItems().add(eItem1);
        eColl.getItems().add(eItem2);

        final DtoMapIterface dMap = new DtoMapToCollectionClass();

        final Assembler assembler = DTOAssembler.newAssembler(dMap.getClass(), eColl.getClass());

        assembler.assembleDto(dMap, eColl, null, new BeanFactory() {

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
        final Set<String> keys = dMap.getItems().keySet();
        for (String key : keys) {
            if ("itm1".equals(key)) {
                assertEquals("itm1", dMap.getItems().get(key).getName());
            } else if ("itm2".equals(key)) {
                assertEquals("itm2", dMap.getItems().get(key).getName());
            } else {
                fail("Unknown key");
            }
        }

        final DtoItemClass dto3 = new DtoItemClass();
        dto3.setName("itm3");
        dMap.getItems().put("itm3", dto3);

        dMap.getItems().remove("itm1"); // first

        assembler.assembleEntity(dMap, eColl, null, new BeanFactory() {

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

        assertNotNull(eColl.getItems());
        assertEquals(2, eColl.getItems().size());
        Iterator<EntityItemInterface> eiter;

        eiter = eColl.getItems().iterator();
        final EntityItemInterface itm1 = eiter.next();
        final EntityItemInterface itm2 = eiter.next();
        assertEquals("itm2", itm1.getName());
        assertEquals("itm3", itm2.getName());
    }

    public static void main(String[] args) {
        new RunMapToCollection().mapToCollectionMapping();
    }

}
