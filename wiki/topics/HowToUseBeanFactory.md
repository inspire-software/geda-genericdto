# How to use BeanFactory

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

Bean factories is GeDA's mechanism to inject your logic of how the object instances should be created.
The bean factories are facilitated by BeanFactory interface, which has and extension ExtensibleBeanFactory, which support DSL. To set obvious questions aside - the implementations for it is not part of GeDA core because GeDA's responsibility is to map classes and transfer data, it is not suppose to interfere with object creation processes of your application. To get a bit technical this is also related to java class loading mechanism and allowing GeDA to cope with any environment that it is used in (e.g. OSGi).

The primary benefit of using BeanFactories are:

* You control the instance creation process. You can use Class.forName(), new MyObject(), cloning prototype objects or any other mechanism - whichever approach gives your application best performance and convenience.
* BeanFactory is also a buffer where you can pre initialise your objects, such as populating immutable data only set through constructors or setting object creation timestamps

So what does BeanFactory look like:

**BeanFactory**

```java

   public interface BeanFactory {                                                                                                                                                                          
                                                                                                                                                                                                        
    /**                                                                                                                                  
    * Return a class or interface that is applicable for given bean key.                                                                 
    *                                                                                                                                    
    * NOTE: this is not a convenience method for #get(entityBeanKey).getClass()                                                          
    *       this method should return a class for DTO instances and an interface                                                         
    *       for entity key whenever possible to keep IoC at maximum and make                                                             
    *       assemblers as generic as possible.                                                                                           
    *                                                                                                                                    
    * @param entityBeanKey string key reference to the bean class or interface required                                                  
    * @return class or interface that best represent bean objects by given key                                                           
    *                                                                                                                                     
     * @since 2.1.0                                                                                                                       
     */                                                                                                                                                                                                 
    Class getClazz(String entityBeanKey);                                                                                                                                                               
                                                                                                                                                                                                        
        /**                                                                                                                                                                                             
     * Returns object instance for given class. Default assumption is                                                                                                                                   
     * that if no mapping is present for this key the factory will return null,                                                                                                                         
     * which will cause Assembler to throw GeDAException.                                                                                                                                               
     *                                                                                                                                                                                                  
     * However it is up to implementor of this interface if an exception is to be thrown                                                                                                                
     * in such cases.                                                                                                                                                                                   
     *                                                                                                                                                                                                  
     * @param entityBeanKey string key reference to the bean required                                                                                                                               
     * @return new domain entity instance                                                                                                                                                           
     */                                                                                                                                                                                             
     Object get(String entityBeanKey);                                                                                                                                                               
                                                                                                                                                                                                        
}
```

So only two methods:

* getCalss(String) - which tells GeDA the class it should be using for mapping (for Entities this can be an Interface)
* get(String) - which provides GeDA with new object instances upon request

GeDA uses BeanFactory objects as a parameter to assembleDto() and assembleEntity(). Strictly speaking for GeDA there is no difference what object it is as long as it provides a Class and an Object instance by key. You can have a unified BeanFactory containing both DTO and Entities or you can have separate factories for DTO's and for Entities.

* assembleDto() will only use BeanFactory using DTO keys
* assembleEntity() will only use BeanFactory using Entity keys.

Extensible Bean factory enhances Bean factory interface to provide interactive access through [[DSL>>url:http://www.inspire-software.com/confluence/display/GeDA/GeDA+DSL+mappings]] registry.

**ExtensibleBeanFactory**

```java

    public interface ExtensibleBeanFactory extends BeanFactory, DisposableContainer {                                                                                                                       
                                                                                                                                                                                                        
    /**                                                                                                                                                                                                 
     * Allows to enrich bean factory with new bean mappings                                                                                                                                             
     *                                                                                                                                                                                                  
     * @param key string key for this class (interface name preferred)                                                                                                                                  
     * @param className fully qualified string representation of java class.                                                                                                                            
     *        No check is made regarding the validity of this class and if it is invalid                                                                                                                
     *        will cause exception during #get()                                                                                                                                                        
     *                                                                                                                                                                                                  
     * @throws IllegalArgumentException if either parameters are null or empty, or if                                                                                                                   
     *         this key is already used.                                                                                                                                                                
     */                                                                                                                                                                                                 
    void registerDto(final String key, final String className) throws IllegalArgumentException;                                                                                                         
                                                                                                                                                                                                        
                                                                                                                                                                                                        
    /**                                                                                                                                                                                                 
     * Allows to enrich bean factory with new bean mappings                                                                                                                                             
     *                                                                                                                                                                                                  
     * @param key string key for this class (interface name preferred)                                                                                                                                  
     * @param className fully qualified string representation of java class.                                                                                                                            
     *        No check is made regarding the validity of this class and if it is invalid                                                                                                                
     *        will cause exception during #get()                                                                                                                                                        
     * @param representative fully qualified string representation of a java interface                                                                                                                  
     *        that best describes #className class.                                                                                                                                                     
     *                                                                                                                                                                                                  
     * @throws IllegalArgumentException if either parameters are null or empty, or if                                                                                                                   
     *         this key is already used.                                                                                                                                                                
     */                                                                                                                                                                                                 
    void registerEntity(final String key, final String className, final String representative) throws IllegalArgumentException;                                                                         
                                                                                                                                                                                                        
}
```

This interface add two more methods:

* registerDto() which provides key to class mapping for DTO. So BeanFactory.getClazz() should return the Class you provided and BeanFactory.get() object instance of that class.
* registerEntity() which provides key to class to interface mapping for Entity. So BeanFactory.getClazz() should return the Interface you provided and BeanFactory.get() object instance of the Class you provided. However you can implement this to ignore 3rd parameter if you are not using entity interfaces.

The simplest (and inefficient) way of implementing a bean factory you can have a set of if statements:

**Simple and Inefficient**

```java

...

Class getClazz(String entityBeanKey) {

    if ("MyDto".equals(entityBeanKey)) {
        return MyDto.class;
    } else if ....

}                                                                                                                                                             
                                                                                                                                                                                                        
Object get(String entityBeanKey) {
    if ("MyDto".equals(entityBeanKey)) {
        return new MyDto();
    } else if ....

}

...
```

Slightly better version is through hash maps:

**Slightly better with Maps**

```java

...
private Map<String, Class> mapping = ... // Can be passed to constructor for example
...

Class getClazz(String entityBeanKey) {

    if (mapping.containsKey(entityBeanKey)) {
        return mapping.get(entityBeanKey);
    } else  .... throw exception ...

}                                                                                                                                                             
                                                                                                                                                                                                        
Object get(String entityBeanKey) {
    if (mapping.containsKey(entityBeanKey)) {
        try {
           return mapping.get(entityBeanKey).newInstance();
        } catch (...) {
            .... throw exception ...
        }
    } else  .... throw exception ...

}

...
```

|(% style="color:green" %){{icon name="check"/}} For more sophisticated examples have a look at the default implementations for spring-integration (DTOFactoryImpl) and osgi (OSGiBundleDTOFactoryImpl) modules


[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
