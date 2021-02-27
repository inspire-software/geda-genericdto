# IoC with GeDA Spring module

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

> This page applied to GeDA **v.2.0.0+**, specifically module geda.spring-integration.

## Module overview

To make this absolutely clear - you __DO NOT__ have to use geda.spring-integration. All it is - is a best practice approach to spring beans setup for GeDA. You are welcome to use just geda.core if you find spring-integration not suitable for you.

So what does it actually do?

Spring integration module uses Spring in order to setup GeDA infrastructure and let you use GeDA assembler without having to work with them directly. 

Version 2.0.0 provided annotation driven approach through AOP with few enhancements in later versions.

Version 2.1.1 provided xml driven approach - a pure bean service IoC.

## Maven dependencies

To integrate GeDA with your Spring 3 project you will need the following two modules:

```xml
<!-- core module: use can you it on its own if you are not using Spring 3 -->
<dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.core</artifactId>
    <version>2.0.0</version>
</dependency>

<!-- spring integration: spring 3 AOP wrapper for core -->
<dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.spring-integration</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Whats in the box?

* DTOSupport - a service singleton what ports the access to GeDA core
* DTOFactory - a wrapper around BeanFactory from core
* DTOAdaptersRegistrar - a bean that allows you to configure your converters, retrievers and matchers
* DTODSLRegistrar - DSL support from core
* DTOEventListener - six events are currently supported (on-dto-assembly, on-dto-assembled, on-dto-failed, on-entity-assembly, on-entity-assembled and on-entity-failed) which are configured through GeDA spring tags
* Two schemas for annotation-driven and xml-driven approaches
* @Transferable - annotation for annotation-driven approach

### Events

* on-dto-assembly - invoked just before assembleDto call
* on-dto-assembled - invoked straight after assembleDto call
* on-dto-failed - invoked if there was an exception during assembleDto call
* on-entity-assembly - invoked just before assembleEntity call
* on-entity-assembled - invoked straight after assembleEntity call
* on-entity-failed- invoked if there was an exception during assembleEntity call

### annotation-driven VS xml-driven

Annotation driven is less intrusive on your code since it only requires you to use @Transferable annotation on your methods and most of the configuration and decisions are done by GeDA. However this comes at a price of use of AOP which may affect performance. Also there are some peculiarities of using "this" since Spring framework uses proxy objects for AOP, so you need to be careful.

XML driven require you to be in control and simply provides the infrastructure for DTOSupport bean. Where you use this bean is up to you through the <dtosupport> tag.

### Spring GeDA 1-2-3

In the end both of these methods are very similar is the way you use them:

1. pick correct schema
2. put <annotation-driven> or <xml-driven> and specify your configuration.
3. use @Transferable or <dtosupport> to specify which beans will use GeDA and how

# Annotation Driven

> This page applied to GeDA **v.2.0.0+**, specifically module geda.spring-integration.

This is a basic tutorial which goes thought the example given in the integration test that can be found in the geda.spring-integration module.

## Annotation driven overview

So what does it actually do?

There are two options supported: dynamic binding and BeanPostProcessor.

In my view BeanPostProcessor is better since it allows Spring to create 'Advice's at startup thus creating a full list of methods from the start. Dynamic binding evaluated applicable methods during execution, so all evaluated beans are scanned on every execution with blacklisting methods which are not applicable (this is more flexible but creates huge method lookup tables). But //BOTH// of these are //VALID// approaches.

The way beans' methods are identified by GeDA annotation-driven strategy is through the @Transferable annotation, placed on the __implementor__ of the bean.

The signature of the method should be supported by @Transferable annotation (see JavaDoc's for more details).

Once the methods are scanned by AOP module before and/or after advice is registered for applicable methods. So that either parameters or return values of the method get converted to/from DTO/Entity objects.

But it is always easier to see with an example...

## Maven dependencies

To integrate GeDA with your Spring 3 project you will need the following two modules:

```xml
<!-- core module: use can you it on its own if you are not using Spring 3 -->
<dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.core</artifactId>
    <version>2.0.0</version>
</dependency>

<!-- spring integration: spring 3 AOP wrapper for core -->
<dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.spring-integration</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Spring 3 beans setup

> There is a number of different schemas which add new features, we recommend to use the latest

