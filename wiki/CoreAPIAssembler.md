DTOAssembler API is the factory for generating assemblers. This factory uses mappings created either by Annotations API or the DSL API. Adapters API provides supporting interfaces for complex DTO transfer processes.

# DTOAssembler API

## com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler

DTOAssembler is a factory for creating DTO assemblers.

The factory serves two purposes:

* Encapsulation of complexity of creating an assembler instance
* Caching assemblers to prevent unnecessary creation step

### Basics

If you are using pure GeDA core (as opposed to Spring or OSGi integrations) the basic use of GeDA is:

```java
// Create (and cache) assembler instance, so there is no need to keep reference to asm
// in fact it is discouraged (see caching)
final Assembler asm = DTOAssembler.newAssembler(MyDto.class, MyEntity.class);

...

// Have both instances ready (blank dto and entity with data) to transfer to dto
final MyDto dto = new MyDto();
final MyEntity entity = ...
// Transfer to dto
asm.assembleDto(dto, entity, null, null);

...
// Have both instances ready (dto with new data and either blank entity or entity for update) to
// transfer to entity
final MyEntity entityToUpdate = ...
// Transfer to entity
asm.assembleEntity(dto, entityToUpdate, null, null);
```

### Caching

DTOAssembler factory methods include internal caching to store created assembler instances in a hash map.
The hash is calculated cumulative hash of:

* dto class
* entity class
* synthesizer
* dsl registry (if available)

Once the assembler instance is configures it is held by a SoftReference to prevent OutOfMemory errors. Hence having assembler instance created once does not guarantee its eternal presence in the system. If system is low on resources garbage collection will remove assembler instances from memory. Having low resources permanently will lead to performance penalty from GeDA as caching will not work.

> Custom assembler factory methods allow specification of method synthesizer. Incorrect implementation of
hasCode() of these object may lead to performance penalty. It is highly recommended to have just one instance
per custom synthesizer implementation as this will incur failures on cache look ups and necessity to create new
assembler instances under different cache key.

There is a choice of four synthesizers OOTB:
* javassist (default) - uses javassist library to generate classes
* suntools - uses com.sun.tools.java.Main from Sun SDK
* bcel - alternative byte code library to javassist
* reflection - pure java.lang.reflect.* implementation

### Available factory methods

Most users will only need to use the basic methods. Advanced features mostly needed for supporting integration modules (Spring and OSGi) and require a deeper understanding of GeDA.

Basic factory methods suitable for most cases

Signature |	Since |	Purpose 
------------ | ------------- | -------------
Assembler newAssembler(final Class dto, final Class entity) |	1.0.0 |	Annotated DTO entity pair (default synthesizer) 
Assembler newAssembler(final Class dto, final Class entity, final Registry registry) |	2.1.0 |	DSL DTO entity pair (default synthesizer) 
Assembler newCompositeAssembler(final Class dto, final Class[] entities) |	2.0.0 |	Annotated DTO to many entities for composite dto assembly (default synthesizer) 
Assembler newAssembler(final Class dto) |	1.0.0 |	Annotated DTO bound to entity specified by @Dto.value (default synthesizer) 

**Basic factory methods with class loader definition for loading generated classes (e.g. for OSGi environments)**

Signature |	Since |	Purpose 
------------ | ------------- | -------------
Assembler newAssembler(final Class dto, final Class entity, final ClassLoader classLoader) |	3.0.0 |	Annotated DTO entity pair (default synthesizer) using specific class loader 
Assembler newAssembler(final Class dto, final Class entity, final ClassLoader classLoader, final Registry registry) |	3.0.0 |	DSL DTO entity pair (default synthesizer) using specific class loader 
Assembler newCompositeAssembler(final Class dto, final Class[] entities, final ClassLoader classLoader) |	3.0.0	| Annotated DTO to many entities for composite dto assembly (default synthesizer) using specific class loader 
Assembler newAssembler(final Class dto, final ClassLoader classLoader) |	3.0.0 |	Annotated DTO bound to entity specified by @Dto.value (default synthesizer) using specific class loader 

**Advanced factory methods with synthesizer definitions to specify which approach to use for code generation**

