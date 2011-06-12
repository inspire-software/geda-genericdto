
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.beans.PropertyDescriptor;

import dp.lib.dto.geda.exception.GeDARuntimeException;
import dp.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Create dynamic classes that will proxy the methods on the original class
 * to avoid using reflection.
 *
 * @author Denis Pavlov
 * @since 1.1.0
 *
 */
public interface MethodSynthesizer {

	/**
	 * Create dynamic class.
	 * 
	 * @param descriptor descriptor whose read method to be examined to generate
	 *        {@link DataReader}.
	 * @return data reader instance.
	 * @throws InspectionPropertyNotFoundException if property cannot be located on the entity 
	 * @throws UnableToCreateInstanceException if unable to create instance of data reader 
	 * @throws GeDARuntimeException unhandled situation with inability to determine return type
	 */
	DataReader synthesizeReader(PropertyDescriptor descriptor) 
		throws InspectionPropertyNotFoundException, UnableToCreateInstanceException, GeDARuntimeException;

	/**
	 * Create dynamic class.
	 * 
	 * @param descriptor descriptor whose write method to be examined to generate
	 *        {@link DataWriter}.
	 * @return data writer instance.
	 * @throws InspectionPropertyNotFoundException if property cannot be located on the entity
	 * @throws UnableToCreateInstanceException if unable to create writer instance
	 */
	DataWriter synthesizeWriter(PropertyDescriptor descriptor) 
		throws InspectionPropertyNotFoundException, UnableToCreateInstanceException;
	
}
