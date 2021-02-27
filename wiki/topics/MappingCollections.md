# Mapping collections

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

# How do I map a collection (list, set) of my Entity to collection of my DTO?

**How GeDA handles collections?**

GeDA is based on the hypothesis that all its DTO and Entities confirm to JavaBean specification. This means that GeDA's **@DtoCollection** annotation may only be applied to synchronize objects if each element of the collection confirms to JavaBean spec.

Some Examples:

**OK** Mapping List < SomeDTO > to List < SomeEntity > is ok if SomeDTO and SomeEntity are JavaBeans.

**NOT OK** Mapping List < String > to List < SomeEntity > is not ok, because String does not confirm to JavaBean standard

**BUT** you can map List< WrappedString > to List < SomeEntity > if your WrappedString confirms to JavaBean standard.

**NOT OK** Mapping List < String > to List < String > is not ok, again becaause String is not JavaBean

**BUT** you can use **@DtoField** annotation to do that 

The basic's of GeDA is use of relflection to copy properties between objects. When we come to realm of collections we have a whole new dimention because we need to provide reflection mechanism for each element to copy across the properties AND also we need to keep our collection is syncronized state. 

So let's look at some examples:

**Problem 1: List < String > to List < SomeEntity >**

We have an entity object Person that has a list of Image objects. Image object contains a URL. The task is to map the person object to PersonDTO object so that within the dto there will be a list of urls from the Images list of the entity.

Unfortunatelly, as I mentioned above we cannot map a Collection of Image to collection of String directly. So what we will do is create and ImageDTO that will wrap the String to make the data object confirm to JavaBean standard. 

**The solution:**

Here are samples of Person and Image class.

**The Person Class:**

```java

package dp.lib.dto.geda.sample.entity;

import java.util.List;

/**
 * This is our domain entity class for Person object
 */
public class Person {

 private List < Image > images;

 public List < Image > getImages() {
  return images;
 }

 public void setImages(List < Image > images) {
  this.images = images;
 }

}
```

**The Image Class:**

```java

package dp.lib.dto.geda.sample.entity;

/**
 * This is our domain entity class for Image object
 */
public class Image {

 private String url;

 public String getUrl() {
  return url;
 }

 public void setUrl(String url) {
  this.url = url;
 }

}
```

Here are samples of the DTO objects.

**The Person DTO Class:**

```java

package dp.lib.dto.geda.sample.dto;

import java.util.List;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoCollection;
import dp.lib.dto.geda.sample.dto.matcher.PersonImageMatcher;
import dp.lib.dto.geda.sample.entity.Image;

@Dto
public class PersonDTO {

 @DtoCollection(
   value = "images",  // we need this to know which property is used for entity collection
   dtoToEntityMatcher = PersonImageMatcher.class, // we !always! need a matcher to synchronize our collections
   entityGenericType = Image.class, // we !always! need to define a generic return type (recommeded to use interface)
   dtoBeanKey = "ImageDTO", // we !always! need the wrapper for the values contained within entity collection
                            // this is because the DTO's must follow java bean convension
   entityBeanKey = "Image") // we need the entity bean keys when we are writting back to entity
 private List imageUrls;

 public List getImageUrls() {
  return imageUrls;
 }

 public void setImageUrls(List imageUrls) {
  this.imageUrls = imageUrls;
 }

}
```

**The Person DTO Class:**

```java

package dp.lib.dto.geda.sample.dto;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class ImageDTO {

 @DtoField
 private String url;

 public String getUrl() {
  return url;
 }

 public void setUrl(String url) {
  this.url = url;
 }

}
```

As you can see the **@DtoCollection** annotation requires some mandatory configuration:

**dtoToEntityMatcher** dto matcher is basically a class that knows how to compare DTO collection items with Entity collection items so that when we read/write from or to entity we can update the objects and keep the collections in sync. The implementation of matcher is totally up to you, but from my experience the best way is to **always** specify some sort of GUID for your Entities and DTO's, otherwise you end up with complex matching algorithms that involve a lot of object's properties and at the end still give you anomalies when you have different objects with same data.

Here is the sample PersonImageMatcher used in our example

```java

package dp.lib.dto.geda.sample.dto.matcher;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.sample.dto.ImageDTO;
import dp.lib.dto.geda.sample.entity.Image;

public class PersonImageMatcher implements DtoToEntityMatcher {

 public boolean match(ImageDTO dto, Image entity) {
  // This is actually a very BAD example of matcher but it is like that because of
  // our initial lack of data that allows to perform matching.
  // Ideally it is recommended to have some sort of GUID within DTO objects
  // that would allow
  return dto != null && dto.getUrl() != null
    && entity != null
    && dto.equals(entity.getUrl());
 }

}
```


