# OSGi API

## Basics

> This is an advanced topic and requires full understanding of the core API.

OSGi promotes modular approach to the application architecture. Each module is an isolated unit called a bundle. Each bundle has its own class loader and communicate with other bundles through BundleContext. OSGi is very dynamic and complex environment that only allows other bundles to see what other bundles export. Many libraries make a move into OSGi by just OSGi-fying their java classes - meaning that they create a dummy bundle where all classes are exported.

We decided that this is just not right, so GeDA offers a true OSGi bundle that allows working with its API though an exposed bundle service called GeDAFacade, which exposes just enough for you to have full access to mapping and allowing you to perform that data transfers. All the supporting classes are hidden inside.

So what does basic usage looks like?

**Basic usage**


```java
// get facade service
final ServiceReference<GeDAFacade> facadeReference =
        bundleContext.getServiceReference(GeDAFacade.class);
final GeDAFacade facade =
        bundleContext.getService(facadeReference);

// choose the API service (either annotation or DSL)
final DTOSupportAnnotationsService annotationsService = facade.getAnnService(this.getClass().getClassLoader());

...

// for this example we manually construct an entity object
final SimpleEntityClass entity = new SimpleEntityClass();
entity.setString("aether");
entity.setDecimal(new BigDecimal("28.97"));
entity.setInteger(1);

// new DTO instance
final SimpleDTOClass dto = new SimpleDTOClass();

// do the transfer
annotationsService.assembleDto(dto, entity, null, "simple osgi example");
```


## GeDAFacade
GeDA facade service is the central access point for the API. It offers the following methods:

Signature | Since | Purpose
--- | --- | ---
DTOSupportAnnotationsService getAnnService(ClassLoader activator)|3.0.0 |Create client bundle specific service to access the annotations GeDA API
DTOSupportDSLService getDSLService(ClassLoader activator)|3.0.0 |Create client bundle specific service to access the DSL GeDA API
ExtensibleBeanFactory createBeanFactory(ClassLoader activator) |3.0.0 |Create default extensible bean factory object for client bundle
void releaseResources(ClassLoader activator) |3.0.0 |Releasing resources when client bundle unloads

As you may notice all facade methods include class loader parameters. This is due to the fact that GeDA manipulates a lot of objects that could be provided by various bundles so we need the designated class loader to load Class.forName() and load classes that GeDA auto generates using javassist to speed up the transfer.

When a request for an annotation/dsl service is made to the facade the facade service will try to look up service instance for this class loader in soft reference cache. If not not found a new instance is created, otherwise existing instance is re-purposed. This means that annotation service is one instance per client bundle. Hence all information such as listeners, adapters and bean factories is bound to specific bundle (or rather its class loader). And that is why when client bundle is unloaded GeDAFacade.releaseResources(ClassLoader activator) must be called. When the GeDA bundle itself is unloaded this method is called automatically releasing all resources for all bundles.


### DTOSupportAnnotationsService
Annotation service provides a number of convenience methods that would perform the data transfer for annotated DTO classes. They are all simple wrappers around the Assembler API that encapsulate the core internals and provide more user friendly API.

In addition to the transfer methods service allows to attach listeners to the transfer process that may aid in doing some tricky stuff such as time stamping access to objects or adding audit information or whatever your heart desires.

#### Utility methods:

Signature | Since | Purpose
--- | --- | ---
void registerAdapter(final String key, final Object adapter) |3.0.0 |Allows to register an adapter such as value converter, entity matcher or entity retriever.
void addListener(String dtoEvent, DTOEventListener eventListener) |3.0.0 |Add event listener to this service instance
void removeListener(DTOEventListener eventListener) |3.0.0 |Add event listener to this service instance

#### Using BeanFactory

**Basic usage**


```java
final ExtensibleBeanFactory bf = facade.createBeanFactory(this.getClass().getClassLoader())
final DTOSupportAnnotationsService annotationsService = facade.getAnnService(this.getClass().getClassLoader());

...
final SimpleEntityClass entity = ...

// new DTO instance
final SimpleDTOClass dto = new SimpleDTOClass();

// do the transfer
final SimpleDTOClass dto = annotationsService.assembleDto(dto, entity, bf, "bean factory usage");
```


