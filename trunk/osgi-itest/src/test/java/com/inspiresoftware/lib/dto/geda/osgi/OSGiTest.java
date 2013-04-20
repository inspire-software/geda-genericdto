/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.event.DTOEventListener;
import com.inspiresoftware.lib.dto.geda.osgi.test.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * User: denispavlov
 * Date: 13-02-19
 * Time: 2:23 PM
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class OSGiTest {

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public Option[] configFelix() throws Exception{

        return options(
                cleanCaches(),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javassist", "3.12.1.ga_1").start(),
                mavenBundle("com.inspire-software.lib.dto.geda", "geda.osgi").versionAsInProject().start(),
                bootDelegationPackage("com.inspiresoftware.lib.dto.geda.osgi.test"),
                junitBundles()
        );
    }

    @ProbeBuilder
    public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
        return probe;
    }

    @Test
    public void testServicesAreSetup() throws Exception {

        final Logger log = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        String vendor = bundleContext.getBundle(0).getHeaders().get(Constants.BUNDLE_VENDOR);
        if (vendor == null) {
            vendor = bundleContext.getBundle(0).getHeaders().get(Constants.BUNDLE_SYMBOLICNAME);
        }
        String version = bundleContext.getBundle(0).getHeaders().get(Constants.BUNDLE_VERSION);
        log.info("\n\nOSGi Framework : {} - {} \n\n", vendor, version);

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);
        assertNotNull(facadeReference);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);
        assertNotNull(facade);

    }

    @Test
    public void testAnnotationsSimpleByClass() throws Exception {

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);
        assertNotNull(facadeReference);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);
        assertNotNull(facade);

        final DTOSupportAnnotationsService annotationsService = facade.getAnnService(this.getClass().getClassLoader());

        final SimpleEntityClass entity = new SimpleEntityClass();
        entity.setString("aether");
        entity.setDecimal(new BigDecimal("28.97"));
        entity.setInteger(1);
        final SimpleDTOClass dto = new SimpleDTOClass();

        final DTOEventListener expectations = new ExpectationsDTOEventListener(new Object[] { dto, entity });

        annotationsService.addListener("onDtoAssembled", expectations);

        annotationsService.assembleDto(dto, entity, null, "simple");

        annotationsService.removeListener(expectations);

        assertEquals("aether", dto.getString());
        assertTrue(new BigDecimal("28.97").compareTo(dto.getDecimal()) == 0);
        assertEquals(1, dto.getInteger());

        final SimpleEntityClass copy = new SimpleEntityClass();

        annotationsService.assembleEntity(dto, copy, null, "copy");

        assertEquals("aether", copy.getString());
        assertTrue(new BigDecimal("28.97").compareTo(copy.getDecimal()) == 0);
        assertEquals(1, copy.getInteger());

    }

    @Test
    public void testAnnotationsSimpleByInterface() throws Exception {

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);
        assertNotNull(facadeReference);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);
        assertNotNull(facade);

        final DTOSupportAnnotationsService annotationsService = facade.getAnnService(this.getClass().getClassLoader());

        final SimpleEntity entity = new SimpleEntityClass();
        entity.setString("aether");
        entity.setDecimal(new BigDecimal("28.97"));
        entity.setInteger(1);
        final SimpleDTO dto = new SimpleDTOClass();

        final DTOEventListener expectations = new ExpectationsDTOEventListener(new Object[] { dto, entity });

        annotationsService.addListener("onDtoAssembled", expectations);

        annotationsService.assembleDto(dto, entity, null, "simple");

        annotationsService.removeListener(expectations);

        assertEquals("aether", dto.getString());
        assertTrue(new BigDecimal("28.97").compareTo(dto.getDecimal()) == 0);
        assertEquals(1, dto.getInteger());

        final SimpleEntity copy = new SimpleEntityClass();

        annotationsService.assembleEntity(dto, copy, null, "copy");

        assertEquals("aether", copy.getString());
        assertTrue(new BigDecimal("28.97").compareTo(copy.getDecimal()) == 0);
        assertEquals(1, copy.getInteger());

    }


    @Test
    public void testAnnotationsComplex() throws Exception {

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);
        assertNotNull(facadeReference);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);
        assertNotNull(facade);

        final ExtensibleBeanFactory beanFactory = facade.createBeanFactory(this.getClass().getClassLoader());
        final DTOSupportAnnotationsService annotationsService = facade.getAnnService(this.getClass().getClassLoader());

        beanFactory.registerDto("SimpleDTO",
                "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleDTOClass");
        beanFactory.registerEntity("SimpleEntity",
                "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleEntityClass",
                "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleEntity");
        beanFactory.registerDto("ComplexDTO",
                "com.inspiresoftware.lib.dto.geda.osgi.test.ComplexDTOClass");
        beanFactory.registerEntity("ComplexEntity",
                "com.inspiresoftware.lib.dto.geda.osgi.test.ComplexEntityClass",
                "com.inspiresoftware.lib.dto.geda.osgi.test.ComplexEntity");
        annotationsService.registerAdapter("EqualsByString", new EqualsByStringMatcher());

        final SimpleEntity inner = (SimpleEntity) beanFactory.get("SimpleEntity");
        inner.setString("aether");
        inner.setDecimal(new BigDecimal("28.97"));
        inner.setInteger(1);

        final SimpleEntity colItem = (SimpleEntity) beanFactory.get("SimpleEntity");
        colItem.setString("aqua");
        colItem.setDecimal(new BigDecimal("18.0153"));
        colItem.setInteger(2);
        final List<SimpleEntity> col = new ArrayList<SimpleEntity>();
        col.add(colItem);

        final SimpleEntity mapItem = (SimpleEntity) beanFactory.get("SimpleEntity");
        mapItem.setString("terra");
        mapItem.setDecimal(new BigDecimal("150"));
        mapItem.setInteger(3);
        final Map<String, SimpleEntity> map = new HashMap<String, SimpleEntity>();
        map.put(mapItem.getString(), mapItem);

        final ComplexEntity entity = (ComplexEntity) beanFactory.get("ComplexEntity");
        entity.setName("elements");
        entity.setInner(inner);
        entity.setCollection(col);
        entity.setMap(map);

        final ComplexDTO dto = (ComplexDTO) beanFactory.get("ComplexDTO");

        final DTOEventListener expectations = new ExpectationsDTOEventListener(new Object[] { dto, entity });

        annotationsService.addListener("onDtoAssembled", expectations);

        annotationsService.assembleDto(dto, entity, beanFactory, "simple");

        annotationsService.removeListener(expectations);

        assertEquals("elements", dto.getName());

        final SimpleDTO dtoInner = dto.getInner();
        assertNotNull(dtoInner);
        assertEquals("aether", dtoInner.getString());
        assertTrue(new BigDecimal("28.97").compareTo(dtoInner.getDecimal()) == 0);
        assertEquals(1, dtoInner.getInteger());

        final List<SimpleDTO> dtoCol = dto.getCollection();
        assertNotNull(dtoCol);
        assertEquals(1, dtoCol.size());
        final SimpleDTO dtoColItem = dtoCol.get(0);
        assertNotNull(dtoColItem);
        assertEquals("aqua", dtoColItem.getString());
        assertTrue(new BigDecimal("18.0153").compareTo(dtoColItem.getDecimal()) == 0);
        assertEquals(2, dtoColItem.getInteger());

        final Map<String, SimpleDTO> dtoMap = dto.getMap();
        assertNotNull(dtoMap);
        assertEquals(1, dtoMap.size());
        final Map.Entry<String, SimpleDTO> dtoMapItem = dtoMap.entrySet().iterator().next();
        assertNotNull(dtoMapItem);
        assertEquals("terra", dtoMapItem.getKey());
        assertNotNull(dtoMapItem.getValue());
        assertEquals("terra", dtoMapItem.getValue().getString());
        assertTrue(new BigDecimal("150").compareTo(dtoMapItem.getValue().getDecimal()) == 0);
        assertEquals(3, dtoMapItem.getValue().getInteger());

        final ComplexEntity copy = new ComplexEntityClass();

        annotationsService.assembleEntity(dto, copy, beanFactory, "copy");

        assertEquals("elements", copy.getName());

        final SimpleEntity copyInner = copy.getInner();
        assertNotNull(copyInner);
        assertEquals("aether", copyInner.getString());
        assertTrue(new BigDecimal("28.97").compareTo(copyInner.getDecimal()) == 0);
        assertEquals(1, copyInner.getInteger());

        final List<SimpleEntity> copyCol = copy.getCollection();
        assertNotNull(copyCol);
        assertEquals(1, copyCol.size());
        final SimpleEntity copyColItem = copyCol.get(0);
        assertNotNull(copyColItem);
        assertEquals("aqua", copyColItem.getString());
        assertTrue(new BigDecimal("18.0153").compareTo(copyColItem.getDecimal()) == 0);
        assertEquals(2, copyColItem.getInteger());

        final Map<String, SimpleEntity> copyMap = copy.getMap();
        assertNotNull(copyMap);
        assertEquals(1, copyMap.size());
        final Map.Entry<String, SimpleEntity> copyMapItem = copyMap.entrySet().iterator().next();
        assertNotNull(copyMapItem);
        assertEquals("terra", copyMapItem.getKey());
        assertNotNull(copyMapItem.getValue());
        assertEquals("terra", copyMapItem.getValue().getString());
        assertTrue(new BigDecimal("150").compareTo(copyMapItem.getValue().getDecimal()) == 0);
        assertEquals(3, copyMapItem.getValue().getInteger());

    }

    @Test
    public void testDSLSimple() throws Exception {

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);
        assertNotNull(facadeReference);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);
        assertNotNull(facade);

        final ExtensibleBeanFactory beanFactory = facade.createBeanFactory(this.getClass().getClassLoader());
        final DTOSupportDSLService dslService = facade.getDSLService(this.getClass().getClassLoader());
        Registry basic = dslService.getRegistry("basic");
        if (basic == null) {
            basic = dslService.createRegistry("basic", beanFactory);
            basic.dto(SimpleDTOClass.class).alias("SimpleDTO").forEntityGeneric()
                    .withField("string").and().withField("decimal").and().withField("integer");
        }

        final SimpleEntity entity = new SimpleEntityClass();
        entity.setString("aether");
        entity.setDecimal(new BigDecimal("28.97"));
        entity.setInteger(1);
        final SimpleDTO dto = new SimpleDTOClass();

        final DTOEventListener expectations = new ExpectationsDTOEventListener(new Object[] { dto, entity });

        dslService.addListener("onDtoAssembled", expectations);

        dslService.assembleDto(dto, entity, basic, "simple");

        dslService.removeListener(expectations);

        assertEquals("aether", dto.getString());
        assertTrue(new BigDecimal("28.97").compareTo(dto.getDecimal()) == 0);
        assertEquals(1, dto.getInteger());

        final SimpleEntity copy = new SimpleEntityClass();

        dslService.assembleEntity(dto, copy, basic, "copy");

        assertEquals("aether", copy.getString());
        assertTrue(new BigDecimal("28.97").compareTo(copy.getDecimal()) == 0);
        assertEquals(1, copy.getInteger());

    }


    @Test
    public void testDSLComplex() throws Exception {

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);
        assertNotNull(facadeReference);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);
        assertNotNull(facade);

        final ExtensibleBeanFactory beanFactory = facade.createBeanFactory(this.getClass().getClassLoader());
        final DTOSupportDSLService dslService = facade.getDSLService(this.getClass().getClassLoader());
        Registry basic = dslService.getRegistry("basic");
        if (basic == null) {

            beanFactory.registerEntity("SimpleEntity",
                    "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleEntityClass",
                    "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleEntity");

            basic = dslService.createRegistry("basic", beanFactory);
            basic.dto(SimpleDTOClass.class).alias("SimpleDTO").forEntityGeneric()
                    .withField("string").and().withField("decimal").and().withField("integer");
            basic.dto(ComplexDTOClass.class).alias("ComplexDTO").forEntityGeneric()
                    .withField("name").and()
                    .withField("inner")
                        .dtoBeanKey("SimpleDTO")
                        .entityBeanKeys("SimpleEntity").and()
                    .withCollection("collection")
                        .dtoBeanKey("SimpleDTO")
                        .entityBeanKeys("SimpleEntity")
                        .dtoToEntityMatcherKey("EqualsByString").and()
                    .withMap("map")
                        .dtoBeanKey("SimpleDTO")
                        .entityBeanKeys("SimpleEntity")
                        .dtoToEntityMatcherKey("EqualsByString").and();

        }

        dslService.registerAdapter("EqualsByString", new EqualsByStringMatcher());


        final SimpleEntity inner = new SimpleEntityClass();
        inner.setString("aether");
        inner.setDecimal(new BigDecimal("28.97"));
        inner.setInteger(1);

        final SimpleEntity colItem = new SimpleEntityClass();
        colItem.setString("aqua");
        colItem.setDecimal(new BigDecimal("18.0153"));
        colItem.setInteger(2);
        final List<SimpleEntity> col = new ArrayList<SimpleEntity>();
        col.add(colItem);

        final SimpleEntity mapItem = new SimpleEntityClass();
        mapItem.setString("terra");
        mapItem.setDecimal(new BigDecimal("150"));
        mapItem.setInteger(3);
        final Map<String, SimpleEntity> map = new HashMap<String, SimpleEntity>();
        map.put(mapItem.getString(), mapItem);

        final ComplexEntity entity = new ComplexEntityClass();
        entity.setName("elements");
        entity.setInner(inner);
        entity.setCollection(col);
        entity.setMap(map);

        final ComplexDTO dto = new ComplexDTOClass();

        final DTOEventListener expectations = new ExpectationsDTOEventListener(new Object[] { dto, entity });

        dslService.addListener("onDtoAssembled", expectations);

        dslService.assembleDto(dto, entity, basic, "simple");

        dslService.removeListener(expectations);


        assertEquals("elements", dto.getName());

        final SimpleDTO dtoInner = dto.getInner();
        assertNotNull(dtoInner);
        assertEquals("aether", dtoInner.getString());
        assertTrue(new BigDecimal("28.97").compareTo(dtoInner.getDecimal()) == 0);
        assertEquals(1, dtoInner.getInteger());

        final List<SimpleDTO> dtoCol = dto.getCollection();
        assertNotNull(dtoCol);
        assertEquals(1, dtoCol.size());
        final SimpleDTO dtoColItem = dtoCol.get(0);
        assertNotNull(dtoColItem);
        assertEquals("aqua", dtoColItem.getString());
        assertTrue(new BigDecimal("18.0153").compareTo(dtoColItem.getDecimal()) == 0);
        assertEquals(2, dtoColItem.getInteger());

        final Map<String, SimpleDTO> dtoMap = dto.getMap();
        assertNotNull(dtoMap);
        assertEquals(1, dtoMap.size());
        final Map.Entry<String, SimpleDTO> dtoMapItem = dtoMap.entrySet().iterator().next();
        assertNotNull(dtoMapItem);
        assertEquals("terra", dtoMapItem.getKey());
        assertNotNull(dtoMapItem.getValue());
        assertEquals("terra", dtoMapItem.getValue().getString());
        assertTrue(new BigDecimal("150").compareTo(dtoMapItem.getValue().getDecimal()) == 0);
        assertEquals(3, dtoMapItem.getValue().getInteger());

        final ComplexEntity copy = new ComplexEntityClass();

        dslService.assembleEntity(dto, copy, basic, "copy");

        assertEquals("elements", copy.getName());

        final SimpleEntity copyInner = copy.getInner();
        assertNotNull(copyInner);
        assertEquals("aether", copyInner.getString());
        assertTrue(new BigDecimal("28.97").compareTo(copyInner.getDecimal()) == 0);
        assertEquals(1, copyInner.getInteger());

        final List<SimpleEntity> copyCol = copy.getCollection();
        assertNotNull(copyCol);
        assertEquals(1, copyCol.size());
        final SimpleEntity copyColItem = copyCol.get(0);
        assertNotNull(copyColItem);
        assertEquals("aqua", copyColItem.getString());
        assertTrue(new BigDecimal("18.0153").compareTo(copyColItem.getDecimal()) == 0);
        assertEquals(2, copyColItem.getInteger());

        final Map<String, SimpleEntity> copyMap = copy.getMap();
        assertNotNull(copyMap);
        assertEquals(1, copyMap.size());
        final Map.Entry<String, SimpleEntity> copyMapItem = copyMap.entrySet().iterator().next();
        assertNotNull(copyMapItem);
        assertEquals("terra", copyMapItem.getKey());
        assertNotNull(copyMapItem.getValue());
        assertEquals("terra", copyMapItem.getValue().getString());
        assertTrue(new BigDecimal("150").compareTo(copyMapItem.getValue().getDecimal()) == 0);
        assertEquals(3, copyMapItem.getValue().getInteger());

    }




}
