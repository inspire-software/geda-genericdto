/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.config;

import com.inspiresoftware.lib.dto.geda.impl.DTOSupportImpl;
import com.inspiresoftware.lib.dto.geda.interceptor.GeDAInterceptor;
import com.inspiresoftware.lib.dto.geda.interceptor.impl.AdviceConfigResolverImpl;
import com.inspiresoftware.lib.dto.geda.interceptor.impl.GeDAStaticMethodMatcherPointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
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

    public static final String ADVISOR_BEAN_NAME = AnnotationDrivenGeDABeanDefinitionParser.class.getPackage().getName() + ".internalGeDAAdvisor";


    public BeanDefinition parse(final Element element, final ParserContext parserContext) {

        AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
        if (!parserContext.getRegistry().containsBeanDefinition(ADVISOR_BEAN_NAME)) {

            final String dtoSupportBeanName = element.getAttribute(XSD_ATTR__DTO_SUPPORT);

            final BeanDefinitionRegistry registry = parserContext.getRegistry();
            final Object elementSource = parserContext.extractSource(element);

            final RuntimeBeanReference dtoSupportDef;

            if (!registry.containsBeanDefinition(dtoSupportBeanName)) {

                final String dtoFactoryBeanName = element.getAttribute(XSD_ATTR__DTO_FACTORY);

                final RuntimeBeanReference dtoFactoryRef = new RuntimeBeanReference(dtoFactoryBeanName);

                dtoSupportDef =
                        this.setupDtoSupport(element, dtoSupportBeanName, registry, elementSource, dtoFactoryRef);

            } else {

                dtoSupportDef = new RuntimeBeanReference(dtoSupportBeanName);

            }

            final RuntimeBeanReference defaultCfgAdvice =
                    this.setupTransferableAdviceConfigResolver(parserContext, elementSource);

            final RuntimeBeanReference pointcut =
                    this.setupPointcut(parserContext, elementSource, defaultCfgAdvice);

            final RuntimeBeanReference defaultInterceptor =
                    this.setupGeDAInterceptor(parserContext, elementSource, dtoSupportDef, defaultCfgAdvice);

            this.setupPointcutAdvisor(element, parserContext, elementSource, pointcut, defaultInterceptor);
        }
        return null;
    }

    protected RuntimeBeanReference setupDtoSupport(final Element element, final String dtoSupportBeanName, final BeanDefinitionRegistry registry, final Object elementSource, final RuntimeBeanReference dtoFactoryRef) {

        final RootBeanDefinition dtoSupportDef = new RootBeanDefinition(DTOSupportImpl.class);
        dtoSupportDef.setSource(elementSource);
        dtoSupportDef.setRole(BeanDefinition.ROLE_APPLICATION);

        final ConstructorArgumentValues constructorArgs = dtoSupportDef.getConstructorArgumentValues();
        constructorArgs.addGenericArgumentValue(dtoFactoryRef);

        final MutablePropertyValues valuesArgs = dtoSupportDef.getPropertyValues();
        setupListenerProperty(valuesArgs, "onDtoAssembled", element.getAttribute(XSD_ATTR__ON_DTO_ASSEMBLED));
        setupListenerProperty(valuesArgs, "onDtoFailed", element.getAttribute(XSD_ATTR__ON_DTO_FAILED));
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

    protected RuntimeBeanReference setupTransferableAdviceConfigResolver(final ParserContext parserContext,
                                                                         final Object elementSource) {

        final RootBeanDefinition defaultResolver = new RootBeanDefinition(AdviceConfigResolverImpl.class);
        defaultResolver.setSource(elementSource);
        defaultResolver.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        final XmlReaderContext readerContext = parserContext.getReaderContext();

        final String beanName = readerContext.registerWithGeneratedName(defaultResolver);

        return new RuntimeBeanReference(beanName);    
    }

    protected RuntimeBeanReference setupPointcut(final ParserContext parserContext,
                                                 final Object elementSource,
                                                 final RuntimeBeanReference resolver) {

        final RootBeanDefinition pointcut = new RootBeanDefinition(GeDAStaticMethodMatcherPointcut.class);
        pointcut.setSource(elementSource);
        pointcut.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        final ConstructorArgumentValues constructorArgs = pointcut.getConstructorArgumentValues();
        constructorArgs.addGenericArgumentValue(resolver);

        final XmlReaderContext readerContext = parserContext.getReaderContext();
        final String pointcutBeanName = readerContext.registerWithGeneratedName(pointcut);
        
        return new RuntimeBeanReference(pointcutBeanName);
    }

    protected RuntimeBeanReference setupGeDAInterceptor(final ParserContext parserContext,
                                                        final Object elementSource,
                                                        final RuntimeBeanReference defaultSupport,
                                                        final RuntimeBeanReference defaultResolver) {

        final RootBeanDefinition defaultInterceptor = new RootBeanDefinition(GeDAInterceptor.class);
        defaultInterceptor.setSource(elementSource);
        defaultInterceptor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        final ConstructorArgumentValues constructorArgs = defaultInterceptor.getConstructorArgumentValues();
        constructorArgs.addIndexedArgumentValue(0, defaultSupport);
        constructorArgs.addIndexedArgumentValue(1, defaultResolver);

        final XmlReaderContext readerContext = parserContext.getReaderContext();

        final String beanName = readerContext.registerWithGeneratedName(defaultInterceptor);

        return new RuntimeBeanReference(beanName);

    }

    protected RuntimeBeanReference setupPointcutAdvisor(final Element element,
                                                        final ParserContext parserContext,
                                                        final Object elementSource,
                                                        final RuntimeBeanReference pointcutBeanReference,
                                                        final RuntimeBeanReference interceptorBeanReference) {

        final RootBeanDefinition pointcutAdvisor = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
        pointcutAdvisor.setSource(elementSource);
        pointcutAdvisor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        final MutablePropertyValues propertyValues = pointcutAdvisor.getPropertyValues();
        propertyValues.addPropertyValue("adviceBeanName", interceptorBeanReference.getBeanName());
        propertyValues.addPropertyValue("pointcut", pointcutBeanReference);
        if (element.hasAttribute("order")) {
            propertyValues.addPropertyValue("order", element.getAttribute("order"));
        }

        final BeanDefinitionRegistry registry = parserContext.getRegistry();
        registry.registerBeanDefinition(ADVISOR_BEAN_NAME, pointcutAdvisor);
        return new RuntimeBeanReference(ADVISOR_BEAN_NAME);
    }


}
