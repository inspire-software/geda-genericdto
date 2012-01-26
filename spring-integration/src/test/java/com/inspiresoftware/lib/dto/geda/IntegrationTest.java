/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda;

import com.inspiresoftware.lib.dto.geda.config.AnnotationDrivenGeDABeanDefinitionParser;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import com.inspiresoftware.lib.dto.geda.interceptor.GeDAInterceptor;
import com.inspiresoftware.lib.dto.geda.test.DomainObject;
import com.inspiresoftware.lib.dto.geda.test.ExtendedDataTransferObject;
import com.inspiresoftware.lib.dto.geda.test.SimpleTransferableService;
import com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener;
import com.inspiresoftware.lib.dto.geda.test.impl.DataTransferObjectImpl;
import com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl;
import com.inspiresoftware.lib.dto.geda.test.impl.ExtendedDataTransferObjectImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:30:53 PM
 */
@ContextConfiguration("/spring.xml")
public class IntegrationTest extends AbstractTestNGSpringContextTests {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationTest.class);

    @Test
    public void testStartContext() {

    }

    @Test(dependsOnMethods = "testStartContext")
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
    public void testSimpleBeforeOnly() {

        final CountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", CountingEventListener.class);
        final CountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", CountingEventListener.class);
        final CountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", CountingEventListener.class);
        final CountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", CountingEventListener.class);

        assertEquals(onDtoSuccess.getCount(), 0);
        assertEquals(onDtoFailure.getCount(), 0);
        assertEquals(onEntitySuccess.getCount(), 0);
        assertEquals(onEntityFailure.getCount(), 0);

        // ENTITY to DTO tests

        final SimpleTransferableService simpleTransferableService =
                this.applicationContext.getBean("simpleTransferableService", SimpleTransferableService.class);

        final DomainObjectImpl entity1 = new DomainObjectImpl();
        entity1.setValue("val1");
        entity1.setValue2("val2");
        entity1.setTimestamp(new Date());

        // Simple population of dto1 with entity1 data

        final ExtendedDataTransferObjectImpl dto1 = new ExtendedDataTransferObjectImpl();

        simpleTransferableService.loadDtoBefore(dto1, entity1);

        assertEquals(onDtoSuccess.getCount(), 1);

        assertNotNull(dto1.getValue());
        assertEquals(dto1.getValue(), entity1.getValue());
        assertNotNull(dto1.getValue2());
        assertEquals(dto1.getValue2(), entity1.getValue2());
        assertNotNull(dto1.getTimestamp());
        assertEquals(dto1.getTimestamp(), entity1.getTimestamp());

        // Filtered population of dto2 with entity1 data (only use basic data)

        final ExtendedDataTransferObjectImpl dto2 = new ExtendedDataTransferObjectImpl();

        simpleTransferableService.loadDtoBefore(DataTransferObjectImpl.class, dto2, entity1);

        assertEquals(onDtoSuccess.getCount(), 2);

        assertNotNull(dto2.getValue());
        assertEquals(dto2.getValue(), entity1.getValue());
        assertNull(dto2.getValue2());
        assertNotNull(dto2.getTimestamp());
        assertEquals(dto2.getTimestamp(), entity1.getTimestamp());

        // Filtered population of dto3 with entity1 data (all data since filter class is equal to dto3 class)

        final ExtendedDataTransferObjectImpl dto3 = new ExtendedDataTransferObjectImpl();

        simpleTransferableService.loadDtoBefore(ExtendedDataTransferObjectImpl.class, dto3, entity1);

        assertEquals(onDtoSuccess.getCount(), 3);

        assertNotNull(dto3.getValue());
        assertEquals(dto3.getValue(), entity1.getValue());
        assertNotNull(dto3.getValue2());
        assertEquals(dto3.getValue2(), entity1.getValue2());
        assertNotNull(dto3.getTimestamp());
        assertEquals(dto3.getTimestamp(), entity1.getTimestamp());

        // Simple population of dtos1 collection with entities1 collection data

        final List<ExtendedDataTransferObject> dtos1 = new ArrayList<ExtendedDataTransferObject>();

        final List<DomainObject> entities1 = Arrays.asList((DomainObject) entity1);

        simpleTransferableService.loadDtoBefore(ExtendedDataTransferObjectImpl.class, dtos1, entities1);

        assertEquals(onDtoSuccess.getCount(), 4);

        assertEquals(dtos1.size(), 1);
        final ExtendedDataTransferObject dto4 = dtos1.get(0);
        assertNotNull(dto4.getValue());
        assertEquals(dto4.getValue(), entity1.getValue());
        assertNotNull(dto4.getValue2());
        assertEquals(dto4.getValue2(), entity1.getValue2());
        assertNotNull(dto4.getTimestamp());
        assertEquals(dto4.getTimestamp(), entity1.getTimestamp());

        // DTO to ENTITY tests

        final ExtendedDataTransferObjectImpl dto5 = new ExtendedDataTransferObjectImpl();
        dto5.setValue("dtoVal1");
        dto5.setValue2("dtoVal2");
        dto5.setTimestamp(new Date());

        // Simple population of entity2 with dto5 data

        final DomainObjectImpl entity2 = new DomainObjectImpl();

        simpleTransferableService.loadEntityBefore(dto5, entity2);

        assertEquals(onEntitySuccess.getCount(), 1);

        assertNotNull(entity2.getValue());
        assertEquals(entity2.getValue(), dto5.getValue());
        assertNotNull(entity2.getValue2());
        assertEquals(entity2.getValue2(), dto5.getValue2());
        assertNull(entity2.getTimestamp());

        // Filtered population of entity3 with dto5 data (only basic data)

        final DomainObjectImpl entity3 = new DomainObjectImpl();

        simpleTransferableService.loadEntityBefore(DataTransferObjectImpl.class, dto5, entity3);

        assertEquals(onEntitySuccess.getCount(), 2);

        assertNotNull(entity3.getValue());
        assertEquals(entity3.getValue(), dto5.getValue());
        assertNull(entity3.getValue2());
        assertNull(entity3.getTimestamp());

        // Simple population of entities2 with dtos2 data

        final Set<DomainObject> entities2 = new HashSet<DomainObject>();
        final List<ExtendedDataTransferObject> dtos2 = Arrays.asList((ExtendedDataTransferObject) dto5);

        simpleTransferableService.loadEntityBefore(DomainObjectImpl.class, dtos2, entities2);

        assertEquals(onEntitySuccess.getCount(), 3);

        assertEquals(entities2.size(), 1);
        final DomainObject entity4 = entities2.iterator().next();

        assertNotNull(entity4.getValue());
        assertEquals(entity4.getValue(), dto5.getValue());
        assertNotNull(entity4.getValue2());
        assertEquals(entity4.getValue2(), dto5.getValue2());
        assertNull(entity4.getTimestamp());

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

    @Test(dependsOnMethods = "testSimpleBeforeOnly")
    public void testSimpleAfterOnly() {


        final CountingEventListener onDtoSuccess = this.applicationContext.getBean("onDtoSuccess", CountingEventListener.class);
        final CountingEventListener onDtoFailure = this.applicationContext.getBean("onDtoFailure", CountingEventListener.class);
        final CountingEventListener onEntitySuccess = this.applicationContext.getBean("onEntitySuccess", CountingEventListener.class);
        final CountingEventListener onEntityFailure = this.applicationContext.getBean("onEntityFailure", CountingEventListener.class);

        assertEquals(onDtoSuccess.getCount(), 0);
        assertEquals(onDtoFailure.getCount(), 0);
        assertEquals(onEntitySuccess.getCount(), 0);
        assertEquals(onEntityFailure.getCount(), 0);

        final SimpleTransferableService simpleTransferableService =
                this.applicationContext.getBean("simpleTransferableService", SimpleTransferableService.class);

        // ENTITY to DTO test

        final DomainObjectImpl entity1 = new DomainObjectImpl();
        entity1.setValue("val1");
        entity1.setValue2("val2");
        entity1.setTimestamp(new Date());

        // Simple population of dto1 with entity1 data

        final ExtendedDataTransferObjectImpl dto1 = new ExtendedDataTransferObjectImpl();

        final ExtendedDataTransferObject result1 = simpleTransferableService.loadDtoAfter(dto1, entity1, false);

        assertEquals(onDtoSuccess.getCount(), 1);

        assertSame(result1, dto1);

        assertNotNull(dto1.getValue());
        assertEquals(dto1.getValue(), entity1.getValue());
        assertNotNull(dto1.getValue2());
        assertEquals(dto1.getValue2(), entity1.getValue2());
        assertNotNull(dto1.getTimestamp());
        assertEquals(dto1.getTimestamp(), entity1.getTimestamp());

        // Population of the method's result1_1 dto with entity1 data without altering dto1_1

        final ExtendedDataTransferObjectImpl dto1_1 = new ExtendedDataTransferObjectImpl();

        final ExtendedDataTransferObject result1_1 = simpleTransferableService.loadDtoAfter(dto1_1, entity1, true);

        assertEquals(onDtoSuccess.getCount(), 2);

        assertNotSame(result1_1, dto1_1);

        assertNull(dto1_1.getValue());
        assertNull(dto1_1.getValue2());
        assertNull(dto1_1.getTimestamp());

        assertNotNull(result1_1.getValue());
        assertEquals(result1_1.getValue(), entity1.getValue());
        assertNotNull(result1_1.getValue2());
        assertEquals(result1_1.getValue2(), entity1.getValue2());
        assertNotNull(result1_1.getTimestamp());
        assertEquals(result1_1.getTimestamp(), entity1.getTimestamp());


        // Filtered population of dto2 with entity1 data (only basic info)

        final ExtendedDataTransferObjectImpl dto2 = new ExtendedDataTransferObjectImpl();

        final ExtendedDataTransferObject result2 =
                simpleTransferableService.loadDtoAfter(DataTransferObjectImpl.class, dto2, entity1, false);

        assertEquals(onDtoSuccess.getCount(), 3);

        assertSame(result2, dto2);

        assertNotNull(dto2.getValue());
        assertEquals(dto2.getValue(), entity1.getValue());
        assertNull(dto2.getValue2());
        assertNotNull(dto2.getTimestamp());
        assertEquals(dto2.getTimestamp(), entity1.getTimestamp());

        // Population of the method's result2_1 dto with entity1 data without altering dto2_1

        final ExtendedDataTransferObjectImpl dto2_1 = new ExtendedDataTransferObjectImpl();

        final ExtendedDataTransferObject result2_1 =
                simpleTransferableService.loadDtoAfter(DataTransferObjectImpl.class, dto2_1, entity1, true);

        assertEquals(onDtoSuccess.getCount(), 4);

        assertNotSame(result2_1, dto2_1);

        assertNull(dto2_1.getValue());
        assertNull(dto2_1.getValue2());
        assertNull(dto2_1.getTimestamp());

        assertNotNull(result2_1.getValue());
        assertEquals(result2_1.getValue(), entity1.getValue());
        assertNull(result2_1.getValue2());
        assertNotNull(result2_1.getTimestamp());
        assertEquals(result2_1.getTimestamp(), entity1.getTimestamp());

        // Simple population of dtos1 collection with entities1 collection data

        final List<ExtendedDataTransferObject> dtos1 = new ArrayList<ExtendedDataTransferObject>();

        final List<DomainObject> entities1 = Arrays.asList((DomainObject) entity1);

        final Collection<ExtendedDataTransferObject> results1 =
                simpleTransferableService.loadDtoAfter(ExtendedDataTransferObjectImpl.class, dtos1, entities1, false);

        assertEquals(onDtoSuccess.getCount(), 5);

        assertSame(results1, dtos1);

        assertEquals(dtos1.size(), 1);
        final ExtendedDataTransferObject dto4 = dtos1.get(0);
        assertNotNull(dto4.getValue());
        assertEquals(dto4.getValue(), entity1.getValue());
        assertNotNull(dto4.getValue2());
        assertEquals(dto4.getValue2(), entity1.getValue2());
        assertNotNull(dto4.getTimestamp());
        assertEquals(dto4.getTimestamp(), entity1.getTimestamp());

        // Population of the method's results1_1 dtos with entities1 data without altering dtos1_1

        final List<ExtendedDataTransferObject> dtos1_1 = new ArrayList<ExtendedDataTransferObject>();

        final Collection<ExtendedDataTransferObject> results1_1 =
                simpleTransferableService.loadDtoAfter(ExtendedDataTransferObjectImpl.class, dtos1_1, entities1, true);

        assertEquals(onDtoSuccess.getCount(), 6);

        assertNotSame(results1_1, dtos1_1);

        assertTrue(dtos1_1.isEmpty());

        assertEquals(results1_1.size(), 1);
        final ExtendedDataTransferObject dto5 = results1_1.iterator().next();
        assertNotNull(dto5.getValue());
        assertEquals(dto5.getValue(), entity1.getValue());
        assertNotNull(dto5.getValue2());
        assertEquals(dto5.getValue2(), entity1.getValue2());
        assertNotNull(dto5.getTimestamp());
        assertEquals(dto5.getTimestamp(), entity1.getTimestamp());


         // DTO to ENTITY tests











        onDtoSuccess.reset();
        onDtoFailure.reset();
        onEntitySuccess.reset();
        onEntityFailure.reset();

        assertEquals(onDtoSuccess.getCount(), 0);
        assertEquals(onDtoFailure.getCount(), 0);
        assertEquals(onEntitySuccess.getCount(), 0);
        assertEquals(onEntityFailure.getCount(), 0);
        

    }



}