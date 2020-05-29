# Composite DTO Objects

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

> This feature is available since **v.2.0.2**

> **Feature limitation**
assembleEntities(Collection, Collection) method is not currently supported by this feature and will throw an UnsupportedOperationException

## The Problem

In many cases our domain model is much more hierarchical than our presentation layer. This is due to the fact that when designing domain model we often want to normalise data to enforce referential integrity, ensure there is no duplication and provide flexible design. The presentation layer goal is however different - its purpose is to ensure maximum ease of use of the system for the end user. Therefore it is quite common that some of the input forms are actually represented by several domain objects in the persistence layer.

Consider the following example where we provide design for the domain model entities Person and Address to make it flexible for person to potentially have many contact addresses but on our presentation layer (say a registration form) we would like to present a single form for a person to input all their detail.


![geda-composite-dto.png](https://github.com/inspire-software/geda-genericdto/blob/master/wiki/topics/geda-composite-dto.png?raw=true)

## Solution

With GeDA 2.0.2 it is possible to map a single DTO class to several domain entities. So if we consider that we have:

* Two domain objects Person and Address
* Single ContactDTO object to capture contact form input
* A DAO and a Service layer

We may end up with DTO to Entity classes mapping that looks like this:

**PersonImpl**

```java
package com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.impl;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;

public class PersonImpl implements Person {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
}
```

**AddressImpl**

```java
package com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.impl;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Address;

public class AddressImpl implements Address {


    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postCode;
    private String country;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(final String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
```

**ContactDTOImpl**

```java
package com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.ContactDTO;

@Dto
public class ContactDTOImpl implements ContactDTO {

    @DtoField
    private String firstName;
    @DtoField
    private String lastName;

    @DtoField
    private String addressLine1;
    @DtoField
    private String addressLine2;
    @DtoField
    private String city;
    @DtoField
    private String postCode;
    @DtoField
    private String country;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(final String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
```

Example DAO and Service layer interfaces like these:

**PersonDAO**

```java
package com.inspiresoftware.lib.dto.geda.examples.addressbook.service;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Address;
import com.inspiresoftware.lib.dto.geda.examples.addressbook.domain.Person;

import java.util.List;

public interface PersonDAO {

    Person addPerson(String firstName, String lastName);

    Address addAddress(Person person, String street, String city, String postCode, String country);

    List<Person> findByFirstName(String firstName);

    Address lookUpAddress(Person person);

}
```

**AddressBookService**

```java

package com.inspiresoftware.lib.dto.geda.examples.addressbook.service;

import com.inspiresoftware.lib.dto.geda.examples.addressbook.dto.ContactDTO;

import java.util.List;

public interface AddressBookService {


    List<ContactDTO> getContactsByName(String firstName);

}
```

**Example implementation of the getContactByFirstName method**

```java
    public List<ContactDTO> getContactsByName(final String firstName) {


 // use DAO to retrieve people objects
        final List<Person> people = personDAO.findByFirstName(firstName);


 // use DAO to lookup addresses and assemble [Person, Address] pair as array
        final List<Object[]> entities  = new ArrayList<Object[]>();

        for (Person person : people) {

            entities.add(new Object[] {
                    person,
                    personDAO.lookUpAddress(person)
            });

        }


 // use Composite Assembler factory method and define the array of classes 
 // that represent entities in arrays of object instances that are passed as
 // "entity" parameter to assembly methods
        final Assembler asm = DTOAssembler.newCompositeAssembler(
                ContactDTOImpl.class, new Class[] { Person.class, Address.class });

        final List<ContactDTO> dtos = new ArrayList<ContactDTO>();


 // perform assembly using same API methods as for single DTO conversion
        asm.assembleDtos(dtos, entities, null, null);

        return dtos;
    }
```

In our controller we may do the following to preload the input form backed by ContactDTO object:

**Load the data into DTO**

```java
        final List<ContactDTO> contacts = service.getContactsByName("John H.");
```

**Notes**

Working with composite DTO's is exactly the same as working with simple one to one DTO to Entity assemblies. The two small differences are:

* Use correct DTOAssembler.newCompositeAssembler or DTOAssembler.newCustomCompositeAssembler
* When using assemble* methods provide array of entity objects (order does not matter and you DO NOT have to provide all entity instances - a partial load)

Few things to mention:

* Order of interfaces in the factory methods does not matter, nor does it matter in the assembly methods. However, the order of assembly follows the elements sequence passed to the assemble* method. So if we specify *assemleDto(dto, new Object[] { foo, bar }, null, null)* then **foo** will be used first to populate dto, then **bar**.
* Number of arguments to assemble* methods also does not matter. E.g. *assemleDto(dto, new Object[] { foo, bar }, null, null)* or **assemleDto(dto, bar, null, null)** or **assemleDto(dto, foo, null, null)** or *assemleDto(dto, new Object[] { foo }, null, null)* are **ALL VALID** uses but quite obviously some of these will result in partial DTO load (which may be desirable in some cases e.g. if we want to preset first name and last name in our form but not the address)
* Entity arguments passed to assemble* methods whose classes do not match the classes specified in factory method (e.g. DTOAssembler.newCompositeAssembler) are simply **IGNORED** (without any warnings). __This is by design__ as otherwise we would need to specify fixed number of argument for entity array.

Hope that this feature will make your work with GeDA event more enjoyable!

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
