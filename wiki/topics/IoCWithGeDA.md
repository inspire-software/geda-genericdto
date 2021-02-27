# IoC with GeDA

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

## So what should I do when I always use interfaces?

Nowadays it is more apparent to developers that IoC, coding to interface and other SOLID principles are absolute must in the software development.

So how does GeDA fit into all of this? As you might have already noticed GeDA actually targets concrete classes only (although providing some basic functionality with interfaces). This contradicts with the first paragraph but the answer is very simple - it is a technical implementation decision.

For cuorious the more full explanation is:

1. Interfaces define behaviour - they usually spread across several concrete classes that behave in certain way. This is what interfaces are for. So from this point of view GeDA assemblers have nothing to do with interfaces. since they provide coupling between specific DTO implementations and entities.

2. As pointed out above interfaces are implemented (usually) by several classes. If we map interface we map all DTO's to couple the same way to entities and this may not be such a good idea. If you really need to do that use - Abstract concrete classes to provide common mappings.

So how to go about dealing with interfaces in GeDA? Well you need  [[BeanFactories>>doc:GeDA - Generic DTO Assembler.Topics.How to use BeanFactory.WebHome]] - this is why GeDA defined this interface you inject those while invoking methods: assembleDto and assembleEntity the assemble will try to delegate bean instances creation to the bean factories while traversing object.

That's ok but what about the actual construction of the Assembler? This takes concrete classes too? well I am afraid there needs to be a little hack:

```java

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;

public class HowToInterface {

 /**
  * @return factory that is able to create instances of dto.
  */
 BeanFactory getDtoFactory() {
  return ...
 }

 /**
  * @return factory that is able to create instances of classes.
  *         In my opinion this should be a facade for DAO's of your
  *         domain objects, since each DAO is (usually) aware of the
  *         concrete implementations.
  */
 BeanFactory getEntityFactory() {
  return ...
 }

 public static void main(final String ... args) {

  HowToInterface locator = new HowToInterface();

  // we use factories to generate a blank new instance wihich will
  // initialise the assembler.
  DTOAssembler.newAssembler(
    locator.getDtoFactory().get("dto-key").getClass(),
    locator.getEntityFactory().get("entity-key").getClass());


 }

}
```


OK, but I hear yopu saying: "why did not you implement this to be injected inside the constructor so that I do not need to provide these as parameters all over the place?"

Well again the short answer is - technical implementation decision.

The long:

1. Assembler does the assembly - nothing more and nothing less. This is its simple and only responsibility

2. BeanFactory - provides instances by key to provide IoC mechanism that is also its only responsibility

3. Assemble must not do BeanFactories job and vise versa

3. BeanFactories may be pooled which means if Assembler will hold reference to bean factory it may not function well as pooled instance.

4. Same way Assembler may be contained inside a service (it usually should) that is also may be pooled. Same problem as with 3.

5. Assembler and BeanFactory must be kept stateless for reuse - therefore they should be coupled only for the time of processing and entity/dto then decoupled back. 

I hope this explanation will help you understand better why GeDA is like it is.

And remember there is always a trade off between simplicity and good architectual design.

Code well, peace out...


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
