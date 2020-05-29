# DTO Parent

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

## Working with parent reations of entities

There are some times when you need to extract a full cascade of entities as DTO cascade and it works fine with GeDA - the problem arises when you want to update the entities back.

Of course you may put readOnly annotation and do it by hand, but this kind of looses the point of using GeDA since you do half of the work your self.

So **DtoParent** annotation to the rescue. This annotation allows GeDA to mark parent fields i.e. for entities that are to be retrieved from persistance layer rather then being updated.

The annotation defines two settings:
value - which is the key to **property** that defines the **PK** of **entity** on **!DTO!** object
retriever - the object that will find our parent entities by primary key.

Now, to the example. I deliberatelly used interfaces for everything to make it explicit how to get it done. So sorry if it turned out harder to read than expected.

So here are our test classes:

**DTOS**

Child Class

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;
import dp.lib.dto.geda.annotations.DtoParent;

/**
 * Test DTO for Assembler.
 */
@Dto
@Ignore
public class TestDto11ChildClass implements TestDto11ChildInterface {

 @DtoParent
 @DtoField(value = "parent",
     dtoBeanKeys = { "dp.lib.dto.geda.assembler.TestDto11ParentClass" },
     entityBeanKeys = { "dp.lib.dto.geda.assembler.TestEntity11ParentClass" })
 private TestDto11ParentInterface parent;

 @DtoField
 private String name;

 /**
  * @return parent entity.
  */
 public TestDto11ParentInterface getParent() {
  return parent;
 }
 /**
  * @param parent parent entity.
  */
 public void setParent(final TestDto11ParentInterface parent) {
  this.parent = parent;
 }

 /**
  * @return name
  */
 public String getName() {
  return name;
 }
 /**
  * @param name name
  */
 public void setName(final String name) {
  this.name = name;
 }

}
```

Child Interface

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 */
@Ignore
public interface TestDto11ChildInterface {

 /**
  * @return parent entity.
  */
 TestDto11ParentInterface getParent();
 /**
  * @param parent parent entity.
  */
 void setParent(final TestDto11ParentInterface parent);
 /**
  * @return name
  */
 String getName();
 /**
  * @param name name
  */
 void setName(final String name);

}
```

Parent Class

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

/**
 * Test DTO for Assembler.
 */
@Dto
@Ignore
public class TestDto11ParentClass implements TestDto11ParentInterface {

 @DtoField
 private long entityId;
 @DtoField
 private String name;

 /**
  * @return PK of this entity.
  */
 public long getEntityId() {
  return entityId;
 }
 /**
  * @param parentId PK of this entity.
  */
 public void setEntityId(final long entityId) {
  this.entityId = entityId;
 }
 /**
  * @return name
  */
 public String getName() {
  return name;
 }
 /**
  * @param name name
  */
 public void setName(final String name) {
  this.name = name;
 }

}
```

Parent Interface

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 */
@Ignore
public interface TestDto11ParentInterface {

 /**
  * @return PK of this entity.
  */
 long getEntityId();
 /**
  * @param parentId PK of this entity.
  */
 void setEntityId(final long entityId);
 /**
  * @return name
  */
 String getName();
 /**
  * @param name name
  */
 void setName(final String name);

}
```

**ENTITIES**

Child Class

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 */
@Ignore
public class TestEntity11ChildClass implements TestEntity11ChildInterface {

 private TestEntity11ParentInterface parent;
 private String name;

 /**
  * @return parent entity.
  */
 public TestEntity11ParentInterface getParent() {
  return parent;
 }
 /**
  * @param parent parent entity.
  */
 public void setParent(final TestEntity11ParentInterface parent) {
  this.parent = parent;
 }

 /**
  * @return name
  */
 public String getName() {
  return name;
 }
 /**
  * @param name name
  */
 public void setName(final String name) {
  this.name = name;
 }

}
```

Child Interface

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 */
@Ignore
public interface TestEntity11ChildInterface {

 /**
  * @return parent entity.
  */
 TestEntity11ParentInterface getParent();
 /**
  * @param parent parent entity.
  */
 void setParent(final TestEntity11ParentInterface parent);
 /**
  * @return name
  */
 String getName();
 /**
  * @param name name
  */
 void setName(final String name);

}
```

Parent Class

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 */
@Ignore
public class TestEntity11ParentClass implements TestEntity11ParentInterface {