Signature |	Since |	Purpose 
------------ | ------------- | -------------
Assembler newCustomAssembler(final Class dto, final Class entity, final Object synthesizer) |	1.0.0 |	Annotated DTO entity pair with custom synthesizer 
Assembler newCustomAssembler(final Class dto, final Class entity, final ClassLoader classLoader, final Object synthesizer) |	3.0.0 |	Annotated DTO entity pair with custom synthesizer using specific class loader 
Assembler newCustomAssembler(final Class dto, final Class entity, final Registry registry, final Object synthesizer) |	3.0.0	| DSL DTO entity pair with custom synthesizer 
Assembler newCustomAssembler(final Class dto, final Class entity, final ClassLoader classLoader, final Registry registry, final Object synthesizer) |	3.0.0	| DSL DTO entity pair with custom synthesizer using specific class loader 
Assembler newCustomCompositeAssembler(final Class dto, final Class[] entities, final Object synthesizer) | 	2.0.0 |	Annotated DTO to entities for composite DTO with custom synthesizer 
Assembler newCustomCompositeAssembler(final Class dto, final Class[] entities, final ClassLoader classLoader, final Object synthesizer) |	3.0.0	| Annotated DTO to entities for composite DTO with custom synthesizer using specific class loader 
Assembler newCustomAssembler(final Class dto, final Object synthesizer) |	1.0.0 |	Annotated DTO bound to entity specified by @Dto.value with custom synthesizer 
Assembler newCustomAssembler(final Class dto, final ClassLoader classLoader, final Object synthesizer) |	3.0.0 |	Annotated DTO bound to entity specified by @Dto.value with custom synthesizer using specific class loader 

### Annotation API methods

#### DTOAssembler.newAssembler(Class dto, Class entity)

Basic factory method that allows to create assembler instance for given DTO and entity pair.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dto) 	| Class |	Dto concrete class that is annotated 
param(entity) |	Class |	the entity class or interface that has appropriate getters and setters 
return 	| Assembler | 	assembler instance which can be used to assemble DTO and Entity objects 

**Example minimal single property DTO class**

```java
package example;

@Dto
public class MyDto {

   @DtoField
   private String field1

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }
}
```

**Example minimal single property Entity class**

```java
package example;

public class MyEntity {

   private String field1

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }
}
```

**Example use**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class, MyEntity.class);

final MyEntity entity = ...
final MyDto dto = new MyDto();

asm.assembleDto(dto, entity, null, null)
```

#### DTOAssembler.newAssembler(Class dto) - autobinding

Basic factory method that allows to create assembler instance for given DTO bound to entity.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dto) |	Class |	Dto concrete class that is annotated and @Dto.value is specified 
return |	Assembler |	assembler instance which can be used to assemble DTO and Entity objects 

**Example minimal single property DTO class bound to example.MyEntity by default**

```java
package example;

@Dto("example.MyEntity")
public class MyDto {

   @DtoField
   private String field1

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }
}
```

**Example use**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final MyEntity entity = ...
final MyDto dto = new MyDto();

asm.assembleDto(dto, entity, null, null)
```

#### DTOAssembler.newCompositeAssembler(Class dto, Class[] entities) - combined

Basic factory method that allows to create assembler instance for given DTO that is composed of one of more entities.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dto) |	Class |	Dto concrete class that is annotated 
param(entities) |	Class[] |	the entity class or interface array that has appropriate getters and setters 
return |	Assembler |	assembler instance which can be used to assemble DTO and Entity objects 

**Example minimal single property per entity DTO class**

```java
package example;

@Dto
public class MyDto {

   @DtoField
   private String field1
   @DtoField
   private String field2

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }

   public String getField2() {
      return field2;
   }

   public void setField2(String field2) {
      this.field2 = field2;
   }
}
```

**Example minimal single property Entity1 and Entity2 class**

```java
package example;

public class MyEntity1 {

   private String field1

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }
}

package example;

public class MyEntity2 {

   private String field2

   public String getField2() {
      return field2;
   }

   public void setField2(String field2) {
      this.field2 = field2;
   }
}
```

**Example use**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class, new Class[] {MyEntity1.class,MyEntity2.class});

