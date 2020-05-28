# Adapters API

## Basics

Adapters API represented by the following interfaces:

Name | Since | Usage | Purpose
--- | --- | --- | ---
Adapters|3.1.0 | |IoC facility that allows to create default instances of bean factory and adapters repository.
BeanFactory|1.0.0 |throughout |IoC facility that allows to create object instances via keys.
ExtensibleBeanFactory|3.0.0|DSL|Extension to BeanFactory that allows registering new key-class pairs which is required by some DSL features.
ValueConverter|1.0.0 |mapping of field and virtual field |Adapter to port incompatibilities between DTO and Entity fields.
EntityRetriever|1.0.0 |mapping of parent |Adapter to link to the data access layer (DAO) to look up parent entities during entity update.
DtoToEntityMatcher|1.0.0 |mapping of collection and map fields |Adapter to aid synchronisation process of elements of entity collections and maps during entity update.


## Adapters

Originally all adapters in core were supposed to be interfaces since their implementations are too specific to a particular project. However due to numerous requests regarding BeanFactory implementation we decided to add this utility to provide default implementation which will suffice for the basic usage. For more advanced complex mappings we encourage implement your own adapters.

Signature | Since | Purpose
--- | --- | --- 
ExtensibleBeanFactory beanFactory() |3.1.0 |Create Bean factory for current class loader.
ExtensibleBeanFactory beanFactory(final ClassLoader classLoader) |3.1.0 |Create Bean factory for provided class loader.
AdaptersRepository adaptersRepository() |3.1.0 |Create adapter repository which is a convenience container to hold your adapters.

**Example usage of default bean factory**


```java
final ExtensibleBeanFactory bf = Adapters.beanFactory();
...
assembler.assembleDto(dto, entity, null, bf);
```


**Example usage of adapter repository**


```java
final AdaptersRepository adapters = Adapters.adaptersRepository();
adapters.registerAdapter("myAdapter1", new MyValueConverter());
adapters.registerAdapter("myAdapter2", new MyEntityRetriever());
adapters.registerAdapter("myAdapter3", new MyDtoToEntityMatcher());
...
assembler.assembleDto(dto, entity, adapters.getAll(), null);
```



## BeanFactory
Bean factory provides two simple methods to understand the type of the object and to generate new object instances.

Signature | Since | Purpose
--- | --- | --- 
Class getClazz(String entityBeanKey) |2.1.0 |Provides class that best describes the object instance. For DTO keys this has to be a class. For entity it is recommended to use interface (if applicable) or class
Object get(String entityBeanKey) |1.0.0 |Creates a new instance of object by key.

> We strongly recommend to provide your own BeanFactory implementation. The reason for this is that instantiation of concrete implementations is something very specific to each individual project and hence there was no "one-size-fits-all" solution

