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
import com.inspiresoftware.lib.dto.geda.interceptor.GeDAInterceptor;
import com.inspiresoftware.lib.dto.geda.interceptor.impl.*;
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
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 4:12:57 PM
 */
public class AnnotationDrivenGeDABeanDefinitionParser implements BeanDefinitionParser {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationDrivenGeDABeanDefinitionParser.class);

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
    private static final String XSD_ATTR__USE_PREPROCESSOR = "use-bean-preprocessor";
    private static final String XSD_ATTR__POINTCUT_MATCH_REGEX = "pointcut-match-regex";
    private static final String XSD_ATTR__POINTCUT_NOMATCH_REGEX = "pointcut-nomatch-regex";

    public static final String ADVISOR_BEAN_NAME = AnnotationDrivenGeDABeanDefinitionParser.class.getPackage().getName() + ".internalGeDAAdvisor";


    public BeanDefinition parse(final Element element, final ParserContext parserContext) {

        AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
        if (!parserContext.getRegistry().containsBeanDefinition(ADVISOR_BEAN_NAME)) {

            final Boolean usePreprocessor = Boolean.valueOf(element.getAttribute(XSD_ATTR__USE_PREPROCESSOR));
            final String dtoSupportBeanName = element.getAttribute(XSD_ATTR__DTO_SUPPORT);

            final BeanDefinitionRegistry registry = parserContext.getRegistry();
            final Object elementSource = parserContext.extractSource(element);

            final RuntimeBeanReference dtoSupportDef;

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

                dtoSupportDef =
                        this.setupDtoSupport(element, dtoSupportBeanName, registry, elementSource,
                                dtoFactoryRef, dtoVcrRef, dtoDslRef);

            } else {

                dtoSupportDef = new RuntimeBeanReference(dtoSupportBeanName);

            }

            if (usePreprocessor) {
                // bean pre processor version of configuration
                this.setupBootstapBeanPostprocessor(element, parserContext, elementSource);

                final RuntimeBeanReference defaultCfgAdvice =
                        this.setupTransferableAdviceConfigResolver(
                                parserContext, elementSource, BootstrapAdviceConfigResolverImpl.class);

                final String[] matchRegEx = this.getPointcutRegex(element, XSD_ATTR__POINTCUT_MATCH_REGEX);
                final String[] noMatchRegEx = this.getPointcutRegex(element, XSD_ATTR__POINTCUT_NOMATCH_REGEX);

                final RuntimeBeanReference pointcut =
                        this.setupPointcut(parserContext, elementSource, defaultCfgAdvice, matchRegEx, noMatchRegEx);

                final RuntimeBeanReference defaultInterceptor =
                        this.setupGeDAInterceptor(parserContext, elementSource, dtoSupportDef, defaultCfgAdvice);

                this.setupPointcutAdvisor(element, parserContext, elementSource, pointcut, defaultInterceptor);

            } else {
                // runtime advice discovery
                final RuntimeBeanReference defaultCfgAdvice =
                        this.setupTransferableAdviceConfigResolver(
                                parserContext, elementSource, RuntimeAdviceConfigResolverImpl.class);

                final String[] matchRegEx = this.getPointcutRegex(element, XSD_ATTR__POINTCUT_MATCH_REGEX);
                final String[] noMatchRegEx = this.getPointcutRegex(element, XSD_ATTR__POINTCUT_NOMATCH_REGEX);

                final RuntimeBeanReference pointcut =
                        this.setupPointcut(parserContext, elementSource, defaultCfgAdvice, matchRegEx, noMatchRegEx);

                final RuntimeBeanReference defaultInterceptor =
                        this.setupGeDAInterceptor(parserContext, elementSource, dtoSupportDef, defaultCfgAdvice);

                this.setupPointcutAdvisor(element, parserContext, elementSource, pointcut, defaultInterceptor);
            }
        }
        return null;
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

    protected RuntimeBeanReference setupTransferableAdviceConfigResolver(final ParserContext parserContext,
                                                                         final Object elementSource,
                                                                         final Class impl) {

        final RootBeanDefinition defaultResolver = new RootBeanDefinition(impl);
        defaultResolver.setSource(elementSource);
        defaultResolver.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        final XmlReaderContext readerContext = parserContext.getReaderContext();

        final String beanName = readerContext.registerWithGeneratedName(defaultResolver);

        return new RuntimeBeanReference(beanName);    
    }

    protected String[] getPointcutRegex(final Element element, final String attrName) {
        final String matchRegexAttr = element.getAttribute(attrName);
        final String[] matchRegex;
        if (StringUtils.hasText(matchRegexAttr)) {
            final String[] regex = matchRegexAttr.split(",");
            matchRegex = new String[regex.length];
            for (int i = 0; i < regex.length; i++) {
                matchRegex[i] = regex[i].trim();
            }
            return matchRegex;
        }
        return new String[0];
    }

    protected RuntimeBeanReference setupPointcut(final ParserContext parserContext,
                                                 final Object elementSource,
                                                 final RuntimeBeanReference resolver,
                                                 final String[] pointcutMatchRegex,
                                                 final String[] pointcutNoMatchRegex) {

        final RootBeanDefinition pointcut;

        if (pointcutMatchRegex.length == 0 && pointcutNoMatchRegex.length == 0) {
            pointcut = new RootBeanDefinition(GeDAMethodMatcherPointcut.class);
            final ConstructorArgumentValues constructorArgs = pointcut.getConstructorArgumentValues();
            constructorArgs.addGenericArgumentValue(resolver);
        } else {
            pointcut = new RootBeanDefinition(GeDAMethodRegExMatcherPointcut.class);            
            final ConstructorArgumentValues constructorArgs = pointcut.getConstructorArgumentValues();
            constructorArgs.addGenericArgumentValue(resolver);
            final MutablePropertyValues propertyValues = pointcut.getPropertyValues();
            if (pointcutMatchRegex.length > 0) {
                propertyValues.addPropertyValue("patterns", pointcutMatchRegex);
            }
            if (pointcutNoMatchRegex.length > 0) {
                propertyValues.addPropertyValue("excludedPatterns", pointcutNoMatchRegex);                
            }
        }

        pointcut.setSource(elementSource);
        pointcut.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

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

        final MutablePropertyValues propertyValues = defaultInterceptor.getPropertyValues();
        propertyValues.addPropertyValue("support", defaultSupport);
        propertyValues.addPropertyValue("resolver", defaultResolver);

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

    protected RuntimeBeanReference setupBootstapBeanPostprocessor(final Element element,
                                                                  final ParserContext parserContext,
                                                                  final Object elementSource) {

        final RootBeanDefinition beanPostProcessor = new RootBeanDefinition(GeDABootstrapAdvicePostProcessor.class);
        beanPostProcessor.setSource(elementSource);
        beanPostProcessor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        final XmlReaderContext readerContext = parserContext.getReaderContext();

        final String beanName = readerContext.registerWithGeneratedName(beanPostProcessor);

        return new RuntimeBeanReference(beanName);
    }



}
