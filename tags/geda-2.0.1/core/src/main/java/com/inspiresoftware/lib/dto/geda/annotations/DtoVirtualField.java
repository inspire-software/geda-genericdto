
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a virtual field in a DTO. A virtual field does not exist of the entity
 * thus all logic for populating to/from the DTO's field relies on the converter. 
 * Provides a mapping between the target
 * entity and the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto};
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoVirtualField {
	
	/**
	 * Textual reference to converter to use when assembling DTO's and Entities. This is passed in a
	 * converter map an must implement {@link com.inspiresoftware.lib.dto.geda.adapter.ValueConverter}.
	 */
	String converter() default "";
	
	/**
	 * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
	 * readOnly set to true will be ignored.
	 */
	boolean readOnly() default false;

    /**
	 * This annotation is mandatory for {@link DtoVirtualField} with
     * deeply nested entity object i.e. when a '.' syntax is used.
     * Failure to supply this parameter will result in
	 * {@link com.inspiresoftware.lib.dto.geda.exception.GeDAException}.
	 *
	 * Specifies entity bean key that will be used by injected to {@link com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler}
	 * bean factory
	 */
	String[] entityBeanKeys() default "";

    /**
     * This annotation is mandatory for nested objects that are used as fields within the top
     * level DTO.
     */
    String dtoBeanKey() default "";
		
}
