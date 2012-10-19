/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

import java.util.Map;

/**
 * DSL version of {@link com.inspiresoftware.lib.dto.geda.annotations.DtoMap}.
 *
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 12:39 PM
 */
public interface DtoMapContext extends DtoEntityContextAppender {

    /**
     * Map this DTO field to entity field.
     *
     * field name on entity class that will be bound to this dto field
     * (reflection notation e.g. myField.mySubfield).
     *
     * @param fieldName name of field on DTO class
     * @return dto field context
     */
    DtoMapContext forField(String fieldName);

    /**
     * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
     * readOnly set to true will be ignored.
     *
     * @return dto field context
     */
    DtoMapContext readOnly();

    /**
     * Class that defines the type of class for creating new Domain object collection or map.
     *
     * Default is {@link java.util.HashMap} but this can also be instance of {@link java.util.Collection}.
     *
     * Assembler will create an instance of this class and set it to entity property during #assembleEntity
     * method call if that property was null.
     *
     * @param entityMapOrCollectionClass entity map or collection class
     * @return dto field context
     */
    DtoMapContext entityMapOrCollectionClass(Class entityMapOrCollectionClass);

    /**
     * Key that defines the type of instance to be retrieved from beanFactory
     * for creating new Domain object collection or map.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleEntity methods.
     *
     * This setting has priority over the {@link #entityMapOrCollectionClass(Class)} setting.
     *
     * @param entityMapOrCollectionClassKey  entity map or collection key
     * @return dto field context
     */
    DtoMapContext entityMapOrCollectionClassKey(String entityMapOrCollectionClassKey);

    /**
     * Class that defines the type of class for creating new DTO object map.
     *
     * Default is {@link java.util.HashMap}
     *
     * @param dtoMapClass dto map class
     * @return dto field context
     */
    DtoMapContext dtoMapClass(Class< ? extends Map> dtoMapClass);

    /**
     * Key that defines the type of instance to be retrieved from beanFactory
     * for creating new DTO object map.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * This setting has priority over the {@link #dtoMapClass(Class)} setting.
     *
     * @param dtoMapClassKey dto map class key
     * @return dto field context
     */
    DtoMapContext dtoMapClassKey(String dtoMapClassKey);

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * If the map has deep nested mapping (e.g. myField.myMap) the key
     * for the Item bean will be the last in this chain (e.g.
     * { "beanWithMyMapProperty", "beanMapItem" }).
     *
     * @param entityBeanKeys bean keys
     * @return dto field context
     */
    DtoMapContext entityBeanKeys(String ... entityBeanKeys);

    /**
     * Specifies DTO bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * DTO object bean factory key for creating new DTO map item object instances.
     * To specify the collection instance class use #dtoMapClass or #dtoMapClassKey.
     *
     * @param dtoBeanKey bean key
     * @return dto field context
     */
    DtoMapContext dtoBeanKey(String dtoBeanKey);

