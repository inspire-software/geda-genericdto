# DSL

## Basics

The Domain specific language (DSL) API is represented by the following classes:

Name | Since | Purpose 
--- | --- | ---
Registry|3.0.0 |Container for DtoContext objects that allows to register and configure them and later retrieve them. Each registry holds its own context and hence provide a unique collection of mappings. Assemblers that use DSL registries are specific to them i.e. they only able to access mappings provided by the contexts of registry they are bound to. 
Registries|3.0.0|Factory for creating Registry instances. 
DtoContext|3.0.0|Reference to a DTO class context and its mappings to entity(ies). These mappings are held in DtoEntityContext. 
DtoEntityContext|3.0.0|A mapping context for specific DTO entity pair. Holds field contexts for this pair. 
DtoFieldContext|3.0.0|A basic unit of mapping that defines bond between DTO and entity property. 
DtoParentContext|3.0.0|Additional sub context of DtoFieldContext that defines Domain level relationship on the entity thus introducing special conditions to its update 
DtoVirtualFieldContext|3.0.0|Additional sub context of DtoFieldContext that instructs GeDA that there is no corresponding field on the entity object (i.e. this DTO field is computed or derived from overall entity state) 
DtoCollectionContext|3.0.0|The basic algorithm that GeDA follows is copy by reference. Collections are special since a new instance of the collection object must be created. To do this you need to to use DtoCollectionContext rather than DtoFieldContext.DtoCollectionContext also enables full automatic synchronisation upon writing to entity with help of DtoToEntityMatcher from [[Adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API. 
DtoMapContext|3.0.0|Similar to collections maps need new container instance and hence require this context. In addition to full synchronisations DtoMapContext can be configured to a collection on entity or a map with DTO being either key or value. 

## Registry

In a nutshell registries hold mappings between DTO and Entity classes/interfaces. There is no limit on how many registries you can have. Registries **DO NOT** share mappings and assembler instances are bound to a single registry.

**Mapping visibility**

```java
public class MyDtoClass {

   private String field1;
   private String field2;
   ... getters/setters ...
}
...
public class MyEntityClass {

   private String field1;
   private String field2;
   ... getters/setters ...
}
...

// Create reg1 with single DTO class context for generic entity (wildcard for all
// entity objects) with a single field (field1)
final Registry reg1 = Registries.registry();
reg1.dto(MyDtoClass.class).forEntityGeneric().withField("field1");

// Create reg1 with single DTO class context for generic entity (wildcard for all
// entity objects) with a single field (field2)
final Registry reg2 = Registries.registry();
reg1.dto(MyDtoClass.class).forEntityGeneric().withField("field2");

final MyEntityClass entity = ...;

final Assembler asm1 = DTOAssembler.newAssembler(MyDtoClass.class, MyEntityClass.class, reg1);
final MyDtoClass dto1 = new MyDtoClass();
// This assembler only transfers data from field1
asm1.assembleDto(dto1, entity, null, null);

final Assembler asm2 = DTOAssembler.newAssembler(MyDtoClass.class, MyEntityClass.class, reg2);
final MyDtoClass dto2 = new MyDtoClass();
// This assembler only transfers data from field2
asm1.assembleDto(dto2, entity, null, null);
```

Registry provides a continuos flow close to natural English - hence DSL. This enables building complex contexts by just chaining methods. Each method leads to a context that will expose methods only applicable for its configuration.

Below is an example from the examples module in code base. Please do not try to understand it - the purpose of this code sample is to show the basics of how one would register contexts in a registry.

**Creating complex registries example from the examples module**

```java
// Bean factory has no default implementation, see Adapters API for more information
final ExtensibleBeanFactory bf = ...;
final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = Registries.registry(bf);

// map entities for IoC
bf.registerEntity("myEntityField3Entity", MyEntityField3Class.class.getCanonicalName(), MyEntityField3Class.class.getCanonicalName());
bf.registerEntity("field4ParentEntity", MyEntityField4Class.class.getCanonicalName(), MyEntityField4Class.class.getCanonicalName());
bf.registerEntity("field2", MyEntityField2Class.class.getCanonicalName(), MyEntityField2Class.class.getCanonicalName());

registry
        .dto(MyDtoField3Class.class).alias("myDtoField3Dto").forEntity("myEntityField3Entity")
        .withField("subField1")
;

registry
        .dto(MyDtoField4Class.class).alias("field4ParentDto").forEntity("field4ParentEntity")
        .withField("id")
        .and().withField("subField1")
;

registry
        // main mapping
        .dto(MyDtoClass.class).alias("myDto").forEntity(MyEntityClass.class).alias("myEntity", MyEntity.class)
        // field 1
        .withField("field1").forField("field1")
        .readOnly()
        .converter("field1Converter")
                // field 2
        .and()
        .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
        // field 3
        .and()
        .withField("field3")
        .dtoBeanKey("myDtoField3Dto")
        .entityBeanKeys("myEntityField3Entity")
                // field 4
        .and()
        .withField("field4parent")
        .dtoBeanKey("field4ParentDto")
        .entityBeanKeys("field4ParentEntity")
        .dtoParent("id")
        .retriever("parentFieldEntityById")
                // field 5
        .and()
        .withField("field5virtual").forVirtual()
        .converter("field5VirtualConverter")
                // field 6
        .and()
        .withCollection("field6").forField("field6")
        .dtoBeanKey("field6CollectionDtoItem")
        .entityBeanKeys("field6CollectionEntityItem")
        .dtoToEntityMatcherKey("field6CollectionMatcher")
                // field 7
        .and()
        .withMap("field7").forField("field7")
        .dtoBeanKey("field7MapDtoItem")
        .entityBeanKeys("field7MapEntityItem")
        .dtoToEntityMatcherKey("field7MapMatcher")
;

```

The registry also allows reusing DtoContexts to save some time on duplicate mappings. For example if you have defined a specific DTO entity pair (i.e. not forEntityGeneric() but for a class) and would like to apply this to say a Map<String, Object> to load your data there you can. Below is sample code from examples module that shows how context from MyDtoClass-MyEntity can be reused for MyDtoClass-Map<String, Object>.

**Reusing already defined DtoEntityContext for other classes**

```java
final ExtensibleBeanFactory bf = ...;

final com.inspiresoftware.lib.dto.geda.dsl.Registry registry = Registries.regisrty(bf);

registry
        // main mapping
        .dto(MyDtoClass.class).forEntity(MyEntity.class)
        // field 1
        .withField("field1").forField("field1")
        .readOnly()
        .converter("field1Converter")
                // field 2
        .and()
        .withField("field2").forField("field2.subField1").entityBeanKeys("field2")
        // field 5
        .and()
        .withField("field5virtual").forVirtual()
        .converter("field5VirtualConverter")

;


registry.dto("myDto").useContextFor(
        registry
                // main mapping
                .dto("myDto").forEntity(MyEntity.class),
        Map.class
);

```

The above code will produce two different contexts for MyDtoClass. One for MyEntity and one for Map. When DTOAssembler.newAssembler(Class, Class, registry) API will be used the assembler will be created for the best match.

> It is highly recommended to have forEntityGeneric() context as this is a "catch all" for all types of entities. Once this is defined you can create other DTO-entity class specific contexts.

```java
final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
// define catch all
reg.dto(MyDtoClass.class).forEntityGeneric().
     .withField("id").and().withField("name");
// define specific MyDtoClass-Map pair that takes precedence over generic
reg.dto(MyDtoClass.class).forEntity(Map.class). ...
     .withField("id").readOnly().and().withField("name").readOnly();
```

## Registries

Registries is a simple factory that provides static methods:

Name | Since | Purpose 
--- | --- | ---
Registry registry() |3.0.0|Creates a default registry instance with no BeanFactory support - which means that you cannot reference classes by keys, nor you can registers keys. This method is only applicable for registries with simple field mappings. 
Registry registry(ExtensibleBeanFactory beanFactory) |3.0.0|Creates default registry with bean factory support that permits all IoC features in contexts 
BeanFactory beanFactory(Registry registry) |3.0.0|Convenience method to extract bean factory from registry if there is one available 

So what does it mean IoC is not supported in registry(). In simple terms the code below with throw exception as the class for given key cannot be looked up:

**No IoC if no bean factory specified**

```java
// Fails because registry needs bf to look up class for MyDtoClass_key
Registries.registry().dto("MyDtoClass_key");

// This also fails since alias() needs bf to registerDto()
Registries.regisrty().dto(MyDtoClass.class).alias("MyDtoClass_key");
```


## DtoContext

DtoContext represents mapping context of a DTO class. This context leads to one or more DtoEntityContext which is a specific pair.

It provides the following API:

Name | Since | Purpose 
--- | --- | ---
DtoContext alias(String beanKey)|3.0.0|(IoC) Allows to register DTO implementation in bean factory with specified key. 
DtoEntityContext forEntity(Class entityClass)|3.0.0|Create or access existing DTO-entity context 
DtoEntityContext forEntity(Object entityInstance)|3.0.0|Create or access existing DTO-entity context by using entityInstance.getClass() 
DtoEntityContext forEntity(String beanKey)|3.0.0|Create or access existing DTO-entity context by key. Requires bean factory. 
DtoEntityContext forEntityGeneric()|3.0.0|Create DTO generic mapping (no specific restriction on the entity class) 
DtoEntityContext has(Class entityClass)|3.0.0|Check if DTO-entity context exists 
DtoEntityContext useContextFor(DtoEntityContext ctx, Class entityClass)|3.0.0|Create a copy of this context and assign given entityClass as target entity 
DtoEntityContext useContextFor(DtoEntityContext ctx, String beanKey)|3.0.0|Create a copy of this context and assign given entityClass as target entity. Requires bean factory 

### DtoContext.alias(String beanKey)

Setting aliases allows to register DTO implementations in extensible bean factory bound to this registry. This is especially useful when DTO defined is a parent or a map/collection element elsewhere in your mapping as you can simply refer to it by alias rather than using Class.

**Example use of alias**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}

public class MySuperDtoClass {
   private MyDtoClass inner;
   private String reference;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}

public class MySuperEntityClass {
   private MyEntityClass inner;
   private String reference;
   ... getters/setters ...
}

...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
// same alias can be used for dtoBeanKey in field mappings
reg.dto(MyDtoClass.class).alias("MyDtoClass_key").forEntityGeneric()
     .withField("id").and().withField("name");
reg.dto(MySuperDtoClass.class).forEntityGeneric()
     .withField("reference").and().withField("inner").dtoBeanKey("MyDtoClass_key");

...
final MySuperEntityClass entity = ...;
final MySuperDtoClass dto = new MySuperDtoClass();
DTOAssembler.newAssembler(MySuperDtoClass.class, MySuperEntityClass.class, reg).assembleDto(dto, entity, null, bf);
...

```


### DtoContext.forEntity(Class entityClass)

Creates context for specific DTO-entity pair. When assembler instance is constructed it will only match class pair for DTO class and Entity class specified or its sub classes.

> Use DtoContext.forEntityGeneric() if you do not want such strict bond to entity type.

**Example use of forEntity**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class)
     .withField("id").and().withField("name");
...
final MyEntityClass entity = ...;
final MyDtoClass dto = new MyDtoClass();
DTOAssembler.newAssembler(MyDtoClass.class, MyEntityClass.class, reg).assembleDto(dto, entity, null, bf);
...
```


### DtoContext.forEntity(Object entityInstance)

This method is a variation of DtoEntityContext forEntity(Class entityClass) which is a convenience method for runtime context registration, when the registry is populated as new instances come in.

**Example use of forEntity**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...

final MyEntityClass entity = ...;

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
reg.dto(MyDtoClass.class).forEntity(entity)
     .withField("id").and().withField("name");
...
final MyDtoClass dto = new MyDtoClass();
DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), reg).assembleDto(dto, entity, null, bf);
...
```


### DtoContext.forEntity(String beanKey)

This method is an IoC version of DtoEntityContext forEntity(Class entityClass) which uses bean factory to identify the class implementation. Requires bean factory bound to DSL registry.

**Example use of forEntity**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...

final MyEntityClass entity = ...;

final ExtensibleBeanFactory bf = new ExtensibleBeanFactory() {
...
   public Class getClazz(String key) {
      if ("MyEntityClass_key".equals(key)) {
         return MyEntityClass.class;
      }
      ...
   }
}
...
};
final Registry reg = Registries.regisrty(bf);
reg.dto(MyDtoClass.class).forEntity("MyEntityClass_key")
     .withField("id").and().withField("name");
...
final MyDtoClass dto = new MyDtoClass();
DTOAssembler.newAssembler(MyDtoClass.class, entity.getClass(), reg).assembleDto(dto, entity, null, bf);
...
```