I am going to describe the BeanPostProcessor approach in this tutorial, so:

```XML
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema/geda-spring-integration-1.0.xsd">


    <!-- 
        dto-factory must match the dtoFactory bean name. This is mandatory attribute.
        dto-adapters-registrar must also match bean name. This is optional attribute.
        All on-* are optional, and define even handlers to give you last minute tidy up hooks
            for processing your DTO's and Entities.
        use-bean-processor="true" defines the BeanPostProcessor method of Advice enrichment for GeDA
     -->
    <geda:annotation-driven dto-factory="dtoFactory"
                            dto-adapters-registrar="adapterRegistrar"
                            on-dto-assembly="onDto"
                            on-dto-assembled="onDtoSuccess"
                            on-dto-failed="onDtoFailure"
                            on-entity-assembly="onEntity"
                            on-entity-assembled="onEntitySuccess"
                            on-entity-failed="onEntityFailure"
                            use-bean-preprocessor="true"/>


    <!-- 
        Standard factory, which is used for ALL your DTO/Entity implementations.
        Judging from experience - you should not put entities in here, these should only be
                                  created manually, or through converters invoking special methods
                                  on your Entity. E.g. Order object might have Order.createOrderLine()
                                  method. And you can use on-entity-assembly event handler to invoke
                                  that. 
    -->
    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="dtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.ExtendedDataTransferObjectImpl"/>
                <entry key="filterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>



    <!--
 Event handler. You are only allowed to specify one event handler per event. However, each have
        "context" parameter, which you can pass through using @Transferable. Therefore it is possible
        to create composite event handlers that use sub-handler depending on the event context.
     -->


    <bean id="onDto" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onDtoSuccess" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onDtoFailure" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onEntity" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onEntitySuccess" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onEntityFailure" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>

    <bean id="simpleTransferableService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestServiceImpl"/>


    <!--
        Registrar allows to register your converters as a map of spring beans.
        If Converter/Retriever/Matcher bean implement DTOSupportAwareAdapter interface then
        they will be injected with DTOSupport object, which is the central hub service for
        the GeDA conversions. So you can bootstrap any chain of manual conversions
        through your adapters.
     -->
    <bean id="adapterRegistrar" class="com.inspiresoftware.lib.dto.geda.test.impl.ExposedDTOAdaptersRegistrarImpl">
        <constructor-arg>
            <map>
                <entry key="vc" value-ref="adapterValueConverter"/>
                <entry key="er" value-ref="adapterEntityRetriever"/>
                <entry key="ma" value-ref="adapterMatcher"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="adapterValueConverter" class="com.inspiresoftware.lib.dto.geda.test.impl.ValueConverterImpl"/>
    <bean id="adapterEntityRetriever" class="com.inspiresoftware.lib.dto.geda.test.impl.EntityRetrieverImpl"/>
    <bean id="adapterMatcher" class="com.inspiresoftware.lib.dto.geda.test.impl.DtoToEntityMatcherImpl"/>

</beans>
```

### Spring bean service and @Transferable

So, now how do you define transferable methods. Interested reader is highly advised to look at the source code of module test classes.

Here I will demonstrate a couple of examples:

```java
package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Transferable;
import com.inspiresoftware.lib.dto.geda.test.DomainObject;
import com.inspiresoftware.lib.dto.geda.test.ExtendedDataTransferObject;
import com.inspiresoftware.lib.dto.geda.test.TestService;

import java.util.Collection;
import java.util.Date;

public class TestServiceImpl implements TestService {

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityBeforeExact")
    public void dtoToEntityBeforeExact(Object dto, Object entity) {
        // at this point entity will contain all values transferable from dto
    }


    @Transferable(after = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "dtoKey", context = "dtoToEntityKeyAfter")
    public Object dtoToEntityKeyAfter(Object dto, Object extra) {
        // notice the return null. This is deliberate to clear confusion
        // With "after" mode the return value of method is never returned
        // A new instance of Entity is constructed (using bean factory), then 
        // populated from dto and returned.
        return null;
    }

    @Transferable(before = Direction.DTO_TO_ENTITY, after = Direction.ENTITY_TO_DTO,
            entityKey = "entityKey", dtoKey = "dtoKey", dtoFilterKey = "filterKey", context = "dtoToEntityAndBackToDtoByFilter")
    public Object dtoToEntityAndBackToDtoByFilter(Object sourceD, Object targetE, Object extra) {
        // This example does both ways conversion with a new instance of dto object returned
        // from this method
        swapEntityValues(targetE);
        return null;
    }

}
```

