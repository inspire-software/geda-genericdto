
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package dp.lib.dto.geda.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;

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
	 * textual reference to field that will be binded to this field (reflection notation).
	 */
	String value();

    /**
	 * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
	 * readOnly set to true will be ignored.
	 */
	boolean readOnly() default false;

    /**
     * Class that defines the type of class for creating new Domain object collection.
     * Default is {@link java.util.HashMap} but this can also be instance of {@link java.util.Collection}
     */
    Class entityMapOrCollectionClass() default HashMap.class;
    /**
     * Class that defines the type of class for creating new DTO object collection.
     * Default is {@link java.util.ArrayList}
     */
    Class dtoMapClass() default HashMap.class;

    /**
     * Domain object bean factory key for creating new domain object instance
     * within collection.
     */
    String[] entityBeanKeys() default "";

    /**
     * DTO object bean factory key for creating new domain object instance 
     * within collection.
     */
    String dtoBeanKey() default "";

    /**
     * Entity generic type i.e. the type of collection/map item for entities. This cannot be extracted 
     * through reflection API since collection might be null.
     */
    Class entityGenericType() default Object.class;
    
    /**
     * If entity property is a collection this maps a property of collection item
     * to be the key in the dto map.
     */
    String entityCollectionMapKey() default "";
    
    /**
     * If entity property is a map there are two options: to use map values as source
     * for DTO's (default behaviour when {@link #useEntityMapKey()} is false); alternatively
     * use map keys as source for DTO's (set {@link #useEntityMapKey()} to true).
     */
    boolean useEntityMapKey() default false;

    /**
     * Matcher used to synchronize collection of DTO's and Entities.
     * This is different to {@link DtoCollection#dtoToEntityMatcher()} since this
     * matches matches a key from Dto Map to either key of Entity Map or item of entity
     * collection depending on whether entity's property is a collection or a map.
     */
	Class< ? extends DtoToEntityMatcher> dtoToEntityMatcher() default DtoToEntityMatcher.class;

}