### DtoContext.forEntityGeneric()

This method is a generic bond of DTO to any entity that is passed to the assembler. This is the recommended way to map all classes. If you have variations in mapping (e.g. some entity types require additional converters or have readOnly fields) then you can add more entity class specific contexts. However if those specific DtoEntityContext's do not match to what contexts are in the registry then a generic context is used.

**Example use of forEntityGeneric**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;

   public MyEntityClass(Long id, String name) {
      ...
   }

   ... getters/setters ...

}
...
final MyEntityClass entityObj = new MyEntityClass(1L, "object");
final Map<String, Object> entityMap = new HashMap<String, Object>() {{
   put("id", 2L);
   put("name", "map");
}};
final List<Object> entityList = new ArrayList();
entityList.add("id");
entityList.add(3L);
entityList.add("name");
entityList.add("list");
...
final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
reg.dto(MyDtoClass.class).forEntityGeneric()
     .withField("id").and().withField("name");
...
final MyDtoClass dto = new MyDtoClass();
DTOAssembler.newAssembler(MyDtoClass.class, entityObj.getClass(), reg).assembleDto(dto, entityObj, null, bf);
DTOAssembler.newAssembler(MyDtoClass.class, entityMap.getClass(), reg).assembleDto(dto, entityMap, null, bf);
DTOAssembler.newAssembler(MyDtoClass.class, entityList.getClass(), reg).assembleDto(dto, entityList, null, bf);
...
```


### DtoContext.has(Class entityClass)

Has method allows to check if a particular DtoEntityContext already exists in the registry.

**Example use of has check**

```java
final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
// generic read only access to properties
reg.dto(MyDtoClass.class).forEntityGeneric()
     .withField("id").readOnly().and().withField("name").readOnly();
// full access so the entity is writable
reg.dto(MyDtoClass.class).forEntity("MyEntityClass_key")
  .withField("id").and().withField("name");

// check generic mapping exists will return true
reg.dto(MyDtoClass.class).has(Object.class);
// check specific mapping exists will return true
reg.dto(MyDtoClass.class).has(MyEntityClass.class);

