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
import com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 30, 2012
 * Time: 6:35:10 PM
 */
@ContextConfiguration("/spring-xml.xml")
public class IntegrationXmlTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testBeansSetup() {

        final DTOFactory factory = this.applicationContext.getBean("dtoFactory", DTOFactory.class);
        assertNotNull(factory);

        final DTOSupport support = this.applicationContext.getBean("dtoSupport", DTOSupport.class);
        assertNotNull(support);

        final ExposedDTOAdaptersRegistrar registrar = this.applicationContext.getBean("adapterRegistrar", ExposedDTOAdaptersRegistrar.class);
        assertNotNull(registrar);

        final Map<String, Object> adapters = registrar.getAdapters();
        assertNotNull(adapters);
        assertEquals(3, adapters.size());

        final ExposedValueConverter vc = (ExposedValueConverter) adapters.get("vc");
        assertNotNull(vc.getDtoSupport());

        final ExposedEntityRetriever er = (ExposedEntityRetriever) adapters.get("er");
        assertNotNull(er.getDtoSupport());

        final ExposedMatcher ma = (ExposedMatcher) adapters.get("ma");
        assertNotNull(ma.getDtoSupport());

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testTransferCustomProperty() {

        final TestXmlService service =
                this.applicationContext.getBean("xmlTestServiceCustomProperty", TestXmlService.class);

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");

        final ExtendedDataTransferObject dto = service.transfer(entity);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");

    }

    @Test(dependsOnMethods = "testBeansSetup")
    public void testTransferDefaultProperty() {

        final TestXmlService service =
                this.applicationContext.getBean("xmlTestServiceDefaultProperty", TestXmlService.class);

        final DomainObject entity = new DomainObjectImpl();
        entity.setValue("entityVal1");
        entity.setValue2("entityVal2");

        final ExtendedDataTransferObject dto = service.transfer(entity);

        assertEquals(dto.getValue(), "entityVal1");
        assertEquals(dto.getValue2(), "entityVal2");

    }



}