> This example assumes that objects passed as dto's are valid instances of @Dto annotated or DSL classes.

### Event listeners

So for each of the above examples (given the above spring config) event listeners will fire before and after __each transfer from/to dto object and entity object__.

```java
   public class CountingEventListener implements DTOCountingEventListener, BeanNameAware {

    private static final Logger LOG = LoggerFactory.getLogger(CountingEventListener.class);

    private int count = 0;
    private String name;

    public void onEvent(final Object... context) {
        // Current implementation of DTOSupport provides the following context array:
        // context[0] = context string identifier (e.g. for dtoToEntityBeforeExact method above it will be "dtoToEntityBeforeExact")
        // context[1] = DTO object (e.g. for dtoToEntityBeforeExact method above it will be first argument)
        // context[2] = Entity object (e.g. for dtoToEntityBeforeExact method above it will be second argument)
        // context[3] = Exception value if an exception occurs during transfer (it is up to the event handler to re-throw it!!!)
        count++;
        LOG.debug(name + " is called " + count + " times");
    }

    public int getCount() {
        return count;
    }

    public void setBeanName(final String name) {
        this.name = name;
    }
}
```

### Note on spring AOP

It is very important to understand how spring AOP works and that you **cannot call this.dtoToEntityBeforeExact from within TestServiceImpl.**

__This is not a bug of GeDA, this is how spring AOP works__.

One trick you can do - is to use spring lookup methods to invoke self from the spring container, which would bring the AOP proxy of the bean like so:

```java
   public class TestServiceImpl ... {

     ...

     public void doIt(...) {
         ...
         getSelf().dtoToEntityBeforeExact()
         ...
     }

     public TestService getSelf() {
         return null;
     }

}
```

```xml
        <bean id="simpleTransferableService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestServiceImpl">
               <lookup-method name="getSelf" bean="simpleTransferableService"/>
        </bean>
```

