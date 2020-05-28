![GeDA Logo](https://github.com/inspire-software/geda-genericdto/blob/master/logo/geda-logo.png?raw=true)

# GeDA – smarter and faster DTO assembly

GeDA is java open source Generic DTO assembler library. The aim of this project was to provide large and enterprise level applications with a robust and fast solution that is capable of supporting data transfer under specific business rules.

The main goals are efficiency in terms of *performance* and *architectural flexibility* to support **IoC**, **Complex conversions**, **ORM integration**, **multi class loader environments** (e.g. OSGi) and **data transfer control**.

There is a number of "data mappers" around and therefore we specifically refer to GeDA as an assembler because it is not just a data mapper but much much more.

# So what's GeDA?

A while ago I was working on large scale enterprise project in java that contained massive Domain model. In most circumstances UI and third party systems would require only a subset of the information from the domain object either due to performance or security issues. DTO was the perfect fit. However our implementation was quite elaborate containing much of redundant code mostly due to specifics of Java. Personally I think Java is awesome but sometimes you need a little bit dynamics in the language which Java lacks (which in many cases is a good thing in my opinion - but not in this one).

In retrospective I decided to fix the errors of my ways which were the inspiration for this project. GeDA uses byte code generation to remove the code redundancies imposed by Java language. It does so by automatically generating bridges (at runtime) called "data pipes" to transfer data from and to DTO as opposed to using Reflection API and so many other mappers do.

Having this architecture allowed a world of possibilities such as injection of business logic (i.e. read only, custom converters, virtual fields), flattening or combine domain object data (i.e. change graph of objects into single DTO class), looking up parent objects from ORM when assembling entities, arbitrary conversions between DTO, domain objects, maps, collections not to mention dominant performance.

But that is not all. GeDA is a true TDD library. Every feature is thoroughly tested by unit tests and integration tests (Spring and OSGi). The architecture of the library overall is sound and follows the SOLID principles. There are numerous extension points to extends the transfer logic but the core is closed to modification ensuring staggering performance and robustness of the system.

# What's in the box?

GeDA is split into the following modules:

**core** which is a plain Java library with dependency only to slf4j and byte code library of choice (You can use javassist, bcel or suntools for Sun Java SDK). The code includes:

* Annotations API that allows to declare mappings as annotations on DTO
* DSL API that allows to declare mappings in a Registry object - all plain java
* Adapter API for IoC and data conversions to support complex mappings
* DTOAssembler API for creating GeDA assembler instances

**spring-integration** is an integration with Spring v.3.x.x. The module includes:

* Annotation driven integration by marking service beans' methods as @Transferable
* XML driven integration by using <dtosupport/> tag in service bean XML declaration

**osgi** is a bundle for OSGi environments tested for Fleix v.4.2.0 and Eclipse Tycho. The module includes:

* GeDAFacade osgi service to access core API
* DTOSupportAnnotationsService for using API for annotated DTOs
* DTOSupportDSLService for using API for DSL mapped DTOs

Supporting modules:

* examples - module dedicated to real working examples for all features of GeDA
* core-btest - sandbox for benchmark tests
* core-ptest - sandbox for performance tests and multithreading tests
* osgi-itest - OSGi bundle test

Snippet of how to use of core library:

```java
MyDomainObject entity = ... // Domain can be anything you like, even another DTO

MyDto dtoObject = new MyDto();
Map<String, Object> dtoMap = new HashMap();
List<Object> dtoList = new ArrayList();

// call to newAssembler uses internal cache so you get the right assembler for given pair 
DTOAssembler.newAssembler(dtoObject.getClass(), entity.getClass())
      .assembleDto(dtoObject, entity, null, null);

DTOAssembler.newAssembler(dtoMap.getClass(), entity.getClass())
      .assembleDto(dtoMap, entity, null, null);

DTOAssembler.newAssembler(dtoList.getClass(), entity.getClass())
      .assembleDto(dtoList, entity, null, null);

// ... make some modifications to dto and write back the changes

DTOAssembler.newAssembler(dtoObject.getClass(), entity.getClass())
      .assembleEntity(dtoObject, entity, null, null);

DTOAssembler.newAssembler(dtoMap.getClass(), entity.getClass())
      .assembleEntity(dtoMap, entity, null, null);

DTOAssembler.newAssembler(dtoList.getClass(), entity.getClass())
      .assembleEntity(dtoList, entity, null, null);

MyOtherDto dtoOtherObject = new MyOtherDto();

// Passing data between dto's is just as easy 
DTOAssembler.newAssembler(dtoOtherObject.getClass(), dtoObject.getClass())
      .assembleDto(dtoOtherObject, dtoObject, null, null);

// and back
DTOAssembler.newAssembler(dtoOtherObject.getClass(), dtoObject.getClass())
      .assembleEntity(dtoOtherObject, dtoObject, null, null);

// or event straight to the entity
DTOAssembler.newAssembler(dtoOtherObject.getClass(), entity.getClass())
      .assembleEntity(dtoOtherObject, entity, null, null);

```

# Performance

Performance had always been top priority and it is not surprising that GeDA is the fastest library of its kind and the fastest data mapper for that matter.

As part of GeDA code base we keep benchmarks in the core-btest, so that anyone can try GeDA for performance in comparison to Orika, ModelMapper or Dozer.

In most cases GeDA will take only half the time in comparison to the fastest of them as can be seen on the benchmark graph. We also would like to note that there is Java manual copy on the benchmarks diagram to show that it is simply not possible to compete with "native" code of simple get/set invocations. What is remarkable though that as GeDA is put under more load it performs better. So, for example 25K objects transfers will take same time as 10K using Orika.

# Enterprise

GeDA was designed to work in heavy duty environments from the start. Its superb performance makes it ideal for enterprise level applications. Its architecture easily accommodates the most demanding requirements by providing flexible configurations to apply business rules to the process of data transfer.

GeDA is not only attractive for its functional features but also because its functionality is fully covered by suite of unit tests as well as integration tests for Spring, OSGi and multi threading. GeDA assures its users that the system will remain robust and functional under all circumstances.

GeDA API is very simple so the learning curve is minimal to start using it. Moreover all of it is plain Java core without any additional dependencies apart from logging and byte code generation, which makes the library lightweight and does not polute your project.

Lastly, GeDA is not intrusive - meaning that contamination of your code with GeDA imports is minimal. In fact if you are using DSL approach there is no overlap of your code with GeDA apart from the code that invokes DTO assembly.

If you have any further question please contact us through the GitHub.

# The community

GeDA community welcomes all feedback and is ready to help with any issues that you may encounter.

At the moment most of communication was e-mail based send straight to the team that looks after GeDA. However we wish to change that to make this knowledge available to others and hence we've created a new [Google Group](https://groups.google.com/d/forum/geda-generic-dto-assembler-discussion-group) where you can discuss all GeDA matters.

If you would like to contact us please use GitHub.

# Quick start

Please refer to [examples module](https://github.com/inspire-software/geda-genericdto/tree/master/examples) to see how to use the Assembler.

Module [core-btest](https://github.com/inspire-software/geda-genericdto/tree/master/core-btest) contains Performance benchmarks. To run those use -Pwith-btest option in maven:
```
mvn install -Pwith-btest
```

Module [core-ptest](https://github.com/inspire-software/geda-genericdto/tree/master/core-ptest) is used for multithreading tests and load testing. To run those use -Pwith-ptest option in maven:
```
mvn install -Pwith-ptest
```

Module [osgi-itest](https://github.com/inspire-software/geda-genericdto/tree/master/osgi-itest) is the PAX exam for OSGI. Due to the nature of mvn osgi installation this has to be a separate module.
```
mvn install -Pwith-itest
```

To run all extra's use
```
mvn install -Pall
```

Also see how to setup GeDA in [IDE guide](https://github.com/inspire-software/geda-genericdto/blob/master/SetupGuide.md)

Spring integration schemas can be found [here](https://github.com/inspire-software/geda-genericdto/tree/master/spring-integration/src/main/resources/com/inspiresoftware/lib/dto/geda/config)