> Since version 3.1.0 default implementation of bean factory is available in core through [[Adapters.beanFactory()

> Integration modules Spring and OSGi contain implementations of BeanFactory specific to its internals. Interested reader is advised to look at com.inspiresoftware.lib.dto.geda.impl.DTOFactoryImpl in spring-integration module and com.inspiresoftware.lib.dto.geda.osgi.impl.OSGiBundleDTOFactoryImpl in osgi module

### Hello World BeanFactory

> Hello World example is for educational purpose only. Using if's to match keys is not an efficient approach.

**Example "Hello World" BeanFactory from examples module**


```java
    public class BlogBeanFactory implements BeanFactory {

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        if ("BaseUserDTO".equals(entityBeanKey)) {
            return BaseUserDTOImpl.class;
        } else if ("BaseUserEntryDTO".equals(entityBeanKey)) {
            return BaseUserEntryDTOImpl.class;
        } else if ("BaseUserEntryReplyDTO".equals(entityBeanKey)) {
            return BaseUserEntryReplyDTOImpl.class;
        } else if ("UserDTO".equals(entityBeanKey)) {
            return UserDTOImpl.class;
        } else if ("UserEntryDTO".equals(entityBeanKey)) {
            return UserEntryDTOImpl.class;
        }

        if ("User".equals(entityBeanKey)) {
            return User.class;
        } else if ("UserEntry".equals(entityBeanKey)) {
            return UserEntry.class;
        } else if ("UserEntryReply".equals(entityBeanKey)) {
            return UserEntryReply.class;
        }

        throw new IllegalArgumentException("No representative for : " + entityBeanKey);
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        if ("BaseUserDTO".equals(entityBeanKey)) {
            return new BaseUserDTOImpl();
        } else if ("BaseUserEntryDTO".equals(entityBeanKey)) {
            return new BaseUserEntryDTOImpl();
        } else if ("BaseUserEntryReplyDTO".equals(entityBeanKey)) {
            return new BaseUserEntryReplyDTOImpl();
        } else if ("UserDTO".equals(entityBeanKey)) {
            return new UserDTOImpl();
        } else if ("UserEntryDTO".equals(entityBeanKey)) {
            return new UserEntryDTOImpl();
        }

        if ("User".equals(entityBeanKey)) {
            return new UserImpl();
        } else if ("UserEntry".equals(entityBeanKey)) {
            return new UserEntryImpl();
        } else if ("UserEntryReply".equals(entityBeanKey)) {
            return new UserEntryReplyImpl();
        }

        throw new IllegalArgumentException("No entry for : " + entityBeanKey);
    }
}

```


### HashMap BeanFactory

Slightly better hash map approach:

**Example HashMap BeanFactory, improved "Hello World"**


```java
    public class BlogBeanFactory implements BeanFactory {

    private Map<String, Class> map = new HashMap<String, Class> {{
        put("BaseUserDTO", BaseUserDTOImpl.class);
        put("BaseUserEntryDTO", BaseUserEntryDTO.class);
...
    }};

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        if (map.containsKey(entityBeanKey)) {
            return map.get(entityBeanKey);
        }

        throw new IllegalArgumentException("No representative for : " + entityBeanKey);
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        if (mapping.containsKey(entityBeanKey)) {
           try {
               return mapping.get(entityBeanKey).newInstance();
           } catch (...)         .... throw exception ...
           }
        } else  .... throw exception ...
    }
}

```


## ExtensibleBeanFactory

Extensible bean factory inherits [[BeanFactory to add mapping registration capabilities used by DSL.

Signature | Since | Purpose
--- | --- | --- 
void registerDto(String key, String className) throws IllegalArgumentException |3.0.0|Allows to enrich bean factory with new DTO bean mappings. **key** string key for this class (interface name preferred), **className** fully qualified string representation of java class. No check is made regarding the validity of this class and if it is invalid will cause exception during #get()
void registerEntity(String key, String className, String representative) throws IllegalArgumentException |3.0.0|Allows to enrich bean factory with new entity bean mappings. **key** string key for this class (interface name preferred), **className** fully qualified string representation of java class. **representative** is an interface that best describes className or same as className as no interface available. No check is made regarding the validity of this class and if it is invalid will cause exception during #get()

### Spring module BeanFactory

Here is a real implementation of extensible bean factory. In a nutshell all mappings are held in hash maps and looked up by key either for DTO or entity.

**DTOFactoryImpl from spring-integration**


```java
    public class DTOFactoryImpl implements DTOFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DTOFactoryImpl.class);

    private final Map<String, String> mappingClasses = new ConcurrentHashMap<String, String>();
    private final Map<String, String> mappingReps = new ConcurrentHashMap<String, String>();
    private final Map<String, Class> cacheClasses = new ConcurrentHashMap<String, Class>();
    private final Map<String, Class> cacheReps = new ConcurrentHashMap<String, Class>();

    public DTOFactoryImpl() {
    }

    /**
     * @param mappingClasses simple key to class mapping
     */
    @Deprecated
    public DTOFactoryImpl(final Map<String, String> mappingClasses) {
        this(mappingClasses, mappingClasses);
    }

    /**
     * @param mappingClasses simple key to class mapping
     * @param mappingEntityRepresentatives simple key to interface mapping for entities
     */
    public DTOFactoryImpl(final Map<String, String> mappingClasses,
                          final Map<String, String> mappingEntityRepresentatives) {
        // put all classes
        this.mappingClasses.putAll(mappingClasses);
        // by default all representatives are implementation classes
        this.mappingReps.putAll(mappingClasses);
        // override all entity ones
        this.mappingReps.putAll(mappingEntityRepresentatives);
    }

    /** {@inheritDoc} */
    public void register(final String key, final String className) throws IllegalArgumentException {
        registerDto(key, className);
    }

    /** {@inheritDoc} */
    public void registerDto(final String key, final String className) throws IllegalArgumentException {
        if (StringUtils.hasText(key) && StringUtils.hasText(className)) {
            if (this.mappingClasses.containsKey(key)) {
                throw new IllegalArgumentException("key is already used: [key=" + key + ", className=" + className + "]");
            }
            this.mappingClasses.put(key, className);
            this.mappingReps.put(key, className);
        } else {
            throw new IllegalArgumentException("all args are mandatory: [key=" + key + ", className=" + className + "]");
        }
    }

    /** {@inheritDoc} */
    public void registerEntity(final String key, final String className, final String representative) throws IllegalArgumentException {
        if (StringUtils.hasText(key) && StringUtils.hasText(className)) {
            if (this.mappingClasses.containsKey(key)) {
                throw new IllegalArgumentException("key is already used: [key=" + key + ", className=" + className + "]");
            }
            this.mappingClasses.put(key, className);
            this.mappingReps.put(key, representative);
        } else {
            throw new IllegalArgumentException("all args are mandatory: [key=" + key + ", className=" + className + "]");
        }
    }

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        return getClassFromMapping(entityBeanKey, mappingReps, cacheReps);
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        final Class clazz = getClassFromMapping(entityBeanKey, mappingClasses, cacheClasses);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception exp) {
                LOG.error("Unable to create instance for key = {}", entityBeanKey);
                LOG.error(exp.getMessage(), exp);
            }
        }
        return null;
    }

    private Class getClassFromMapping(final String entityBeanKey,
                                      final Map<String, String> mapping,
                                      final Map<String, Class> cache) {

        if (cache.containsKey(entityBeanKey)) {
            return cache.get(entityBeanKey);
        }
        final String className = mapping.get(entityBeanKey);
        if (className != null) {
            try {
                final Class clazz = Class.forName(className);
                cache.put(entityBeanKey, clazz);
                return clazz;
            } catch (Exception exp) {
                LOG.error("Unable to create class for key = {}", entityBeanKey);
                LOG.error(exp.getMessage(), exp);
            }
        } else {
            LOG.error("No mapping for key = {}", entityBeanKey);
        }
        return null;
    }

    /** {@inheritDoc} */
    public void releaseResources()     mappingClasses.clear();
        mappingReps.clear();
        cacheClasses.clear();
        cacheReps.clear();
    }
}
```



## ValueConverter

Value converter allows to port incompatible types for simple fields.

There are two types of converter:

* DTO field converter that uses values retrieved from the entity getter
* DTO virtual field converter that uses the whole entity as value

Signature | Since | Purpose
--- | --- | --- 
Object convertToDto(Object object, BeanFactory beanFactory) |1.0.0 |Invoked when entity data is transferred to DTO. The object is the value to convert. Return value is the value that will be written to DTO. NOTE: for virtual fields object is the whole entity.
Object convertToEntity(Object object, Object oldEntity, BeanFactory beanFactory) |1.0.0 |Invoked when DTO data is transferred to entity. The object is the value from DTO property, oldEntity is the entity that is being updated. NOTE: for virtual fields the updates should be done inside the convertToEntity method right on the oldEntity since the return value is disregarded.

> Both methods contain bean factory reference supplied in the assembleDto/assembleEntity method and can be used for IoC. It is perfectly acceptable to call DTOAssembler.newAssembler() inside the methods to provide customised conversions

### Two way field converter

**Example Enum to String converter**


```java
@Dto
public class DtoClass {

 /**
  * Test enum for conversion testing.
  */
 public enum Decision {
  /** true. */
  Decided,
  /** false. */
  Undecided
 };

 @DtoField(value = "decision", converter = "boolToEnum")
 private Decision myEnum;

 /**
  * @return enum (converted from boolean).
  */
 public Decision getMyEnum() {
  return myEnum;
 }
 /**
  * @param myEnum enum (converted from boolean).
  */
 public void setMyEnum(final Decision myEnum) {
  this.myEnum = myEnum;
 }



}

...

public class EntityClass {

 private boolean decision;

 /**
  * @return boolean value.
  */
 public boolean getDecision() {
  return decision;
 }

 /**
  * @param decision boolean value.
  */
 public void setDecision(final boolean decision) {
  this.decision = decision;
 }
}

...

public class Converter implements ValueConverter {

 /** {@inheritDoc} */
 public Object convertToDto(final Object object, final BeanFactory beanFactory) {
  final Boolean value = (Boolean) object;
  if (value != null && value) {
   return Decision.Decided;
  }
  return Decision.Undecided;
 }

 /** {@inheritDoc} */
 public Object convertToEntity(final Object object, final Object oldValue, final BeanFactory beanFactory)
                final Decision value = (Decision) object;
  return (value != null && Decision.Decided.equals(value));
 }

}
```


### Two way virtual field converter

**Example virtual field method invoking converter**


```java
@Dto
public class DtoClass {

 @DtoVirtualField(converter = "VirtualMyBoolean")
 private Boolean myBoolean;

 /**
  * @return boolean value.
  */
 public Boolean getMyBoolean() {
  return myBoolean;
 }
 /**
  * @param myBoolean boolean value.
  */
 public void setMyBoolean(final Boolean myBoolean) {
  this.myBoolean = myBoolean;
 }
}

...

public class EntityClass {

 private boolean decided;

 /**
  * @return result of a complex decision.
  */
 public Boolean whatWasComplexDecision() {
                ... some complex logic ...
  return Boolean.TRUE;
 }

 /**
  * @param dtoValue value from dto object.
  */
 public void makeComplexDecision(final Boolean dtoValue) {
  this.decided = dtoValue;
 }

}

...

public class VirtualBooleanConverter implements ValueConverter {

 /** {@inheritDoc} */
 public Object convertToDto(final Object object, final BeanFactory beanFactory) {
  final EntityClass entity = (EntityClass) object;
  return entity.whatWasComplexDecision();
 }

 /** {@inheritDoc} */
 public Object convertToEntity(final Object object, final Object oldEntity,
   final BeanFactory beanFactory) {
  final EntityClass entity = (EntityClass) oldEntity;
  entity.makeComplexDecision((Boolean) object);
  return entity;
 }

}

```


### One way virtual field converter

> Above converters are two way, meaning that they convert to DTO and from DTO to entity. However sometimes it is not possible to convert back to entity. E.g. total sum field. In this case the field should be marked readOnly and convertToEntity method can be left unimplemented.

**Example virtual field method invoking converter**


```java
@Dto
public class MyDtoClassReadOnly {
  @DtoVirtualField(converter = "Sum", readOnly= true)
  private String sum;
  ...getters/setters...
}

public class MyEntityClass {

  private int val1;
  private int val2;
  private int val3;
  ...getters/setters...
}

...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("Sum", new ValueConverter() {
   public Object convertToDto(final Object object, final BeanFactory beanFactory) {
      final MyEntityClass entity = (MyEntityClass) object;
      return entity.getVal1() + entity.getVal2() + entity.getVal3();
   }

   public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
      throws new UnsupportedOperationException("since we cannot split the sum back to separate values");
   }

});

```



## EntityRetriever
Entity retriever provides GeDA with access to the DAO layer to lookup parent entity objects for DTO parent fields.

Signature | Since | Purpose
--- | --- | --- 
Object retrieveByPrimaryKey(Class entityInterface, Class entityClass, Object primaryKey) |1.0.0 |**entityInterface** interface as specified by the BeanFactory.getClazz(), **entityClass** actual entity class, **primaryKey** as specified by dto parent value property.

### Hibernate entity retriever

**Example simplest entity retriever**


```java
@Dto
public class MyDtoClass {
  @DtoParent("subField1", retriever = "MySubDtoClass_retriever")
  @DtoField(dtoBeanKey = "MySubDtoClass_key", entityBeanKeys = "MySubEntityClass_key")
  private MySubDtoClass field1;
  ...getters/setters...
}

@Dto
public class MySubDtoClass {
  @DtoField
  private String subField1;
  ...getters/setters...
}


public class MyEntityClass {
  private String field1;
  ...getters/setters...
}

public class MySubEntityClass {
  private String subField1;
  ...getters/setters...
}

...

final BeanFactory bf = ...;
final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("MySubDtoClass_retriever", new EntityRetriever() {
   // example of using hibernate to facilitate entity retrieval
   public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
      Session session = getSessionFactory().openSession();
      Transaction tx = session.beginTransaction();
      final Object parent = session.get(entityClass, primaryKey);
      tx.commit();
      session.close();
      return parent;
   }
   Session getSessionFactory() {
      return ...
   }
});

...

asm.assembleDto(dto, entity, adapters, bf);
...
asm.assembleEntity(dto, entity, adapters, bf);

```



## DtoToEntityMatcher

Dto to entity matchers enable GeDA reliably synchronise collection and map elements during entity update.

There are three types of matchers

* Matching DTO to entity elements used by collection mapping and entry value map mapping
* Matching by primitive keys used by default map mapping
* Matching primitive to entity used by dto map to entity collection mapping

The synchronisation process occurs in two stages:

1. Scan all entity collection and determine which entity elements are no longer in DTO collection and remove them
1. Scan the DTO collection and if an element matches update it, otherwise create a new entity element and add it

Signature | Since | Purpose
--- | --- | --- 
boolean match(DTO dto, Entity entity) |1.0.0 |**dto** is the value coming from the DTO object which is either a primitive or a dto element. **entity** is the value coming from entity object which is either a primitive or an entity element.

### Collections matcher

Collection matcher matches DTO to entity element to determine the match and hence update the correct one.

**Example collection matcher**


```java
@Dto
public class MyDtoClass {
  @DtoCollection(dtoToEntityMatcher = MyDtoElClassToMyEntityElClassMatcher.class,
                 entityGenericType = MyEntityElClass.class,
                 dtoBeanKey = "MyDtoElClass_key", entityBeanKeys = "MyEntityElClass_key")
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}

// Simple by ID matching assuming MyDtoElClass and MyEntityElClass have getId() method
public class MyDtoElClassToMyEntityElClassMatcher implements DtoToEntityMatcher<MyDtoElClass, MyEntityElClass> {
   public boolean match(final MyDtoElClass dto, final MyEntityElClass entity) {
      return dto.getId().equals(entity.getId());
   }
}
```


### Map matcher by key

This matcher applies to default mapping of maps when both DTO and entity fields are maps with primitive keys and entity/DTOs for values in the map entry.

**Example map keys matcher**


```java
@Dto
public class MyDtoClass {
  @DtoMap(dtoToEntityMatcher = MapKeysMatcher.class,
          entityGenericType = MyEntityElClass.class,
          dtoBeanKey = "MyDtoElClass_key", entityBeanKeys = "MyEntityElClass_key")
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}

// Simple by ID matching key to key
public class MapKeysMatcher implements DtoToEntityMatcher<String, String> {
   public boolean match(final String dtoMapKey, final String entityMapKey) {
      return dtoMapKey.equals(entityMapKey);
   }
}
```


### Map matcher by value

This matcher applies to inverse mapping of maps when both DTO and entity fields are maps with entity/DTOs for keys and primitive values in the map entry.

**Example map value matcher**


```java
@Dto
public class MyDtoClass {
  @DtoMap(dtoToEntityMatcher = MyDtoElClassToMyEntityElClassMatcher.class, useEntityMapKey = true,
          entityGenericType = MyEntityElClass.class,
          dtoBeanKey = "MyDtoElClass_key", entityBeanKeys = "MyEntityElClass_key")
  private Map<MyDtoElClass, String> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<MyEntityElClass, String> map1;
  ...getters/setters...
}

// Dto to entity matching assuming both have getId() method
public class MyDtoElClassToMyEntityElClassMatcher implements DtoToEntityMatcher<String, MyEntityElClass> {
   public boolean match(final MyDtoElClass dto, final MyEntityElClass entity) {
      return dto.getId().equals(entity.getId());
   }
}
```


### Map matcher key to collection element

This matcher applies to mapping of map on DTO to a collection on entity fields.

**Example map key to collection element matcher**


```java
@Dto
public class MyDtoClass {
  // Assuming MyEntityElClass has field "String id"
  @DtoMap(dtoToEntityMatcher = StringKeyToMyEntityElClassMatcher.class,
          entityMapCollectionClass = ArrayList.class, entityCollectionMapKey = "id",
          entityGenericType = MyEntityElClass.class,
          dtoBeanKey = "MyDtoElClass_key", entityBeanKeys = "MyEntityElClass_key")
  private Map<String, MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}

// Simple by ID matching assuming MyDtoElClass and MyEntityElClass have getId() method
public class StringKeyToMyEntityElClassMatcher implements DtoToEntityMatcher<String, MyEntityElClass> {
   public boolean match(final String dtoMapKey, final MyEntityElClass entity) {
      return dtoMapKey.equals(entity.getId());
   }
```

