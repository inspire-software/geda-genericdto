/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOFactory;
import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.config.AnnotationDrivenGeDABeanDefinitionParser;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import com.inspiresoftware.lib.dto.geda.interceptor.GeDAInterceptor;
import com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl;
import com.inspiresoftware.lib.dto.geda.test.impl.ExtendedDataTransferObjectImpl;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:30:53 PM
 */
public abstract class IntegrationTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testBeansSetup() {

        final DTOFactory factory = this.applicationContext.getBean("dtoFactory", DTOFactory.class);
        assertNotNull(factory);

        final DTOSupport support = this.applicationContext.getBean("dtoSupport", DTOSupport.class);
        assertNotNull(support);

        final AdviceConfigResolver resolver = this.applicationContext.getBean(AdviceConfigResolver.class);
        assertNotNull(resolver);

        final GeDAInterceptor interceptor = this.applicationContext.getBean(GeDAInterceptor.class);
        assertNotNull(interceptor);

        final PointcutAdvisor advisor =
                this.applicationContext.getBean(
                        AnnotationDrivenGeDABeanDefinitionParser.ADVISOR_BEAN_NAME, PointcutAdvisor.class);
        assertNotNull(advisor);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityBeforeExact(dto, entity);

        assertEquals(entity.getValue(), "dtoVal1");
        assertEquals(entity.getValue2(), "dtoVal2");
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityBefore(dto, entity, new Object());

        assertEquals(entity.getValue(), "dtoVal1");
        assertEquals(entity.getValue2(), "dtoVal2");
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityByFilterBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityByFilterBeforeExact(dto, entity);

        assertEquals(entity.getValue(), "dtoVal1");
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityByFilterBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityByFilterBefore(dto, entity, new Object());

        assertEquals(entity.getValue(), "dtoVal1");
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesBeforeExact(dtos, entities);

