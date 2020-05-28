# Spring API

## Basics

> This is an advanced topic and requires full understanding of the core API.

Spring is an excellent foundation for a java application and we thought that GeDA should have a natural way of integrating with Spring. 

GeDA Spring module uses either AOP or spring instrumentation to integrate with spring 3.x.x (the dependency is to 3.0.0 but it is working with any 3.x.x version).

In essence integration module provides a spring bean DTOSupport and a number adapters to wrap the complexity of integration of the GeDA core API and leave user with a simple interface for working with DTO.

Integration offers two approaches:

* @Transferable annotation placed on your service beans methods whih are resolved using AOP.
* XML configuration by using <dtosupport/> inside <bean/> declaration which injects DTOSupport service to your beans though spring instrumentation.

API is represented by:

**Schemas**

* geda-spring-integration-annotation-3.0.0.xsd - that defines annotation driven approach
* geda-spring-integration-xml-3.0.0.xsd - that defines xml configuration driven approach


**Interfaces**

Interface | Since | Purpose 
--- | --- | ---
DTOSupport|2.0.0 |Facade for accessing assembleDto and assebleEntity methods. Wraps complexity of code API. 
DTOFactory|2.0.0 |Spring interface for ExtensibleBeanFactory from [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API 
DTOAdaptersRegistrar|2.0.0 |Spring service that allows to adapters defined as spring beans 
DTODSLRegistrar|2.0.0 |Spring service that enabled access to DSL registry in order to configure [[DSL>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.DSL API.WebHome]] mappings 
DTOEventListener|2.0.0 |Event listener to make the assembly process a bit more flexible. 

In general you should not worry about any infrastructure apart from:

* Defining your bean factory as only you know what DTO and Entity mappings you need
* Defining adapters through adapters registrar if you use converter, matcher or retriever
* Defining DSL contexts using DSL registrar if you use DSL approach

### Basic annotation driven example

**Basic annotation driven xml configuration**


```xml
    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://www.genericdtoassembler.org/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.genericdtoassembler.org/schema
                           http://www.genericdtoassembler.org/schema/geda-spring-integration-annotation-3.0.0.xsd">

    <geda:annotation-driven dto-factory="dtoFactory" use-bean-preprocessor="true"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="annDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedExtendedDataTransferObjectImpl"/>
                <entry key="annFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedDataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="annSimpleTransferableService"
          class="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedTestServiceImpl"/>

</beans>
```


**Basic annotation driven spring bean and its usage**


```java
    public class AnnotatedTestServiceImpl implements TestService {

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "annDtoKey", context ## "dtoToEntityBeforeExact")
    public void dtoToEntityBeforeExact(Object dto, Object entity) {

    }
}
...
final TestService service        this.applicationContext.getBean("annSimpleTransferableService", TestService.class);

final Date time = new Date();
final ExtendedDataTransferObject dto = new AnnotatedExtendedDataTransferObjectImpl();
dto.setValue("dtoVal1");
dto.setValue2("dtoVal2");
dto.setTimestamp(time);

final DomainObject entity = new DomainObjectImpl();

service.dtoToEntityBeforeExact(dto, entity);
```


> Note how we use **service.dtoToEntityBeforeExact(dto, entity);**. It is pulled from application context which means that service is not AnnotatedTestServiceImpl but a proxy wrapping this class so that AOP can be executed. This is very important to understand since the following code will not work!


```java
 public class AnnotatedTestServiceImpl implements TestService {
 
 public void directCallWillNotWork() {
  final Date time = new Date();
  final ExtendedDataTransferObject dto = new AnnotatedExtendedDataTransferObjectImpl();
  dto.setValue("dtoVal1");
  dto.setValue2("dtoVal2");
  dto.setTimestamp(time);

  final DomainObject entity = new DomainObjectImpl();
  // Using this bypasses Spring AOP, so no DTOSupport here!
  this.dtoToEntityBeforeExact(dto, entity);
 }

    @Transferable(before = Direction.DTO_TO_ENTITY,
            entityKey = "entityKey", dtoKey = "annDtoKey", context = "dtoToEntityBeforeExact")
    public void dtoToEntityBeforeExact(Object dto, Object entity) {

    }
}
```


> Look at this Topics IoC with GeDA Spring 3 module to see how to overcome this via self proxy.


> Please not that enabling Spring support enables Spring auto-proxy mechanism, which means that all beans become proxies which may have side effects. E.g. if you are using plain classes as beans (that do not have interfaces) the context will crash - you need interfaces for Spring proxy mechanism to work

### Basic XML driven example

**Basic annotation driven xml configuration**


```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://www.genericdtoassembler.org/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.genericdtoassembler.org/schema
                           http://www.genericdtoassembler.org/schema/geda-spring-integration-xml-3.0.0.xsd">

    <geda:xml-driven dto-factory="dtoFactory"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="annDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedExtendedDataTransferObjectImpl"/>
                <entry key="annFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedDataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

    <!-- Test default property -->
    <bean id="xmlTestServiceDefaultProperty"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestXmlServiceImpl">
        <!--
            Be careful when putting this tag in a bean with proxy template
            This tag should be placed inside the actual bean - NOT bean proxy template.
        -->
        <geda:dtosupport/>
    </bean>

</beans>
```


**Basic XML driven spring bean and its usage**


```java
    public class TestXmlServiceImpl implements TestXmlService {

    private DTOSupport dtoSupport;

    public <T> T transfer(final Object from) {
        return dtoSupport.assembleDtoByKey("annDtoKey", from, "manual");
    }
 // method by convention to set dto support to
    public void setDtoSupport(final DTOSupport dtoSupport) {
        this.dtoSupport ## dtoSupport;
    }

}
...
final TestXmlService service   =     this.applicationContext.getBean("xmlTestServiceCustomProperty", TestXmlService.class);

final DomainObject entity = new DomainObjectImpl();
entity.setValue("entityVal1");
entity.setValue2("entityVal2");

final ExtendedDataTransferObject dto = service.transfer(entity);

```



## DTOSupport

DTOSupport exposes comprehensive list of assembleDto and assembleEntity signatures. Each method effectively wrapps DTOAssembler API methods and simplifies their use by injecting some of the parameters from the spring context.

If you follow XML driven approach you have direct access to the DTOSupport spring bean as it is injected into your configured spring beans.

If you follow the annotation driven approach then DTOSupport will be accessed by spring via AOP by deducing the most applicable method signature to the configuration you provided in @Transferable 

### Annotation VS XML

Annotation | XML
--- | ---
Indirectly uses the API. The coupling to GeDA is only through annotation | DTOSupport is directly used in spring bean
AOP may infer performance hit, need careful configuration |No performance hit as AOP is not used all is configured at bean configuration time
All configuration is within spring bean implementation |The configuration is split between bean configuration in XML and usage of API in class implementation
Constraint by the guessing mechanism of AOP with respect to @Transferable |Full direct control over how DTOSupport is used.
Need to be careful with AOP proxies as direct usage this.doIt() may bypass proxy | Direct usage of API

Do you want my opinion? I would choose XML as it is better performance and more control!

### DTOSupport methods

#### Utility methods:

Signature | Since | Purpose
--- | --- | ---
void registerAdapter(final String key, final Object adapter) |2.0.0 |Allows to register an adapter such as value converter, entity matcher or entity retriever. It is recommended that you use DTOAdaptersRegistrar rather than invoking this method directly. 

#### Transfer methods:
Every transfer method has context parameter - this is simply a marker that is passed to listeners that you can use to fine tune your logic. 

Signature | Since | Purpose
--- | --- | ---
<T> T assembleDto(T dto, Object entity, String context) |2.0.0 |Pure dto assembly from an entity 
<T> T assembleDtoByKey(String dtoKey, Object entity, String context) |2.0.0 |IoC version of dto assembly that enabled you to indirectly reference dto object. Bean factory is used to create new instance of dto object. 
<T> T assembleDto(String dtoFilter, T dto, Object entity, String context) |2.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance. 
<T> T assembleDtoByKey(String dtoFilter, String dtoKey, Object entity, String context) |2.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter and dto is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance. 
<T> void assembleDtos(String dtoKey, Collection<T> dtos, Collection entities, String context) |2.0.0 |processing bulk transfers - effectively a loop on top of assembleDto. dtos collection must be non null empty collection. 
<T> void assembleDtos(String dtoFilter, String dtoKey, Collection<T> dtos, Collection entities, String context) |2.0.0 |processing bulk transfers and creating new dto elements using bean factory by key - effectively a loop on top of assembleDto. dtos collection must be non null empty collection. 
<T> T assembleEntity(Object dto, T entity, String context) |2.0.0 |Pure entity assembly from a DTO 
<T> T assembleEntityByKey(Object dto, String entityKey, String context) |2.0.0 |IoC version of entity assembly that enabled you to indirectly reference entity object. Bean factory is used to create new instance of entity object. 
<T> T assembleEntity(String dtoFilter, Object dto, T entity, String context) |2.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter is looked up through bean factory. The purpose is facilitation of partial update of data. E.g. when you just wish to update only basic data but use the full class dto instance. 
<T> T assembleEntityByKey(String dtoFilter, Object dto, String entityKey, String context) |2.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter and entity is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance. 
<T> void assembleEntities(String entityKey, Collection dtos, Collection<T> entities, String context) |2.0.0 |processing bulk transfers - effectively a loop on top of assembleEntity. entity collection must be non null empty collection. 
<T> void assembleEntities(String dtoFilter, String entityKey, Collection dtos, Collection<T> entities, String context) |2.0.0 |processing bulk transfers and creating new entity elements using bean factory by key - effectively a loop on top of assembleEntity. entities collection must be non null empty collection. 

### DTOEventListener

Event listener are tracking the following events:

* onDtoAssembly - prior assembler attempts to transfer data for a single dto
* onEntityAssembly - prior assembler attempts to transfer data for a single entity
* onDtoAssembled - after assembler successfully transferred data to a single dto
* onDtoFailed - right at the first occurence of an exception during transfer to dto
* onEntityAssembled - after assembler successfully transferred data to a single entity
* onEntityFailed - right at the first occurence of an exception during transfer to entity

> Currently only one listener is permitted per event. Setting another listener to same event will overwrite it.

**Basic listener configuration**


```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://www.genericdtoassembler.org/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.genericdtoassembler.org/schema
                           http://www.genericdtoassembler.org/schema/geda-spring-integration-xml-3.0.0.xsd">

    <geda:xml-driven dto-factory="dtoFactory"
                     on-dto-assembly="onDto"
                     on-dto-assembled="onDtoSuccess"
                     on-dto-failed="onDtoFailure"
                     />

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

    <!-- Test default property -->
    <bean id="xmlTestServiceDefaultProperty"
          class="com.inspiresoftware.lib.dto.geda.test.impl.TestXmlServiceImpl">
        <!--
            Be careful when putting this tag in a bean with proxy template
            This tag should be placed inside the actual bean - NOT bean proxy template.
        -->
        <geda:dtosupport/>
    </bean>

</beans>
```


**Basic listener example**


```java
    public class CountingEventListener implements DTOCountingEventListener, BeanNameAware {

    private static final Logger LOG = LoggerFactory.getLogger(CountingEventListener.class);

    private int count = 0;
    private String name;

    // context[0] is the context marker, context[1] - dto, context[2] - entity, context[3] - exception
    public void onEvent(final Object... context) {
        count++;
        LOG.debug("{} is called {} times", name, count);
    }

    public int getCount() {
        return count;
    }

    public void setBeanName(final String name) {
        this.name = name;
    }
}

...
final TestXmlService service    =    this.applicationContext.getBean("xmlTestServiceDefaultProperty", TestXmlService.class);

final DomainObject entity = new DomainObjectImpl();
entity.setValue("entityVal1");
entity.setValue2("entityVal2");

final ExtendedDataTransferObject dto = service.transfer(entity);

final DTOCountingEventListener attempted    =    this.applicationContext.getBean("onDto", DTOCountingEventListener.class);
final DTOCountingEventListener success =
        this.applicationContext.getBean("onDtoSuccess", DTOCountingEventListener.class);
final DTOCountingEventListener failed =
        this.applicationContext.getBean("onDtoFailure", DTOCountingEventListener.class);

Log.info("attempted  " + attempted.getCount() + " dto transfers with " + failed.getCount() + " failed");

```


## Annotation driven

### XML configuration

Annotation driven configuration is facilitated using one tag: <geda:annotation-driven/>, which offers the following configuration attributes:

Property | Default | Since | Purpose
--- | --- | --- | --- 
dto-factory |dtoFactory |2.0.0 |Name of the bean that implements DTOFactory 
dto-adapters-registrar | |2.0.0 |Name of the bean that implements DTOAdaptersRegistrar 
on-dto-assembly | |2.0.0 |Name of the bean that implements DTOEventListener that will receive notifications before a DTO is assembled 
on-dto-assembled | |2.0.0 |Name of the bean that implements DTOEventListener that will receive notifications after successful DTO assembly 
on-dto-failed | |2.0.0 |Name of the bean that implements DTOEventListener that will receive notifications if an exception occurs during DTO assembly 
on-entity-assembly | |2.0.0 |Name of the bean that implements DTOEventListener that will receive notifications before a entity is assembled 
on-entity-assembled | |2.0.0 |Name of the bean that implements DTOEventListener that will receive notifications after successful entity assembly 
on-entity-failed | |2.0.0 |Name of the bean that implements DTOEventListener that will receive notifications if an exception occurs during entity assembly 
use-bean-preprocessor |false |2.0.0 |By default AOP evaluates eligibility for dto support at runtime, so that all methods that are executed under AOP interceptor will be examined for eligibility. This may have a performance hit on the system. To optimise this you can set bean preprocessor to true, which is spring instrumentation tools that will resolve eligible methods at the context creation time. 
pointcut-match-regex |* |2.0.0 |Allows to set package and class paths for eligible spring beans. This will improve performance as if the spring bean is not under provided package names AOP will short circuit. The values are comma separated e.g. "com.inspiresoftware.lib.dto.geda.test.**,com.inspiresoftware.lib.dto.geda.other.MyService.**" (This example allows all in package test and all methods from MyService class). By default all packages are eligible. 
pointcut-nomatch-regex | |2.0.0 |Allows to set the exclusions for the eligible beans that are matched by pointcut-match-regex. The values are comma separated e.g. "com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedTestNoMatch.**,com.inspiresoftware.lib.dto.geda.test.TestNoMatch.**" (This example excludes all methods from AnnotatedTestNoMatch and TestNoMatch classes)

**Example full set annotation driven**


```xml
    <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://www.genericdtoassembler.org/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.genericdtoassembler.org/schema
                           http://www.genericdtoassembler.org/schema/geda-spring-integration-annotation-3.1.0.xsd">

    <geda:annotation-driven dto-factory="dtoFactory"
                            dto-adapters-registrar="adapterRegistrar"
                            on-dto-assembly="onDto"
                            on-dto-assembled="onDtoSuccess"
                            on-dto-failed="onDtoFailure"
                            on-entity-assembly="onEntity"
                            on-entity-assembled="onEntitySuccess"
                            on-entity-failed="onEntityFailure"
                            use-bean-preprocessor="true"
                            pointcut-match-regex="com.inspiresoftware.lib.dto.geda.test.*"
                            pointcut-nomatch-regex="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedTestNoMatch.*,com.inspiresoftware.lib.dto.geda.test.TestNoMatch.*"/>
...
</beans>
```


### @Transferable

Transferable allows to set some meta data to your Spring beans methods to allow GeDA match the best way of transferring data. 

The execution sequence is:

1. If before() flow is set do the transfer and use objects with loaded data as parameters to the method
1. Execute your methods body
1. If after() flow is set do the transfer and use objects with loaded data as return values

@Transferable offers the following configurations:

Property | Default | Since | Purpose 
--- | --- | --- | ---
Direction before() |Direction.NONE |2.0.0 |Define the transfer direction prior execution of the method body. Either DTO_TO_ENTITY, ENTITY_TO_DTO or NONE 
Direction after() |Direction.NONE |2.0.0 |Define the transfer direction after execution of the method body. Either DTO_TO_ENTITY, ENTITY_TO_DTO or NONE 
String dtoFilterKey() | |2.0.0 |Super type of the DTO object passed as parameter to allow partial DTO loads or partial entity updates depending on the direction 
String dtoKey() | |2.0.0 |Key for dto instance that will be used by BeanFactory to create new DTO instance to support method signatures like 


```java
@Transferable(after = ENTITY_TO_DTO, dtoKey = "MyDtoClass_key")
DTO load(Entity entity);
```


String entityKey() | |2.0.0 |Key for dto instance that will be used by BeanFactory to create new entity instance to support method signatures like 


```java
@Transferable(after = DTO_TO_ENTITY, entityKey = "MyEntityClass_key")
Entity update(DTO dto);
```


String context() | |2.0.0 |A marker that is passed to listeners. This is not used by GeDA but may aid you in your logic in DTOEventListener 

## XML Driven
### XML configuration
XML driven configuration is facilitated using two tags: <geda:xml-driven/> and <geda:dtosupport/>.

#### <geda:xml-driven/>
This tag is the main configuration for GeDA to setup the infrastructure, which offers the following configuration attributes:

Property | Default | Since | Purpose 
--- | --- | --- | ---
dto-factory |dtoFactory |2.1.1 |Name of the bean that implements DTOFactory 
dto-adapters-registrar | |2.1.1 |Name of the bean that implements DTOAdaptersRegistrar 
on-dto-assembly | |2.1.1 |Name of the bean that implements DTOEventListener that will receive notifications before a DTO is assembled 
on-dto-assembled | |2.1.1 |Name of the bean that implements DTOEventListener that will receive notifications after successful DTO assembly 
on-dto-failed | |2.1.1 |Name of the bean that implements DTOEventListener that will receive notifications if an exception occurs during DTO assembly 
on-entity-assembly | |2.1.1 |Name of the bean that implements DTOEventListener that will receive notifications before a entity is assembled 
on-entity-assembled | |2.1.1 |Name of the bean that implements DTOEventListener that will receive notifications after successful entity assembly 
on-entity-failed | |2.1.1 |Name of the bean that implements DTOEventListener that will receive notifications if an exception occurs during entity assembly 
use-bean-preprocessor |false |2.1.1 |By default AOP evaluates eligibility for dto support at runtime, so that all methods that are executed under AOP interceptor will be examined for eligibility. This may have a performance hit on the system. To optimise this you can set bean preprocessor to true, which is spring instrumentation tools that will resolve eligible methods at the context creation time. 

**Example full set XML driven**


```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:geda="http://www.genericdtoassembler.org/schema"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.genericdtoassembler.org/schema
                           http://www.genericdtoassembler.org/schema/geda-spring-integration-xml-3.1.0.xsd">

    <geda:xml-driven dto-factory="dtoFactory"
                     dto-adapters-registrar="adapterRegistrar"
                     on-dto-assembly="onDto"
                     on-dto-assembled="onDtoSuccess"
                     on-dto-failed="onDtoFailure"
                     on-entity-assembly="onEntity"
                     on-entity-assembled="onEntitySuccess"
                     on-entity-failed="onEntityFailure"/>
...
</beans>
```


#### <geda:dtosupport/>

This tag allows to inject DTOSupport into your spring beans, which offers the following configuration attributes:

Property | Default | Since | Purpose 
--- | --- | --- | ---
property |dtoSupport |2.1.1 |Name of the public setter on your Spring bean to set the dto support 

**Example default bean initialisation**


```xml
...
<bean id="xmlTestServiceDefaultProperty"
      class="com.inspiresoftware.lib.dto.geda.test.impl.TestXmlServiceImpl">
    <geda:dtosupport/>
</bean>...
```


**Example default bean implementation**


```java
    public class TestXmlServiceImpl implements TestXmlService {

    private DTOSupport dtoSupport;

    public <T> T transfer(final Object from) {
        return dtoSupport.assembleDtoByKey("annDtoKey", from, "manual");
    }

    public void setDtoSupport(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }

}
```


**Example custom bean initialisation**


```xml
...
<bean id="xmlTestServiceDefaultProperty"
      class="com.inspiresoftware.lib.dto.geda.test.impl.TestXmlServiceImpl">
    <geda:dtosupport property="dtoSupportBean"/>
</bean>...
```


**Example custom bean implementation**


```java
    public class TestXmlServiceImpl implements TestXmlService {

    private DTOSupport dtoSupport;

    public <T> T transfer(final Object from) {
        return dtoSupport.assembleDtoByKey("annDtoKey", from, "manual");
    }

    public void setDtoSupportBean(final DTOSupport dtoSupport) {
        this.dtoSupport = dtoSupport;
    }

}
```



## DTOFactory
DTO factory has default DTOFactoryImpl so that it can be configured as a spring bean.

**Example use of bean factory**


```xml
    <geda:annotation-driven dto-factory="dtoFactory"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="annDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedExtendedDataTransferObjectImpl"/>
                <entry key="annFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedDataTransferObjectImpl"/>
                <entry key="dslDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.DSLExtendedDataTransferObjectImpl"/>
                <entry key="dslFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DSLDataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

```



## DTOAdaptersRegistrar

Adapters registrar provides a simple wrapper to use spring beans as [[adapters>>url:http://www.inspire-software.com/confluence/display/GeDA/Adapters+API]] to core API. 

**Example use of adapters registrar**


```xml
    <geda:annotation-driven dto-adapters-registrar="adapterRegistrar"/>
...
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
```


Above example allows to use adapters in DTO entity mappings

**Example annotations**


```java
@Dto
public class AnnotatedDataTransferObjectImpl implements DataTransferObject {

    @DtoField(converter = "vc")
    private String value;

... getters/setters...
}
```


**Example DSL**


```java
    public class DSLDataTransferObjectImpl implements DataTransferObject {
    private String value;

... getters/setters...
}
...
final Registry reg = Registries.registry();
reg.dto(DSLDataTransferObjectImpl.class).forEntityGeneric().withField("value").converter("vc");

```



## DTODSLRegistrar

DSL registrar allows to use DSL API as opposed to annotation API for mapping.

**Example using DSL**


```xml
    <geda:annotation-driven  dto-factory="dtoFactory"
                             dto-dsl-registrar="dslRegistrar"/>

    <bean id="dtoFactory" class="com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl">
        <constructor-arg>
            <map>
                <entry key="entityKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DomainObjectImpl"/>
                <entry key="annDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedExtendedDataTransferObjectImpl"/>
                <entry key="annFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.AnnotatedDataTransferObjectImpl"/>
                <entry key="dslDtoKey"    value="com.inspiresoftware.lib.dto.geda.test.impl.DSLExtendedDataTransferObjectImpl"/>
                <entry key="dslFilterKey" value="com.inspiresoftware.lib.dto.geda.test.impl.DSLDataTransferObjectImpl"/>
            </map>
        </constructor-arg>
    </bean>

    <bean id="dslRegistrar" class="com.inspiresoftware.lib.dto.geda.test.impl.DSLRegistrar"/>

</beans>

```


**Example DSL registrar**


```java
    public class DSLRegistrar implements DTODSLRegistrar {

    // Both DTOSupport and Registry objects will provided by GeDA when beans 
    // are initilised which in turn will invoke this method, so that you can create mappings
    public void registerMappings(final DTOSupport dtoSupport, final com.inspiresoftware.lib.dto.geda.dsl.Registry dslRegistry) {

        dslRegistry.dto("dslFilterKey").forEntity("entityKey")
                .withField("value")
                .and()
                .withField("timestamp").readOnly();

        dslRegistry.dto("dslDtoKey").forEntity("entityKey")
                .withField("value")
                .and()
                .withField("timestamp").readOnly()
                .and()
                .withField("value2");

    }
```

