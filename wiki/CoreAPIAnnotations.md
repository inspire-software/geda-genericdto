# Annotations

## Basics

The annotation API is represented by six annotations:

Name | Level | Since | Pupose
--- | --- | --- | ---
@Dto|Class |1.0.0 |maker annotation that is used to mark the dto classes. If you are using annotation API then this is a requirement to have all DTO classes to be annotated with this. 
@DtoField|field |1.0.0 |basic unit of transfer is field that defines a basic property that is copied **by reference** or represents a sub dto for which a inner assembler should be created 
@DtoParent|field |1.0.0|additional configuration that supplements @DtoField. It instructs assembler not to recreate a blank entity instance but rather to use EntityRetriever from [[Adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API to fetch it. Useful for child-parent relationships of RDBMs. 
@DtoVirtualField|field |1.1.3 |alternative to @DtoField which does not require a corresponding property on the entity. This enables having derived (or computed) field on the DTO. 
@DtoCollection|field |1.0.0|The basic algorithm that GeDA follows is copy by reference. Collections are special since a new instance of the collection object must be created. To do this you need to mark collections with @DtoCollection rather than @DtoField. @DtoCollection also enables full automatic synchronisation upon writing to entity with help of DtoToEntityMatcher from [[Adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API. 
@DtoMap|field |1.0.0|Similar to collections maps need new container instance and hence require this annotation. In addition to full synchronisations @DtoMap can be configured to a collection on entity or a map with DTO being either key or value. 

> If you are using annotations API for mapping you still can use DSL mappings for these classes provided that DSL registry is supplied as an argument for Assembler factory method.

## @Dto

Defines an object that will serve as a DTO for another entity. If you are using annotation API this is a mandatory marker for all DTO classes. The reason for this is to prevent accidental use of unauthorised DTO classes with assembler.

**Configurations**

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
value|String[] | | |1.1.0 |Full canonical class (or interface) name that this DTO bounds to. (e.g. example.MyEntityClass or example.MyEntityInterface), or several in case of composite auto binding. 

## @Dto.value

This is an optional parameter and defines default bindings for this DTO. By design GeDA does not enforce the mapping until creation of the assembler for a given DTO-Entity pair. This value hence is only a guide which enables use of a short form of assembler factory method (see [[Assembler>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.DTOAssembler API.WebHome]] API autobinding).

**Example single auto binding**

```java
@Dto("autowire.MyEntityClass")
public class MyDtoClass {
  ...
}

...
// use correct factory method
final Assembler assembler = DTOAssembler.newAssembler(MyDtoClass.class);
```

**Example composite auto bindings**

```java
@Dto({
        "composite.EntityPart1Class",
        "composite.EntityPart2Class",
        "composite.EntityPart3Class"
    })
public class CompositeDtoClass {
...
}

// use correct factory method
final Assembler asm = DTOAssembler.newAssembler(CompositeDtoClass.class);
```

It is perfectly acceptable to use other factory menhods thus bypassing the default value.

**Example bypassing auto bindings**

```java
@Dto({
        "composite.EntityPart1Class",
        "composite.EntityPart2Class",
        "composite.EntityPart3Class"
    })
public class CompositeDtoClass {
...
}

// using simple pair factory method discards the autobinding
final Assembler asm = DTOAssembler.newAssembler(
                CompositeDtoClass.class,
                EntityPart1Class.class);
```


## @DtoField

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
value|String |  |same as dto field name |1.0.0|Field name on entity class that will be bound to this dto field (use "dot" notation for graph path e.g. myField.mySubfield). 
converter|String | | |1.0.0|Reference key to a converter to use when assembling DTO's and Entities. This reference is used to lookup converter in adapters map passed into assembleDto and assembleEntity methods. This converter must implement ValueConverter adapter interface. Requires adapters parameter during assembly. 
readOnly|boolean | |false |1.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 
dtoBeanKey|String | | |1.0.0|Specifying this value tells assembler that a "sub" assembler is needed to create sub DTO for this field. New DTO instance will be created using bean factory with this key. Requires bean factory parameter during DTO assembly. 
entityBeanKeys|String[] | Yes if "dot" notation is used | |1.0.0|This annotation is mandatory for @DtoField.value with deeply nested entity object i.e. when a "dot" syntax is used. When assembling entity if null is encountered on object path bean factory will be used with specified key to create new instances of inner entity objects. Requires bean factory parameter during Entity assembly. 

### @DtoField.value

Value is used to specify name of the property on entity object. By default (if this value is not set) assembler uses the same field name as on the DTO. However is is acceptable to have different name on the entity or even use value from inner entity.

**Example default configuration**

```java
@Dto
public class MyDtoClass {
  @DtoField
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String field1;
  ...getters/setters...
}
```

**Example with different name on entity**

```java
@Dto
public class MyDtoClass {
  @DtoField("entityField1")
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String entityField1;
  ...getters/setters...
}
```

If you specify "dot" notation to flatten the entity (i.e. make some sub entity fields as fields on top level DTO) you either need to specify readOnly = true or entityBeanKeys.

> If your entity only has a getter (i.e. immutable implementations) use @DtoField.readOnly = true. This will prevent attempt to access setter of the property

**Example with object path read only**

```java
@Dto
public class MyDtoClassReadOnly {
  @DtoField("inner.entityField1", readOnly = true)
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
```

entityBeanKeys is an array of String each element of which should correspond to path specified in value. E.g. "MySubEntityClass_key" corresponds to "inner" path of field1 mapping.

**Example with object path with bean key**

```java
@Dto
public class MyDtoClass {
  @DtoField("inner.entityField1", entityBeanKeys = "MySubEntityClass_key")
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
```java

> See adapters API for bean factory implementation examples

### @DtoField.converter

Not all fields are easily convertable. For example enum on entity may need to be String on DTO and therefore a conversion of the values needs to take place before they are written to DTO or Entity property. Converters allow to do exactly that. Converter property defines key for converter object that is passed in a Map of adapters to assembleDto and assembleEntity. Converter instance references must be of type ValueConverter (see [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API).

**Example of using converter**

```java
@Dto
public class MyDtoClass {
  @DtoField(converter = "EnumToString")
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {

  public enum Field1 { ONE, TWO, THREE };

  private Field1 inner;
  ...getters/setters...
}

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

### @DtoField.readOnly

Read only allows to control the flow of data to the entity. It is sometimes undesirable to write the value straight to entity due to security or data integrity issues. Setting value to readOnly = true ensures that DTO get loaded with the data but the data is not written back to the entity.

Setting readOnly = true also improves performance as GeDA uses short circuit switches to exit the process and not to create unnecessary auto generated classes.

**Example with non updatable entity ID**

```java
@Dto
public class MyDtoClass {
  @DtoField(readOnly = true)
  private String id;
  @DtoField
  private String field1;
  ...getters/setters...
}

public class MyEntityClass {
  private String id;
  private String field1;
  ...getters/setters...
}
```


### @DtoValue.dtoBeanKey

The basic mapping assumes "copy by reference" and hence to tell assembler that field is an inner DTO object dtoBeanKey is used. This key allows creation of inner DTO object using bean factory and later automatically create sub assembler for that object.

**Example inner DTO field**

```java
@Dto
public class MyDtoClass {
  @DtoField(dtoBeanKey = "MySubDtoClass_key")
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
```

Above example possibly would also need either entityBeanKeys to specify key for MySubEntityClass or MyDtoClass.field1 should be marked as readOnly. However it may be the case that none of these are necessary if the domain model guarantees that MyEntityClass.field1 is always set to a value, in which case this may be a hint for necessity to use @DtoParent.

> See adapters API for bean factory implementation examples

### @DtoValue.entityBeanKeys

Entity bean keys serve the same purpose as dtoBeanKey, however since it is possible to use "dot" notation to specify object path this property is an array of String. The principle is the same however. The key is used with bean factory to create new object instances

**Example with object path with bean key**

```java
@Dto
public class MyDtoClass {
  @DtoField("inner.entityField1", entityBeanKeys = "MySubEntityClass_key")
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
```

> See adapters API for bean factory implementation examples

## @DtoParent

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
value|String | Yes |entityId |1.0.0|Field name on entity class that will be used as primary key for entity look ups. 
retriever|String | Yes |retriever |1.0.0|Reference to an entity retriever to use when assembling Entities. 

### @DtoParent.value

The value should be an immediate property on the inner entity. This property value will be used as a primary key parameter for fetching the Entity upon write to entity operation. The retrieval is accomplished by [[EntityRetriever>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] adapter.

**Example with parent field**

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
      return hibernateSession.get(entityClass, primaryKey);
   }
});

...

asm.assembleDto(dto, entity, adapters, bf);
...
asm.assembleEntity(dto, entity, adapters, bf);

```

> See adapters API for entity retriever and bean factory implementation examples

### @DtoParent.retriever

Entity retriever is effectively a hook to the DAO layer to prevent assembler from updating the entity object. For example if we have an Address and Person entity and we want to assign the address to different person we will not expect to update the existing relationship with data but rather we want to fetch the other Person entity and set it to the Address.

**Example with parent field**

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
final org.hibernate.Session hibernateSession = ...;
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

## @DtoVirtualField

Virtual fields have no contra-part on entities. This is sometimes desirable when computed (e.g. calculating totals) or logically derived (e.g. set flags upon combination of values in fields) fields are needed on DTO. This extra computation is achieved through a converter.

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
converter|String | Yes | |1.1.3 |Reference key to a converter to use when assembling DTO's and Entities. This reference is used to lookup converter in adapters map passed into assembleDto and assembleEntity methods. This converter must implement ValueConverter adapter interface. Requires adapters parameter during assembly. 
readOnly|boolean | |false |1.1.3 |Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 

### @DtoVirtualField.converter

Converter property defines key for converter object that is passed in a Map of adapters to assembleDto and assembleEntity. Converter instance references must be of type ValueConverter (see adapters API). Virtual fields allows to decouple additional business logic from the DTO itself since DTO in theory should only be data containers. For some virtual fields the transfer is only supported in one direction (to DTO) as for example sum calculations as we cannot decompose the sum back to individual addendum, however it is perfectly acceptable to have updates back to entity in the convertToEntity method of the converter.

**Example of using one way converter**

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

...

asm.assembleDto(dto, entity, adapters, null);
...
asm.assembleEntity(dto, entity, adapters, null);

```

**Example of using two way converter**

```java
@Dto
public class MyDtoClass {
  @DtoVirtualField(converter = "Locked")
  private boolean accountLocked;
  ...getters/setters...
}

public class MyEntityClass {

  private boolean lockedByAdmin;
  private boolean expired;
  ...getters/setters...
}

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


### @DtoVirtualField.readOnly

Read only follows the same principle as for @DtoField.readOnly - setting this to true prevents entity writes.


## @DtoCollection

Apart from defining collection field DtoCollection also manages the synchronization of data when it is written back to the entity.

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
value|String | |same as dto field name |1.0.0|Field name on entity class that will be bound to this dto field (use "dot" notation for graph path e.g. myField.mySubfield). 
readOnly|boolean | |false |1.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 
entityCollectionClass|Class | Yes |ArrayList |1.0.0|Class that defines the type of class for creating new Domain object collection. When writing to entity assembler encounters a null instance of class defined will be set on the entity prior the transfer of individual collection elements 
entityCollectionClassKey|String | | |1.0.0|Reference key for bean factory to create collection instance. This property is IoC version of the entityCollectionClass that instructs assembler to use bean factory to get collection instance. This configuration takes precedence over entityCollectionClass. Requires bean factory parameter during Entity assembly. 
dtoCollectionClass|Class | Yes |ArrayList |1.0.0|Class that defines the type of class for creating new DTO object collection. When writing to DTO assembler creates instance of class defined and sets on the DTO prior the transfer of individual collection elements. 
dtoCollectionClassKey|String | | |1.0.0|Reference key for bean factory to create collection instance. This property is IoC version of the dtoCollectionClass that instructs assembler to use bean factory to get collection instance. This configuration takes precedence over dtoCollectionClass. Requires bean factory parameter during DTO assembly. 
dtoBeanKey|String | Yes | |1.0.0|Defines bean key for collection element. New DTO instance will be created using bean factory with this key. Requires bean factory parameter during DTO assembly. 
entityBeanKeys|String[] | Yes | |1.0.0|This annotation is mandatory and defines path of keys to the entity item object where last key represents the collection item itself. When assembling entity if null is encountered on object path bean factory will be used with specified key to create new instances of inner entity objects. Requires bean factory parameter during Entity assembly. 
entityGenericType|Class |No, but recommended |Object |1.0.0|Provides hint for the assembler as to what items are in the entity collection. Otherwise each items class is used to lookup assembler on per item basis. 
entityGenericTypeKey|String |No, but recommended | |1.0.0|Reference key for bean factory to lookup class. This property is IoC version of the entityGenericType that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over entityGenericType. 
dtoToEntityMatcher|Class |Yes, if entity property is writable | |1.0.0|When writing back to the entity collection to update we need to synchronise items. i.e. match which dto elements correspond to which items elements so that changes can be correctly applied. In order to do this assembler uses DtoToEntityMatcher instances from the [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API. 
dtoToEntityMatcherKey|String | | |1.0.0|Reference key for adapters map to lookup matchers. This property is IoC version of the dtoToEntityMatcher that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over dtoToEntityMatcher. Requires adapters parameter during Entity assembly. 

### @DtoCollection.value

Value follows the same principle as for @DtoField.value - allows to map dto property to entity property (or path to property using "dot" notation). The value refers to the property of the collection.

**Example configuration of "dot" notation for collection**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(value = "inner.collection1",
                 dtoBeanKey = "MyDtoElClass_key", readOnly = true)
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
```

> See adapters API for bean factory implementation examples

### @DtoCollection.readOnly

Read only follows the same principle as for @DtoField.readOnly - setting this to true prevents entity writes.


### @DtoCollection.entityCollectionClass

This is the class for the collection instance on the entity, which is used upon entity write when the collection property is null.

**Example configuration of HashSet as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(entityCollectionClass = HashSet.class,
                 dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoCollection.entityCollectionClassKey

This is the key (for bean factory) for the class for the collection instance on the entity, which is used upon entity write when the collection property is null.

> This setting overrides the entityCollectionClass

**Example configuration of ArrayList by key as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(entityCollectionClassKey = "ArrayList100",
                 dtoBeanKey = "MyDtoElClass_key", entityBeanKeys = "MyEntityElClass_key")
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private List<MyEntityElClass> collection1;
  ...getters/setters...
}

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

### @DtoCollection.dtoCollectionClass

This is class for the collection instance on the DTO, which is used upon transfer of data to DTO.

**Example configuration of HashSet as container for DTO elements**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(dtoCollectionClass = HashSet.class,
                 dtoBeanKey = "MyDtoElClass_key", entityBeanKeys = "MyEntityElClass_key")
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Set<MyEntityElClass> collection1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoCollection.dtoCollectionClassKey

This is the key (for bean factory) for the class for the collection instance on the DTO, which is used upon transfer of data to DTO.

> This setting overrides the dtoCollectionClass

**Example configuration of ArrayList by key as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(dtoCollectionClassKey = "ArrayList100",
                 dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private List<MyEntityElClass> collection1;
  ...getters/setters...
}

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

### @DtoCollection.dtoBeanKey

Value follows the same principle as for @DtoField.dtoBeanKey - allows to specify key for bean factory for the DTO element.


### @DtoCollection.entityBeanKeys

Value follows the same principle as for @DtoField.entityBeanKeys - allows to specify key for bean factory for the entity collection element. If "dot" notation is used for the @DtoCollection.value last element of the entityBeanKeys refers to the element entity class, all preceding keys are for the object path.

**Example configuration of "dot" notation for collection**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(value = "inner.collection1",
                 entityBeanKeys = { "MyEntityColClass_key", "MyEntityElClass_key" },
                 dtoBeanKey = "MyDtoElClass_key")
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
```


### @DtoCollection.entityGenericType

This is the class (ideally interface) that best represents the elements of this collection. It is recommended to use this property for collection with mixed items, otherwise assembler will create item specific sub assemblers.

**Example configuration of element generic type**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(entityGenericType = MyEntityElClass.class,
                 dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoCollection.entityGenericTypeKey

This is the key (for bean factory) for the class (ideally interface) that best represents the elements of this collection. It is recommended to use this property for collection with mixed items, otherwise assembler will create item specific sub assemblers.

> This setting overrides the entityGenericType

**Example configuration of entity generic type by key**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(entityGenericTypeKey = "MyEntityElClass_key",
                 dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Collection<MyDtoElClass> collection1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoCollection.dtoToEntityMatcher

Matcher class that has to have default constructor. The matcher is used to synchronize collections when updating entity collection.

**Example of basic matcher**

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

> See adapters API for matchers and bean factory implementation examples

### @DtoCollection.dtoToEntityMatcherKey

Reference key for matcher adapter passed to assembler methods. The matcher is used to synchronize collections when updating entity collection.

> This setting overrides the dtoToEntityMatcher

**Example of basic matcher**

```java
@Dto
public class MyDtoClass {
  @DtoCollection(dtoToEntityMatcher = "MyDtoElClassToMyEntityElClassMatcher",
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


...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("MyDtoElClassToMyEntityElClassMatcher",  new MyDtoElClassToMyEntityElClassMatcher()));
...
// not needed for DTO
asm.assembleDto(dto, entity, null, null);
...
// needed for entity
asm.assembleEntity(dto, entity, adapters, null);


```

> See adapters API for matchers and bean factory implementation examples

## @DtoMap

Apart from defining map field DtoMap also manages the synchronization of data when it is written back to the entity. Map supports three configurations:

* Entity collection to DTO map
* Entity map with sub entity as key to DTO map
* Entity map with sub entity as value to DTO map

Name | Type | Required | Default | Since | Description/Purpose 
--- | --- | --- | --- | --- | --- 
value|String | |same as dto field name |1.0.0|Field name on entity class that will be bound to this dto field (use "dot" notation for graph path e.g. myField.mySubfield). 
readOnly|boolean | |false |1.0.0|Marks Dto for read only state. When assembler assembles entity the data in Dto fields with readOnly set to true will be ignored. 
entityMapOrCollectionClass|Class | Yes |HashMap |1.0.0|Class that defines the type of class for creating new Domain object map or collection. When writing to entity assembler encounters a null instance of class defined will be set on the entity prior the transfer of individual map or collection elements 
entityMapOrCollectionClassKey|String | | |1.0.0|Reference key for bean factory to create map or collection instance. This property is IoC version of the entityMapOrCollectionClass that instructs assembler to use bean factory to get map or collection instance. This configuration takes precedence over entityMapOrCollectionClass. Requires bean factory parameter during Entity assembly. 
dtoMapClass|Class | Yes |HashMap |1.0.0|Class that defines the type of class for creating new DTO object map. When writing to DTO assembler creates instance of class defined and sets on the DTO prior the transfer of individual map elements. 
dtoMapClassKey|String | | |1.0.0|Reference key for bean factory to create map instance. This property is IoC version of the dtoMapClass that instructs assembler to use bean factory to get map instance. This configuration takes precedence over dtoMapClass. Requires bean factory parameter during DTO assembly. 
dtoBeanKey|String | Yes | |1.0.0|Defines bean key for map element (whether it is key or value). New DTO instance will be created using bean factory with this key. Requires bean factory parameter during DTO assembly. 
entityBeanKeys|String[] | Yes | |1.0.0|This annotation is mandatory and defines path of keys to the entity item object where last key represents the map or collection item itself (whether it is key or value of map or element of collection). When assembling entity if null is encountered on object path bean factory will be used with specified key to create new instances of inner entity objects. Requires bean factory parameter during Entity assembly. 
entityGenericType|Class |No, but recommended |Object |1.0.0|Provides hint for the assembler as to what items are in the entity map or collection. Otherwise each items class is used to lookup assembler on per item basis. 
entityGenericTypeKey|String |No, but recommended | |1.0.0|Reference key for bean factory to lookup class. This property is IoC version of the entityGenericType that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over entityGenericType. 
entityCollectionMapKey|String |Yes if entity property is a collection | |1.0.0|If entity property for this field mapping is a collection then this setting maps the property with specified name of collection item on the entity collection item to be the key for the dto map item. 
useEntityMapKey|String | Yes |false |1.0.0|If entity property is a map this configuration instruct whether the entity element is the value or the key of the map 
dtoToEntityMatcher|Class |Yes  if entity property is writable | |1.0.0|When writing back to the entity map or collection to update we need to synchronise items. i.e. match which dto elements correspond to which items elements so that changes can be correctly applied. In order to do this assembler uses DtoToEntityMatcher instances from the [[adapters>>doc:GeDA - Generic DTO Assembler.Documentation.Core API.Adapters API.WebHome]] API. 
dtoToEntityMatcherKey|String | | |1.0.0|Reference key for adapters map to lookup matchers. This property is IoC version of the dtoToEntityMatcher that instructs assembler to use bean factory to lookup entity class. This configuration takes precedence over dtoToEntityMatcher. Requires adapters parameter during Entity assembly. 

### @DtoMap.value

Value follows the same principle as for @DtoField.value - allows to map dto property to entity property (or path to property using "dot" notation). The value refers to the property of the collection or map on the entity.

**Example configuration of "dot" notation for map**

```java
@Dto
public class MyDtoClass {
  @DtoMap(value = "inner.map1",
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
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
```

> See adapters API for bean factory implementation examples

### @DtoMap.readOnly

Read only follows the same principle as for @DtoField.readOnly - setting this to true prevents entity writes.


### @DtoMap.entityMapOrCollectionClass

This is the class for the map or collection instance on the entity, which is used upon entity write when the map or collection property is null.

**Example configuration of ConcurrentHashMap with DTO value as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(entityMapCollectionClass = ConcurrentHashMap.class,
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
```

**Example configuration of ConcurrentHashMap with DTO key as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(entityMapCollectionClass = ConcurrentHashMap.class, useEntityMapKey = true,
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<MyDtoElClass, String> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<MyEntityElClass, String> map1;
  ...getters/setters...
}
```

**Example configuration of ArrayList as container for entity elements**

```java
@Dto
public class MyDtoClass {
  // Assuming MyEntityElClass has field "String id"
  @DtoMap(entityMapCollectionClass = ArrayList.class, entityCollectionMapKey = "id",
          value = "collection1", dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoMap.entityMapOrCollectionClassKey

This is the key (for bean factory) for the class for the map or collection instance on the entity, which is used upon entity write when the map or collection property is null.

> This setting overrides the entityMapOrCollectionClass

**Example configuration of HashMap with DTO value as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(entityMapCollectionClassKey = "HashMap100",
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}

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

> See adapters API for bean factory implementation examples

### @DtoMap.dtoMapClass

This is class for the map instance on the DTO, which is used upon transfer of data to DTO.

**Example configuration of ConcurrentHashMap as container for DTO elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(dtoMapClass = ConcurrentHashMap.class,
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoMap.dtoMapClassKey

This is the key (for bean factory) for the class for the map instance on the DTO, which is used upon transfer of data to DTO.

> This setting overrides the dtoMapClass

**Example configuration of ConcurrentHashMap as container for DTO elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(dtoMapClassKey = "HashMap100",
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}

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

### @DtoMap.dtoBeanKey

Value follows the same principle as for @DtoField.dtoBeanKey - allows to specify key for bean factory for the DTO element (which could be either key of value of the map element depending on the mapping).


### @DtoMap.entityBeanKeys

Value follows the same principle as for @DtoField.entityBeanKeys - allows to specify key for bean factory for the entity collection element. If "dot" notation is used for the @DtoMap.value last element of the entityBeanKeys refers to the element entity class, all preceding keys are for the object path.

**Example configuration of "dot" notation for map**

```java
@Dto
public class MyDtoClass {
  @DtoMap(value = "inner.map1",
          entityBeanKeys = { "MyEntityMapClass_key", "MyEntityElClass_key" },
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
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

```

> See adapters API for bean factory implementation examples

### @DtoMap.entityGenericType

This is the class (ideally interface) that best represents the elements of this map or collection. It is recommended to use this property for map or collection with mixed items, otherwise assembler will create item specific sub assemblers.

**Example configuration of element generic type**

```java
@Dto
public class MyDtoClass {
  @DtoMap(entityGenericType = MyEntityElClass.class,
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoMap.entityGenericTypeKey

This is the key (for bean factory) for the class (ideally interface) that best represents the elements of this map or collection. It is recommended to use this property for collection with mixed items, otherwise assembler will create item specific sub assemblers.

> This setting overrides the entityGenericType

**Example configuration of entity generic type by key**

```java
@Dto
public class MyDtoClass {
  @DtoMap(entityGenericTypeKey = "MyEntityElClass_key",
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoMap.entityCollectionMapKey

This configuration applies only to mappings where DTO map is targeted at entity collection. It defines property on the collection item that should be used for key on the DTO map. The entity element is converter to DTO element and is set as map entry value.

**Example configuration of ArrayList as container for entity elements**

```java
@Dto
public class MyDtoClass {
  // Assuming MyEntityElClass has field "String id"
  @DtoMap(entityMapCollectionClass = ArrayList.class, entityCollectionMapKey = "id",
          value = "collection1", dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Collection<MyEntityElClass> collection1;
  ...getters/setters...
}
```


### @DtoMap.useEntityMapKey

This configuration applies only to mappings where DTO map is targeted at entity map. It defines whether the sub entity is a key or a value of the map entry.

**Example configuration of a map with DTO value (default) as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(useEntityMapKey = false,
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<String, MyDtoElClass> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<String, MyEntityElClass> map1;
  ...getters/setters...
}
```

**Example configuration of a map with DTO key as container for entity elements**

```java
@Dto
public class MyDtoClass {
  @DtoMap(useEntityMapKey = true,
          dtoBeanKey = "MyDtoElClass_key", readOnly = true)
  private Map<MyDtoElClass, String> map1;
  ...getters/setters...
}

public class MyEntityClass {
  private Map<MyEntityElClass, String> map1;
  ...getters/setters...
}
```

> See adapters API for bean factory implementation examples

### @DtoMap.dtoToEntityMatcher

Matcher class that has to have default constructor. The matcher is used to synchronize collections when updating entity collection. Map matcher is different to collection matcher as it matches entity to the map key.

**Example of basic matcher with default (entity as entry value) setup**

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

**Example of basic matcher with entity as entry key setup**

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

**Example of matcher for map to collection setup**

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
}
```

> See adapters API for matchers and bean factory implementation examples

### @DtoMap.dtoToEntityMatcherKey

Reference key for matcher adapter passed to assembler methods. The matcher is used to synchronize collections when updating entity map or collection.

> This setting overrides the dtoToEntityMatcher

**Example of basic matcher**

```java
@Dto
public class MyDtoClass {
  @DtoMap(dtoToEntityMatcherKey = "MapKeysMatcher",
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


...

final Map<String, Object> adapters = new HashMap<String, Object>();
adapters.put("MapKeysMatcher",  new MapKeysMatcher()));
...
// not needed for DTO
asm.assembleDto(dto, entity, null, null);
...
// needed for entity
asm.assembleEntity(dto, entity, adapters, null);


```

> See adapters API for matchers and bean factory implementation examples
