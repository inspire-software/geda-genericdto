/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.config;

import com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl;
import com.inspiresoftware.lib.dto.geda.impl.DTOSupportImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 4:12:57 PM
 */
public class AnnotationDrivenGeDABeanDefinitionParser implements BeanDefinitionParser {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationDrivenGeDABeanDefinitionParser.class);

    private static final String XSD_ATTR__DTO_SUPPORT = "dto-support";
    private static final String XSD_ATTR__DTO_FACTORY = "dto-factory";
    private static final String XSD_ATTR__ON_DTO_ASSEMBLED = "on-dto-assembled";
    private static final String XSD_ATTR__ON_DTO_FAILED = "on-dto-failed";
    private static final String XSD_ATTR__ON_ENTITY_ASSEMBLED = "on-entity-assembled";
    private static final String XSD_ATTR__ON_ENTITY_FAILED = "on-entity-failed";

    public BeanDefinition parse(final Element element, final ParserContext parserContext) {

        final String dtoSupportBeanName = element.getAttribute(XSD_ATTR__DTO_SUPPORT);

        if (!parserContext.getRegistry().containsBeanDefinition(dtoSupportBeanName)) {

            final Object elementSource = parserContext.extractSource(element);

            final String dtoFactoryBeanName = element.getAttribute(XSD_ATTR__DTO_FACTORY);

            final RuntimeBeanReference dtoFactoryRef =
                    this.setupDtoFactory(parserContext, elementSource, dtoFactoryBeanName);


            final RootBeanDefinition dtoSupportDef = new RootBeanDefinition(DTOSupportImpl.class);
            dtoSupportDef.setSource(elementSource);
            dtoSupportDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

            final ConstructorArgumentValues constructorArgs = dtoSupportDef.getConstructorArgumentValues();
            constructorArgs.addGenericArgumentValue(dtoFactoryRef);

            final MutablePropertyValues valuesArgs = dtoSupportDef.getPropertyValues();
            setupListenerProperty(valuesArgs, "onDtoAssembled", element.getAttribute(XSD_ATTR__ON_DTO_ASSEMBLED));
            setupListenerProperty(valuesArgs, "onDtoFailed", element.getAttribute(XSD_ATTR__ON_DTO_FAILED));
            setupListenerProperty(valuesArgs, "onEntityAssembled", element.getAttribute(XSD_ATTR__ON_ENTITY_ASSEMBLED));
            setupListenerProperty(valuesArgs, "onEntityFailed", element.getAttribute(XSD_ATTR__ON_ENTITY_FAILED));

        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("DTOSupport bean has already been defined... skipping config");
            }
        }
        return null;
    }

    protected void setupListenerProperty(final MutablePropertyValues valuesArgs,
                                         final String property, final String listenerName) {
        if (StringUtils.hasLength(listenerName)) {
            valuesArgs.addPropertyValue(property, new RuntimeBeanReference(listenerName));
        }
    }

    protected RuntimeBeanReference setupDtoFactory(final ParserContext parserContext,
                                                   final Object elementSource,
                                                   final String beanName) {

        final BeanDefinitionRegistry registry = parserContext.getRegistry();

        if (!registry.containsBeanDefinition(beanName)) {
            final RootBeanDefinition defaultDtoFactory = new RootBeanDefinition(DTOFactoryImpl.class);
            defaultDtoFactory.setSource(elementSource);
            defaultDtoFactory.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(beanName, defaultDtoFactory);
        }

        return new RuntimeBeanReference(beanName);

    }

}
