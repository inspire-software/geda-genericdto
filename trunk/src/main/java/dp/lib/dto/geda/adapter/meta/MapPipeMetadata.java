
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.adapter.meta;

import java.util.Map;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Metadata specific to collection pipes.
 * 
 * @author DPavlov
 */
@SuppressWarnings("unchecked")
public interface MapPipeMetadata extends PipeMetadata {

	/**
	 * @return DTO map impl class
	 */
	Class getDtoMapClass();

	/**
	 * @return new map instance.
	 * 
	 * @throws UnableToCreateInstanceException  if unable to create collection instance
	 */
	Map newDtoMap() throws UnableToCreateInstanceException;

	/**
	 * @return entity collection/map impl class
	 */
	Class getEntityMapOrCollectionClass();

	/**
	 * @return new collection instance.
	 * 
	 * @throws UnableToCreateInstanceException if unable to create collection instance 
	 */
	Object newEntityMapOrCollection() throws UnableToCreateInstanceException;

	/**
	 * @return the entity's collection/ map item generic type to identity the type of items in entity collection.
	 */
	Class< ? > getReturnType();
	
	/**
	 * @return property whose value will be used as key for dto map.
	 */
	String getMapKeyForCollection();
	
	/**
	 * @return true if map key is entity object, false if map value is entity object.
	 */
	boolean isEntityMapKey();

	/**
	 * @return matcher instance that will help synchronize collections/maps.
	 */
	DtoToEntityMatcher getDtoToEntityMatcher();

}
