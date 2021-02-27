# Multi ClassLoader and OSGi

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

> This is feature is available to v.3.0.0+

## The Problem

Class loading is very complex topic which most developer try to avoid. The crux of the problem can be depicted by this snippet:

**Class cast exception**

```java
import mypack.Example;

...


final MyCustomClassLoader cl = MyCustomClassLoader();

final Class exampleClass = cl.loadClass("mypack.Example");

final Object example = Class.newInstance();

(Example) example // ClassCastException!!!!

...
```

The point we try to make here is that each class loader has a different parent and hence its own version of the class loaded, which are regarded different from the point of view of JVM. Frameworks such as OSGi exploit this feature to promote modular design of the applications and hence destroy the brains of amateur developers.

## GeDA Solution

Taking multi class loader environments into account GeDA 3.x.x core was completely reworked since dynamic class generation was heavily dependent on the class loader context.

What does this mean in terms of API? Not much - apart from an additional parameter to already know API.

**2.x.x VS 3.x.x API**

```java
// version 2.x.x assembler instantiation
DTOAssembler.newCustomAssembler(dtoClassFilter, entity.getClass(), registry, osgiJavassisstSynthesizer)

// version 3.x.x assembler instantiation
DTOAssembler.newCustomAssembler(dtoClassFilter, entity.getClass(), classLoader, registry, osgiJavassisstSynthesizer)

```

### OSGi bundle

The OSGi framework (OSGi being an acronym for "Open Services Gateway initiative") is a module system and service platform for the Java programming language that implements a complete and dynamic component model. The specification relies upon manifest file of a jar. Each jar represents a module that imports and/or exports classes. The control over what is imported and exported is accomplished using class loaders. Each bundle comes with its own - hence we stumble upon our problem.

Unfortunately developer always keen for a quick fix and such a great idea as OSGi has became perverted by developers OSGifying their libraries to make them work under OSGi contains. This kind of makes you wonder - why go OSGi and then defile all its principles by exposing all you have got? GeDA went the proper way.

As of 3.0.0 a genuine OSGi bundle has became part of GeDA maven modules family. The maven dependency is:

#### Maven

**Maven dependency GeDA OSGi**

```xml
  <dependency>
    <groupId>com.inspire-software.lib.dto.geda</groupId>
    <artifactId>geda.osgi</artifactId>
    <type>bundle</type>
    <version>3.0.0-SNAPSHOT</version>
</dependency>
```

As GeDA uses Javassist to generate some code on the fly Javassis bundle also must be loaded:

**Maven dependency GeDA OSGi**

```xml
  <dependency>
    <groupId>org.apache.servicemix.bundles</groupId>
    <artifactId>org.apache.servicemix.bundles.javassist</artifactId>
    <type>bundle</type>
    <version>3.12.1.ga_1</version>
</dependency>
```

#### GeDA OSGi API

So what do we mean by genuine?

GeDA does not expose any of it internals but rather registers a facade service that can be obtained through bundle context:

**GeDA Bundle Client Activator**

```java
 public class Activator implements BundleActivator {

    public void start(final BundleContext bundleContext) throws Exception {

        final ServiceReference<GeDAFacade> facadeReference =
                bundleContext.getServiceReference(GeDAFacade.class);

        final GeDAFacade facade =
                bundleContext.getService(facadeReference);

    }
}
```

What does this facade offer?

**GeDA Facade**

```java
  public interface GeDAFacade {

    /**
     * Annotations driven DTO service.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     *
     * @return DTO service
     */
    DTOSupportAnnotationsService getAnnService(ClassLoader activator);

    /**
     * DSL driven DTO service.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     *
     * @return DTO service
     */
    DTOSupportDSLService getDSLService(ClassLoader activator);

    /**
     * Create BeanFactory adapter for activators bundle. Activators class
     * loader will be used for Class.forName() invocations.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     *
     * @return bean factory instance.
     */
    ExtensibleBeanFactory createBeanFactory(ClassLoader activator);

    /**
     * Release resources for given activator.
     *
     * Recommended to use this bundle Activator.class.getClassLoader()
     *
     * @param activator class loader service activator
     */
    void releaseResources(ClassLoader activator);

}

```

