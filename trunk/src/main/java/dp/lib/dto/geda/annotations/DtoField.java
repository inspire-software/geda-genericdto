/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */
package dp.lib.dto.geda.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a field in a DTO. Provides a mapping between the target
 * entity and the {@link dp.lib.dto.geda.annotations.Dto};
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoField {

	/**
	 * textual reference to field that will be binded to this field (reflection notation).
	 */
	String value();
	
	/**
	 * Textual reference to converter to use when assembling DTO's and Entities.
	 */
	String converter() default "";
	
	/**
	 * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
	 * readOnly set to true will be ignored.
	 */
	boolean readOnly() default false;

    /**
	 * This annotation is mandatory for {@link DtoField} with
     * deeply nested entity object i.e. when a '.' syntax is used.
     * Failure to supply this parameter will result in
	 * {@link IllegalArgumentException}.
	 *
	 * Specifies entity bean key that will be used by injected to {@link dp.lib.dto.geda.assembler.DTOAssembler}
	 * bean factory
	 */
	String[] entityBeanKeys() default "";

    /**
     * This annotation is mandatory for nested objects that are used as fields within the top
     * level DTO.
     */
    String[] dtoBeanKeys() default "";
		
}
