/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.parent;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 11:08 PM
 */
public class RunWithParent {


    /**
     * Example showing how entity parent works whereby GeDA will look up parent objects
     * rather than requesting BeanFactory for new instance before writing data to entity.
     */
    public void parentMapping() {

        final EntityParentInterface parentEntity = new EntityParentClass();
        final String parentName = "parent with id 3";
        parentEntity.setEntityId(3L);
        parentEntity.setName(parentName);

        final EntityChildInterface childEntity = new EntityChildClass();
        final String childName = "child of parent with id 3";
        childEntity.setParent(parentEntity);
        childEntity.setName(childName);

        final DtoChildInterface childDto = new DtoChildClass();

        final Assembler assembler =
                DTOAssembler.newAssembler(childDto.getClass(), childEntity.getClass());

        assembler.assembleDto(childDto, childEntity, null, createDtoBeanFactory());

        assertEquals(childName, childDto.getName());
        assertNotNull(childDto.getParent());
        assertEquals(parentName, childDto.getParent().getName());
        assertEquals(Long.valueOf(3L), Long.valueOf(childDto.getParent().getEntityId()));

        final EntityParentInterface parentEntity2 = new EntityParentClass();
        final String parentName2 = "parent with id 0";
        parentEntity2.setEntityId(0);
        parentEntity2.setName(parentName2);

        // change dto parent.
        childDto.getParent().setEntityId(0L);
        childDto.setName("child with changed parent");

        final Map<String, Object> conv = createMapWithEntityRetriever(parentEntity2, Long.valueOf(0));

        assembler.assembleEntity(childDto, childEntity, conv, createEntityBeanFactory());

        assertNotNull(childEntity.getParent());
        assertSame(parentEntity2, childEntity.getParent());
        assertEquals("child with changed parent", childEntity.getName());

    }

    private BeanFactory createEntityBeanFactory() {
        return new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                if (entityBeanKey.equals("EntityParentClass")) {
                    return new EntityParentClass();
                }
                return null;
            }

        };
    }

    private BeanFactory createDtoBeanFactory() {
        return new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                return null;
            }

            public Object get(final String entityBeanKey) {
                if (entityBeanKey.equals("DtoParentClass")) {
                    return new DtoParentClass();
                }
                return null;
            }

        };
    }

    private Map<String, Object> createMapWithEntityRetriever(final EntityParentInterface parentEntity2, final Object idExpectations) {
        final EntityRetriever retriever = new EntityRetriever() {

            /*
             * Simple retrieve mock. Actual retriever would probably call a DAO object.
             */
            @SuppressWarnings("unchecked")
            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                assertEquals(idExpectations, primaryKey);
                assertTrue(entityInterface.equals(EntityParentInterface.class));
                assertTrue(entityClass.equals(EntityParentClass.class));
                return parentEntity2;
            }

        };

        final Map<String, Object> conv = new HashMap<String, Object>();
        conv.put("retriever", retriever);
        return conv;
    }


    public static void main(String[] args) {
        new RunWithParent().parentMapping();
    }


}