but make sure your beans are stateless (not that I would ever think that you would do such a thing with a service bean.

# XML config Driven

> This page applied to GeDA **v.2.1.1+**, specifically module geda.spring-integration.

This is a basic tutorial which goes thought the example given in the integration test that can be found in the geda.spring-integration module.

## Module overview

So what does it actually do?

Spring integration module uses Spring IoC in order to enrich the functionality of your services. 

The way beans' methods are enriched by GeDA is through the <dtosupport> tag, placed on the //__xml declaration__// of the bean.

This injects DTOSupport instance singleton that will have more than enough to support your needs (see JavaDoc's for more details).

But it is always easier to see with an example...

## Maven dependencies

To integrate GeDA with your Spring 3 project you will need the following two modules:

```xml
<!-- core module: use can you it on its own if you are not using Spring 3 -->
<dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.core</artifactId>
    <version>2.1.1</version>
</dependency>

<!-- spring integration: spring 3 AOP wrapper for core -->
<dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.spring-integration</artifactId>
    <version>2.1.1</version>
</dependency>
```

## Spring 3 beans setup

> You will need to use schema.

I am going to describe the basic approach in this tutorial, so:

```XML
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema/geda-spring-integration-xml-2.1.1.xsd">

    <geda:xml-driven dto-factory="dtoFactory"
                     dto-adapters-registrar="adapterRegistrar"
                     on-dto-assembly="onDto"
                     on-dto-assembled="onDtoSuccess"
                     on-dto-failed="onDtoFailure"
                     on-entity-assembly="onEntity"
                     on-entity-assembled="onEntitySuccess"
                     on-entity-failed="onEntityFailure"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="annDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedExtendedDataTransferObjectImpl"/>
                <entry key="annFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedDataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="onDto" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onDtoSuccess" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onDtoFailure" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onEntity" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onEntitySuccess" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>
    <bean id="onEntityFailure" class="com.inspiresoftware.lib.dto.geda.test.impl.CountingEventListener"/>

    <!-- Test custom property -->
    <bean id="xmlTestServiceCustomProperty"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestXmlServiceImpl">
        <geda:dtosupport property="dtoSupportBean"/>
    </bean>

    <!-- Test default property -->
    <bean id="xmlTestServiceDefaultProperty"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestXmlServiceImpl">
        <geda:dtosupport/>
    </bean>

    <bean id="adapterRegistrar" class="com.inspiresoftware.lib.dto.geda.test.impl.ExposedDTOAdaptersRegistrarImpl">
        <constructor-arg>
            <map>
                <entry key="vc" value-ref="adapterValueConverter"/>
                <entry key="er" value-ref="adapterEntityRetriever"/>
                <entry key="ma" value-ref="adapterMatcher"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="adapterValueConverter" class="com.inspiresoftware.lib.dto.geda.test.impl.ValueConverterImpl"/>
    <bean id="adapterEntityRetriever" class="com.inspiresoftware.lib.dto.geda.test.impl.EntityRetrieverImpl"/>
    <bean id="adapterMatcher" class="com.inspiresoftware.lib.dto.geda.test.impl.DtoToEntityMatcherImpl"/>

</beans>
```

## Spring bean service and DTOSupport

> Interested reader is highly advised to look at the source code of module test classes.

So, now how do you define and implement the service?

Start with interface:

```java
package com.inspiresoftware.lib.dto.geda.test;

public interface TestXmlService {

    <T> T transfer(Object from);

}
```

Well, that was easy, now the implementation:

```java
package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.test.TestXmlService;

public class TestXmlServiceImpl implements TestXmlService {

    private DTOSupport dtoSupport;

    public <T> T transfer(final Object from) {
        return dtoSupport.assembleDtoByKey("annDtoKey", from, "manual");
    }

    public void setDtoSupportBean(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }

    public void setDtoSupport(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }

}
```

> Please do not be confused with two IoC methods: setDtoSupportBean, setDtoSupport. This is just for demonstration purposes that the name of the method can be different. You will only need one such method. setDtoSupport is the default if you do not specify any "property" on the <dtosupport> tag.

> This example assumes that objects passed as dto's are valid instances of @Dto annotated or DSL classes.

## Event listeners

So for each of the above examples (given the above spring config) event listeners will fire before and after //__each transfer from/to dto object and entity object__//.

```java
public class CountingEventListener implements DTOCountingEventListener, BeanNameAware {

    private static final Logger LOG = LoggerFactory.getLogger(CountingEventListener.class);

    private int count = 0;
    private String name;

    public void onEvent(final Object... context) {
        // Current implementation of DTOSupport provides the following context array:
        // context[0] = context string identifier (i.e. in this example "manual")
        // context[1] = DTO object (i.e. instance of DTO named by "annDtoKey" in DTOFactory)
        // context[2] = Entity object (i.e. from object)
        // context[3] = Exception value if an exception occurs during transfer (it is up to the event handler to re-throw it!!!)
        count++;
        LOG.debug(name + " is called " + count + " times");
    }

    public int getCount() {
        return count;
    }

    public void setBeanName(final String name) {
        this.name = name;
    }
}
```

So this pretty much sums it up. And as mentioned above this does couple your code a bit more with GeDA - however you do get the benefit of by passing AOP (hence better performance)

## Proxy template

> Be careful when putting this tag in a bean with proxy template dtosupport tag should be placed inside the actual bean - NOT bean proxy template.

Here is an example of correct tag placement (inside the inner bean):

**txProxyTemplate**

```xml
       <bean id="authUserRepository"  parent="txProxyTemplate">
        <property name="target">
            <bean class="com.inspiresoftware.erp.security.locator.impl.LoggedInUserRepositoryImpl">
                <constructor-arg index="0" ref="userDAO"/>
                <geda:dtosupport/>
            </bean>
        </property>
        <property name="transactionAttributes">
            <props>
                <prop key="login">PROPAGATION_REQUIRED,readOnly,-Throwable</prop>
                <prop key="logout">PROPAGATION_REQUIRED,readOnly,-Throwable</prop>
                <prop key="transfer*">PROPAGATION_REQUIRED,readOnly,-Throwable</prop>
                <prop key="*">PROPAGATION_NOT_SUPPORTED</prop>
            </props>
        </property>
    </bean>
```

# Spring 3 AOP finetuning

> This page applied to GeDA **v.2.0.1+**, specifically module geda.spring-integration.

This short article briefly explains use of regex match and nomatch attribute of annotation driven tag.

## Overview

GeDA 2.0.1 offers a way to fine tune the advices method matching by providing two new attributes. A sample configuration may look something like this:

```XML
    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema/geda-spring-integration-1.1.xsd">

    <geda:annotation-driven dto-factory="dtoFactory"
                            dto-adapters-registrar="adapterRegistrar"
                            use-bean-preprocessor="true"
                            pointcut-match-regex="com.inspiresoftware.lib.dto.geda.test.impl.TestService.*"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="dtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.ExtendedDataTransferObjectImpl"/>
                <entry key="filterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="simpleTransferableService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestServiceImpl"/>
    <bean id="simpleTransferableNoMatchService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestNoMatchServiceImpl"/>

    <bean id="adapterRegistrar" class="com.inspiresoftware.lib.dto.geda.test.impl.ExposedDTOAdaptersRegistrarImpl">
        <constructor-arg>
            <map>
                <entry key="vc" value-ref="adapterValueConverter"/>
                <entry key="er" value-ref="adapterEntityRetriever"/>
                <entry key="ma" value-ref="adapterMatcher"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="adapterValueConverter" class="com.inspiresoftware.lib.dto.geda.test.impl.ValueConverterImpl"/>
    <bean id="adapterEntityRetriever" class="com.inspiresoftware.lib.dto.geda.test.impl.EntityRetrieverImpl"/>
    <bean id="adapterMatcher" class="com.inspiresoftware.lib.dto.geda.test.impl.DtoToEntityMatcherImpl"/>

</beans>
```

In the above example the match regex is set to:pointcut-match-regex="com.inspiresoftware.lib.dto.geda.test.impl.TestService.*"

Which means that in this particular case on service bean with id "simpleTransferableService" will be eligible to be enriched with AOP advice. The service bean with id "simpleTransferableNoMatchService" will not however even though the underlaying implementation has the same annotations that the simpleTransferableService does (see junit of the module).

> Therefore we are able to fine tune which bean we need to support DTO transfer and which ones should not.

**This is especially useful in complex Spring applications when different AOP advices may be in conflict or where GeDA advice will produce adverse effect of starting transactions, say with hibernate lazy objects**

Similarly we can use the "pointcut-nomatch-regex" property to **exclude** whatever get captures by the "pointcut-match-regex".

> "pointcut-nomatch-regex" only has effect when "pointcut-match-regex" is specified. This is by design and is a limitation imposed by the Spring AOP framework.

Here is an example of nomatch configuration:

```XML
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema
                           http://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto/schema/geda-spring-integration-1.1.xsd">

    <geda:annotation-driven dto-factory="dtoFactory"
                            dto-adapters-registrar="adapterRegistrar"
                            use-bean-preprocessor="true"
                            pointcut-match-regex="com.inspiresoftware.lib.dto.geda.test.*"
                            pointcut-nomatch-regex="com.inspiresoftware.lib.dto.geda.test.impl.TestNoMatch.*,com.inspiresoftware.lib.dto.geda.test.TestNoMatch.*"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="dtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.ExtendedDataTransferObjectImpl"/>
                <entry key="filterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="simpleTransferableService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestServiceImpl"/>
    <bean id="simpleTransferableNoMatchService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestNoMatchServiceImpl"/>

    <bean id="adapterRegistrar" class="com.inspiresoftware.lib.dto.geda.test.impl.ExposedDTOAdaptersRegistrarImpl">
        <constructor-arg>
            <map>
                <entry key="vc" value-ref="adapterValueConverter"/>
                <entry key="er" value-ref="adapterEntityRetriever"/>
                <entry key="ma" value-ref="adapterMatcher"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="adapterValueConverter" class="com.inspiresoftware.lib.dto.geda.test.impl.ValueConverterImpl"/>
    <bean id="adapterEntityRetriever" class="com.inspiresoftware.lib.dto.geda.test.impl.EntityRetrieverImpl"/>
    <bean id="adapterMatcher" class="com.inspiresoftware.lib.dto.geda.test.impl.DtoToEntityMatcherImpl"/>

</beans>
```

> Note from the example that it is possible to create a list of regex expressions by separating then with a comma.



[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