Facade provides access to:

* Annotation driven service that works with annotation based DTO mappings
* DSL driven service that uses DSL registry
* Bean factory instantiator
* Hook method for releasing resources

Each of these accessors requires class loader context (remember the problem with class loading? - simple solution)

The basic use of facade is:

**Facade use**

```java
final DTOSupportAnnotationsService annotationsService = facade.getAnnService(Activator.class.getClassLoader());

final SimpleEntityClass entity = new SimpleEntityClass();
entity.setString("aether");
entity.setDecimal(new BigDecimal("28.97"));
entity.setInteger(1);
final SimpleDTOClass dto = new SimpleDTOClass();

final DTOEventListener expectations = new ExpectationsDTOEventListener(new Object[] { dto, entity });

annotationsService.assembleDto(dto, entity, null, "simple");

```

Each dto support service has a list of methods to satisfy the most demanding connoisseurs of DTO pattern.

Furthermore it is possible to add listeners to the DTO support services:

**Facade use**

```java
final final DTOEventListener onDtoAssembled = ...
        
service.addListener("onDtoAssembled", expectations);
...
service.assembleDto(dto, entity, null, "simple");
...
service.removeListener(expectations);

```

> Remember that service instances are singleton with respect to a class loader context. Therefore if you have multiple bundles please use each bundles own activator class loader or you may run into class cast exceptions.

#### Annotations Service

Using bean factories is also easy:

**Annotation service and Bean factory**

```java

final ExtensibleBeanFactory beanFactory = facade.createBeanFactory(Activator.class.getClassLoader());
final DTOSupportAnnotationsService annotationsService = facade.getAnnService(Activator.class.getClassLoader());

beanFactory.registerDto("SimpleDTO", "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleDTOClass");
beanFactory.registerDto("SimpleEntity", "com.inspiresoftware.lib.dto.geda.osgi.test.SimpleEntityClass");
beanFactory.registerDto("ComplexDTO", "com.inspiresoftware.lib.dto.geda.osgi.test.ComplexDTOClass");
beanFactory.registerDto("ComplexEntity", "com.inspiresoftware.lib.dto.geda.osgi.test.ComplexEntityClass");
annotationsService.registerAdapter("EqualsByString", new EqualsByStringMatcher());
...
annotationsService.assembleDto(dto, entity, beanFactory, "simple");

```

Extensible bean factory allows you to register all necessary bean keys and corresponding implementations.

Adapters can be registered using the service interface.

Using DSL is event easier and in fact is a recommended way as it follows the OSGi pattern better and keeps you DTO clean and completely decoupled from GeDA.

#### DSL Service

**DSL service**

```java
final ExtensibleBeanFactory beanFactory = facade.createBeanFactory(Activator.class.getClassLoader());
final DTOSupportDSLService dslService = facade.getDSLService(Activator.class.getClassLoader());
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

...

dslService.assembleDto(dto, entity, basic, "simple");

```

#### Releasing resources

GeDA uses internal caching to make things run faster, not to mention the class references that are held in mappings as well as other things. So keeping that in mind is very important. Luckily GeDA has already solved all these problems. And when the bundle is uninstalled all resources are automatically cleaned up. However since GeDA uses class loader context it keeps soft references during runtime, so it may keep some objects longer than necessary in cases when you uninstall bundles that are clients of GeDA. When this happens it is recommended to call GeDAFacade.releaseResources(ClassLoader) to ensure that nothing is left behind when you bundle bids farewell.

#### Tips

* Do not mix BeanFactories DSL Registries and Services that have different class loader contexts
* For DSL mappings, adapters registration, listeners it is recommended to register all of those at the point of bundle activation
* Do not keep hard references to any of the GeDA API - this may prevent bundle uninstallation if you do plan to load and unload GeDA 

> GeDA will keep your singletons until GeDA bundle is uninstalled or GeDAFacade.releaseResources(ClassLoader) is called so do not worry about keeping hard references - always use bundleContext to get to Facade

* GeDAFacade.releaseResources(ClassLoader) should be called when your bundles is being uninstalled so that GeDA frees up the resources - otherwise do not worry about it.


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
