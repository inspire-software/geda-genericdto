# Complex Class Hierarchies

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

It is quite often the case that the relations in your Domain model are quite complex and if not careful with DTO mappings you may be doing more than you intended to do.

For Example consider a basic Node tree of objects, say a Category hierarchy.

```java
class Category {

  private String id;
  private String name;

  private Category parent;
  private Set<Category> children;

  ... getters/setters ...

}

```

If we have just a one to one mapping to DTO like so:

```java
@Dto
class CategoryDto {

  @DtoField
  private String id;
  @DtoField
  private String name;

  @DtoField(dtoBeanKey = "category")
  private CategoryDto parent;
  @DtoCollection(dtoBeanKey = "category", ...)
  private Set<CategoryDto> children;

  ... getters/setters ...

}

```

It becomes immediately obvious that running dto assembly will result in full category hierarchy being mapped.

> This is a **bad practice** approach

```java
...
final Category cat = categoryDAO.find(...);
final CategoryDTO dto = new CategoryDto();

// The assembler will dynamically navigate via parent and children of every encountered category entity
assembler.assembleDto(dto, cat, adapters, beanFactory);
...
```

So what is the good practice approach?

You need to think about how Dto's are modelled and minimum amount of data that you require.

With respect to above example the minimum amount of data we need about a Category is the id and name. So lets see what happens to our Dtos

```java
@Dto
class BasicCategoryDto {

  @DtoField
  private String id;
  @DtoField
  private String name;

  ... getters/setters ...

}


@Dto
class CategoryDto extends BasicCategoryDto {

  @DtoField(dtoBeanKey = "category")
  private BasicCategoryDto parent;
  @DtoCollection(dtoBeanKey = "category", ...)
  private Set<BasicCategoryDto> children;

  ... getters/setters ...

}

```

So where do we end up with this when we run the same assembly code?

```java
...
final Category cat = categoryDAO.find(...);
final CategoryDTO dto = new CategoryDto();

// The assembler will dynamically navigate via parent and children of every encountered category entity
// but since the parent and children are BasicCategoryDto the assembler will stop providing us
// with CategoryDto that has all information and basic information for parent and children
assembler.assembleDto(dto, cat, adapters, beanFactory);
...
```

This is just a basic example of how you can restructure you DTO's to achieve maximum efficiency.

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

