
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
 * Defines a parent object in a DTO. Provides a mapping between the target
 * entity and the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} and used in conjunction with
 * {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField}. This annotation has no influence on 
 * constructing a DTO but provides a means for GeDA to identify that this property is a parent
 * entity when the Entity object is populated with dto data.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoParent {

	/**
     * field name on entity class that will be bound to this dto field
     * (reflection notation e.g. myField.mySubfield).
     *
     * This fields value will be passed on to {@link com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever}
     * as primary key.
	 */
	String value() default "entityId";

	/**
	 * textual reference to {@link com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever} within 
	 * converters map that will do the entity retrieval.
	 */
	String retriever() default "retriever";
		
}
