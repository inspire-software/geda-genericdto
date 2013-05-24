
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
 * Defines an object that will serve as a DTO for another entity.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Dto {
	
	/**
	 * Full canonical class (or interface) name that this DTO bounds to.
	 * (e.g. com.inspiresoftware.lib.dto.geda.example.MyEntityClass or
     * com.inspiresoftware.lib.dto.geda.example.MyEntityInterface)
	 */
	String[] value() default "";
	
}