// check non mapped will return false
reg.dto(MyDtoClass.class).has(SomeOtherEntity.class);
```


### DtoContext.useContextFor(DtoEntityContext ctx, Class entityClass)

Some of the DtoEntityContext can become quite verbose and to reduce code duplication DtoContext provides means to re-purpose mappings that you have specified for one DTO-entity pair to use with another entity by creating a copy.

**Example use of useContextFor**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
// create generic mapping for read only id
reg.dto(MyDtoClass.class).forEntityGeneric()
     .withField("id").readOnly();
// copy the id field mapping from generic and add name field mapping
reg.dto(MyDtoClass.class).useContextFor(reg.dto(MyDtoClass.class).forEntityGeneric(), MyEntityClass.class)
  .withField("name");
```

> useContextFor invocation produces a **copy** of context and therefore any modifications to the original will not have effect on the copy.

### DtoContext.useContextFor(DtoEntityContext ctx, String beanKey)

This is an IoC version of DtoEntityContext useContextFor(DtoEntityContext ctx, Class entityClass) that uses bean factory to look up the target entity class.
Some of the DtoEntityContext can become quite verbose and to reduce code duplication DtoContext provides means to re-purpose mappings that you have specified for one DTO-entity pair to use with another entity by creating a copy.

**Example use of useContextFor**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
final ExtensibleBeanFactory bf = ...;
bf.registerEntity("MyEntityClass_key", MyEntityClass.class.getCannonicalName(), MyEntityClass.class.getCannonicalName());

final Registry reg = Registries.regisrty(bf);
// create generic mapping for read only id
reg.dto(MyDtoClass.class).forEntityGeneric()
     .withField("id").readOnly();
// copy the id field mapping from generic and add name field mapping
reg.dto(MyDtoClass.class).useContextFor(reg.dto(MyDtoClass.class).forEntityGeneric(), "MyEntityClass_key")
  .withField("name");
```

> useContextFor invocation produces a **copy** of context and therefore any modifications to the original will not have effect on the copy.

## DtoEntityContext

Dto entity context defines a specific DTO-entity pair and allows to access the actual mapping API through .with methods. GeDA distinguishes three types of fields:

* Simple field that holds a single primitive, object or a sub DTO (primitives and objects are copied **by reference**)
* Collection field that holds a collection of entity objects
* Map field that holds a collection of entity objects

The special thing about collection and maps is that their elements need to be synchronised. i.e. client application that uses DTO with a collection or a map may add new items, remove old items and update existing ones. All this is built into GeDA but requires some extra configuration.

The dto entity context provides the following API:

Name | Since | Purpose 
--- | --- | ---
DtoEntityContext alias(String beanKey, Class representative)|3.0.0|IoC support to register entity implementation in bean factory. Requires bean factory bound to DSL registry. 
DtoFieldContext withField(String fieldName)|3.0.0|Field name on the DTO object that participates in data transfer. Can be a primitive, object or a sub DTO. primitive and objects will be copied by reference. Only single field names immediate on DTO class are applicable. 
DtoEntityContextAppender withFieldsSameAsIn(String beanKey, String ... excluding)|3.1.0 |Add all DTO fields mappings on DTO object that are same as on entity class. The fields are regarded same if their name and type are the same. If field name or type is different then those fields are ignored. If beanKey reference a class then all fields of this class (including super classes fields will be scanned). For beanKey that references an interface only getters on that interface will be counted towards fields mapping. There are no "extends" scanning for interfaces. 
DtoEntityContextAppender withFieldsSameAsIn(Class clazz, String ... excluding)|3.1.0 |Add all DTO fields mappings on DTO object that are same as on entity class. The fields are regarded same if their name and type are the same. If field name or type is different then those fields are ignored. If clazz is a class then all fields of this class (including super classes fields will be scanned). For clazz that is an interface only getters on that interface will be counted towards fields mapping. There are no "extends" scanning for interfaces. 
DtoCollectionContext withCollection(String fieldName)|3.0.0|Collection field name on the DTO object that participates in data transfer. Only single field names immediate on DTO class are applicable. 
DtoMapContext withMap(String fieldName)|3.0.0|Map field name on the DTO object that participates in data transfer. Only single field names immediate on DTO class are applicable. 

### DtoEntityContext.alias(String beanKey, Class representative)

Setting aliases allows to register entity implementations in extensible bean factory bound to this registry. This is especially useful when Entities defined are used in other mappings as you can simply refer to it by alias rather than using Class.

**Example use of alias**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...
public class MyOtherDtoClass {
   private Long id;
   private String description;
   ... getters/setters ...
}

...
public class MyEntityClass {
   private Long id;
   private String name;
   private String description;
   ... getters/setters ...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);
// register alias once
reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class).alias("MyEntityClass_key", MyEntityClass.class.getCannonicalName())
     .withField("id").and().withField("name");
// use it thereafter (also same alias can be used for entityBeanKeys in field mappings)
reg.dto(MyOtherDtoClass.class).forEntity("MyEntityClass_key")
     .withField("id").and().withField("description");

...
final MyEntityClass entity = ...;
final MyDtoClass dto = new MyDtoClass();
DTOAssembler.newAssembler(dto.getClass(), entity.getClass(), reg).assembleDto(dto, entity, null, bf);
...
final MyOtherDtoClass otherDto = new MyOtherDtoClass();
DTOAssembler.newAssembler(otherDto.getClass(), entity.getClass(), reg).assembleDto(otherDto, entity, null, bf);

```


### DtoEntityContext.withField(String fieldName)

Field allows to map a single field on DTO to a field or field path on entity. Calling this method creates a DtoFieldContext for DTO a field.

**Example use of withField**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   ... getters/setters ...
}

...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

// register MyDtoClass with two fields "id" and "name" - generic way
reg.dto(MyDtoClass.class).forEntityGeneric()
     .withField("id").and().withField("name");

// register MyDtoClass with two fields "id" and "name" - specific class
reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class)
      .withField("id").and().withField("name");
```


### DtoEntityContext.withFieldsSameAsIn(String beanKey, String ... excluding)

This is a convenience method that allows to add default (or like for like) fields to DtoEntityContext. excluding parameter specifies the field that should be ignored. The fields are regarded same if their name and type are the same. If field name or type is different then those fields are ignored. If beanKey reference a class then all fields of this class (including super classes fields will be scanned). For beanKey that references an interface only getters on that interface will be counted towards fields mapping. There are no "extends" scanning for interfaces.

**Example use of withFieldsSameAsIn**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   private String description;
   ... getters/setters ...
}

...
public class MyEntityClass implements Nameable, Identifiable {
   private Long id;
   private String name;
   ... getters/setters ...
}

public interface Identifiable {
  public Long getId();
  public void setId(Long id)
}


public interface Nameable {
  public String getName();
  public void setName(String name)
}
...

final ExtensibleBeanFactory bf = ...;
bf.registerEntity("Identifiable_key", MyEntityClass.class..getCannonicalName(), Identifiable.class.getCannonicalName())
bf.registerEntity("Nameable_key", MyEntityClass.class..getCannonicalName(), Nameable.class.getCannonicalName())

final Registry reg = Registries.regisrty(bf);

// register MyDtoClass with two fields "id" and "name" - generic way
reg.dto(MyDtoClass.class).alias("MyDtoClass_key").forEntityGeneric()
 .withFieldsSameAsIn("MyDtoClass_key", "description");

// register MyDtoClass with two fields "id" and "name" - specific
reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class)
 .withFieldsSameAsIn("MyDtoClass_key", "description");

// register MyDtoClass with two fields "id" and "name" - specific by interfaces (same can be done with classes)
reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class)
  .withFieldsSameAsIn("Identifiable_key").and().withFieldsSameAsIn("Nameable_key");

```

