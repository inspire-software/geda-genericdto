# Loading data from DTO to Map or List

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

> This feature is available in 2.1.0+ and deprecates DTOHelper

GeDA offers loading unloading data to/from DTO from/to Map or a List. Previously this was partially supported through DTOHelper class that used only reflection API in order to do this. However after some refactoring done in version 2.0.2 + 2.0.3 it became possible to reuse generic Assembler approach to reuse all the internal goodies and even more, so that this functionality becomes as efficient as the rest of GeDA.

The basic principle remains the same - all you need to do is specify a valid java.util.Map or java.util.List class as your entity class mapping and use GeDA as you would normally do.

So something like this will suffice for using Map as target Entity object:

**Maps**

```java

final Object dto = ...
final Map<String, Object> values = ...
final Assembler asm = DTOAssembler.newAssembler(dto.getClass(), values.getClass());
...
asm.assembleDto(dto, values, null, null);
...
asm.assembleEntity(dto, values, null, null);...
```

 or something like this for Lists:

**Lists**

```java

final Object dto = ...
final List<Object> entity = ...
final Assembler asm = DTOAssembler.newAssembler(dto.getClass(), entity.getClass());
...asm.assembleDto(dto, values, null, null);...
asm.assembleEntity(dto, entity, null, null);

...
```


With map what you will get is Map.Entry'es where each key is a string representing property name and value would be the actual value.

With list it is slightly different but basically you will get a list of an even size where by 1st element would be string representing property, 2nd would be value for property in 1st element, 3rd would be the name for second property, 4th value for property in 3rd and so on.


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