        assertEquals(entities.size(), 2);        
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertEquals(entity1.getValue2(), "dto1Val2");
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertEquals(entity2.getValue2(), "dto2Val2");
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesBefore(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertEquals(entity1.getValue2(), "dto1Val2");
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertEquals(entity2.getValue2(), "dto2Val2");
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesByFilterBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesByFilterBeforeExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertNull(entity1.getValue2());
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertNull(entity2.getValue2());
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesByFilterBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesByFilterBefore(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertNull(entity1.getValue2());
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertNull(entity2.getValue2());
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void dtoToEntityKeyAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = (DomainObject) service.dtoToEntityKeyAfterExact(dto);

        assertEquals(entity.getValue(), "dto1Val1");
        assertEquals(entity.getValue2(), "dto1Val2");
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void dtoToEntityKeyAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = (DomainObject) service.dtoToEntityKeyAfter(dto, new Object());

        assertEquals(entity.getValue(), "dto1Val1");
        assertEquals(entity.getValue2(), "dto1Val2");
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }


    @Test(dependsOnMethods = "testBeansSetup")
    public void dtoToEntityKeyByFilterAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = (DomainObject) service.dtoToEntityKeyByFilterAfterExact(dto);

        assertEquals(entity.getValue(), "dto1Val1");
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void dtoToEntityKeyByFilterAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = (DomainObject) service.dtoToEntityKeyByFilterAfter(dto, new Object());

        assertEquals(entity.getValue(), "dto1Val1");
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityAfterExact(dto, entity);

        assertEquals(entity.getValue(), "dto1Val1");
        assertEquals(entity.getValue2(), "dto1Val2");
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityAfter(dto, entity, new Object());

        assertEquals(entity.getValue(), "dto1Val1");
        assertEquals(entity.getValue2(), "dto1Val2");
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityByFilterAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityByFilterAfterExact(dto, entity);

        assertEquals(entity.getValue(), "dto1Val1");
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityByFilterAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dto1Val1");
        dto.setValue2("dto1Val2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityByFilterAfter(dto, entity, new Object());

        assertEquals(entity.getValue(), "dto1Val1");
        assertNull(entity.getValue2());
        assertNull(entity.getTimestamp());

        assertEquals(dto.getValue(), "dto1Val1");
        assertEquals(dto.getValue2(), "dto1Val2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesByFilterAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesByFilterAfterExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertNull(entity1.getValue2());
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertNull(entity2.getValue2());
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesByFilterAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesByFilterAfter(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertNull(entity1.getValue2());
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertNull(entity2.getValue2());
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesAfterExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertEquals(entity1.getValue2(), "dto1Val2");
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertEquals(entity2.getValue2(), "dto2Val2");
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtosToEntitiesAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto1 = new ExtendedDataTransferObjectImpl();
        dto1.setValue("dto1Val1");
        dto1.setValue2("dto1Val2");
        dto1.setTimestamp(time);
        final ExtendedDataTransferObject dto2 = new ExtendedDataTransferObjectImpl();
        dto2.setValue("dto2Val1");
        dto2.setValue2("dto2Val2");
        dto2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = Arrays.asList(dto1, dto2);
        final List<DomainObject> entities = new ArrayList<DomainObject>();

        service.dtosToEntitiesAfter(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final DomainObject entity1 = entities.get(0);
        assertEquals(entity1.getValue(), "dto1Val1");
        assertEquals(entity1.getValue2(), "dto1Val2");
        assertNull(entity1.getTimestamp());
        final DomainObject entity2 = entities.get(1);
        assertEquals(entity2.getValue(), "dto2Val1");
        assertEquals(entity2.getValue2(), "dto2Val2");
        assertNull(entity2.getTimestamp());

        assertEquals(dto1.getValue(), "dto1Val1");
        assertEquals(dto1.getValue2(), "dto1Val2");
        assertEquals(dto1.getTimestamp(), time);

        assertEquals(dto2.getValue(), "dto2Val1");
        assertEquals(dto2.getValue2(), "dto2Val2");
        assertEquals(dto2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 0, onDtoFailure, 0, onEntitySuccess, 2, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        service.entityToDtoBeforeExact(dto, entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        service.entityToDtoBefore(dto, entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);
    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoByFilterBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        service.entityToDtoByFilterBeforeExact(dto, entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertNull(dto.getValue2());
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoByFilterBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        service.entityToDtoByFilterBefore(dto, entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertNull(dto.getValue2());
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosByFilterBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosByFilterBeforeExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertNull(dto1.getValue2());
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertNull(dto2.getValue2());
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosByFilterBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosByFilterBefore(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertNull(dto1.getValue2());
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertNull(dto2.getValue2());
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosBeforeExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosBeforeExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertEquals(dto1.getValue2(), "entity1Val2");
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertEquals(dto2.getValue2(), "entity2Val2");
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosBefore() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosBefore(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertEquals(dto1.getValue2(), "entity1Val2");
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertEquals(dto2.getValue2(), "entity2Val2");
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoKeyAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = (ExtendedDataTransferObject) service.entityToDtoKeyAfterExact(entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoKeyAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = (ExtendedDataTransferObject) service.entityToDtoKeyAfter(entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoKeyByFilterAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = (ExtendedDataTransferObject) service.entityToDtoKeyByFilterAfterExact(entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertNull(dto.getValue2());
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoKeyByFilterAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = (ExtendedDataTransferObject) service.entityToDtoKeyByFilterAfter(entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertNull(dto.getValue2());
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoAfterExact(dto, entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoAfter(dto, entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoByFilterAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoByFilterAfterExact(dto, entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertNull(dto.getValue2());
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoByFilterAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoByFilterAfter(dto, entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertEquals(dto.getValue(), "entityVal1");
        assertNull(dto.getValue2());
        assertEquals(dto.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosByFilterAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosByFilterAfterExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertNull(dto1.getValue2());
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertNull(dto2.getValue2());
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosByFilterAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosByFilterAfter(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertNull(dto1.getValue2());
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertNull(dto2.getValue2());
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosAfterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosAfterExact(dtos, entities);

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertEquals(dto1.getValue2(), "entity1Val2");
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertEquals(dto2.getValue2(), "entity2Val2");
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntitiesToDtosAfter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final DomainObject entity1 = new DomainObjectImpl();
        entity1.setValue("entity1Val1");
        entity1.setValue2("entity1Val2");
        entity1.setTimestamp(time);
        final DomainObject entity2 = new DomainObjectImpl();
        entity2.setValue("entity2Val1");
        entity2.setValue2("entity2Val2");
        entity2.setTimestamp(time);

        final List<ExtendedDataTransferObject> dtos = new ArrayList<ExtendedDataTransferObject>();
        final List<DomainObject> entities = Arrays.asList(entity1, entity2);

        service.entitiesToDtosAfter(dtos, entities, new Object());

        assertEquals(entities.size(), 2);
        final ExtendedDataTransferObject dto1 = dtos.get(0);
        assertEquals(dto1.getValue(), "entity1Val1");
        assertEquals(dto1.getValue2(), "entity1Val2");
        assertEquals(dto1.getTimestamp(), time);
        final ExtendedDataTransferObject dto2 = dtos.get(1);
        assertEquals(dto2.getValue(), "entity2Val1");
        assertEquals(dto2.getValue2(), "entity2Val2");
        assertEquals(dto2.getTimestamp(), time);

        assertEquals(entity1.getValue(), "entity1Val1");
        assertEquals(entity1.getValue2(), "entity1Val2");
        assertEquals(entity1.getTimestamp(), time);

        assertEquals(entity2.getValue(), "entity2Val1");
        assertEquals(entity2.getValue2(), "entity2Val2");
        assertEquals(entity2.getTimestamp(), time);

        // final check counters
        assertCounters(onDtoSuccess, 2, onDtoFailure, 0, onEntitySuccess, 0, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        final ExtendedDataTransferObject res = (ExtendedDataTransferObject) service.dtoToEntityAndBackToDtoExact(dto, entity);

        assertNotSame(res, dto);

        assertEquals(entity.getValue(), "dtoVal2");
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        assertEquals(res.getValue(), "dtoVal2");
        assertEquals(res.getValue2(), "dtoVal1");
        assertEquals(res.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDto() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        final ExtendedDataTransferObject res = (ExtendedDataTransferObject) service.dtoToEntityAndBackToDto(dto, entity, new Object());

        assertNotSame(res, dto);

        assertEquals(entity.getValue(), "dtoVal2");
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        assertEquals(res.getValue(), "dtoVal2");
        assertEquals(res.getValue2(), "dtoVal1");
        assertEquals(res.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoByFilterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        final ExtendedDataTransferObject res = (ExtendedDataTransferObject) service.dtoToEntityAndBackToDtoByFilterExact(dto, entity);

        assertNotSame(res, dto);

        assertNull(entity.getValue());
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        assertNull(res.getValue(), "dtoVal1");
        assertNull(res.getValue2());
        assertEquals(res.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoByFilter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        final ExtendedDataTransferObject res = (ExtendedDataTransferObject) service.dtoToEntityAndBackToDtoByFilter(dto, entity, new Object());

        assertNotSame(res, dto);

        assertNull(entity.getValue());
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertEquals(dto.getValue(), "dtoVal1");
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), time);

        assertNull(res.getValue());
        assertNull(res.getValue2());
        assertEquals(res.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoVoidExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityAndBackToDtoVoidExact(dto, entity);

        assertEquals(entity.getValue(), "dtoVal2");
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertEquals(dto.getValue(), "dtoVal2");
        assertEquals(dto.getValue2(), "dtoVal1");
        assertEquals(dto.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoVoid() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityAndBackToDtoVoid(dto, entity, new Object());

        assertEquals(entity.getValue(), "dtoVal2");
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertEquals(dto.getValue(), "dtoVal2");
        assertEquals(dto.getValue2(), "dtoVal1");
        assertEquals(dto.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoVoidByFilterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityAndBackToDtoVoidByFilterExact(dto, entity);

        assertNull(entity.getValue());
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertNull(dto.getValue());
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testDtoToEntityAndBackToDtoVoidByFilter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();
        dto.setValue("dtoVal1");
        dto.setValue2("dtoVal2");
        dto.setTimestamp(time);

        final DomainObject entity = new DomainObjectImpl();

        service.dtoToEntityAndBackToDtoVoidByFilter(dto, entity, new Object());

        assertNull(entity.getValue());
        assertEquals(entity.getValue2(), "dtoVal1");
        assertNotNull(entity.getTimestamp());
        assertTrue(entity.getTimestamp().after(time));

        assertNull(dto.getValue());
        assertEquals(dto.getValue2(), "dtoVal2");
        assertEquals(dto.getTimestamp(), entity.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject res = (DomainObject) service.entityToDtoAndBackToEntityExact(dto, entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertNotSame(res, entity);

        assertEquals(dto.getValue(), "entityVal2");
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertEquals(res.getValue(), "entityVal2");
        assertEquals(res.getValue2(), "entityVal1");
        assertNull(res.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntity() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject res = (DomainObject) service.entityToDtoAndBackToEntity(dto, entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertNotSame(res, entity);

        assertEquals(dto.getValue(), "entityVal2");
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertEquals(res.getValue(), "entityVal2");
        assertEquals(res.getValue2(), "entityVal1");
        assertNull(res.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityByFilterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject res = (DomainObject) service.entityToDtoAndBackToEntityByFilterExact(dto, entity);

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertNotSame(res, entity);

        assertNull(dto.getValue());
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertNull(res.getValue());
        assertNull(res.getValue2());
        assertNull(res.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityByFilter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        final DomainObject res = (DomainObject) service.entityToDtoAndBackToEntityByFilter(dto, entity, new Object());

        assertEquals(entity.getValue(), "entityVal1");
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);

        assertNotSame(res, entity);

        assertNull(dto.getValue());
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertNull(res.getValue());
        assertNull(res.getValue2());
        assertNull(res.getTimestamp());

        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityVoidExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoAndBackToEntityVoidExact(dto, entity);

        assertEquals(dto.getValue(), "entityVal2");
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertEquals(entity.getValue(), "entityVal2");
        assertEquals(entity.getValue2(), "entityVal1");
        assertEquals(entity.getTimestamp(), time);


        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityVoid() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoAndBackToEntityVoid(dto, entity, new Object());

        assertEquals(dto.getValue(), "entityVal2");
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertEquals(entity.getValue(), "entityVal2");
        assertEquals(entity.getValue2(), "entityVal1");
        assertEquals(entity.getTimestamp(), time);


        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityVoidByFilterExact() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoAndBackToEntityVoidByFilterExact(dto, entity);

        assertNull(dto.getValue());
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertNull(entity.getValue());
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);


        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testEntityToDtoAndBackToEntityVoidByFilter() {

        final DTOCountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", DTOCountingEventListener.class);
        final DTOCountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", DTOCountingEventListener.class);

        final TestService service =
                this.applicationContext.getBean("simpleTransferableService", TestService.class);

        assertCountersAreZero(onDtoSuccess, onDtoFailure, onEntitySuccess, onEntityFailure);

        final Date time = new Date();

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");
        entity.setTimestamp(time);

        final ExtendedDataTransferObject dto = new ExtendedDataTransferObjectImpl();

        service.entityToDtoAndBackToEntityVoidByFilter(dto, entity, new Object());

        assertNull(dto.getValue());
        assertEquals(dto.getValue2(), "entityVal1");
        assertNotNull(dto.getTimestamp());
        assertTrue(dto.getTimestamp().after(entity.getTimestamp()));

        assertNull(entity.getValue());
        assertEquals(entity.getValue2(), "entityVal2");
        assertEquals(entity.getTimestamp(), time);


        // final check counters
        assertCounters(onDtoSuccess, 1, onDtoFailure, 0, onEntitySuccess, 1, onEntityFailure, 0);

    }


    /* TEST SUPPORT UTILITIES ******************************************************* */

    void assertCountersAreZero(final DTOCountingEventListener onDtoSuccess,
                               final DTOCountingEventListener onDtoFailure,
                               final DTOCountingEventListener onEntitySuccess,
                               final DTOCountingEventListener onEntityFailure) {

        // reset event counters

        onDtoSuccess.reset();
        onDtoFailure.reset();
        onEntitySuccess.reset();
        onEntityFailure.reset();

        assertEquals(onDtoSuccess.getCount(), 0);
        assertEquals(onDtoFailure.getCount(), 0);
        assertEquals(onEntitySuccess.getCount(), 0);
        assertEquals(onEntityFailure.getCount(), 0);
    }


    void assertCounters(final DTOCountingEventListener onDtoSuccess,
                        final int onDtoSuccessCount,
                        final DTOCountingEventListener onDtoFailure,
                        final int onDtoFailureCount,
                        final DTOCountingEventListener onEntitySuccess,
                        final int onEntitySuccessCount,
                        final DTOCountingEventListener onEntityFailure,
                        final int onEntityFailureCount) {
        
        assertEquals(onDtoSuccess.getCount(), onDtoSuccessCount);
        assertEquals(onDtoFailure.getCount(), onDtoFailureCount);
        assertEquals(onEntitySuccess.getCount(), onEntitySuccessCount);
        assertEquals(onEntityFailure.getCount(), onEntityFailureCount);

    }


}