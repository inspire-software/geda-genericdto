/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.config;

import com.inspiresoftware.lib.dto.geda.impl.DTOSupportImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * .
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 4:12:57 PM
 */
public class XmlDrivenGeDABeanDefinitionParser implements BeanDefinitionParser, BeanDefinitionDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(XmlDrivenGeDABeanDefinitionParser.class);

    private static final String XSD_ATTR__DTO_SUPPORT = "dto-support";
    private static final String XSD_ATTR__DTO_FACTORY = "dto-factory";
    private static final String XSD_ATTR__DTO_ADAPTERS_REGISTRAR = "dto-adapters-registrar";
    private static final String XSD_ATTR__DTO_DSL_REGISTRAR = "dto-dsl-registrar";
    private static final String XSD_ATTR__ON_DTO_ASSEMBLY = "on-dto-assembly";
    private static final String XSD_ATTR__ON_DTO_ASSEMBLED = "on-dto-assembled";
    private static final String XSD_ATTR__ON_DTO_FAILED = "on-dto-failed";
    private static final String XSD_ATTR__ON_ENTITY_ASSEMBLY = "on-entity-assembly";
    private static final String XSD_ATTR__ON_ENTITY_ASSEMBLED = "on-entity-assembled";
    private static final String XSD_ATTR__ON_ENTITY_FAILED = "on-entity-failed";

    private static final String XSD_ATTR__PROPERTY = "property";

    private String dtoSupportBeanName = null;

    public BeanDefinition parse(final Element element, final ParserContext parserContext) {


        dtoSupportBeanName = element.getAttribute(XSD_ATTR__DTO_SUPPORT);

        final BeanDefinitionRegistry registry = parserContext.getRegistry();
        final Object elementSource = parserContext.extractSource(element);

        if (!registry.containsBeanDefinition(dtoSupportBeanName)) {

            final String dtoFactoryBeanName = element.getAttribute(XSD_ATTR__DTO_FACTORY);
            final RuntimeBeanReference dtoFactoryRef = new RuntimeBeanReference(dtoFactoryBeanName);

            final String dtoVcrBeanName = element.getAttribute(XSD_ATTR__DTO_ADAPTERS_REGISTRAR);
            final RuntimeBeanReference dtoVcrRef;
            if (StringUtils.hasLength(dtoVcrBeanName)) {
                dtoVcrRef = new RuntimeBeanReference(dtoVcrBeanName);
            } else {
                dtoVcrRef = null;
            }

            final String dtoDslBeanName = element.getAttribute(XSD_ATTR__DTO_DSL_REGISTRAR);
            final RuntimeBeanReference dtoDslRef;
            if (StringUtils.hasLength(dtoDslBeanName)) {
                dtoDslRef = new RuntimeBeanReference(dtoDslBeanName);
            } else {
                dtoDslRef = null;
            }

            this.setupDtoSupport(element, dtoSupportBeanName, registry, elementSource,
                            dtoFactoryRef, dtoVcrRef, dtoDslRef);

        }

        return null;
    }

    public BeanDefinitionHolder decorate(final Node node, final BeanDefinitionHolder definition, final ParserContext parserContext) {

        final BeanDefinition beanDefinition = definition.getBeanDefinition();

        final String dtoSupportProperty = node.getAttributes().getNamedItem(XSD_ATTR__PROPERTY).getNodeValue();

        final MutablePropertyValues properties = beanDefinition.getPropertyValues();
        properties.add(dtoSupportProperty, new RuntimeBeanReference(dtoSupportBeanName));

        return definition;
    }

    protected RuntimeBeanReference setupDtoSupport(final Element element,
                                                   final String dtoSupportBeanName,
                                                   final BeanDefinitionRegistry registry,
                                                   final Object elementSource,
                                                   final RuntimeBeanReference dtoFactoryRef,
                                                   final RuntimeBeanReference dtoVcrRef,
                                                   final RuntimeBeanReference dtoDslRef) {

        final RootBeanDefinition dtoSupportDef = new RootBeanDefinition(DTOSupportImpl.class);
        dtoSupportDef.setSource(elementSource);
        dtoSupportDef.setRole(BeanDefinition.ROLE_APPLICATION);

        final MutablePropertyValues valuesArgs = dtoSupportDef.getPropertyValues();
        valuesArgs.addPropertyValue("beanFactory", dtoFactoryRef);
        if (dtoVcrRef != null) {
            valuesArgs.addPropertyValue("adaptersRegistrar", dtoVcrRef);
        }
        if (dtoDslRef != null) {
            valuesArgs.addPropertyValue("dslRegistrar", dtoDslRef);
        }
        setupListenerProperty(valuesArgs, "onDtoAssembly", element.getAttribute(XSD_ATTR__ON_DTO_ASSEMBLY));
        setupListenerProperty(valuesArgs, "onDtoAssembled", element.getAttribute(XSD_ATTR__ON_DTO_ASSEMBLED));
        setupListenerProperty(valuesArgs, "onDtoFailed", element.getAttribute(XSD_ATTR__ON_DTO_FAILED));
        setupListenerProperty(valuesArgs, "onEntityAssembly", element.getAttribute(XSD_ATTR__ON_ENTITY_ASSEMBLY));
        setupListenerProperty(valuesArgs, "onEntityAssembled", element.getAttribute(XSD_ATTR__ON_ENTITY_ASSEMBLED));
        setupListenerProperty(valuesArgs, "onEntityFailed", element.getAttribute(XSD_ATTR__ON_ENTITY_FAILED));

        registry.registerBeanDefinition(dtoSupportBeanName, dtoSupportDef);

        return new RuntimeBeanReference(dtoSupportBeanName);
    }

    protected void setupListenerProperty(final MutablePropertyValues valuesArgs,
                                         final String property, final String listenerName) {
        if (StringUtils.hasLength(listenerName)) {
            valuesArgs.addPropertyValue(property, new RuntimeBeanReference(listenerName));
        }
        
    }

}
