
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.annotations;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;


/**
 * Defines a map to map or a map to collection mapping in DTO.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 11:42:59 AM
 */
@SuppressWarnings("unchecked")
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoMap {

    /**
	 * field name on entity class that will be bound to this dto field
     * (reflection notation e.g. myField.mySubfield).
	 */
	String value() default "";

    /**
	 * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
	 * readOnly set to true will be ignored.
	 */
	boolean readOnly() default false;

	/**
	 * Class that defines the type of class for creating new Domain object collection or map.
     *
	 * Default is {@link java.util.HashMap} but this can also be instance of {@link java.util.Collection}.
     *
     * Assembler will create an instance of this class and set it to entity property during #assembleEntity
     * method call if that property was null.
	 */
	Class entityMapOrCollectionClass() default HashMap.class;

    /**
     * Key that defines the type of instance to be retrieved from beanFactory 
     * for creating new Domain object collection or map.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleEntity methods.
     *
     * This setting has priority over the {@link #entityMapOrCollectionClass()} setting.
     */
    String entityMapOrCollectionClassKey() default "";

    /**
     * Class that defines the type of class for creating new DTO object map.
     *
     * Default is {@link java.util.HashMap}
     */
    Class dtoMapClass() default HashMap.class;

    /**
     * Key that defines the type of instance to be retrieved from beanFactory 
     * for creating new DTO object map.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * This setting has priority over the {@link #dtoMapClass()} setting.
     */
    String dtoMapClassKey() default "";

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * If the map has deep nested mapping (e.g. myField.myMap) the key
     * for the Item bean will be the last in this chain (e.g.
     * { "beanWithMyMapProperty", "beanMapItem" }).
     */
    String[] entityBeanKeys() default "";

    /**
     * Specifies DTO bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * DTO object bean factory key for creating new DTO map item object instances.
     * To specify the collection instance class use #dtoMapClass or #dtoMapClassKey.
     */
    String dtoBeanKey() default "";

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
     */
    Class entityGenericType() default Object.class;

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * This setting has priority over the {@link #entityGenericType()} setting.
     *
     * @since 2.1.0
     */
    String entityGenericTypeKey() default "";

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
     */
    String entityCollectionMapKey() default "";
    
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
     */
    boolean useEntityMapKey() default false;

    /**
     * Matcher used to synchronize collection of DTO's and Entities.
     * This is different to {@link DtoCollection#dtoToEntityMatcher()} since this
     * matcher matches a key from Dto Map to either key of Entity Map or item of entity
     * collection depending on whether entity's property is a collection or a map.
     * The matcher is used for writing to entity, so read only map mapping do not require this.
     *
     * There is no sensible default for this since we are matching incompatible (in theory)
     * types therefore there are no default implementations that can be used for this.
     */
	Class< ? extends DtoToEntityMatcher> dtoToEntityMatcher() default DtoToEntityMatcher.class;
	
	
	/**
     * This reference is used to lookup a matcher in adapters map passed into
     * assembleDto and assembleEntity methods. The matcher has to implement
     * {@link DtoToEntityMatcher} interface.
     *
	 * This is different to {@link DtoCollection#dtoToEntityMatcher()} since this
     * matcher matches a key from Dto Map to either key of Entity Map or item of entity
     * collection depending on whether entity's property is a collection or a map.
     *
     * This setting has priority over the {@link #dtoToEntityMatcher()} setting.
     *
     * The matcher is used for writing to entity, so read only map mapping do not require this.
     *
     * Requires adapters parameter during assembly.
	 */
	String dtoToEntityMatcherKey() default "";

}
