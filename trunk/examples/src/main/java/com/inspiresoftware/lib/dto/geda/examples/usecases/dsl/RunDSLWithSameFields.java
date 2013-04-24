/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.dsl;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.impl.DefaultDSLRegistry;
import com.inspiresoftware.lib.dto.geda.examples.usecases.SimpleMapExtensibleBeanFactory;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-04-24
 * Time: 11:49 AM
 */
public class RunDSLWithSameFields {

    /**
     * Example showing how to map same name same type simple fields quickly.
     * The mapping can be by beanKey, Interface or Class. This example shows
     * the beanKey approach, for others just use the appropriate class instead
     * of beanKey.
     */
    public void withSameFieldsByKey() {

        final ExtensibleBeanFactory bf = new SimpleMapExtensibleBeanFactory();

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        bf.registerEntity("myEntityWithSameFields",
                MyEntityWithSameFieldsClass.class.getCanonicalName(),
                MyEntityWithSameFieldsInterface.class.getCanonicalName());

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

    }

    /**
     * Example showing how to use exclusions with auto mapping of same name same type fields.
     * The basic principle is the same - all you need is to specify the exclusion field names.
     */
    public void withSameFieldsByKeyExcluding() {

        final ExtensibleBeanFactory bf = new SimpleMapExtensibleBeanFactory();

        final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = new DefaultDSLRegistry(bf);

        bf.registerEntity("myEntityWithSameFields",
                MyEntityWithSameFieldsClass.class.getCanonicalName(),
                MyEntityWithSameFieldsInterface.class.getCanonicalName());

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


        dto.setField4(Integer.valueOf(4));
        dto.setField5(5);
        dto.setField6(Long.valueOf(6L));
        dto.setField7(7L);


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

    }

    public static void main(String[] args) {
        new RunDSLWithSameFields().withSameFieldsByKey();
        new RunDSLWithSameFields().withSameFieldsByKeyExcluding();
    }


}
