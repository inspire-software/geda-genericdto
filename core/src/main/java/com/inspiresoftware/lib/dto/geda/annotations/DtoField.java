
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */


package com.inspiresoftware.lib.dto.geda.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a field in a DTO. Provides a mapping between the target
 * entity and the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto}
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoField {

	/**
     * field name on entity class that will be bound to this dto field
     * (use "dot" notation for graph path e.g. myField.mySubfield)).
     */
	String value() default "";
	
	/**
     * Textual reference to a converter to use when assembling DTO's and Entities. This reference is
     * used to lookup converter in adapters map passed into assembleDto and assembleEntity methods.
     * This converter must implement {@link com.inspiresoftware.lib.dto.geda.adapter.ValueConverter}.
     *
     * Requires adapters parameter during assembly.
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
	 * {@link com.inspiresoftware.lib.dto.geda.exception.GeDAException}.
	 *
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     */
	String[] entityBeanKeys() default "";

    /**
     * Specifying this value tells assembler that a "sub" assembler is needed to create
     * sub DTO for this field.
     *
     * This annotation is mandatory for nested objects that are used as fields within the top
     * level DTO.
     *
     * Specifies DTO bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     */
    String dtoBeanKey() default "";
		
}
