/**
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

import dp.lib.dto.geda.assembler.DTOAssembler;

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
	 * This annotation is mandatory for {@link Field} with deeply nested entity objects
	 * i.e. when a '.' syntax is used. Failure to supply this parameter will result in 
	 * {@link IllegalArgumentException}.
	 * 
	 * Specifies entity bean key that will be used by injected to {@link DTOAssembler} 
	 * bean factory
	 */
	String[] entityBeanKeys() default "";
	
}