#### Transfer methods:
Every transfer method has context parameter - this is simply a marker that is passed to listeners that you can use to fine tune your logic.

Signature | Since | Purpose
--- | --- | ---
<T> T assembleDto(T dto, Object entity, BeanFactory beanFactory, String context) |3.0.0 |Pure dto assembly from an entity
<T> T assembleDtoByKey(String dtoKey, Object entity, BeanFactory beanFactory, String context) |3.0.0 |IoC version of dto assembly that enabled you to indirectly reference dto object. Bean factory is used to create new instance of dto object.
<T> T assembleDto(String dtoFilter, T dto, Object entity, BeanFactory beanFactory, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance.
<T> T assembleDtoByKey(String dtoFilter, String dtoKey, Object entity, BeanFactory beanFactory, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter and dto is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance.
<T> void assembleDtos(String dtoKey, Collection<T> dtos, Collection entities, BeanFactory beanFactory, String context) |3.0.0 |processing bulk transfers - effectively a loop on top of assembleDto. dtos collection must be non null empty collection.
<T> void assembleDtos(String dtoFilter, String dtoKey, Collection<T> dtos, Collection entities, BeanFactory beanFactory, String context) |3.0.0 |processing bulk transfers and creating new dto elements using bean factory by key - effectively a loop on top of assembleDto. dtos collection must be non null empty collection.
<T> T assembleEntity(Object dto, T entity, BeanFactory beanFactory, String context)|3.0.0 |Pure entity assembly from a DTO
<T> T assembleEntityByKey(Object dto, String entityKey, BeanFactory beanFactory, String context) |3.0.0 |IoC version of entity assembly that enabled you to indirectly reference entity object. Bean factory is used to create new instance of entity object.
<T> T assembleEntity(String dtoFilter, Object dto, T entity, BeanFactory beanFactory, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter is looked up through bean factory. The purpose is facilitation of partial update of data. E.g. when you just wish to update only basic data but use the full class dto instance.
<T> T assembleEntityByKey(String dtoFilter, Object dto, String entityKey, BeanFactory beanFactory, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter and entity is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance.
<T> void assembleEntities(String entityKey, Collection dtos, Collection<T> entities, BeanFactory beanFactory, String context) |3.0.0 |processing bulk transfers - effectively a loop on top of assembleEntity. entity collection must be non null empty collection.
<T> void assembleEntities(String dtoFilter, String entityKey, Collection dtos, Collection<T> entities, BeanFactory beanFactory, String context) |3.0.0 |processing bulk transfers and creating new entity elements using bean factory by key - effectively a loop on top of assembleEntity. entities collection must be non null empty collection.

### DTOSupportDSLService

DSL service provides a number of convenience methods that would perform the data transfer using DSL registry contexts. They are all simple wrappers around the Assembler API that encapsulate the core internals and provide more user friendly API.

In addition to the transfer methods service allows to attach listeners to the transfer process that may aid in doing some tricky stuff such as time stamping access to objects or adding audit information or whatever your heart desires.

#### Utility methods:

Signature | Since | Purpose
--- | --- | ---
Registry createRegistry(final String namespace, final ExtensibleBeanFactory beanFactory) |3.0.0 |Create a DSL registry with namespace key and bind bean factory to it.
Registry getRegistry(final String namespace) |3.0.0 |Look up DSL registry.
void registerAdapter(final String key, final Object adapter) |3.0.0 |Allows to register an adapter such as value converter, entity matcher or entity retriever.
void addListener(String dtoEvent, DTOEventListener eventListener) |3.0.0 |Add event listener to this service instance
void removeListener(DTOEventListener eventListener) |3.0.0 |Add event listener to this service instance

#### Using DSL Registry

**Basic usage**


```java
final ExtensibleBeanFactory bf = facade.createBeanFactory(this.getClass().getClassLoader())
final DTOSupportDSLService dslService = facade.getDSLService(this.getClass().getClassLoader());
final Registry defReg = dslService.createRegistry("default", bf);
...
final SimpleEntityClass entity = ...

// new DTO instance
final SimpleDTOClass dto = new SimpleDTOClass();

// do the transfer
final SimpleDTOClass dto = annotationsService.assembleDto(dto, entity, defReg, "DSL registry usage");
```



#### Transfer methods:
Every transfer method has context parameter - this is simply a marker that is passed to listeners that you can use to fine tune your logic.

Signature | Since | Purpose
--- | --- | ---
<T> T assembleDto(T dto, Object entity, Registry registry, String context) |3.0.0 |Pure dto assembly from an entity
<T> T assembleDtoByKey(String dtoKey, Object entity, Registry registry, String context) |3.0.0 |IoC version of dto assembly that enabled you to indirectly reference dto object. Bean factory is used to create new instance of dto object.
<T> T assembleDto(String dtoFilter, T dto, Object entity, Registry registry, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance.
<T> T assembleDtoByKey(String dtoFilter, String dtoKey, Object entity, Registry registry, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter and dto is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance.
<T> void assembleDtos(String dtoKey, Collection<T> dtos, Collection entities, Registry registry, String context) |3.0.0 |processing bulk transfers - effectively a loop on top of assembleDto. dtos collection must be non null empty collection.
<T> void assembleDtos(String dtoFilter, String dtoKey, Collection<T> dtos, Collection entities, Registry registry, String context) |3.0.0 |processing bulk transfers and creating new dto elements using bean factory by key - effectively a loop on top of assembleDto. dtos collection must be non null empty collection.
<T> T assembleEntity(Object dto, T entity, Registry registry, String context)|3.0.0 |Pure entity assembly from a DTO
<T> T assembleEntityByKey(Object dto, String entityKey, Registry registry, String context) |3.0.0 |IoC version of entity assembly that enabled you to indirectly reference entity object. Bean factory is used to create new instance of entity object.
<T> T assembleEntity(String dtoFilter, Object dto, T entity, Registry registry, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter is looked up through bean factory. The purpose is facilitation of partial update of data. E.g. when you just wish to update only basic data but use the full class dto instance.
<T> T assembleEntityByKey(String dtoFilter, Object dto, String entityKey, Registry registry, String context) |3.0.0 |Transfer with filtering. dtoFilter key is the key for superclass of dto instance. The filter and entity is looked up through bean factory. The purpose is facilitation of partial loading of data. E.g. when you just wish to load basic data but use the full class dto instance.
<T> void assembleEntities(String entityKey, Collection dtos, Collection<T> entities, Registry registry, String context) |3.0.0 |processing bulk transfers - effectively a loop on top of assembleEntity. entity collection must be non null empty collection.
<T> void assembleEntities(String dtoFilter, String entityKey, Collection dtos, Collection<T> entities, Registry registry, String context) |3.0.0 |processing bulk transfers and creating new entity elements using bean factory by key - effectively a loop on top of assembleEntity. entities collection must be non null empty collection.

### DTOEventListener

Event listener are tracking the following events:

* onDtoAssembly - prior assembler attempts to transfer data for a single dto
* onEntityAssembly - prior assembler attempts to transfer data for a single entity
* onDtoAssembled - after assembler successfully transferred data to a single dto
* onDtoFailed - right at the first occurence of an exception during transfer to dto
* onEntityAssembled - after assembler successfully transferred data to a single entity
* onEntityFailed - right at the first occurence of an exception during transfer to entity

> Currently only one listener is permitted per event. Setting another listener to same event will overwrite it.

**Basic listener example**


```java
 public class DTOAssembledCounter implements DTOEventListener {

 int count = 0;
    // context[0] is the context marker, context[1] - dto, context[2] - entity, context[3] - exception
    public void onEvent(final Object... context) {
        if ("simple".equals(context[0])) {
   count++;
  }
    }

 public int getCount() {
  return count;
 }
}
...
final DTOSupportAnnotationsService annotationsService = facade.getAnnService(this.getClass().getClassLoader());
final DTOAssembledCounter attempted = new DTOAssembledCounter();
final DTOAssembledCounter assembled = new DTOAssembledCounter();
final DTOAssembledCounter failed = new DTOAssembledCounter();
annotationsService.addListener("onDtoAssembly", attempted);
annotationsService.addListener("onDtoAssembled", assembled);
annotationsService.addListener("onDtoFailed", failed);

...do transfers ...

Log.info("attempted  " + attempted.getCount() + " dto transfers with " + failed.getCount() + " failed");

annotationsService.removeListener(attempted);
annotationsService.removeListener(assembled);
annotationsService.removeListener(failed);
```

