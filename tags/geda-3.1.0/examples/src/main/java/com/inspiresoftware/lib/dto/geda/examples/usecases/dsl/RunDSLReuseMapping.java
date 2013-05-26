/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.dsl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever;
import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.dsl.Registries;
import com.inspiresoftware.lib.dto.geda.examples.usecases.SimpleMapExtensibleBeanFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 5:59 PM
 */
public class RunDSLReuseMapping {

    /**
     * Example of how mapping from one DTO-Entity context can be copied to
     * another DTO-Entity context to reduce code duplication.
     */
    public void reuseMapping() {

        final ExtensibleBeanFactory bf = new SimpleMapExtensibleBeanFactory();

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = Registries.registry(bf);

        bf.registerDto("myDto", MyDtoClass.class.getCanonicalName());
        bf.registerDto("myDtoField3Dto", MyDtoField3Class.class.getCanonicalName());
        bf.registerEntity("myEntityField3Entity", MyEntityField3Class.class.getCanonicalName(), MyEntityField3Class.class.getCanonicalName());
        bf.registerDto("field4ParentDto", MyDtoField4Class.class.getCanonicalName());
        bf.registerEntity("field4ParentEntity", MyEntityField4Class.class.getCanonicalName(), MyEntityField4Class.class.getCanonicalName());

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

        final Map<String, Object> toEntity = new HashMap<String, Object>();

        // create asm for class and make sure it picks up on interface
        DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), registry).assembleEntity(dto, toEntity, conv, bf);

        assertNull(toEntity.get("field1")); // it is read only

        assertEquals(toEntity.get("field2"), "my sub data 1");
        assertEquals(toEntity.get("virtual5"), dto.getField5virtual());

    }


    public static void main(String[] args) {
        new RunDSLReuseMapping().reuseMapping();
    }

}