> Above example with .withFieldsSameAsIn("Identifiable_key").and().withFieldsSameAsIn("Nameable_key") also applies to classes. It is possible to scan fields for many classes and/or interfaces. Each of these classes and interfaces can have one of more fields to map. Overlapping fields should be excluded or GeDA will throw duplicate mapping exception.

### DtoEntityContext.withFieldsSameAsIn(Class clazz, String ... excluding)

This is a convenience method that allows to add default (or like for like) fields to DtoEntityContext. excluding parameter specifies the field that should be ignored. The fields are regarded same if their name and type are the same. If field name or type is different then those fields are ignored. If clazz is a class then all fields of this class (including super classes fields will be scanned). For clazz that is an interface only getters on that interface will be counted towards fields mapping. There are no "extends" scanning for interfaces.

**Example use of withFieldsSameAsIn**

```java
public class MyDtoClass {
   private Long id;
   private String name;
   private String description;
   ... getters/setters ...
}

...
public class MyEntityClass {
   private Long id;
   private String name;
   ... getters/setters ...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

// register MyDtoClass with two fields "id" and "name" - generic way
reg.dto(MyDtoClass.class).forEntityGeneric()
 .withFieldsSameAsIn(MyEntityClass.class);

// register MyDtoClass with two fields "id" and "name" - specific
reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class)
 .withFieldsSameAsIn(MyEntityClass.class);

```

> reg.dto(MyDtoClass.class).forEntity(MyEntityClass.class).withFieldsSameAsIn(MyEntityClass.class) seems like a bit of duplication but it is only so for a very specific case of mapping specific entity with all fields. When using the recommended forEntityGeneric() this parameter is a necessity

### DtoEntityContext.withCollection(String fieldName)

With collection follows the same principle as DtoFieldContext withField(String fieldName) but maps a collection field with a slightly richer DtoCollectionContext that allows to synchronise collection elements upon entity writes.

**Example use of withCollection**

```java
public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}
...
public class MyEntityClass {
   private Collection<MyEntityElClass> collection1;
   ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

// assuming that mapping for "MyDtoElClass_key" has been already registered
reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").dtoBeanKey("MyDtoElClass_key").readOnly();

```


### DtoEntityContext.withMap(String fieldName)

With collection follows the same principle as DtoFieldContext withField(String fieldName) but maps a map field with a slightly richer DtoMapContext that allows to synchronise map elements upon entity writes.

**Example use of withCollection**

```java
public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}
...
public class MyEntityClass {
   private Map<String, MyEntityElClass> map1;
   ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

// assuming that mapping for "MyDtoElClass_key" has been already registered
reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").dtoBeanKey("MyDtoElClass_key").readOnly();

```


## DtoFieldContext

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
forField(String fieldName)|String | |same as dto field name |3.0.0|Field name on entity class that will be bound to this dto field (use "dot" notation for graph path e.g. myField.mySubfield). 
readOnly()| | |false |3.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 
converter(String converter)|String | | |3.0.0|Reference key to a converter to use when assembling DTO's and Entities. This reference is used to lookup converter in adapters map passed into assembleDto and assembleEntity methods.
This converter must implement ValueConverter adapter interface. Requires adapters parameter during assembly. 
dtoBeanKey(String dtoBeanKey)|String | | |3.0.0|Specifying this value tells assembler that a "sub" assembler is needed to create sub DTO for this field. New DTO instance will be created using bean factory with this key. Requires bean factory parameter during DTO assembly. 
entityBeanKeys(String... entityBeanKeys)|String[] |Yes if "dot" notation is used | |3.0.0|This annotation is mandatory for DtoFieldContext.forField with deeply nested entity object i.e. when a "dot" syntax is used. When assembling entity if null is encountered on object path bean factory will be used with specified key to create new instances of inner entity objects. Requires bean factory parameter during Entity assembly. 
forVirtual()| | | |3.0.0|Enables virtual field context for this field. Virtual fields do not require a specific property on the entity. They are derived or computed through value converter from adapter API. 
dtoParent(String primaryKeyFieldName)| | | |3.0.0|Enables dto parent field context for this field. Parent fields support ORM layer entity relationships. In many cases it is desirable to "change" parent relation rather than update it with new data from DTO. Dto parent context enables use of EntityRetriever from adapter API to look up the relationships. 

### DtoFieldContext.forField(String fieldName)

Field name is used to specify name of the property on entity object. By default (if this method is not called) assembler uses the same field name as on the DTO. However is is acceptable to have different name on the entity or even use value from inner entity.

**Example default configuration**

```java
public class MyDtoClass {
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String field1;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("field1");

```

**Example with different name on entity**

```java
  public class MyDtoClass {
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String entityField1;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("field1").forField("entityField1");

```

If you specify "dot" notation to flatten the entity (i.e. make some sub entity fields as fields on top level DTO) you either need to specify readOnly = true or entityBeanKeys.

> If your entity only has a getter (i.e. immutable implementations) use DtoFieldContext.readOnly(). This will prevent attempt to access setter of the property

**Example with object path read only**

```java
public class MyDtoClassReadOnly {
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String inner;
  ...getters/setters...
}
public class MySubEntityClass {
  private String entityField1;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClassReadOnly.class).forEntityGeneric()
 .withMap("field1").forField("inner.entityField1").readOnly();

```

entityBeanKeys is an array of String each element of which should correspond to path specified in value. E.g. "MySubEntityClass_key" corresponds to "inner" path of field1 mapping.

**Example with object path with bean key**

```java
public class MyDtoClass {
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String inner;
  ...getters/setters...
}
public class MySubEntityClass {
  private String entityField1;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("field1").forField("inner.entityField1").entityBeanKeys("MySubEntityClass_key");


```

> See adapters API for bean factory implementation examples

### DtoFieldContext.readOnly()

Read only allows to control the flow of data to the entity. It is sometimes undesirable to write the value straight to entity due to security or data integrity issues. Invoking DtoFieldContext.readOnly() ensures that DTO get loaded with the data but the data is not written back to the entity.

Setting DtoFieldContext.readOnly() also improves performance as GeDA uses short circuit switches to exit the process and not to create unnecessary auto generated classes.

**Example with non updatable entity ID**

```java
public class MyDtoClass {
  private String id;
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String id;
  private String field1;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withField("id").readOnly().and().withField("field1");

```


### DtoFieldContext.converter(String converter)

