# DTOHelper

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

> This feature is deprecated and removed since v 2.1.2. Now loading/unloading data to/from DTO is handled by assemblers see "Loading data from DTO to Map or List". 

## What the heck is DTOHelper?

Recently, I received a letter from one of the users of GeDA. He was asking for a new feature - populating DTO from an array. I will be honest and at first I though that this was not a very good idea. I mean why would anybody what to do that? Later I have came back to this, and the reason was that I saw a whole new dimention of use.

One of the cool features that java lacks is ability to easy iterate over fields extract them to an array and so forth. Of course you can do a little bit of trickery through reflection (which is what DTOHelper is doing) but why not just take it for granted with GeDA.

They say that a picture is worth 1000 words, well I think a snippet is like a picture in this sense.

So check this out: 

```java
public void groovyPropertyIteration() {

  final TestDto2Class dto = new TestDto2Class();
  dto.setMyBoolean(true);
  dto.setMyDouble(new Double(2));
  dto.setMyLong(3L);
  dto.setMyString("test");

  Map values = DTOHelper.unloadMap(dto);

  // Iterate over field with two lines of code
  for (String field : values.keySet()) {

   System.out.println("Iterating over field: " + field);

  }

  // Iterate over field values with two lines of code
  for (Object value : values.values()) {

   System.out.println("Iterating over value: " + value);

  }

 }
```


Java is very hard on syntax, but hopefully this groovy new feature will lessen the burden. Enjoy!

P.S. By the way check out the **JUnits** for more examples on loading/unloading from/to Map/Array


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