 private long entityId; // NOTE: this name is default mapping for @DtoParent#value if you have another name
                        // then annotation must be specified.
 private String name;

 /**
  * @return PK of this entity.
  */
 public long getEntityId() {
  return entityId;
 }
 /**
  * @param parentId PK of this entity.
  */
 public void setEntityId(final long entityId) {
  this.entityId = entityId;
 }
 /**
  * @return name
  */
 public String getName() {
  return name;
 }
 /**
  * @param name name
  */
 public void setName(final String name) {
  this.name = name;
 }

}
```

Parent Interface

```java
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

/**
 * Test DTO for Assembler.
 */
@Ignore
public interface TestEntity11ParentInterface {

 /**
  * @return PK of this entity.
  */
 long getEntityId();
 /**
  * @param parentId PK of this entity.
  */
 void setEntityId(final long entityId);
 /**
  * @return name
  */
 String getName();
 /**
  * @param name name
  */
 void setName(final String name);

}
```

**THE TEST**

```java
 /**
  * Test that when dto field is additionally annotated with dto parent when writing
  * back data from dto to entity the assembler does not copy the values but uses
  * {@link dp.lib.dto.geda.adapter.EntityRetriever} to delegate the setting of value.
  */
 @Test
 public void testDtoParentAnnotation() {

  final TestEntity11ParentInterface parentEntity = new TestEntity11ParentClass();
  final String parentName = "parent with id 3";
  parentEntity.setEntityId(L_3);
  parentEntity.setName(parentName);

  final TestEntity11ChildInterface childEntity = new TestEntity11ChildClass();
  final String childName = "child of parent with id 3";
  childEntity.setParent(parentEntity);
  childEntity.setName(childName);

  final TestDto11ChildInterface childDto = new TestDto11ChildClass();

  final DTOAssembler assembler =
   DTOAssembler.newAssembler(childDto.getClass(), childEntity.getClass());

  assembler.assembleDto(childDto, childEntity, null, new BeanFactory() {

   public Object get(final String entityBeanKey) {
    if (entityBeanKey.equals("dp.lib.dto.geda.assembler.TestDto11ParentClass")) {
     return new TestDto11ParentClass();
    }
    return null;
   }

  });

  assertEquals(childName, childDto.getName());
  assertNotNull(childDto.getParent());
  assertEquals(parentName, childDto.getParent().getName());
  assertEquals(Long.valueOf(L_3), Long.valueOf(childDto.getParent().getEntityId()));

  final TestEntity11ParentInterface parentEntity2 = new TestEntity11ParentClass();
  final String parentName2 = "parent with id 0";
  parentEntity2.setEntityId(0);
  parentEntity2.setName(parentName2);

  // change dto parent.
  childDto.getParent().setEntityId(0L);
  childDto.setName("child with changed parent");

  final EntityRetriever retriever = new EntityRetriever() {

   @SuppressWarnings("unchecked")
   public Object retrieveByPrimaryKey(final Class entityInterface, final Class entityClass, final Object primaryKey) {
    assertEquals(Long.valueOf(0), primaryKey);
    assertTrue(entityInterface.equals(TestEntity11ParentInterface.class));
    assertTrue(entityClass.equals(TestEntity11ParentClass.class));
    return parentEntity2;
   }

  };

  final Map conv = new HashMap();
  conv.put("retriever", retriever);

  assembler.assembleEntity(childDto, childEntity, conv, new BeanFactory() {

   public Object get(final String entityBeanKey) {
    if (entityBeanKey.equals("dp.lib.dto.geda.assembler.TestEntity11ParentClass")) {
     return new TestEntity11ParentClass();
    }
    return null;
   }

  });

  assertNotNull(childEntity.getParent());
  assertSame(parentEntity2, childEntity.getParent());
  assertEquals("child with changed parent", childEntity.getName());

 }
```


**GENERIC RETRIEVER EXAMPLE FOR HIBERNATE**

```java
package dp.lib.dto.geda.adapter;


public class HibernateEntityRetriever implements EntityRetriever
{

 ...

 /**
  * Simple example of the hibernate Retriever. 1 line!!!
  */
 public Object retrieveByPrimaryKey(Class entityInterface, Class entityClass, Object primaryKey) {
  return hibernateSession.get(entityClass, primaryKey);
 }



}
```


I know it was brief but these example should be more than enough to get you stated.

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
