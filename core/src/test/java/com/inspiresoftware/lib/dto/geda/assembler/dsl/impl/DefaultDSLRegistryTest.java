/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 8:30 AM
 */
public class DefaultDSLRegistryTest {

    @Test
    public void testExperimental() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myDto")).andReturn(MyDtoClass.class);
        expect(bf.getClazz("myEntity")).andReturn(MyEntity.class);
        expect(bf.getClazz("myDtoField3Dto")).andReturn(MyDtoField3Class.class);
        expect(bf.getClazz("myEntityField3Entity")).andReturn(MyEntityField3Class.class);
        expect(bf.getClazz("field4ParentDto")).andReturn(MyDtoField4Class.class);
        expect(bf.getClazz("field4ParentEntity")).andReturn(MyEntityField4Class.class);
        expect(bf.get("myDtoField3Dto")).andReturn(new MyDtoField3Class());
        expect(bf.get("field4ParentDto")).andReturn(new MyDtoField4Class());

        ctrl.replay();

        registry
                // main mapping
                .dto("myDto").forEntity("myEntity")
                // field 1
                .withField("field1").forField("field1")
                    .readOnly()
                    .converter("field1Converter")
                // field 2
                .and()
                .withField("field2").forField("field2.subField1")
                // field 3
                .and()
                .withField("field3")
                    .dtoBeanKey("myDtoField3Dto")
                    .entityBeanKeys("myEntityField3Entity")
                // field 4
                .and()
                .withField("field4parent")
                    .dtoBeanKey("field4ParentDto")
                    .entityBeanKeys("field4ParentEntity")
                    .dtoParent("id")
                    .retriever("parentFieldEntityById")
                // field 5
                .and()
                .withField("field5virtual").forVirtual()
                    .converter("field5VirtualConverter")
                // field 6
                .and()
                .withCollection("field6").forField("field6")
                    .dtoBeanKey("field6CollectionDtoItem")
                    .entityBeanKeys("field6CollectionEntityItem")
                    .dtoToEntityMatcherKey("field6CollectionMatcher")
                // field 7
                .and()
                .withMap("field7").forField("field7")
                    .dtoBeanKey("field7MapDtoItem")
                    .entityBeanKeys("field7MapEntityItem")
                    .dtoToEntityMatcherKey("field7MapMatcher")
        ;

        registry
                .dto("myDtoField3Dto").forEntity("myEntityField3Entity")
                .withField("subField1")
        ;

        registry
                .dto("field4ParentDto").forEntity("field4ParentEntity")
                .withField("id")
                .and().withField("subField1")
        ;

        final Map<String, Object> conv = new HashMap<String, Object>();
        conv.put("field1Converter", new ValueConverter() {
            public Object convertToDto(final Object object, final BeanFactory beanFactory) {
                final MyEntity.Field1 field1 = (MyEntity.Field1) object;
                return Boolean.valueOf(field1 == MyEntity.Field1.YES);
            }

            public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
                if ((Boolean) object) {
                    return MyEntity.Field1.YES;
                }
                return MyEntity.Field1.NO;
            }
        });
        conv.put("field5VirtualConverter", new ValueConverter() {
            public Object convertToDto(final Object object, final BeanFactory beanFactory) {
                return String.valueOf(object.hashCode());
            }

            public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
                assertEquals(object, "new virtual value");
                return null;
            }
        });

        final Assembler asm = DTOAssembler.newAssembler(MyDtoClass.class, MyEntity.class, registry);

        final MyEntity entity = new MyEntityClass();

        entity.setField1(MyEntity.Field1.YES);

        entity.setField2(new MyEntityField2Class());
        entity.getField2().setSubField1("my sub data 1");

        entity.setField3(new MyEntityField3Class());
        entity.getField3().setSubField1("my sub data 2");

        entity.setField4parent(new MyEntityField4Class());
        entity.getField4parent().setId(99L);
        entity.getField4parent().setSubField1("Parent 99");

        final MyDtoClass dto = new MyDtoClass();

        asm.assembleDto(dto, entity, conv, bf);

        assertTrue(dto.getField1());

        assertEquals(dto.getField2(), "my sub data 1");

        final MyDtoField3Class field3 = dto.getField3();
        assertNotNull(field3);
        assertEquals(field3.getSubField1(), "my sub data 2");

        final MyDtoField4Class field4 = dto.getField4parent();
        assertNotNull(field4);
        assertEquals(field4.getId(), Long.valueOf(99L));
        assertEquals(field4.getSubField1(), "Parent 99");

        assertEquals(dto.getField5virtual(), String.valueOf(entity.hashCode()));

        ctrl.verify();

    }
}
