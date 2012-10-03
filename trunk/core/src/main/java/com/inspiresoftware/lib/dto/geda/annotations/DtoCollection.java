
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
import java.util.ArrayList;


/**
 * Defines a collection in DTO.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 11:42:59 AM
 */
@SuppressWarnings("unchecked")
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoCollection {

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
	 * Class that defines the type of class for creating new Domain object collection.
	 * Default is {@link java.util.ArrayList}.
     *
     * Assembler will create an instance of this class and set it to entity property during #assembleEntity
     * method call if that property was null.
     */
	Class entityCollectionClass() default ArrayList.class;

    /**
     * Key that defines the type of instance to be retrieved from beanFactory 
     * for creating new Domain object collection.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleEntity methods.
     *
     * This setting has priority over the {@link #entityCollectionClass()} setting.
     */
    String entityCollectionClassKey() default "";

    /**
     * Class that defines the type of class for creating new DTO object collection.
     * Default is {@link java.util.ArrayList}
     */
    Class dtoCollectionClass() default ArrayList.class;

    /**
     * Key that defines the type of instance to be retrieved from beanFactory 
     * for creating new DTO object collection.
     *
     * Specifies bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * This setting has priority over the {@link #dtoCollectionClass()} setting.
     */
    String dtoCollectionClassKey() default "";

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * If the collection has deep nested mapping (e.g. myField.myCollection) the key
     * for the Item bean will be the last in this chain (e.g.
     * { "beanWithMyCollectionProperty", "beanCollectionItem" }).
     */
    String[] entityBeanKeys() default "";

    /**
     * Specifies DTO bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * DTO object bean factory key for creating new DTO collection item object instances.
     * To specify the collection instance class use #dtoCollectionClass or #dtoCollectionClassKey.
     */
    String dtoBeanKey() default "";

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
     */
    Class entityGenericType() default Object.class;

    /**
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * This setting has priority over the {@link #entityGenericType()} setting.
     *
     * @since 2.0.4
     */
    String entityGenericTypeKey() default "";

    /**
     * Matcher used to synchronize collection of DTO's and Entities.
     *
     * There is no sensible default for this since we are matching incompatible (in theory)
     * types (i.e. DTO with Entity) therefore there are no default implementations that can be
     * used for this.
     */
	Class< ? extends DtoToEntityMatcher> dtoToEntityMatcher() default DtoToEntityMatcher.class;
	
	/**
     * This reference is used to lookup a matcher in adapters map passed into
     * assembleDto and assembleEntity methods. The matcher has to implement
     * {@link DtoToEntityMatcher} interface.
     *
	 * This setting has priority over the {@link #dtoToEntityMatcher()} setting.
	 */
	String dtoToEntityMatcherKey() default "";

}