Not all fields are easily convertable. For example enum on entity may need to be String on DTO and therefore a conversion of the values needs to take place before they are written to DTO or Entity property. Converters allow to do exactly that. Converter property defines key for converter object that is passed in a Map of adapters to assembleDto and assembleEntity. Converter instance references must be of type ValueConverter (see [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API).

**Example of using converter**

```java

public class MyDtoClass {
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {

  public enum Field1 { ONE, TWO, THREE };

  private Field1 inner;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withField("field1").converter("EnumToString");

...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("EnumToString",  new ValueConverter() {
   // converter receives the field value as the object
   public Object convertToDto(final Object object, final BeanFactory beanFactory) {
      final Field1 field1 = (Field1) object;
      return field1 != null ? field1.name() : null;
   }

   // converter receives string from DTO that we adapt and return enum from the method
   public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
      if (object instanceof String) {
         return MyEntityClass.Field1.valueOf((String) object);
      }
      return null;
   }

});
...

asm.assembleDto(dto, entity, adapters, null);
...
asm.assembleEntity(dto, entity, adapters, null);

```

> See adapters API for value converter implementation examples

### DtoFieldContext.dtoBeanKey(String dtoBeanKey)

The basic mapping assumes "copy by reference" and hence to tell assembler that field is an inner DTO object dtoBeanKey is used. This key allows creation of inner DTO object using bean factory and later automatically create sub assembler for that object.

**Example inner DTO field**

```java
public class MyDtoClass {
  private MySubDtoClass field1;
  ...getters/setters...
}

public class MySubDtoClass {
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

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MySubDtoClass.class).alias("MySubDtoClass_key").forEntityGeneric()
 .withField("subField1");
reg.dto(MyDtoClass.class).forEntityGeneric()
 .withField("field1").dtoBeanKey("MySubDtoClass_key");

```

Above example possibly would also need either entityBeanKeys to specify key for MySubEntityClass or MyDtoClass.field1 should be marked as readOnly. However it may be the case that none of these are necessary if the domain model guarantees that MyEntityClass.field1 is always set to a value, in which case this may be a hint for necessity to use DtoFieldContext.dtoParent().

> See adapters API for bean factory implementation examples

### DtoFieldContext.entityBeanKeys(String... entityBeanKeys)

Entity bean keys serve the same purpose as dtoBeanKey, however since it is possible to use "dot" notation to specify object path this property is an array of String. The principle is the same however. The key is used with bean factory to create new object instances

**Example with object path with bean key**

```java
public class MyDtoClass {
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String inner;
  ...getters/setters...
}
public class MySubEntityClass {
  private String entityField1;
  ...getters/setters...
}
...

final ExtensibleBeanFactory bf = ...;
bf.registerEntity("MySubEntityClass_key", MySubEntityClass.class.getCannonicalName(), MySubEntityClass.class.getCannonicalName());

final Registry reg = Registries.regisrty(bf);

reg.dto(MySubDtoClass.class).forEntityGeneric()
 .withField("field1").forField("inner.entityField1").entityBeanKeys("MySubEntityClass_key");

```

> See adapters API for bean factory implementation examples

### DtoFieldContext.forVirtual()

Virtual fields have no contra-part on entities. This is sometimes desirable when computed (e.g. calculating totals) or logically derived (e.g. set flags upon combination of values in fields) fields are needed on DTO. This extra computation is achieved through a converter.

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
converter(String converter)|String | Yes | |3.0.0|Reference key to a converter to use when assembling DTO's and Entities. This reference is used to lookup converter in adapters map passed into assembleDto and assembleEntity methods.
This converter must implement ValueConverter adapter interface. Requires adapters parameter during assembly. 
readOnly()|boolean | |false |3.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 

#### DtoVirtualFieldContext.converter(String converter)

Converter property defines key for converter object that is passed in a Map of adapters to assembleDto and assembleEntity. Converter instance references must be of type ValueConverter (see adapters API). Virtual fields allows to decouple additional business logic from the DTO itself since DTO in theory should only be data containers. For some virtual fields the transfer is only supported in one direction (to DTO) as for example sum calculations as we cannot decompose the sum back to individual addendum, however it is perfectly acceptable to have updates back to entity in the convertToEntity method of the converter.

**Example of using one way converter**

```java
public class MyDtoClassReadOnly {
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

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClassReadOnly.class).forEntityGeneric()
 .withField("sum").forVirtual().converter("Sum").readOnly();

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

...

asm.assembleDto(dto, entity, adapters, null);
...
asm.assembleEntity(dto, entity, adapters, null);

```

**Example of using two way converter**

```java
public class MyDtoClass {
  private boolean accountLocked;
  ...getters/setters...
}

public class MyEntityClass {

  private boolean lockedByAdmin;
  private boolean expired;
  ...getters/setters...
}

...

final ExtensibleBeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withField("accountLocked").forVirtual().converter("Locked");


...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("Locked", new ValueConverter() {
   public Object convertToDto(final Object object, final BeanFactory beanFactory) {
      // since there is no contra-part field the object is always the whole entity
      final MyEntityClass entity = (MyEntityClass) object;
      return entity.getLockedByAdmin() || new Date().after(entity.getExpired());
   }

   public Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory) {
      final MyEntityClass entity = (MyEntityClass) oldEntity;
      // You MUST perform updates inside the converter
      oldEntity.setLockedByAdmin(Boolean.valueOf(object))
      return null; // virtual converter value is discarded, so no need to return anything other than null
   }

});

...

asm.assembleDto(dto, entity, adapters, null);
...
asm.assembleEntity(dto, entity, adapters, null);

```


#### DtoVirtualFieldContext.readOnly()

Read only follows the same principle as for DtoFieldContext.readOnly() - setting this prevents entity writes.


### DtoVirtualContext.dtoParent(String primaryKeyFieldName)

Dto parent is an enhancement for DtoFieldContext to link GeDA to data access layer to support ORM better for relational domain models. The common use case is when we update parent entity relation we do not wish to overwrite old parent with new data but rather fetch a new parent from the RDBMs.

The primaryKeyFieldName should be an immediate property on the inner entity. This property value will be used as a primary key parameter for fetching the Entity upon write to entity operation. The retrieval is accomplished by EntityRetriever adapter.

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
retriever(String retriever)|String | Yes |retriever |3.0.0|Reference to an entity retriever to use when assembling Entities. 

#### DtoParentContext.retriever(String retriever)

Entity retriever is effectively a hook to the DAO layer to prevent assembler from updating the entity object. For example if we have an Address and Person entity and we want to assign the address to different person we will not expect to update the existing relationship with data but rather we want to fetch the other Person entity and set it to the Address.

**Example with parent field**

```java
public class MyDtoClass {
  private MySubDtoClass field1;
  ...getters/setters...
}

public class MySubDtoClass {
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
bf.registerEntity("MySubEntityClass_key")
final org.hibernate.Session hibernateSession = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MySubDtoClass.class).alias("MySubDtoClass_key").forEntityGeneric()
 .withField("subField1");
reg.dto(MyDtoClass.class).forEntityGeneric()
 .withField("field1").dtoBeanKey("MySubDtoClass_key").entityBeanKeys("MySubEntityClass_key")
    .dtoParent("subField1").retriever("MySubDtoClass_retriever");

...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("MySubDtoClass_retriever", new EntityRetriever() {
   // example of using hibernate to facilitate entity retrieval
   public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
      return hibernateSession.get(entityClass, primaryKey);
   }
});

...

asm.assembleDto(dto, entity, adapters, bf);
...
asm.assembleEntity(dto, entity, adapters, bf);


```

> See adapters API for entity retriever and bean factory implementation examples

DtoCollectionContext|The basic algorithm that GeDA follows is copy by reference. Collections are special since a new instance of the collection object must be created. To do this you need to to use DtoCollectionContext rather than DtoFieldContext.DtoCollectionContext also enables full automatic synchronisation upon writing to entity with help of DtoToEntityMatcher from Adapters API. 
DtoMapContext|Similar to collections maps need new container instance and hence require this context. In addition to full synchronisations DtoMapContext can be configured to a collection on entity or a map with DTO being either key or value. 

## DtoCollectionContext

Apart from defining collection field DtoCollectionContext also manages the synchronization of data when it is written back to the entity.

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
forField(String fieldName)|String | |same as dto field name |3.0.0|Field name on entity class that will be bound to this dto field (use "dot" notation for graph path e.g. myField.mySubfield). 
readOnly()|boolean | |false |3.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 
entityCollectionClass(Class<? extends Collection> entityCollectionClass)|Class | Yes |ArrayList |3.0.0|Class that defines the type of class for creating new Domain object collection. When writing to entity assembler encounters a null instance of class defined will be set on the entity prior the transfer of individual collection elements 
entityCollectionClass(Class<? extends Collection> entityCollectionClass)(String entityCollectionClassKey)|String | | |3.0.0|Reference key for bean factory to create collection instance. This property is IoC version of the entityCollectionClass that instructs assembler to use bean factory to get collection instance. This configuration takes precedence over entityCollectionClass. Requires bean factory parameter during Entity assembly. 
dtoCollectionClass(Class<? extends Collection> dtoCollectionClass)|Class | Yes |ArrayList |3.0.0|Class that defines the type of class for creating new DTO object collection. When writing to DTO assembler creates instance of class defined and sets on the DTO prior the transfer of individual collection elements. 
dtoCollectionClassKey(String dtoCollectionClassKey)|String | | |3.0.0|Reference key for bean factory to create collection instance. This property is IoC version of the dtoCollectionClass that instructs assembler to use bean factory to get collection instance. This configuration takes precedence over dtoCollectionClass. Requires bean factory parameter during DTO assembly. 
dtoBeanKey(String dtoBeanKey)|String | Yes | |3.0.0|Defines bean key for collection element. New DTO instance will be created using bean factory with this key. Requires bean factory parameter during DTO assembly. 
entityBeanKeys(String... entityBeanKeys)|String[] | Yes | |3.0.0|This annotation is mandatory and defines path of keys to the entity item object where last key represents the collection item itself. When assembling entity if null is encountered on object path bean factory will be used with specified key to create new instances of inner entity objects. Requires bean factory parameter during Entity assembly. 
entityGenericType(Class entityGenericType)|Class |  but recommended |Object |3.0.0|Provides hint for the assembler as to what items are in the entity collection. Otherwise each items class is used to lookup assembler on per item basis. 
entityGenericTypeKey(String entityGenericTypeKey)|String |  but recommended | |3.0.0|Reference key for bean factory to lookup class. This property is IoC version of the entityGenericType that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over entityGenericType. 
dtoToEntityMatcher(Class<? extends DtoToEntityMatcher> dtoToEntityMatcher)|Class | Yes  if entity property is writable | |3.0.0|When writing back to the entity collection to update we need to synchronise items. i.e. match which dto elements correspond to which items elements so that changes can be correctly applied. In order to do this assembler uses DtoToEntityMatcher instances from the [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API. 
dtoToEntityMatcherKey(String dtoToEntityMatcherKey)|String | | |3.0.0|Reference key for adapters map to lookup matchers. This property is IoC version of the dtoToEntityMatcher that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over dtoToEntityMatcher. Requires adapters parameter during Entity assembly. 

### DtoCollectionContext.forField(String fieldName)

Value follows the same principle as for DtoFieldContext.forField(String fieldName) - allows to map dto property to entity property (or path to property using "dot" notation). The value refers to the property of the collection.

**Example configuration of "dot" notation for collection**


```java
public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private MyEntityColClass inner;
  ...getters/setters...
}

public class MyEntityColClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").forField("inner.collection1").dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoCollectionContext.readOnly()

Read only follows the same principle as for DtoFieldContext.readOnly() - setting this prevents entity writes.


### DtoCollectionContext.entityCollectionClass(Class<? extends Collection> entityCollectionClass)

This is the class for the collection instance on the entity, which is used upon entity write when the collection property is null.

**Example configuration of HashSet as container for entity elements**


```java
public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").entityCollectionClass(HashSet.class)
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key");


```


> See adapters API for bean factory implementation examples

### DtoCollectionContext.entityCollectionClassKey(String entityCollectionClassKey)

This is the key (for bean factory) for the class for the collection instance on the entity, which is used upon entity write when the collection property is null.

>  This setting overrides the entityCollectionClass

**Example configuration of ArrayList by key as container for entity elements**


```java

public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private List<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").entityCollectionClassKey("ArrayList100")
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key");

...

// example of how the ArrayList100 key might be used
final BeanFactory bf = new BeanFactory() {
...
   public Object get(String entityBeanKey) {
      if ("ArrayList100".equals(entityBeanKey)) {
         // Create array list with 100 elements capacity
         return new ArrayList(100);
      }
      ...
   }
...
}

...
asm.assembleEntity(dto, entity, null, bf);

```



> See adapters API for bean factory implementation examples

### DtoCollectionContext.dtoCollectionClass(Class<? extends Collection> dtoCollectionClass)

This is class for the collection instance on the DTO, which is used upon transfer of data to DTO.

**Example configuration of HashSet as container for DTO elements**


```java

public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Set<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").dtoCollectionClass(HashSet.class)
    .dtoBeanKey("MyDtoElClass_key").readOnly();


```


> See adapters API for bean factory implementation examples

### DtoCollectionContext.dtoCollectionClassKey(String dtoCollectionClassKey)

This is the key (for bean factory) for the class for the collection instance on the DTO, which is used upon transfer of data to DTO.

> This setting overrides the dtoCollectionClass

**Example configuration of ArrayList by key as container for entity elements**


```java

  public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private List<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").dtoCollectionClassKey("ArrayList100")
    .dtoBeanKey("MyDtoElClass_key").readOnly();


...

// example of how the ArrayList100 key might be used
final BeanFactory bf = new BeanFactory() {
...
   public Object get(String entityBeanKey) {
      if ("ArrayList100".equals(entityBeanKey)) {
         // Create array list with 100 elements capacity
         return new ArrayList(100);
      }
      ...
   }
...
}

...
asm.assembleDto(dto, entity, null, bf);

```


> See adapters API for bean factory implementation examples

### DtoCollectionContext.dtoBeanKey(String dtoBeanKey)

Method follows the same principle as for DtoFieldContext.dtoBeanKey - allows to specify key for bean factory for the DTO element.


### DtoCollectionContext.entityBeanKeys(String... entityBeanKeys)

Method follows the same principle as for DtoFieldContext.entityBeanKeys - allows to specify key for bean factory for the entity collection element. If "dot" notation is used for the DtoCollection.forField() last element of the entityBeanKeys refers to the element entity class, all preceding keys are for the object path.

**Example configuration of "dot" notation for collection**


```java

  public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private MyEntityColClass inner;
  ...getters/setters...
}

public class MyEntityColClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").forField("inner.collection1")
    .entityBeanKeys("MyEntityColClass_key", "MyEntityElClass_key")
    .dtoBeanKey("MyDtoElClass_key");

```



### DtoCollectionContext.entityGenericType(Class entityGenericType)

This is the class (ideally interface) that best represents the elements of this collection. It is recommended to use this property for collection with mixed items, otherwise assembler will create item specific sub assemblers.

**Example configuration of element generic type**


```java

  public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").entityGenericType(MyEntityElClass.class)
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoCollectionContext.entityGenericTypeKey(String entityGenericTypeKey)

This is the key (for bean factory) for the class (ideally interface) that best represents the elements of this collection. It is recommended to use this property for collection with mixed items, otherwise assembler will create item specific sub assemblers.

> This setting overrides the entityGenericType

**Example configuration of entity generic type by key**


```java

  public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").entityGenericTypeKey("MyEntityElClass_key")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoCollectionContext.dtoToEntityMatcher(Class<? extends DtoToEntityMatcher> dtoToEntityMatcher)

Matcher class that has to have default constructor. The matcher is used to synchronize collections when updating entity collection.

**Example of basic matcher**


```java

  public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").dtoToEntityMatcher(MyDtoElClassToMyEntityElClassMatcher.class)
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key");
...

// Simple by ID matching assuming MyDtoElClass and MyEntityElClass have getId() method
public class MyDtoElClassToMyEntityElClassMatcher implements DtoToEntityMatcher<MyDtoElClass, MyEntityElClass> {
   public boolean match(final MyDtoElClass dto, final MyEntityElClass entity) {
      return dto.getId().equals(entity.getId());
   }
}
```


> See adapters API for matchers and bean factory implementation examples

### DtoCollectionContext.dtoToEntityMatcherKey(String dtoToEntityMatcherKey);

Reference key for matcher adapter passed to assembler methods. The matcher is used to synchronize collections when updating entity collection.

> This setting overrides the dtoToEntityMatcher

**Example of basic matcher**


```java

  public class MyDtoClass {
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withCollection("collection1").dtoToEntityMatcherKey("MyDtoElClassToMyEntityElClassMatcher")
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key");


// Simple by ID matching assuming MyDtoElClass and MyEntityElClass have getId() method
public class MyDtoElClassToMyEntityElClassMatcher implements DtoToEntityMatcher<MyDtoElClass, MyEntityElClass> {
   public boolean match(final MyDtoElClass dto, final MyEntityElClass entity) {
      return dto.getId().equals(entity.getId());
   }
}


...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("MyDtoElClassToMyEntityElClassMatcher",  new MyDtoElClassToMyEntityElClassMatcher(;
...
// not needed for DTO
asm.assembleDto(dto, entity, null, null);
...
// needed for entity
asm.assembleEntity(dto, entity, adapters, null);


```


> See adapters API for matchers and bean factory implementation examples

## DtoMapContext

Apart from defining map field DtoMapContext also manages the synchronization of data when it is written back to the entity. Map supports three configurations:

* Entity collection to DTO map
* Entity map with sub entity as key to DTO map
* Entity map with sub entity as value to DTO map

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
forField(String fieldName)|String | |same as dto field name |3.0.0|Field name on entity class that will be bound to this dto field (use "dot" notation for graph path e.g. myField.mySubfield). 
readOnly()|boolean | |false |3.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 
entityMapOrCollectionClass(Class entityMapOrCollectionClass)|Class | Yes |HashMap |3.0.0|Class that defines the type of class for creating new Domain object map or collection. When writing to entity assembler encounters a null instance of class defined will be set on the entity prior the transfer of individual map or collection elements 
entityMapOrCollectionClassKey(String entityMapOrCollectionClassKey)|String | | |3.0.0|Reference key for bean factory to create map or collection instance. This property is IoC version of the entityMapOrCollectionClass that instructs assembler to use bean factory to get map or collection instance. This configuration takes precedence over entityMapOrCollectionClass. Requires bean factory parameter during Entity assembly. 
dtoMapClass(Class<? extends Map> dtoMapClass)|Class | Yes |HashMap |3.0.0|Class that defines the type of class for creating new DTO object map. When writing to DTO assembler creates instance of class defined and sets on the DTO prior the transfer of individual map elements. 
dtoMapClassKey(String dtoMapClassKey)|String | | |3.0.0|Reference key for bean factory to create map instance. This property is IoC version of the dtoMapClass that instructs assembler to use bean factory to get map instance. This configuration takes precedence over dtoMapClass. Requires bean factory parameter during DTO assembly. 
dtoBeanKey(String dtoBeanKey)|String | Yes | |3.0.0|Defines bean key for map element (whether it is key or value). New DTO instance will be created using bean factory with this key. Requires bean factory parameter during DTO assembly. 
entityBeanKeys(String... entityBeanKeys)|String[] | Yes | |3.0.0|This annotation is mandatory and defines path of keys to the entity item object where last key represents the map or collection item itself (whether it is key or value of map or element of collection). When assembling entity if null is encountered on object path bean factory will be used with specified key to create new instances of inner entity objects. Requires bean factory parameter during Entity assembly. 
entityGenericType(Class entityGenericType)|Class |  but recommended |Object |3.0.0|Provides hint for the assembler as to what items are in the entity map or collection. Otherwise each items class is used to lookup assembler on per item basis. 
entityGenericTypeKey(String entityGenericTypeKey)|String |  but recommended | |3.0.0|Reference key for bean factory to lookup class. This property is IoC version of the entityGenericType that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over entityGenericType. 
entityCollectionMapKey(String entityCollectionMapKey)|String | Yes  if entity property is a collection | |3.0.0|If entity property for this field mapping is a collection then this setting maps the property with specified name of collection item on the entity collection item to be the key for the dto map item. 
useEntityMapKey()|String | Yes |false |3.0.0|If entity property is a map this configuration instruct whether the entity element is the value or the key of the map 
dtoToEntityMatcher(Class<? extends DtoToEntityMatcher> dtoToEntityMatcher)|Class | Yes  if entity property is writable | |3.0.0|When writing back to the entity map or collection to update we need to synchronise items. i.e. match which dto elements correspond to which items elements so that changes can be correctly applied. In order to do this assembler uses DtoToEntityMatcher instances from the [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]]API. 
dtoToEntityMatcherKey(String dtoToEntityMatcherKey)|String | | |3.0.0|Reference key for adapters map to lookup matchers. This property is IoC version of the dtoToEntityMatcher that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over dtoToEntityMatcher. Requires adapters parameter during Entity assembly. 

### DtoMapContext.forField(String fieldName)

Method follows the same principle as for DtoFieldContext.forField(String fieldName) - allows to map dto property to entity property (or path to property using "dot" notation). The value refers to the property of the collection or map on the entity.

**Example configuration of "dot" notation for map**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private MyEntityMapClass inner;
  ...getters/setters...
}

public class MyEntityMapClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").forField("inner.map1")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoMapContext.readOnly()

Read only follows the same principle as for DtoFieldContext.readOnly() - setting this to true prevents entity writes.


### DtoMapContext.entityMapOrCollectionClass(Class entityMapOrCollectionClass)

This is the class for the map or collection instance on the entity, which is used upon entity write when the map or collection property is null.

**Example configuration of ConcurrentHashMap with DTO value as container for entity elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").entityMapCollectionClass(ConcurrentHashMap.class)
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


**Example configuration of ConcurrentHashMap with DTO key as container for entity elements**


```java

  public class MyDtoClass {
  private Map<MyDtoElClass, String> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<MyEntityElClass, String> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").entityMapCollectionClass(ConcurrentHashMap.class).useEntityMapKey()
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


**Example configuration of ArrayList as container for entity elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
    // Assuming MyEntityElClass has field "String id"
 .withMap("map1").entityMapCollectionClass(ArrayList.class).entityCollectionMapKey("id")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


| Yes  See adapters API for bean factory implementation examples

### DtoMapContext.entityMapOrCollectionClassKey(String entityMapOrCollectionClassKey)
This is the key (for bean factory) for the class for the map or collection instance on the entity, which is used upon entity write when the map or collection property is null.

|  This setting overrides the entityMapOrCollectionClass

**Example configuration of HashMap with DTO value as container for entity elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").entityMapCollectionClassKey("HashMap100")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

...

// example of how the HashMap100 key might be used
final BeanFactory bf = new BeanFactory() {
...
   public Object get(String entityBeanKey) {
      if ("HashMap100".equals(entityBeanKey)) {
         // Create hash map with 100 elements capacity
         return new HashMap(100);
      }
      ...
   }
...
}

...
asm.assembleEntity(dto, entity, null, bf);

```


| Yes  See adapters API for bean factory implementation examples

### DtoMapContext.dtoMapClass(Class<? extends Map> dtoMapClass)
This is class for the map instance on the DTO, which is used upon transfer of data to DTO.

**Example configuration of ConcurrentHashMap as container for DTO elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").dtoMapClass(ConcurrentHashMap.class)
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoMapContext.dtoMapClassKey(String dtoMapClassKey)
This is the key (for bean factory) for the class for the map instance on the DTO, which is used upon transfer of data to DTO.

|  This setting overrides the dtoMapClass

**Example configuration of ConcurrentHashMap as container for DTO elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").dtoMapClassKey("HashMap100")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

...

// example of how the HashMap100 key might be used
final BeanFactory bf = new BeanFactory() {
...
   public Object get(String entityBeanKey) {
      if ("HashMap100".equals(entityBeanKey)) {
         // Create hash map with 100 elements capacity
         return new HashMap(100);
      }
      ...
   }
...
}

...
asm.assembleDto(dto, entity, null, bf);

```


> See adapters API for bean factory implementation examples

### DtoMapContext.dtoBeanKey(String dtoBeanKey)
Value follows the same principle as for [[DtoFieldContext.dtoBeanKey(String dtoBeanKey) - allows to specify key for bean factory for the DTO element (which could be either key of value of the map element depending on the mapping).


### DtoMapContext.entityBeanKeys(String... entityBeanKeys)
Value follows the same principle as for [[DtoFieldContext.entityBeanKeys(String... entityBeanKeys) - allows to specify key for bean factory for the entity collection element. If "dot" notation is used for the @DtoMapContext.value last element of the entityBeanKeys refers to the element entity class, all preceding keys are for the object path.

**Example configuration of "dot" notation for map**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private MyEntityMapClass inner;
  ...getters/setters...
}

public class MyEntityMapClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").forField("inner.map1").entityBeanKeys("MyEntityMapClass_key", "MyEntityElClass_key")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoMapContext.entityGenericType(Class entityGenericType)
This is the class (ideally interface) that best represents the elements of this map or collection. It is recommended to use this property for map or collection with mixed items, otherwise assembler will create item specific sub assemblers.

**Example configuration of element generic type**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").entityGenericType(MyEntityElClass.class)
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoMapContext.entityGenericTypeKey(String entityGenericTypeKey)
This is the key (for bean factory) for the class (ideally interface) that best represents the elements of this map or collection. It is recommended to use this property for collection with mixed items, otherwise assembler will create item specific sub assemblers.

|  This setting overrides the entityGenericType

**Example configuration of entity generic type by key**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").entityGenericTypeKey("MyEntityElClass_key")
    .dtoBeanKey("MyDtoElClass_key").readOnly();


```


> See adapters API for bean factory implementation examples

### DtoMapContext.entityCollectionMapKey(String entityCollectionMapKey)
This configuration applies only to mappings where DTO map is targeted at entity collection. It defines property on the collection item that should be used for key on the DTO map. The entity element is converter to DTO element and is set as map entry value.

**Example configuration of ArrayList as container for entity elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
    // Assuming MyEntityElClass has field "String id"
 .withMap("map1").forField("collection1").entityMapCollectionClass(ArrayList.class).entityCollectionMapKey("id")
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```



### DtoMapContext.useEntityMapKey()
This configuration applies only to mappings where DTO map is targeted at entity map. It defines whether the sub entity is a key or a value of the map entry.

**Example configuration of a map with DTO value (default) as container for entity elements**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1") /* not invoking useEntityMapKey() */
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


**Example configuration of a map with DTO key as container for entity elements**


```java

  public class MyDtoClass {
  private Map<MyDtoElClass, String> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<MyEntityElClass, String> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").useEntityMapKey()
    .dtoBeanKey("MyDtoElClass_key").readOnly();

```


> See adapters API for bean factory implementation examples

### DtoMapContext.dtoToEntityMatcher(Class<? extends DtoToEntityMatcher> dtoToEntityMatcher)
Matcher class that has to have default constructor. The matcher is used to synchronize collections when updating entity collection. Map matcher is different to collection matcher as it matches entity to the map key.

**Example of basic matcher with default (entity as entry value) setup**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").dtoToEntityMatcher(MapKeysMatcher.class)
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key")
    .entityGenericType(MyEntityElClass.class);


// Simple by ID matching key to key
public class MapKeysMatcher implements DtoToEntityMatcher<String, String> {
   public boolean match(final String dtoMapKey, final String entityMapKey) {
      return dtoMapKey.equals(entityMapKey);
   }
}
```


**Example of basic matcher with entity as entry key setup**


```java

  public class MyDtoClass {
  private Map<MyDtoElClass, String> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<MyEntityElClass, String> map1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").dtoToEntityMatcher(MyDtoElClassToMyEntityElClassMatcher.class).useEntityMapKey()
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key")
    .entityGenericType(MyEntityElClass.class);


// Dto to entity matching assuming both have getId() method
public class MyDtoElClassToMyEntityElClassMatcher implements DtoToEntityMatcher<String, MyEntityElClass> {
   public boolean match(final MyDtoElClass dto, final MyEntityElClass entity) {
      return dto.getId().equals(entity.getId());
   }
}
```


**Example of matcher for map to collection setup**


```java

  public class MyDtoClass {
  private Map<String, MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("collection1").dtoToEntityMatcher(StringKeyToMyEntityElClassMatcher.class)
    // Assuming MyEntityElClass has field "String id"
    .entityMapCollectionClass(ArrayList.class).entityCollectionMapKey("id")
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key")
    .entityGenericType(MyEntityElClass.class);


// Simple by ID matching assuming MyDtoElClass and MyEntityElClass have getId() method
public class StringKeyToMyEntityElClassMatcher implements DtoToEntityMatcher<String, MyEntityElClass> {
   public boolean match(final String dtoMapKey, final MyEntityElClass entity) {
      return dtoMapKey.equals(entity.getId());
   }
}
```


image:http://www.inspire-software.com/confluence/images/icons/emoticons/check.gif||width="16" height="16" alt=""]]|See adapters API for matchers and bean factory implementation examples

### DtoMapContext.dtoToEntityMatcherKey(String dtoToEntityMatcherKey)
Reference key for matcher adapter passed to assembler methods. The matcher is used to synchronize collections when updating entity map or collection.

> This setting overrides the dtoToEntityMatcher

**Example of basic matcher**


```java

  public class MyDtoClass {
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
...

final BeanFactory bf = ...;
final Registry reg = Registries.regisrty(bf);

reg.dto(MyDtoClass.class).forEntityGeneric()
 .withMap("map1").dtoToEntityMatcherKey("MapKeysMatcher")
    .dtoBeanKey("MyDtoElClass_key").entityBeanKeys("MyEntityElClass_key")
    .entityGenericType(MyEntityElClass.class);


...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("MapKeysMatcher",  new MapKeysMatcher(;
...
// not needed for DTO
asm.assembleDto(dto, entity, null, null);
...
// needed for entity
asm.assembleEntity(dto, entity, adapters, null);


```


> See adapters API for matchers and bean factory implementation examples
