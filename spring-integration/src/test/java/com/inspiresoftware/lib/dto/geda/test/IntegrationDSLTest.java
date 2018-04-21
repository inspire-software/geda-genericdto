/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.DTOFactory;
import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.config.AnnotationDrivenGeDABeanDefinitionParser;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfigResolver;
import com.inspiresoftware.lib.dto.geda.interceptor.GeDAInterceptor;
import com.inspiresoftware.lib.dto.geda.test.impl.DSLExtendedDataTransferObjectImpl;
import com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.*;

/**
 * .
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 5:30:53 PM
 */
public abstract class IntegrationDSLTest extends AbstractTestNGSpringContextTests {

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

        final TestService service =
                this.applicationContext.getBean("dslSimpleTransferableService", TestService.class);

        final Date time = new Date();
        final ExtendedDataTransferObject dto = new DSLExtendedDataTransferObjectImpl();
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

    }

}