

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;

/**
 * Collection pipe meta contains information on the metadata of the pipe.
 * 
 * @author DPavlov
 */
@SuppressWarnings("unchecked")
public class CollectionPipeMetadata extends BasePipeMetadata implements dp.lib.dto.geda.adapter.meta.CollectionPipeMetadata {

	private static final Map<Class, DtoToEntityMatcher> CACHE = new HashMap<Class, DtoToEntityMatcher>();
	
    private final Class< ? extends Collection> dtoCollectionClass;
    private final Class< ? extends Collection> entityCollectionClass;

    private final Class< ? > returnType;
    private final DtoToEntityMatcher dtoToEntityMatcher;

	/**
     * @param dtoFieldName key for accessing field on DTO object
     * @param entityFieldName key for accessing field on Entity bean
     * @param dtoBeanKey key for constructing DTO bean
     * @param entityBeanKey key for constructing Entity bean
     * @param readOnly read only marker (true then write to entity is omitted)
     * @param dtoCollectionClass the dto collection class for creating new collection instance
     * @param entityCollectionClass the entity collection class for creating new collection instance
     * @param returnType the generic return time for entity collection
     * @param dtoToEntityMatcherClass matcher for synchronising collections
	 */
	public CollectionPipeMetadata(final String dtoFieldName, 
							 final String entityFieldName, 
							 final String dtoBeanKey, 
							 final String entityBeanKey,
							 final boolean readOnly,
							 final Class< ? extends Collection> dtoCollectionClass,
							 final Class< ? extends Collection> entityCollectionClass, 
							 final Class< ? > returnType,
							 final Class< ? extends DtoToEntityMatcher> dtoToEntityMatcherClass) {
		
		super(dtoFieldName, entityFieldName, dtoBeanKey, entityBeanKey, readOnly);
		this.dtoCollectionClass = dtoCollectionClass;
		this.entityCollectionClass = entityCollectionClass;
		this.returnType = returnType;
		
		if (CACHE.containsKey(dtoToEntityMatcherClass)) {
			this.dtoToEntityMatcher = CACHE.get(dtoToEntityMatcherClass);
		} else {
			this.dtoToEntityMatcher = newBeanForClass(
					dtoToEntityMatcherClass, "Unable to create matcher: " + dtoToEntityMatcherClass.getCanonicalName()
	                + " for: " + this.getDtoBeanKey() + " - " + this.getEntityBeanKey());
			CACHE.put(dtoToEntityMatcherClass, this.dtoToEntityMatcher);
		}

	}

	/**
	 * @return DTO collection impl class
	 */
	public Class< ? extends Collection> getDtoCollectionClass() {
		return dtoCollectionClass;
	}
	
	/**
	 * @return new collection instance.
	 */
	public Collection newDtoCollection() {
		return newCollection(getDtoCollectionClass(), " Dto field: " + this.getDtoFieldName());
	}

	/**
	 * @return entity collection impl class
	 */
	public Class< ? extends Collection> getEntityCollectionClass() {
		return entityCollectionClass;
	}
	
	/**
	 * @return new collection instance.
	 */
	public Collection newEntityCollection() {
		return newCollection(getEntityCollectionClass(), " Entity field: " + this.getEntityFieldName());
	}


	/**
	 * @return the entity's collection generic type to identity the type of items in entity collection.
	 */
	public Class< ? > getReturnType() {
		return returnType;
	}

	/**
	 * @return matcher instance that will help synchronize collections.
	 */
	public DtoToEntityMatcher getDtoToEntityMatcher() {
		return dtoToEntityMatcher;
	}

    private Collection newCollection(final Class< ? extends Collection> clazz, final String type) {
        return newBeanForClass(clazz, "Unable to create collection: " + clazz.getCanonicalName() + " for " + type);
    }

    private <T> T newBeanForClass(final Class<T> clazz, final String errMsg) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException iex) {
            throw new IllegalArgumentException(errMsg, iex);
        } catch (IllegalAccessException iaex) {
            throw new IllegalArgumentException(errMsg, iaex);
        }
    }
	
}
