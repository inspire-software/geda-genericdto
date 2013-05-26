/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;
import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 12-09-20
 * Time: 8:30 AM
 */
public class DefaultDSLRegistryTest {

    @Test
    public void testByReference() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

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
                .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
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
                return null;
            }
        });
        conv.put("parentFieldEntityById", new EntityRetriever() {
            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                final MyEntityField4Class parent = new MyEntityField4Class();
                parent.setId(99L);
                parent.setSubField1("Parent 99");
                return parent;
            }
        });

        final Assembler asm = DTOAssembler.newAssembler(MyDtoClass.class, MyEntity.class, registry);

        final MyEntity fromEntity = new MyEntityClass();

        fromEntity.setField1(MyEntity.Field1.YES);

        fromEntity.setField2(new MyEntityField2Class());
        fromEntity.getField2().setSubField1("my sub data 1");

        fromEntity.setField3(new MyEntityField3Class());
        fromEntity.getField3().setSubField1("my sub data 2");

        fromEntity.setField4parent(new MyEntityField4Class());
        fromEntity.getField4parent().setId(99L);
        fromEntity.getField4parent().setSubField1("Parent 99");

        final MyDtoClass dto = new MyDtoClass();

        asm.assembleDto(dto, fromEntity, conv, bf);

        assertTrue(dto.getField1());

        assertEquals(dto.getField2(), "my sub data 1");

        final MyDtoField3Class field3 = dto.getField3();
        assertNotNull(field3);
        assertEquals(field3.getSubField1(), "my sub data 2");

        final MyDtoField4Class field4 = dto.getField4parent();
        assertNotNull(field4);
        assertEquals(field4.getId(), Long.valueOf(99L));
        assertEquals(field4.getSubField1(), "Parent 99");

        assertEquals(dto.getField5virtual(), String.valueOf(fromEntity.hashCode()));

        ctrl.verify();

        ctrl.reset();

        expect(bf.get("field2")).andReturn(new MyEntityField2Class());
        expect(bf.get("myEntityField3Entity")).andReturn(new MyEntityField3Class());
        expect(bf.get("field4ParentEntity")).andReturn(new MyEntityField4Class());

        ctrl.replay();

        final MyEntity toEntity = new MyEntityClass();

        asm.assembleEntity(dto, toEntity, conv, bf);

        assertNull(toEntity.getField1()); // it is read only

        assertNotNull(toEntity.getField2());
        assertEquals(toEntity.getField2().getSubField1(), "my sub data 1");

        assertNotNull(toEntity.getField3());
        assertEquals(toEntity.getField3().getSubField1(), "my sub data 2");

        assertNotNull(toEntity.getField4parent());
        assertEquals(toEntity.getField4parent().getId(), Long.valueOf(99L));
        assertEquals(toEntity.getField4parent().getSubField1(), "Parent 99");

        ctrl.verify();
    }

    @Test
    public void testByInterface() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myDto")).andReturn(MyDtoClass.class);
        expect(bf.getClazz("myDtoField3Dto")).andReturn(MyDtoField3Class.class);
        expect(bf.getClazz("myEntityField3Entity")).andReturn(MyEntityField3Class.class);
        expect(bf.getClazz("field4ParentDto")).andReturn(MyDtoField4Class.class);
        expect(bf.getClazz("field4ParentEntity")).andReturn(MyEntityField4Class.class);
        expect(bf.get("myDtoField3Dto")).andReturn(new MyDtoField3Class());
        expect(bf.get("field4ParentDto")).andReturn(new MyDtoField4Class());

        ctrl.replay();

        registry
                // main mapping
                .dto("myDto").forEntity(MyEntity.class)
                // field 1
                .withField("field1").forField("field1")
                .readOnly()
                .converter("field1Converter")
                        // field 2
                .and()
                .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
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
                return null;
            }
        });
        conv.put("parentFieldEntityById", new EntityRetriever() {
            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                final MyEntityField4Class parent = new MyEntityField4Class();
                parent.setId(99L);
                parent.setSubField1("Parent 99");
                return parent;
            }
        });

        // create asm for interface
        DTOAssembler.newAssembler(MyDtoClass.class, MyEntity.class, registry);

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

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleDto(dto, entity, conv, bf);

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


        ctrl.reset();

        expect(bf.get("field2")).andReturn(new MyEntityField2Class());
        expect(bf.get("myEntityField3Entity")).andReturn(new MyEntityField3Class());
        expect(bf.get("field4ParentEntity")).andReturn(new MyEntityField4Class());

        ctrl.replay();

        final MyEntity toEntity = new MyEntityClass();

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleEntity(dto, toEntity, conv, bf);

        assertNull(toEntity.getField1()); // it is read only

        assertNotNull(toEntity.getField2());
        assertEquals(toEntity.getField2().getSubField1(), "my sub data 1");

        assertNotNull(toEntity.getField3());
        assertEquals(toEntity.getField3().getSubField1(), "my sub data 2");

        assertNotNull(toEntity.getField4parent());
        assertEquals(toEntity.getField4parent().getId(), Long.valueOf(99L));
        assertEquals(toEntity.getField4parent().getSubField1(), "Parent 99");

        ctrl.verify();


    }

    @Test
    public void testByClassWithAlias() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myDto")).andReturn(MyDtoClass.class);
        expect(bf.getClazz("myDtoField3Dto")).andReturn(MyDtoField3Class.class);
        expect(bf.getClazz("myEntityField3Entity")).andReturn(MyEntityField3Class.class);
        expect(bf.getClazz("field4ParentDto")).andReturn(MyDtoField4Class.class);
        expect(bf.getClazz("field4ParentEntity")).andReturn(MyEntityField4Class.class);
        expect(bf.get("myDtoField3Dto")).andReturn(new MyDtoField3Class());
        expect(bf.get("field4ParentDto")).andReturn(new MyDtoField4Class());
        bf.registerEntity("myEntity", "com.inspiresoftware.lib.dto.geda.assembler.dsl.impl.MyEntityClass", "com.inspiresoftware.lib.dto.geda.assembler.dsl.impl.MyEntity");

        ctrl.replay();

        registry
                // main mapping
                .dto("myDto").forEntity(MyEntityClass.class).alias("myEntity", MyEntity.class)
                // field 1
                .withField("field1").forField("field1")
                .readOnly()
                .converter("field1Converter")
                        // field 2
                .and()
                .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
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
                return null;
            }
        });
        conv.put("parentFieldEntityById", new EntityRetriever() {
            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                final MyEntityField4Class parent = new MyEntityField4Class();
                parent.setId(99L);
                parent.setSubField1("Parent 99");
                return parent;
            }
        });

        // create asm for interface
        DTOAssembler.newAssembler(MyDtoClass.class, MyEntity.class, registry);

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

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleDto(dto, entity, conv, bf);

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


        ctrl.reset();

        expect(bf.get("field2")).andReturn(new MyEntityField2Class());
        expect(bf.get("myEntityField3Entity")).andReturn(new MyEntityField3Class());
        expect(bf.get("field4ParentEntity")).andReturn(new MyEntityField4Class());

        ctrl.replay();

        final MyEntity toEntity = new MyEntityClass();

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleEntity(dto, toEntity, conv, bf);

        assertNull(toEntity.getField1()); // it is read only

        assertNotNull(toEntity.getField2());
        assertEquals(toEntity.getField2().getSubField1(), "my sub data 1");

        assertNotNull(toEntity.getField3());
        assertEquals(toEntity.getField3().getSubField1(), "my sub data 2");

        assertNotNull(toEntity.getField4parent());
        assertEquals(toEntity.getField4parent().getId(), Long.valueOf(99L));
        assertEquals(toEntity.getField4parent().getSubField1(), "Parent 99");

        ctrl.verify();


    }

    @Test
    public void testGeneric() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myDto")).andReturn(MyDtoClass.class);
        expect(bf.getClazz("myDtoField3Dto")).andReturn(MyDtoField3Class.class);
        expect(bf.getClazz("field4ParentDto")).andReturn(MyDtoField4Class.class);
        expect(bf.get("myDtoField3Dto")).andReturn(new MyDtoField3Class());
        expect(bf.get("field4ParentDto")).andReturn(new MyDtoField4Class());

        ctrl.replay();

        registry
                // main mapping
                .dto("myDto").forEntityGeneric()
                // field 1
                .withField("field1").forField("field1")
                .readOnly()
                .converter("field1Converter")
                        // field 2
                .and()
                .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
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
                .dto("myDtoField3Dto").forEntityGeneric()
                .withField("subField1")
        ;

        registry
                .dto("field4ParentDto").forEntityGeneric()
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
                return null;
            }
        });
        conv.put("parentFieldEntityById", new EntityRetriever() {
            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                final MyEntityField4Class parent = new MyEntityField4Class();
                parent.setId(99L);
                parent.setSubField1("Parent 99");
                return parent;
            }
        });

        // create asm for interface
        DTOAssembler.newAssembler(MyDtoClass.class, MyEntity.class, registry);

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

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleDto(dto, entity, conv, bf);

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

        ctrl.reset();

        expect(bf.get("field2")).andReturn(new MyEntityField2Class());
        expect(bf.get("myEntityField3Entity")).andReturn(new MyEntityField3Class());
        expect(bf.get("field4ParentEntity")).andReturn(new MyEntityField4Class());

        ctrl.replay();

        final MyEntity toEntity = new MyEntityClass();

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleEntity(dto, toEntity, conv, bf);

        assertNull(toEntity.getField1()); // it is read only

        assertNotNull(toEntity.getField2());
        assertEquals(toEntity.getField2().getSubField1(), "my sub data 1");

        assertNotNull(toEntity.getField3());
        assertEquals(toEntity.getField3().getSubField1(), "my sub data 2");

        assertNotNull(toEntity.getField4parent());
        assertEquals(toEntity.getField4parent().getId(), Long.valueOf(99L));
        assertEquals(toEntity.getField4parent().getSubField1(), "Parent 99");

        ctrl.verify();



    }


    @Test
    public void testReuseMapping() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myDto")).andReturn(MyDtoClass.class).anyTimes();

        ctrl.replay();

        registry
                // main mapping
                .dto("myDto").forEntity(MyEntity.class)
                // field 1
                .withField("field1").forField("field1")
                .readOnly()
                .converter("field1Converter")
                // field 2
                .and()
                .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
                // field 5
                .and()
                .withField("field5virtual").forVirtual()
                .converter("field5VirtualConverter")

        ;


        registry.dto("myDto").useContextFor(
            registry
                    // main mapping
                    .dto("myDto").forEntity(MyEntity.class),
            Map.class
        );

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
                ((Map) oldEntity).put("virtual5", object);
                return null;
            }
        });
        conv.put("parentFieldEntityById", new EntityRetriever() {
            public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
                final MyEntityField4Class parent = new MyEntityField4Class();
                parent.setId(99L);
                parent.setSubField1("Parent 99");
                return parent;
            }
        });

        // create asm for interface
        DTOAssembler.newAssembler(MyDtoClass.class, Map.class, registry);

        final Map<String, Object> entity = new HashMap<String, Object>();

        entity.put("field1", MyEntity.Field1.YES);
        entity.put("field2", "my sub data 1");
        entity.put("field5virtual", "virtual");

        final MyDtoClass dto = new MyDtoClass();

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleDto(dto, entity, conv, bf);

        assertTrue(dto.getField1());

        assertEquals(dto.getField2(), "my sub data 1");

        assertEquals(dto.getField5virtual(), String.valueOf(entity.hashCode()));

        ctrl.verify();


        ctrl.reset();


        ctrl.replay();

        final Map<String, Object> toEntity = new HashMap<String, Object>();

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleEntity(dto, toEntity, conv, bf);

        assertNull(toEntity.get("field1")); // it is read only

        assertEquals(toEntity.get("field2"), "my sub data 1");
        assertEquals(toEntity.get("virtual5"), dto.getField5virtual());

        ctrl.verify();


    }


    @Test
    public void testWithSameFieldsByKey() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myEntityWithSameFields")).andReturn(MyEntityWithSameFieldsInterface.class).anyTimes();

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity("myEntityWithSameFields")
                // map all same name same type fields by key
                .withFieldsSameAsIn("myEntityWithSameFields");


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClass();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertEquals(dto.getField4(), Integer.valueOf(4));
        assertEquals(dto.getField5(), 5);
        assertEquals(dto.getField6(), Long.valueOf(6L));
        assertEquals(dto.getField7(), 7L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertEquals(toEntity.getField4(), Integer.valueOf(4));
        assertEquals(toEntity.getField5(), 5);
        assertEquals(toEntity.getField6(), Long.valueOf(6L));
        assertEquals(toEntity.getField7(), 7L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }

    @Test
    public void testWithSameFieldsByKeyExcluding() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        expect(bf.getClazz("myEntityWithSameFields")).andReturn(MyEntityWithSameFieldsInterface.class).anyTimes();

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity("myEntityWithSameFields")
                // map all same name same type fields by key but not field4, field5, field6, field7
                .withFieldsSameAsIn("myEntityWithSameFields", "field4", "field5", "field6", "field7");


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClass();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertNull(dto.getField4());
        assertEquals(dto.getField5(), 0);
        assertNull(dto.getField6());
        assertEquals(dto.getField7(), 0L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        dto.setField4(Integer.valueOf(4));
        dto.setField5(5);
        dto.setField6(Long.valueOf(6L));
        dto.setField7(7L);


        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertNull(toEntity.getField4());
        assertEquals(toEntity.getField5(), 0);
        assertNull(toEntity.getField6());
        assertEquals(toEntity.getField7(), 0L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }


    @Test
    public void testWithSameFieldsByInterface() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity(MyEntityWithSameFieldsInterface.class)
                // map all same name same type fields by interface
                .withFieldsSameAsIn(MyEntityWithSameFieldsInterface.class);


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClass();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertEquals(dto.getField4(), Integer.valueOf(4));
        assertEquals(dto.getField5(), 5);
        assertEquals(dto.getField6(), Long.valueOf(6L));
        assertEquals(dto.getField7(), 7L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertEquals(toEntity.getField4(), Integer.valueOf(4));
        assertEquals(toEntity.getField5(), 5);
        assertEquals(toEntity.getField6(), Long.valueOf(6L));
        assertEquals(toEntity.getField7(), 7L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }

    @Test
    public void testWithSameFieldsByInterfaceExcluding() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity(MyEntityWithSameFieldsInterface.class)
                // map all same name same type fields by interface but not field4, field5, field6, field7
                .withFieldsSameAsIn(MyEntityWithSameFieldsInterface.class, "field4", "field5", "field6", "field7");


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClass();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertNull(dto.getField4());
        assertEquals(dto.getField5(), 0);
        assertNull(dto.getField6());
        assertEquals(dto.getField7(), 0L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        dto.setField4(Integer.valueOf(4));
        dto.setField5(5);
        dto.setField6(Long.valueOf(6L));
        dto.setField7(7L);


        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertNull(toEntity.getField4());
        assertEquals(toEntity.getField5(), 0);
        assertNull(toEntity.getField6());
        assertEquals(toEntity.getField7(), 0L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }

    @Test
    public void testWithSameFieldsByClass() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity(MyEntityWithSameFieldsInterface.class)
                // map all same name same type fields by class
                .withFieldsSameAsIn(MyEntityWithSameFieldsClass.class);


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClass();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertEquals(dto.getField4(), Integer.valueOf(4));
        assertEquals(dto.getField5(), 5);
        assertEquals(dto.getField6(), Long.valueOf(6L));
        assertEquals(dto.getField7(), 7L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertEquals(toEntity.getField4(), Integer.valueOf(4));
        assertEquals(toEntity.getField5(), 5);
        assertEquals(toEntity.getField6(), Long.valueOf(6L));
        assertEquals(toEntity.getField7(), 7L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }

    @Test
    public void testWithSameFieldsByClassExcluding() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity(MyEntityWithSameFieldsInterface.class)
                // map all same name same type fields by key but not field4, field5, field6, field7
                .withFieldsSameAsIn(MyEntityWithSameFieldsClass.class, "field4", "field5", "field6", "field7");


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClass();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertNull(dto.getField4());
        assertEquals(dto.getField5(), 0);
        assertNull(dto.getField6());
        assertEquals(dto.getField7(), 0L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        dto.setField4(Integer.valueOf(4));
        dto.setField5(5);
        dto.setField6(Long.valueOf(6L));
        dto.setField7(7L);


        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertNull(toEntity.getField4());
        assertEquals(toEntity.getField5(), 0);
        assertNull(toEntity.getField6());
        assertEquals(toEntity.getField7(), 0L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }


    @Test
    public void testWithSameFieldsByClassInheritance() throws Exception {

        final IMocksControl ctrl = EasyMock.createControl();

        final ExtensibleBeanFactory bf = ctrl.createMock("bf", ExtensibleBeanFactory.class);

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        ctrl.replay();

        registry
                // main mapping
                .dto(MyDtoWithSameFieldsClass.class).forEntity(MyEntityWithSameFieldsInterface.class)
                // map all same name same type fields by class that inherits the class with fields
                .withFieldsSameAsIn(MyEntityWithSameFieldsClassInherited.class);


        final Assembler asm = DTOAssembler.newAssembler(MyDtoWithSameFieldsClass.class, MyEntityWithSameFieldsInterface.class, registry);

        final MyEntityWithSameFieldsInterface fromEntity = new MyEntityWithSameFieldsClassInherited();

        fromEntity.setField1("field1");
        fromEntity.setField2(Boolean.TRUE);
        fromEntity.setField3(true);
        fromEntity.setField4(Integer.valueOf(4));
        fromEntity.setField5(5);
        fromEntity.setField6(Long.valueOf(6L));
        fromEntity.setField7(7L);
        fromEntity.setField8(Double.valueOf(8D));
        fromEntity.setField9(9D);
        fromEntity.setField10(BigDecimal.TEN);
        final Object field11Obj = new Object();
        fromEntity.setField11(field11Obj);
        fromEntity.setWrongNameOnEntity("wrongNameOnEntity");
        fromEntity.setWrongType(Double.valueOf(20D));

        final MyDtoWithSameFieldsClass dto = new MyDtoWithSameFieldsClass();

        asm.assembleDto(dto, fromEntity, null, bf);

        assertEquals(dto.getField1(), "field1");
        assertTrue(dto.getField2());
        assertTrue(dto.getField3());
        assertEquals(dto.getField4(), Integer.valueOf(4));
        assertEquals(dto.getField5(), 5);
        assertEquals(dto.getField6(), Long.valueOf(6L));
        assertEquals(dto.getField7(), 7L);
        assertEquals(dto.getField8(), Double.valueOf(8D));
        assertEquals(dto.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(dto.getField10()), 0);
        assertSame(dto.getField11(), field11Obj);
        assertNull(dto.getWrongName());
        assertNull(dto.getWrongType());

        ctrl.verify();

        ctrl.reset();

        ctrl.replay();

        final MyEntityWithSameFieldsInterface toEntity = new MyEntityWithSameFieldsClass();

        asm.assembleEntity(dto, toEntity, null, bf);

        assertEquals(toEntity.getField1(), "field1");
        assertTrue(toEntity.getField2());
        assertTrue(toEntity.getField3());
        assertEquals(toEntity.getField4(), Integer.valueOf(4));
        assertEquals(toEntity.getField5(), 5);
        assertEquals(toEntity.getField6(), Long.valueOf(6L));
        assertEquals(toEntity.getField7(), 7L);
        assertEquals(toEntity.getField8(), Double.valueOf(8D));
        assertEquals(toEntity.getField9(), 9D, 0);
        assertEquals(BigDecimal.TEN.compareTo(toEntity.getField10()), 0);
        assertSame(toEntity.getField11(), field11Obj);
        assertNull(toEntity.getWrongNameOnEntity());
        assertNull(toEntity.getWrongType());

        ctrl.verify();
    }


}
