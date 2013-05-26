/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.dsl;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

import java.util.Collection;

/**
 * DSL version of {@link com.inspiresoftware.lib.dto.geda.annotations.DtoCollection}.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 8:40 AM
 */
public interface DtoCollectionContext extends DtoEntityContextAppender {
    /**
     * Map this DTO field to entity field.
     *
     * field name on entity class that will be bound to this dto field
     * (reflection notation e.g. myField.mySubfield).
     *
     * @param fieldName name of field on DTO class
     * @return dto field context
     */
    DtoCollectionContext forField(String fieldName);

    /**
     * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
     * readOnly set to true will be ignored.
     *
     * @return dto field context
     */
    DtoCollectionContext readOnly();

    /**
     * Class that defines the type of class for creating new Domain object collection.
     * Default is {@link java.util.ArrayList}.
     *
     * Assembler will create an instance of this class and set it to entity property during #assembleEntity
     * method call if that property was null.
     *
     * @param entityCollectionClass entity collection class
     * @return dto field context
     */
    DtoCollectionContext entityCollectionClass(Class<? extends Collection> entityCollectionClass);

    /**
     * Key that defines the type of instance to be retrieved from beanFactory
     * for creating new Domain object collection.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleEntity methods.
     *
     * This setting has priority over the {@link #entityCollectionClass(Class)} setting.
     *
     * @param entityCollectionClassKey entity collection class key (requires BeanFactory parameter during assembly)
     * @return dto field context
     */
    DtoCollectionContext entityCollectionClassKey(String entityCollectionClassKey);

    /**
     * Class that defines the type of class for creating new DTO object collection.
     * Default is {@link java.util.ArrayList}
     *
     * @param dtoCollectionClass dto collection class
     * @return  dto field context
     */
    DtoCollectionContext dtoCollectionClass(Class<? extends Collection> dtoCollectionClass);

    /**
     * Key that defines the type of instance to be retrieved from beanFactory
     * for creating new DTO object collection.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * This setting has priority over the {@link #dtoCollectionClass(Class)} setting.
     *
     * @param dtoCollectionClassKey dto collection class key (requires BeanFactory parameter during assembly)
     * @return  dto field context
     */
    DtoCollectionContext dtoCollectionClassKey(String dtoCollectionClassKey);

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * If the collection has deep nested mapping (e.g. myField.myCollection) the key
     * for the Item bean will be the last in this chain (e.g.
     * { "beanWithMyCollectionProperty", "beanCollectionItem" }).
     *
     * @param entityBeanKeys bean keys (requires BeanFactory parameter during assembly)
     * @return dto field context
     */
    DtoCollectionContext entityBeanKeys(String... entityBeanKeys);

    /**
     * Specifies DTO bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * DTO object bean factory key for creating new DTO collection item object instances.
     * To specify the collection instance class use #dtoCollectionClass or #dtoCollectionClassKey.
     *
     * @param dtoBeanKey bean key (requires BeanFactory parameter during assembly)
     * @return dto field context
     */
    DtoCollectionContext dtoBeanKey(String dtoBeanKey);

    /**
     * Entity generic type i.e. the type of collection item for entities. Can be either
     * class or interface to be used for DTO collection items mapping.
     *
     * This property is optional as entity type will be deduced from newly created beans
     * or existing bean classes. Although care should be taken when not specifying this
     * parameter as auto detect concrete classes mapping may make your collections inflexible
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
    DtoCollectionContext entityGenericType(Class entityGenericType);

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * This setting has priority over the {@link #entityGenericType(Class)} setting.
     *
     * @param entityGenericTypeKey entity generic type key (requires BeanFactory parameter during assembly)
     * @return dto field context
     */
    DtoCollectionContext entityGenericTypeKey(String entityGenericTypeKey);

    /**
     * Matcher used to synchronize collection of DTO's and Entities. The matcher is used for
     * writing to entity, so read only collection mapping do not require this.
     *
     * There is no sensible default for this since we are matching incompatible (in theory)
     * types (i.e. DTO with Entity) therefore there are no default implementations that can be
     * used for this.
     *
     * @param dtoToEntityMatcher dto to entity matcher
     * @return dto field context
     */
    DtoCollectionContext dtoToEntityMatcher(Class<? extends DtoToEntityMatcher> dtoToEntityMatcher);

    /**
     * This reference is used to lookup a matcher in adapters map passed into
     * assembleDto and assembleEntity methods. The matcher has to implement
     * {@link com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher} interface.
     *
     * This setting has priority over the {@link #dtoToEntityMatcher(Class)} setting.
     *
     * The matcher is used for writing to entity, so read only collection mapping do not require this.
     *
     * @param dtoToEntityMatcherKey dto to entity matcher key (requires adapters parameter during assembly).
     * @return  dto field context
     */
    DtoCollectionContext dtoToEntityMatcherKey(String dtoToEntityMatcherKey);

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
     * @return entity collection class
     */
    Class getValueOfEntityCollectionClass();

    /**
     * @return entity collection class key
     */
    String getValueOfEntityCollectionClassKey();

    /**
     * @return dto collection class
     */
    Class getValueOfDtoCollectionClass();

    /**
     * @return dto collection class key
     */
    String getValueOfDtoCollectionClassKey();

    /**
     * @return entity generic type
     */
    Class getValueOfEntityGenericType();

    /**
     * @return entity generic type key
     */
    String getValueOfEntityGenericTypeKey();

    /**
     * @return dto to entity matcher class
     */
    Class<? extends DtoToEntityMatcher> getValueOfDtoToEntityMatcher();

    /**
     * @return dto to entity matcher key
     */
    String getValueOfDtoToEntityMatcherKey();
}