**entityGenericType** is the type of item within the Entity's collection. The reason for its existance is the hadnling of null collections since the generic type of the collection is hard to handle during runtime. So this way you can ensure the type safeness and give GeDA some hind on how to handle data copying from entity collection items to DTO collection items. It is recommended to use interface as the best Practice. In fact it is always best practice to work with interfaces during runtime and keep the knowledge of your concrete implementation in your bean factories or IoC containers.

**dtoBeanKey** and **entityBeanKey** these two will guide GeDA on how to ask for concremet implementation of new instances from your bean factories that you supply during convertion calls.

Finally, I guess you all curious how to make this darn thing work. Here JUnit test: 

```java

package dp.lib.dto.geda.sample;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.assembler.DTOAssembler;
import dp.lib.dto.geda.sample.dto.ImageDTO;
import dp.lib.dto.geda.sample.dto.PersonDTO;
import dp.lib.dto.geda.sample.entity.Image;
import dp.lib.dto.geda.sample.entity.Person;

public class CollectionMappingTest {

 @Test
 public void testConversion() {

  // we need the DTO bean factory so that we can assemble deeply nested beans within DTO and
  // DTO wrappers for collection objects
  final BeanFactory dtoBeanFactory = new BeanFactory() {

   public Object get(final String dtoBeanKey) {
    if ("ImageDTO".equals(dtoBeanKey)) {
     return new ImageDTO();
    }
    return null;
   }

  };

  // we need the Entity bean factory so that we can assemble deeply nested beans within Entity
  // when we are writing updated data from DTO to Entity
  final BeanFactory entityBeanFactory = new BeanFactory() {

   public Object get(final String entityBeanKey) {
    if ("Image".equals(entityBeanKey)) {
     return new Image();
    }
    return null;
   }

  };

  // Sample empty DTO and custom Entity Person
  final PersonDTO dto = new PersonDTO();
  final Person entity = new Person();

  final Image image1 = new Image();
  image1.setUrl("url1");
  final Image image2 = new Image();
  image2.setUrl("url2");
  entity.setImages(new ArrayList < Image > ());
  entity.getImages().add(image1);
  entity.getImages().add(image2);

  // initialising assembler
  final DTOAssembler da = DTOAssembler.newAssembler(PersonDTO.class, Person.class);

  // do the assembly of DTO from Entity
  da.assembleDto(dto, entity, null, dtoBeanFactory);

  // verify that all data has been copied correctly to DTO
  assertNotNull(dto.getImageUrls());
  assertEquals(2, dto.getImageUrls().size());
  final List < ImageDTO > dtoUrls = dto.getImageUrls();
  assertNotNull(dtoUrls.get(0));
  assertEquals("url1", dtoUrls.get(0).getUrl());
  assertNotNull(dtoUrls.get(1));
  assertEquals("url2", dtoUrls.get(1).getUrl());

  // modify DTO object by adding another image
  final ImageDTO addImage = new ImageDTO();
  addImage.setUrl("newUrl");
  dto.getImageUrls().add(addImage);

  // do the update on the entity from DTO
  da.assembleEntity(dto, entity, null, entityBeanFactory);

  // verify that all data has been copied correctly to Entity
  assertNotNull(entity.getImages());
  assertEquals(3, entity.getImages().size());
  final List  < Image >  entityUrls = entity.getImages();
  assertNotNull(entityUrls.get(0));
  assertEquals("url1", entityUrls.get(0).getUrl());
  assertNotNull(entityUrls.get(1));
  assertEquals("url2", entityUrls.get(1).getUrl());
  assertNotNull(entityUrls.get(2));
  assertEquals("newUrl", entityUrls.get(2).getUrl());


 }

}
```


I know, I know, it's a mouthfull, but look on the bright side, after doing this config you get:

* Fully de-coupled assembler

* On the fly DTO and Entity creation

* Deep object nesting extraction, totally under control of your configuration. You control how deep to go, what entities to extract and how

So I guess overall it seems reasonable, and if you have any suggestion on improvement I am always wellcome to hear them 

**Problem 2: List < String > to List < String >**

Suppose we have a Student entity that hold a list of course code represendes by a String. We want a corresponding DTO to store the data.

This actually simpler than it looks. The String (and Integer and Long and all the rest of them) can be though of a primitive type object - it is concrete and immutable, so in terms of GeDA you can thing of this collection as just a primitive property, since it does not require any special knowledge on how to handle the collection. 

**The Solution:**

**The Student Class** 

