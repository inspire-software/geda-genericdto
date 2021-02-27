# Method Synthesizers

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

GeDA has a notion of method synthesizers - these are classes that produce the byte code (synthesize methods) that allow to transfer data between entities and DTO's.

Out of the box GeDA has four different method synthesizer implementations:

Name | Implementation | Comments 
--- | --- | ---
javassist |Javassist v 3.8.0 GA + |default 
bcel |BCEL v 5.2 | 
suntools |Requires com.sun.tools.javac in your JVM | 
reflection |java.lang.reflect.* pure reflection | 

So why does GeDA have all of these?
Well the answer is not that straightforward actually.

The main reason for all of these is have as much speed as possible with what you have.

Javassist is the most common library (part of Hibernate dependencies) and is very easy to use so it is obvious choice for default implementation.

BCEL is less popular since it is quite complex and requires knowledge of byte code in order to generate classes on the fly but offers a bit better performance

Sun Tools is only available if you have com.sun.tools.javac.Main class in your JVM libraries (which is not always available)

Reflection is the basic mechanism possible if you do not want any extra dependencies, but of course the most inefficient one.

## How to specify synthesizer to use?

Quite easy actually - all you need is to make sure that you have required library dependencies on class path and then just invoke custom assembler factory method like so:

```java

final Assembler asm = DTOAssembler.newCustomAssembler(PersonDTO.class, Person.class, synthesizer);
```

The synthesizer can be either built-in id (see "name" column in the table above) or an instance of MethodSynthesizer.

So if for some reason you are unhappy with OOTB implementation of synthesizers you can implement your own version with library of your choice and feed it to the factory method above.

All you need is to implement two methods "DataReader synthesizeReader(PropertyDescriptor pd)" and "DataWriter synthesizeWriter(PropertyDescriptor pd)" each instance allows to copy single field data from/to entity/DTO.

## Why go through so much trouble?

Well, code has to be performant and to get speed up on the last couple of milliseconds is always tough - that's why :)

Here are some performance benchmarks between different synthesizers. 


![GeDA-synth-bechmark.png](https://github.com/inspire-software/geda-genericdto/blob/master/wiki/topics/GeDA-synth-bechmark.png?raw=true)


On average SUNTOOLS and BCEL implementations offer a bit better performance.


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
