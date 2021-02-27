# DTO autobinding

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

## What if I have only one-to-one relation between dto and entity or if I want to specify some default entity for dto?

> This feauture was inspired by [[Igor Azarny>>http://ua.linkedin.com/in/igorazarny/]] and available since v.1.0.2

In many cases you end up with a simple on-to-one relation between dto and entity. Therefore it seems kind of lengthy to specify the entity class for DTOAssembler every time.

So GeDA introduced a new value parameter for the @Dto annotation that specifies the entity class name. The class name can be a java (% style="color:red" %){{icon name="times-circle"/}} class or interface. 

```java
@Dto("dp.lib.dto.geda.assembler.TestEntity1Class")
@Ignore
class TestDto1Class implements TestDto1Interface {
...
}
```


The class name is defined as string. This is a consious decision to prevent premature class loading and dependencies during development. So what does this configuration give you? Well, now you can create DTOAssembler just by specifying dto class like so: 

```java
 /**
  * Test that if @Dto specifies class name for entity the assembler is auto created
  * for that class or interface.
  */
 @Test
 public void testDtoEntityClassAutoBinding() {

  final TestDto1Interface dto = new TestDto1Class();
  final TestEntity1Interface entity = new TestEntity1Class();

  final DTOAssembler assembler = DTOAssembler.newAssembler(TestDto1Class.class);

  dto.setMyLong(1L);
  dto.setMyDouble(0.2d);
  dto.setMyString("string");

  assembler.assembleEntity(dto, entity, null, null);

  assertEquals(Long.valueOf(1L), entity.getEntityId());
  assertEquals(Double.valueOf(0.2d), entity.getNumber());
  assertEquals("string", entity.getName());

  entity.setEntityId(2L);
  entity.setNumber(2d);
  entity.setName("name");

  assembler.assembleDto(dto, entity, null, null);

  assertEquals(Long.valueOf(2L), dto.getMyLong());
  assertEquals(Double.valueOf(2d), dto.getMyDouble());
  assertEquals("name", dto.getMyString());

 }
```

Hope this will make your GeDA experience even better!

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
