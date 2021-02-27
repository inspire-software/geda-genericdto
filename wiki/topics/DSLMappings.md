# DSL mappings

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

> This feature is available in 2.1.0+

Although annotation provide good means for mapping DTO to Entity there are two problems with them:

* They have to be specified at coding time, so your runtime is pretty much set in stone
* They pollute your code - i.e. by using GeDA annotation specifically in your classes you create a direct dependency of your code to GeDA

So to address these issues we looked into how we can adapt GeDA and the solution was to create a DSL (Domain Specific Language) that would allow to express mappings and a registry object that would hold all the runtime mappings that we create.

All this now resides in the **com.inspiresoftware.lib.dto.geda.assembler.dsl** package.
It all starts with a **Registry** class which is the object that will contain all your mappings. There is no restriction on how many of these registries you will have so in theory you can have a registry per each mapping, although this may force you to duplicate your mappings.

If you look at the Registry - it is an interface, so how do you actually go about using it. Well, GeDA provides a default implementation that you can use which is called (surprise, surprise) DefaultDSLRegistry.

> TIP: Read more about BeanFactories

Here is a sample code for creating a basic mapping:

**DSL mapping example**

```java
final ExtensibleBeanFactory bf = ...
final Registry registry = new DefaultDSLRegistry(bf);

        registry
                // main mapping
                .dto("myDto").forEntity("myEntity")
                // field 1
                .withField("field1").forField("field1")
                    .readOnly()
                    .converter("field1Converter")
                // field 2
                .and()
                .withField("field2").forField("field2.subField1")
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

In this particular example we use key references for DTO and entity classes that will be looked up through the bean factory object, however if you are not planning to use keys you may just use the classes directly:

**Direct Class to Class mapping**

```java
...
registry
      .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
...
```

In which case you can leave beanFactory constructor parameter as null.

In the mapping code sample you may see that method calls keep on chaining with a certain pattern. The basic idea is to invoke with[Something] method which will specify a certain type of binding of a field and then invoking methods to configure this binding.

Three methods are equivalents of the annotations:

* withField - @DtoField (with @DtoVirtualField and @DtoParent field features)
* withCollection - @DtoCollection
* withMap - @DtoMap

Each of the methods invoked on the binding objects are equivalent (or very similar) to properties specified on the equivalent annotations. So no concept is new - you just use it in a slightly different manner.

As you can see you can chain binding as long as you wish. And do not worry if you need to add something later on - since Default Registry implementation tracks Class to Class mappings so "with" methods can resume where you left off.

In other words below example will create a single MyDtoField3Class to MyEntityField3Class mapping with two bindings (field1 and field2):

**Resume mapping**

```java
...
registry
   .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
   .withField("field1")
...
registry
   .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
   .withField("field2")
...
```

If you however specify the same field name you will be modifying the already existing binding.

**Resume mapping on same binding**

```java
...
registry
  .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
  .withField("field1").readOnly()
...
registry
  .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
  .withField("field1").converter("field1Converter")
...
```

So the above example will result in a single MyDtoField3Class to MyEntityField3Class mapping with one binding for field1 which is read only and has a converter.

However if you do this on different instances of registry that will produce a completely different mappings:

**Different registries**

```java
...
registry1
   .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
   .withField("field1").readOnly()
...
registry2
   .dto(MyDtoField3Class.class).forEntity(MyEntityField3Class.class)
   .withField("field1").converter("field1Converter")
...
```

So the above example will produce two mappings: one in registry1 where field1 is read only and one in registry2 where field one has a converter.

This is all great so far but what if I do not know what target entity I will be creating DTO assemblers for (Same way as in annotations). Well that is easy to:

**Generic Entity mapping**

```java
registry
                // main mapping
                .dto("myDto").forEntityGeneric()
                // field 1
                .withField("field1").forField("field1")
                .readOnly()
                .converter("field1Converter")
```

So the above mapping will apply to any entity that DTOAssembler will be created for. Which is useful when you have multiple implementations or indeed different classes that have same properties.
Now at this point it seems to be quite verbose - I mean every entity class has to be specified, and you'd be wrong because we can do this:

**Reuse mapping**

```java
           registry.dto("myDto").useContextFor(
            registry
                    // main mapping
                    .dto("myDto").forEntity(MyEntity.class),
            Map.class
        );
```

So in this snippet we tell to DSL registry that when we encounter a Map class we want to use the same mapping as for MyEntity class. This way you can specify all sorts of classes that should behave in a certain way **but no more than you specify**, so you remain in full control of the situation.
Hope you get the idea. So what is next? Well now you can use yet another factory method to create a DSL enabled assembler that you can use on Dto classes that are not annotated like so:

**Creating DSL enabled assembler**

```java
...
final Assembler asm = DTOAssembler
   .newAssembler(MyDtoClass.class, MyEntity.class, registry);

final MyEntity entity = ...
final MyDtoClass dto = ...

asm.assembleDto(dto, entity, conv, bf);
```

So nothing new here but a whole new approach to how you code.


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
