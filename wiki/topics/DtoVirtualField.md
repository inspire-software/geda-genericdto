# DtoVirtualField

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

## Virtual Field annotation

Sometimes, the converted just does not cut it, since we may need to gather information from several fields of the entity in order to produce a field on the DTO. Virtual field is designed precisely for this purpose. This is a special way to use converter on a field which would accept the the whole entity as parameter rather than a specific field. Within this converted you can do whatever you like as long as you produce a sensible dto property value. All normal rules for ValueConverter interface applies.

Here is the sample: 

**DTO with two virtual fields**

```java
package dp.lib.dto.geda.assembler.examples.virtual;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoVirtualField;

@Dto
@Ignore
public class TestDto20Class {

 @DtoVirtualField(converter = "VirtualMyBoolean")
 private Boolean myBoolean;

 @DtoVirtualField(converter = "VirtualMyLong")
 private Long myLong;

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

 /**
  * @return long value
  */
 public Long getMyLong() {
  return myLong;
 }

 /**
  * @param myLong long value.
  */
 public void setMyLong(final Long myLong) {
  this.myLong = myLong;
 }



}
```


**Corresponding entity**

```java
package dp.lib.dto.geda.assembler.examples.virtual;

import org.junit.Ignore;

@Ignore
public class TestEntity20Class {

 private boolean decided;

 private long pk;

 /**
  * @return result of a complex decision.
  */
 public Boolean whatWasComplexDecision() {
  return Boolean.TRUE;
 }

 /**
  * @param dtoValue value from dto object.
  */
 public void makeComplexDecision(final Boolean dtoValue) {
  this.decided = dtoValue;
 }

 /**
  * @return boolean value
  */
 public boolean isDecided() {
  return decided;
 }

 /**
  * @return some PK
  */
 public long getPk() {
  return pk;
 }

 /**
  * @param pk some PK
  */
 public void setPk(final long pk) {
  this.pk = pk;
 }



}
```

**Corresponding converters**

```java
package dp.lib.dto.geda.assembler.examples.virtual;

import org.junit.Ignore;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;

@Ignore
public class VirtualMyBooleanConverter implements ValueConverter {

 /** {@inheritDoc} */
 public Object convertToDto(final Object object, final BeanFactory beanFactory) {
  final TestEntity20Class entity = (TestEntity20Class) object;
  return entity.whatWasComplexDecision();
 }

 /** {@inheritDoc} */
 public Object convertToEntity(final Object object, final Object oldEntity,
   final BeanFactory beanFactory) {
  final TestEntity20Class entity = (TestEntity20Class) oldEntity;
  entity.makeComplexDecision((Boolean) object);
  return entity;
 }

}

@Ignore
public class VirtualMyLongConverter implements ValueConverter {

 /** {@inheritDoc} */
 public Object convertToDto(final Object object, final BeanFactory beanFactory) {
  final TestEntity20Class entity = (TestEntity20Class) object;
  return entity.getPk();
 }

 /** {@inheritDoc} */
 public Object convertToEntity(final Object object, final Object oldEntity,
   final BeanFactory beanFactory) {
  final TestEntity20Class entity = (TestEntity20Class) oldEntity;
  if (object instanceof Long) {
   entity.setPk((Long) object);
  } else {
   entity.setPk(0L);
  }
  return entity;
 }

}
```

The test code:

**Test example**

```java
...
 @Test
 public void testVirtualFieldMapping() throws GeDAException {

  final TestDto20Class dto = new TestDto20Class();
  final TestEntity20Class entity = new TestEntity20Class();

  final Map<String, Object> converters = new HashMap<String, Object>();
  converters.put("VirtualMyBoolean", new VirtualMyBooleanConverter());
  converters.put("VirtualMyLong", new VirtualMyLongConverter());

  final DTOAssembler assembler = DTOAssembler.newCustomAssembler(dto.getClass(), entity.getClass(), synthesizer);

  assembler.assembleDto(dto, entity, converters, null);

  assertTrue(dto.getMyBoolean());
  assertEquals(Long.valueOf(0L), dto.getMyLong());
  dto.setMyBoolean(true);
  dto.setMyLong(2L);
  assertFalse(entity.isDecided());

  assembler.assembleEntity(dto, entity, converters, null);

  assertTrue(entity.isDecided());
  assertEquals(2L, entity.getPk());

 }
...
```

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