final MyEntity1 entity1 = ...
final MyEntity2 entity2 = ...
final MyDto dto = new MyDto();
// field1 from entity1 and field2 from entity2 are copied to MyDto structure 
// having the same field names with the same data types from the combined entities
asm.assembleDto(dto, new Object[] { entity1, entity2 }, null, null)
```

> When using composite assembler you have to be careful so that individual entities do not have fields with same names that participate in transfer to DTO. If for example field1 existed in both MyEntity1 and MyEntity2 then there would be two transfers in the order specified by DTOAssembler.newAssembler with second one overwriting the changes made by the first. I.e. First MyEntity1 field1 would be transferred to MyDto.field1 and then MyEntity2 field1 would be transferred to MyDto.field1 and overwrite it. This order is given by new Class[] {MyEntity1.class,MyEntity2.class} class array.

### DSL API methods

#### DTOAssembler.newAssembler(Class dto, Class entity, Registry registry)

Basic factory method that allows to create assembler instance for given DTO and entity pair.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dot) |	Class |	Dto concrete class
param(entity) |	Class |	the entity class or interface that has appropriate getters and setters
param(registry) |	Registry	| DSL registry instance containing mappings
return	| Assembler	| assembler instance which can be used to assemble DTO and Entity objects

**Example minimal single property DTO class**

```java
package example;

public class MyDto {

   private String field1

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }
}
```

**Example minimal single property Entity class**

```java
package example;

public class MyEntity {

   private String field1

   public String getField1() {
      return field1;
   }

   public void setField1(String field1) {
      this.field1 = field1;
   }
}
```

**Example use**

```java
final Registry registry = new DefaultDSLRegistry();

registry.dto(MyDto.class).forEntityGeneric().withField("field1");

final Assembler asm = DTOAssembler.newAssembler(MyDto.class, MyEntity.class, registry);

final MyEntity entity = ...
final MyDto dto = new MyDto();

asm.assembleDto(dto, entity, null, null)
```

### Extra setup hook

DTOAssembler has a static "void setup(final Properties props)" method that allows to preconfigure some of the properties before the first use of DTOAssembler.

Configuration Name | 	Value |	Example |	Purpose 
------------ | ------------- | ------------- | -------------
com.inspiresoftware.lib.dto.geda.assembler. DTOAssembler.SETTING_SYNTHESIZER_IMPL |	String key for OOTB synthesizer or custom instance of MethodSynthesizer |	bcel | 	Specify the default synthesizer to be used when basic factory methods of DTOAssembler are invoked 
com.inspiresoftware.lib.dto.geda.assembler. DTOAssembler.SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN |	String | 	_\$\$_ |	Pattern to match blacklisted class names, so that their parent is used rather than the class itself. E.g. if ORM such as Hibernates creates javassist proxy for domain class we need to map to parent of this proxy, which is user defined class rather than the proxy itself 

## com.inspiresoftware.lib.dto.geda.assembler.Assembler

Signatures |	Since |	Purpose 
------------ | ------------- | -------------
void assembleDto(Object dto, Object entity, Map<String, Object> converters, BeanFactory dtoBeanFactory) |	1.0.0 |	Transfer data from a single entity instance to single dto instance. Converters and bean factory are optional depending on the mapping. 
void assembleDtos(Collection dtos, Collection entities, Map<String, Object> converters, BeanFactory dtoBeanFactory) |	1.1.0 |	Transfer data from collection of entities (not null) to collection of dto (not null empty collection). Converters and bean factory are optional depending on the mapping. 
void assembleEntity(Object dto, Object entity, Map<String, Object> converters, BeanFactory entityBeanFactory) |	1.0.0 |	Transfer data from a single dto instance to single entity instance. Converters and bean factory are optional depending on the mapping. 
void assembleEntities(Collection dtos, Collection entities, Map<String, Object> converters, BeanFactory entityBeanFactory) |	1.1.0	| Transfer data from collection of dto (not null) to collection of entity (not null empty collection). Converters and bean factory are optional depending on the mapping. 

### Assembler.assembleDto(Object dto, Object entity, Map<String, Object> converters, BeanFactory dtoBeanFactory)

Transfer single entity data to single DTO. Entity object can be a custom class, Map or List.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dto) |	Object |	dto instance. must not be null 
param(entity) |	Object |	entity instance must not be null 
param(converters) |	Map |	map of stateless converters. Please refer to Annotations API, DSL API and Adapters API. 
param(dtoBeanFactory) |	BeanFactory |	bean factory instance that facilitates IoC. Please refer to Adapters API. 

**Example use custom class**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final MyEntity entity = ...
final MyDto dto = new MyDto();

asm.assembleDto(dto, entity, null, null)
```

