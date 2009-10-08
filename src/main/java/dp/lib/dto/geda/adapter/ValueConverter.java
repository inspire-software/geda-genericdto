/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.adapter;

/**
 * Adapter for data to resolve incompatibilities between DTO and Entities.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public interface ValueConverter {

	/**
	 * Convert one object into another to resolve incompatibilities between DTO and Entities.
	 * @param object the object to convert
	 * @return converted object.
	 */
	Object convertToDto(final Object object);

	/**
	 * Convert one object into another to resolve incompatibilities between DTO and Entities.
	 * @param object the object to convert
	 * @return converted object.
	 */
	Object convertToEntity(final Object object);
	
}
