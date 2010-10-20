
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.adapter.meta;

import java.util.Collection;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;

/**
 * Metadata specific to collection pipes.
 * 
 * @author DPavlov
 */
@SuppressWarnings("unchecked")
public interface CollectionPipeMetadata extends PipeMetadata {

	/**
	 * @return DTO collection impl class
	 */
	Class< ? extends Collection> getDtoCollectionClass();

	/**
	 * @return new collection instance.
	 */
	Collection newDtoCollection();

	/**
	 * @return entity collection impl class
	 */
	Class< ? extends Collection> getEntityCollectionClass();

	/**
	 * @return new collection instance.
	 */
	Collection newEntityCollection();

	/**
	 * @return the entity's collection generic type to identity the type of items in entity collection.
	 */
	Class< ? > getReturnType();

	/**
	 * @return matcher instance that will help synchronize collections.
	 */
	DtoToEntityMatcher getDtoToEntityMatcher();

}