```java

package dp.lib.dto.geda.sample.entity;

import java.util.List;

public class Student {

 private List < String > courseCodes;

 public List < String > getCourseCodes() {
  return courseCodes;
 }

 public void setCourseCodes(List < String > courseCodes) {
  this.courseCodes = courseCodes;
 }



}
```

**The Student DTO Class** 

```java

package dp.lib.dto.geda.sample.dto;

import java.util.List;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class StudentDTO {

 @DtoField
 private List < String > courseCodes;

 public List < String > getCourseCodes() {
  return courseCodes;
 }

 public void setCourseCodes(List < String > courseCodes) {
  this.courseCodes = courseCodes;
 }



}
```

**The Test:** 

```java

package dp.lib.dto.geda.sample;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import dp.lib.dto.geda.assembler.DTOAssembler;
import dp.lib.dto.geda.sample.dto.StudentDTO;
import dp.lib.dto.geda.sample.entity.Student;

public class PseudoCollectionMappingTest {

 @Test
 public void testSimple() {

  // Sample empty DTO and custom Entity
  final StudentDTO dto = new StudentDTO();
  final Student entity = new Student();
  entity.setCourseCodes(new ArrayList < String >() { {
   add("MSC1001");
   add("MSC1002");
  } });

  // initialising assembler
  final DTOAssembler da = DTOAssembler.newAssembler(StudentDTO.class, Student.class);

  // do the assembly of DTO from Entity
  da.assembleDto(dto, entity, null, null);

  // verify that all data has been copied correctly to DTO
  assertNotNull(dto.getCourseCodes());
  assertEquals(2, dto.getCourseCodes().size());
  assertEquals("MSC1001", dto.getCourseCodes().get(0));
  assertEquals("MSC1002", dto.getCourseCodes().get(1));

  // modify DTO object by adding another course
  dto.getCourseCodes().add("CS2222");

  // see that the collection is !!shared!!
  // The changes are also have been done in the entity since it is the same collection
  assertEquals(3, entity.getCourseCodes().size());
  assertEquals("CS2222", entity.getCourseCodes().get(2));

 }

}
```

> NOTE that when we changed the DTO's collection we also changed the entity's collection since they are the same thing!. GeDA uses copies accross properties from one object to another so when we are talking about objects we are talking about references and therefore the DTO gets  reference to the collection to which entity's property id referencing. This is very important for collections so be aware.

So how do we actually overcome this? The answer is simple - use converter to tell GeDA hpow you want your collection to be assembled. The easiest way is to create own copy of the collection object. Here is how: 

Modifications to StudenDTO: 

```java

package dp.lib.dto.geda.sample.dto;

import java.util.List;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

@Dto
public class StudentDTO {

 @DtoField(converter = "cloneit")
 private List < String > courseCodes;

 public List < String > getCourseCodes() {
  return courseCodes;
 }

 public void setCourseCodes(List < String > courseCodes) {
  this.courseCodes = courseCodes;
 }



}
```

And the Test:

```java

package dp.lib.dto.geda.sample;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.assembler.DTOAssembler;
import dp.lib.dto.geda.sample.dto.StudentDTO;
import dp.lib.dto.geda.sample.entity.Student;

public class PseudoCollectionMappingTest {

 @Test
 public void testSimple() {

  final ValueConverter cloneit = new ValueConverter() {

   public Object convertToDto(Object object, BeanFactory beanFactory) {
    return new ArrayList((List) object);
   }

   public Object convertToEntity(Object object, Object oldEntity,
     BeanFactory beanFactory) {
    return new ArrayList((List) object);
   }



  };
  final Map < String, ValueConverter > conv = new HashMap < String, ValueConverter > () { {
   put("cloneit", cloneit);
  } };

  // Sample empty DTO and custom Entity
  final StudentDTO dto = new StudentDTO();
  final Student entity = new Student();
  entity.setCourseCodes(new ArrayList < String >() { {
   add("MSC1001");
   add("MSC1002");
  } });

  // initialising assembler
  final DTOAssembler da = DTOAssembler.newAssembler(StudentDTO.class, Student.class);

  // do the assembly of DTO from Entity
  da.assembleDto(dto, entity, conv, null);

  // verify that all data has been copied correctly to DTO
  assertNotNull(dto.getCourseCodes());
  assertEquals(2, dto.getCourseCodes().size());
  assertEquals("MSC1001", dto.getCourseCodes().get(0));
  assertEquals("MSC1002", dto.getCourseCodes().get(1));

  // modify DTO object by adding another course
  dto.getCourseCodes().add("CS2222");

  // see that the collection is NOT shared anymore
  assertEquals(2, entity.getCourseCodes().size());

 }

}
```


Wov, it took longer than I though but I hope it will save you guys some time. Enjoy !


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