    /**
     * Entity generic type i.e. the type of collection/map item for entities. Can be either
     * class or interface to be used for DTO collection items mapping.
     *
     * This property is optional as entity type will be deduced from newly created beans
     * or existing bean classes. Although care should be taken when not specifying this
     * parameter as auto detect concrete classes mapping may make your collections/maps inflexible
     * to accepting other item types (say by interface). It is strongly recommended to
     * provide reasonable interface for this setting.
     *
     * Assembler will automatically generate an internal sub assembler to map DTO items
     * to entity items and this is the class or interface that will be used for
     * creating assembler instance.
     *
     * @param entityGenericType entity generic type
     * @return dto field context
     */
    DtoMapContext entityGenericType(Class entityGenericType);

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * This setting has priority over the {@link #entityGenericType(Class)} setting.
     *
     * @param entityGenericTypeKey entity generic type key
     * @return dto field context
     */
    DtoMapContext entityGenericTypeKey(String entityGenericTypeKey);

    /**
     * If entity property for this field mapping is a collection then this setting maps the property
     * with specified name of collection item on the entity collection item to be the key for
     * the dto map item.
     *
     * e.g. if we have a collection as (pseudo code):
     *          myEntityCollection[ subItem1Entity { myKey: 1, prop1: 1 ... }, ... ]
     *      and we specify
     *          entityCollectionMapKey = "myKey"
     *      this with transform to (pseudo code):
     *          myDtoMap[ mapEntry { key: 1, value: subItem1Dto }, ... ]
     *
     * @param entityCollectionMapKey entity collection map key
     * @return dto field context
     */
    DtoMapContext entityCollectionMapKey(String entityCollectionMapKey);

    /**
     * If entity property is a map there are two options:
     *
     * 1. to use DTO objects as map values (default behaviour when {@link #useEntityMapKey()} is false)
     *    e.g. if we have a map as (pseudo code):
     *              myEntityMap[ mapEntry { key: 1, value: subItem1Entity { myKey: 1, prop1: 1 ... } }, ... ]
     *         and we specify
     *              useEntityMapKey = false;
     *         this with transform to (pseudo code):
     *              myDtoMap[ mapEntry { key: 1, value: subItem1Dto }, ... ]
     *
     * 2. to use DTO's as map keys (set {@link #useEntityMapKey()} to true).
     *    e.g. if we have a map as (pseudo code):
     *              myEntityMap[ mapEntry { key: subItem1Entity { myKey: 1, prop1: 1 ... }, value: "some value" }, ... ]
     *         and we specify
     *              useEntityMapKey = true;
     *         this with transform to (pseudo code):
     *              myDtoMap[ mapEntry { key: subItem1Dto, value: "some value" }, ... ]
     *
     * @return dto field context
     */
    DtoMapContext useEntityMapKey();

    /**
     * Matcher used to synchronize collection of DTO's and Entities.
     * This is different to {@link com.inspiresoftware.lib.dto.geda.annotations.DtoCollection#dtoToEntityMatcher()} since this
     * matcher matches a key from Dto Map to either key of Entity Map or item of entity
     * collection depending on whether entity's property is a collection or a map.
     *
     * There is no sensible default for this since we are matching incompatible (in theory)
     * types therefore there are no default implementations that can be used for this.
     *
     * @param dtoToEntityMatcher dto to entity matcher
     * @return dto field context
     */
    DtoMapContext dtoToEntityMatcher(Class< ? extends DtoToEntityMatcher> dtoToEntityMatcher);

    /**
     * This reference is used to lookup a matcher in adapters map passed into
     * assembleDto and assembleEntity methods. The matcher has to implement
     * {@link DtoToEntityMatcher} interface.
     *
     * This is different to {@link com.inspiresoftware.lib.dto.geda.annotations.DtoCollection#dtoToEntityMatcher()} since this
     * matcher matches a key from Dto Map to either key of Entity Map or item of entity
     * collection depending on whether entity's property is a collection or a map.
     *
     * This setting has priority over the {@link #dtoToEntityMatcher(Class)} setting.
     *
     * @param dtoToEntityMatcherKey dto to entity matcher key
     * @return  dto field context
     */
    DtoMapContext dtoToEntityMatcherKey(String dtoToEntityMatcherKey);

    /**
     * @return dto field name
     */
    String getValueOfDtoField();

    /**
     * @return entity field name
     */
    String getValueOfEntityField();

    /**
     * @return true if read only
     */
    boolean getValueOfReadOnly();

    /**
     * @return entity bean keys
     */
    String[] getValueOfEntityBeanKeys();

    /**
     * @return dto bean key
     */
    String getValueOfDtoBeanKey();

    /**
     * @return entity map/collection class
     */
    Class getValueOfEntityMapOrCollectionClass();

    /**
     * @return entity map/collection class key
     */
    String getValueOfEntityMapOrCollectionClassKey();

    /**
     * @return dto map class
     */
    Class getValueOfDtoMapClass();

    /**
     * @return dto map class key
     */
    String getValueOfDtoMapClassKey();

    /**
     * @return entity generic type
     */
    Class getValueOfEntityGenericType();

    /**
     * @return entity generic type key
     */
    String getValueOfEntityGenericTypeKey();

    /**
     * @return entity collection map key
     */
    String getValueOfEntityCollectionMapKey();

    /**
     * @return true if we are using entity map key
     */
    boolean getValueOfUseEntityMapKey();

    /**
     * @return dto entity matcher class
     */
    Class<? extends DtoToEntityMatcher> getValueOfDtoToEntityMatcher();

    /**
     * @return dto entity matcher key
     */
    String getValueOfDtoToEntityMatcherKey();

}
