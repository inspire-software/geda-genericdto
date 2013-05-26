/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.adapter.impl;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 13-05-24
 * Time: 9:45 AM
 */
public class ClassLoaderBeanFactoryTest {

    @Test
    public void testGetClazzNotFound() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        final Class myDtoClass = fb.getClazz("myDto");
        assertNull(myDtoClass);

    }

    @Test
    public void testGetNotFound() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        final Object myDtoInstance = fb.get("myDto");
        assertNull(myDtoInstance);

    }

    @Test
    public void testGetClazzNull() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        final Class myDtoClass = fb.getClazz(null);
        assertNull(myDtoClass);

    }

    @Test
    public void testGetClazzEmpty() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        final Class myDtoClass = fb.getClazz(null);
        assertNull(myDtoClass);

    }

    @Test
    public void testGetNull() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        final Object myDtoInstance = fb.get(null);
        assertNull(myDtoInstance);

    }

    @Test
    public void testGetEmpty() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        final Object myDtoInstance = fb.get("");
        assertNull(myDtoInstance);

    }

    @Test
    public void testRegisterDto() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerDto("myDto", DtoClass.class.getCanonicalName());

        final Class myDtoClass = fb.getClazz("myDto");
        assertNotNull(myDtoClass);
        assertEquals(myDtoClass, DtoClass.class);

        final Object myDtoInstance = fb.get("myDto");
        assertNotNull(myDtoInstance);
        assertEquals(myDtoInstance.getClass(), DtoClass.class);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDtoNullKey() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerDto(null, DtoClass.class.getCanonicalName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDtoEmptyKey() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerDto("", DtoClass.class.getCanonicalName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDtoNullClass() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerDto("myDto", null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDtoEmptyClass() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerDto("myDto", "");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterDtoDuplicateKey() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerDto("myDto", DtoClass.class.getCanonicalName());
        fb.registerDto("myDto", DtoClass.class.getCanonicalName());
    }

    @Test
    public void testRegisterEntityWithInterface() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", EntityClass.class.getCanonicalName(), EntityInterface.class.getCanonicalName());

        final Class myEntityClass = fb.getClazz("myEntity");
        assertNotNull(myEntityClass);
        assertEquals(myEntityClass, EntityInterface.class);

        final Object myEntityInstance = fb.get("myEntity");
        assertNotNull(myEntityInstance);
        assertEquals(myEntityInstance.getClass(), EntityClass.class);


    }

    @Test
    public void testRegisterEntityWithClass() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", EntityClass.class.getCanonicalName(), EntityClass.class.getCanonicalName());

        final Class myEntityClass = fb.getClazz("myEntity");
        assertNotNull(myEntityClass);
        assertEquals(myEntityClass, EntityClass.class);

        final Object myEntityInstance = fb.get("myEntity");
        assertNotNull(myEntityInstance);
        assertEquals(myEntityInstance.getClass(), EntityClass.class);


    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityNullKey() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity(null, EntityClass.class.getCanonicalName(), EntityClass.class.getCanonicalName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityEmptyKey() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("", EntityClass.class.getCanonicalName(), EntityClass.class.getCanonicalName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityNullClass() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", null, EntityClass.class.getCanonicalName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityEmptyClass() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", "", EntityClass.class.getCanonicalName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityNullRep() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", EntityClass.class.getCanonicalName(), null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityEmptyRep() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", EntityClass.class.getCanonicalName(), "");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterEntityDuplicate() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(ClassLoaderBeanFactoryTest.class.getClassLoader());

        fb.registerEntity("myEntity", EntityClass.class.getCanonicalName(), EntityInterface.class.getCanonicalName());
        fb.registerEntity("myEntity", EntityClass.class.getCanonicalName(), EntityInterface.class.getCanonicalName());
    }

    @Test
    public void testMapConstructor() throws Exception {

        final ExtensibleBeanFactory fb = new ClassLoaderBeanFactory(
                ClassLoaderBeanFactoryTest.class.getClassLoader(),
                new HashMap<String, String>() {{
                    put("myDto", DtoClass.class.getCanonicalName());
                    put("myEntity", EntityClass.class.getCanonicalName());
                }},
                new HashMap<String, String>() {{
                    put("myEntity", EntityInterface.class.getCanonicalName());
                }}
            );


        final Class myDtoClass = fb.getClazz("myDto");
        assertNotNull(myDtoClass);
        assertEquals(myDtoClass, DtoClass.class);

        final Object myDtoInstance = fb.get("myDto");
        assertNotNull(myDtoInstance);
        assertEquals(myDtoInstance.getClass(), DtoClass.class);

        final Class myEntityClass = fb.getClazz("myEntity");
        assertNotNull(myEntityClass);
        assertEquals(myEntityClass, EntityInterface.class);

        final Object myEntityInstance = fb.get("myEntity");
        assertNotNull(myEntityInstance);
        assertEquals(myEntityInstance.getClass(), EntityClass.class);

    }


}