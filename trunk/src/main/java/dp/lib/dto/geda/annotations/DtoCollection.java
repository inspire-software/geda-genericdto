/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */

package dp.lib.dto.geda.annotations;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;

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
     * Default is {@link java.util.ArrayList}
     */
    Class entityCollectionClass() default ArrayList.class;
    /**
     * Class that defines the type of class for creating new DTO object collection.
     * Default is {@link java.util.ArrayList}
     */
    Class dtoCollectionClass() default ArrayList.class;

    /**
     * Domain object bean factory key for creating new domain object instance
     * within collection.
     */
    String entityBeanKey() default "";

    /**
     * DTO object bean factory key for creating new domain object instance 
     * within collection.
     */
    String dtoBeanKey() default "";

    /**
     * Entity generic type i.e. the type of collection for entities.
     */
    Class entityGenericType() default Object.class;

    /**
     * Matcher used to synchronize collection of DTO's and Entities.
     */
	Class< ? extends DtoToEntityMatcher> dtoToEntityMatcher() default DtoToEntityMatcher.class;

}
