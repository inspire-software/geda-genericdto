/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics.extend;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-23
 * Time: 3:01 PM
 */
public class RunWithGenericsExtend {

    /**
     * Example showing complex extends generic i.e. &lt? extends MyClass&gt.
     * 
     * EntityCode is extended by EntityCatalogCode
     * EntityCatalog includes a primary code and a set of codes
     * There is also a twist where catalog code has reference to catalog to show
     * some issues with recursive object graphs.
     */
    public void genericsExtendMapping() {

        final DtoCatalogClass<DtoCodeClass> dto = new DtoCatalogClass<DtoCodeClass>();
        final EntityCatalog<EntityCatalogCode> entity = new EntityCatalogClass<EntityCatalogCode>();
        final EntityCatalogCode entityCodePrime = new EntityCatalogCodeClass();
        final EntityCatalogCode entityCodeAdditional = new EntityCatalogCodeClass();


        entity.setId("ID-ABC");
        entityCodePrime.setCatalog(entity);
        entityCodePrime.setSectionName("AdvancedGenerics");
        entityCodePrime.setCode("CODE-AG1");
        entityCodePrime.setId("ID-123");
        entityCodeAdditional.setCatalog(entity);
        entityCodeAdditional.setSectionName("DtoTrandformations");
        entityCodeAdditional.setCode("CODE-DT1");
        entityCodeAdditional.setId("ID-235");
        entity.setType(entityCodePrime);
        entity.setCodes(new ArrayList<EntityCatalogCode>(Arrays.asList(entityCodePrime, entityCodeAdditional)));

        final Assembler assembler = DTOAssembler.newAssembler(dto.getClass(), entity.getClass());

        final Map<String, Object> adapters = new HashMap<String, Object>();
        adapters.put("CatalogCodeMatcher", new CatalogCodeMatcher());

        assembler.assembleDto(dto, entity, adapters, new BeanFactory() {
            public Class getClazz(final String entityBeanKey) {
                if ("DtoCatalogCode".equals(entityBeanKey)) {
                    return DtoCatalogCodeClass.class;
                } else if ("DtoCode".equals(entityBeanKey)) {
                    return DtoCodeClass.class;
                } else if ("DtoCatalog".equals(entityBeanKey)) {
                    return DtoCatalogClass.class;
                }
                fail("Unknown DTO key: " + entityBeanKey);
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("DtoCatalogCode".equals(entityBeanKey)) {
                    return new DtoCatalogCodeClass();
                } else if ("DtoCode".equals(entityBeanKey)) {
                    return new DtoCodeClass();
                } else if ("DtoCatalog".equals(entityBeanKey)) {
                    return new DtoCatalogClass();
                }
                fail("Unknown DTO key: " + entityBeanKey);
                return null;
            }
        });

        assertEquals("ID-ABC", dto.getId());

        final DtoCodeClass dtoType = dto.getType();
        assertNotNull(dtoType);
        assertEquals("ID-123", dtoType.getId());
        assertEquals("CODE-AG1", dtoType.getCode());

        final Collection<DtoCodeClass> dtoCodes = dto.getCodes();
        assertNotNull(dtoCodes);
        assertFalse(dtoCodes.isEmpty());
        // The collection is actually an ArrayList, so we will cheat a little
        final List<DtoCodeClass> dtoCodesAsList = (List) dtoCodes;
        assertEquals(2, dtoCodesAsList.size());

        final DtoCodeClass dtoCode1 = dtoCodesAsList.get(0);
        assertNotNull(dtoCode1);
        assertEquals("ID-123", dtoCode1.getId());
        assertEquals("CODE-AG1", dtoCode1.getCode());

        final DtoCodeClass dtoCode2 = dtoCodesAsList.get(1);
        assertNotNull(dtoCode2);
        assertEquals("ID-235", dtoCode2.getId());
        assertEquals("CODE-DT1", dtoCode2.getCode());

        final EntityCatalog<EntityCatalogCode> entityCopy = new EntityCatalogClass<EntityCatalogCode>();

        assembler.assembleEntity(dto, entityCopy, adapters, new BeanFactory() {
            public Class getClazz(final String entityBeanKey) {
                if ("CatalogCode".equals(entityBeanKey)) {
                    return EntityCatalogCodeClass.class;
                } else if ("Catalog".equals(entityBeanKey)) {
                    return EntityCatalogClass.class;
                }
                fail("Unknown Entity key: " + entityBeanKey);
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("CatalogCode".equals(entityBeanKey)) {
                    return new EntityCatalogCodeClass();
                } else if ("Catalog".equals(entityBeanKey)) {
                    return new EntityCatalogClass();
                }
                fail("Unknown Entity key: " + entityBeanKey);
                return null;
            }
        });

        assertEquals("ID-ABC", entityCopy.getId());

        final EntityCatalogCode entityCopyType = entityCopy.getType();
        assertNotNull(entityCopyType);

        assertEquals("ID-123", entityCopyType.getId());
        assertEquals("CODE-AG1", entityCopyType.getCode());
        assertNull(entityCopyType.getSectionName()); // This property is not part of mapping to prevent recursion
        assertNull(entityCopyType.getCatalog()); // This property is not part of mapping to prevent recursion

        final Collection<EntityCatalogCode> entityCopyCodes = entityCopy.getCodes();
        assertNotNull(entityCopyCodes);
        assertFalse(entityCopyCodes.isEmpty());
        // The collection is actually an ArrayList, so we will cheat a little
        final List<EntityCatalogCode> entityCopyCodesAsList = (List) entityCopyCodes;
        assertEquals(2, entityCopyCodesAsList.size());

        final EntityCatalogCode entityCopyCode1 = entityCopyCodesAsList.get(0);
        assertEquals("ID-123", entityCopyCode1.getId());
        assertEquals("CODE-AG1", entityCopyCode1.getCode());
        assertNull(entityCopyCode1.getSectionName()); // This property is not part of mapping to prevent recursion
        assertNull(entityCopyCode1.getCatalog()); // This property is not part of mapping to prevent recursion

        final EntityCatalogCode entityCopyCode2 = entityCopyCodesAsList.get(1);
        assertEquals("ID-235", entityCopyCode2.getId());
        assertEquals("CODE-DT1", entityCopyCode2.getCode());
        assertNull(entityCopyCode2.getSectionName()); // This property is not part of mapping to prevent recursion
        assertNull(entityCopyCode2.getCatalog()); // This property is not part of mapping to prevent recursion

    }


    public static void main(String[] args) {
        new RunWithGenericsExtend().genericsExtendMapping();
    }

}