**Example use Map**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final Map<String, Object> entity = ...
final MyDto dto = new MyDto();

asm.assembleDto(dto, entity, null, null)
```

**Example use List**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final List<Object> entity = ...
final MyDto dto = new MyDto();

asm.assembleDto(dto, entity, null, null)
```

### Assembler.assembleDtos(Collection dtos, Collection entities, Map<String, Object> converters, BeanFactory dtoBeanFactory)

Transfer an entity collection data to DTO collection. This is a form of batch processing to load collection of DTOs in bulk. Entity object can be a custom class, Map or List.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dtos) |	Collection |	Non null empty collection 
param(entities) |	Collection |	entity collection must not be null 
param(converters) |	Map |	map of stateless converters. Please refer to Annotations API, DSL API and Adapters API. 
param(dtoBeanFactory) |	BeanFactory |	bean factory instance that facilitates IoC. Please refer to Adapters API. 

**Example use custom class**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final Collection<MyEntity> entities = ...
final List<MyDto> dtos = new ArrayList();

asm.assembleDtos(dtos, entities, null, null)
```

**Example use Map**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final Collection<Map<String, Object>> entities = ...
final List<MyDto> dtos = new ArrayList();

asm.assembleDtos(dtos, entities, null, null)
```

**Example use List**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final List<List<Object>> entities = ...
final List<MyDto> dtos = new ArrayList();

asm.assembleDtos(dtos, entities, null, null)
```

### Assembler.assembleEntity(Object dto, Object entity, Map<String, Object> converters, BeanFactory dtoBeanFactory)

Transfer single DTO data to single entity. Entity object can be a custom class, Map or List.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dto) |	Object |	dto instance. must not be null 
param(entity) |	Object |	entity instance must not be null 
param(converters) |	Map |	map of stateless converters. Please refer to Annotations API, DSL API and Adapters API. 
param(dtoBeanFactory) |	BeanFactory |	bean factory instance that facilitates IoC. Please refer to Adapters API. 

**Example use custom class**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final MyEntity entity = ...
final MyDto dto = new MyDto();
dto.setField1("value");

asm.assembleEntity(dto, entity, null, null)
```

**Example use Map**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final Map<String, Object> entity = ...
final MyDto dto = new MyDto();
dto.setField1("value");

asm.assembleEntity(dto, entity, null, null)
```

**Example use List**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final List<Object> entity = ...
final MyDto dto = new MyDto();
dto.setField1("value");

asm.assembleEntity(dto, entity, null, null)
```

### Assembler.assembleEntities(Collection dtos, Collection entities, Map<String, Object> converters, BeanFactory dtoBeanFactory)

Transfer a DTO collection data to entity collection. This is a form of batch processing to assemble collection of entities in bulk. Entity object can be a custom class, Map or List.

Signature |	Type |	Description 
------------ | ------------- | -------------
param(dtos) |	Collection |	DTO collection must not be null 
param(entities) |	Collection |	Non null empty collection 
param(converters) |	Map |	map of stateless converters. Please refer to Annotations API, DSL API and Adapters API. 
param(dtoBeanFactory) |	BeanFactory |	bean factory instance that facilitates IoC. Please refer to Adapters API. 

**Example use custom class**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final Collection<MyEntity> entities = new HashSet<MyEntity>();
final List<MyDto> dtos = new ArrayList();

asm.assembleDtos(dtos, entities, null, null)
```

**Example use Map**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final Collection<Map<String, Object>> entities = new LinkedList<Map<String, Object>>();
final List<MyDto> dtos = new ArrayList();
final MyDto dto = new MyDto();
dto.setField1("value");
dtos.add(dto);

asm.assembleDtos(dtos, entities, null, null)
```

**Example use List**

```java
final Assembler asm = DTOAssembler.newAssembler(MyDto.class);

final List<List<Object>> entities = new ArrayList<List<Object>>();
final List<MyDto> dtos = new ArrayList();
final MyDto dto = new MyDto();
dto.setField1("value");
dtos.add(dto);

asm.assembleDtos(dtos, entities, null, null)
```
