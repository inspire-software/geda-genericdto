/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.collections;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 11:25 PM
 */
public class RunWithCollection {

    /**
     * Test to check nested collections mapping works.
     *
     * @throws com.inspiresoftware.lib.dto.geda.exception.GeDAException exception
     */
    public void collectionMapping() throws GeDAException {

        final EntityItemInterface eItem1 = new EntityItemClass();
        eItem1.setName("itm1");
        final EntityItemInterface eItem2 = new EntityItemClass();
        eItem2.setName("itm2");

        final EntityInterface eColl = new EntityClass();
        eColl.setItems(new ArrayList<EntityItemInterface>());
        eColl.getItems().add(eItem1);
        eColl.getItems().add(eItem2);

        final DtoIterface dColl = new DtoClass();

        final Assembler assembler = DTOAssembler.newAssembler(dColl.getClass(), eColl.getClass());

        assembler.assembleDto(dColl, eColl, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return new DtoItemClass();
                }
                return null;
            }

        });

        assertNotNull(dColl.getItems());
        assertEquals(2, dColl.getItems().size());
        Iterator<DtoItemIterface> iter;

        iter = dColl.getItems().iterator();
        final DtoItemIterface dto1 = iter.next();
        final DtoItemIterface dto2 = iter.next();
        assertEquals("itm1", dto1.getName());
        assertEquals("itm2", dto2.getName());

        final DtoItemClass dto3 = new DtoItemClass();
        dto3.setName("itm3");
        dColl.getItems().add(dto3);

        iter = dColl.getItems().iterator();
        iter.next();
        iter.remove(); // first

        assembler.assembleEntity(dColl, eColl, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
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
        new RunWithCollection().collectionMapping();
    }


}
