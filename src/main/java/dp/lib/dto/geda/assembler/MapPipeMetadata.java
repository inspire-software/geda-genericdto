

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
 * Collection/Map pipe meta contains information on the metadata of the pipe.
 * 
 * @author DPavlov
 */
@SuppressWarnings("unchecked")
public class MapPipeMetadata extends BasePipeMetadata implements dp.lib.dto.geda.adapter.meta.MapPipeMetadata {

	private static final Map<Class, DtoToEntityMatcher> CACHE = new HashMap<Class, DtoToEntityMatcher>();
	
    private final Class< ? extends Collection> dtoMapClass;
    private final Class< ? > entityMapOrCollectionClass;

    private final Class< ? > returnType;
    private final String mapKeyForCollection;
    
    private final DtoToEntityMatcher dtoToEntityMatcher;
    
	/**
     * @param dtoFieldName key for accessing field on DTO object
     * @param entityFieldName key for accessing field on Entity bean
     * @param dtoBeanKey key for constructing DTO bean
     * @param entityBeanKey key for constructing Entity bean
     * @param readOnly read only marker (true then write to entity is omitted)
     * @param dtoMapClass the dto collection class for creating new collection instance
     * @param entityMapOrCollectionClass the entity collection/map class for creating new collection/map instance
     * @param mapKeyForCollection property whose value will be used as key for dto map.
     * @param returnType the generic return time for entity collection
     * @param dtoToEntityMatcherClass matcher for synchronising collections
	 */
	public MapPipeMetadata(final String dtoFieldName, 
							 final String entityFieldName, 
							 final String dtoBeanKey, 
							 final String entityBeanKey,
							 final boolean readOnly,
							 final Class< ? extends Collection> dtoMapClass,
							 final Class< ? > entityMapOrCollectionClass, 
							 final Class< ? > returnType,
							 final String mapKeyForCollection,
							 final Class< ? extends DtoToEntityMatcher> dtoToEntityMatcherClass) {
		
		super(dtoFieldName, entityFieldName, dtoBeanKey, entityBeanKey, readOnly);
		this.dtoMapClass = dtoMapClass;
		this.entityMapOrCollectionClass = entityMapOrCollectionClass;
		this.returnType = returnType;
		this.mapKeyForCollection = mapKeyForCollection;
		
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
	public Class< ? > getDtoMapClass() {
		return dtoMapClass;
	}
	
	/**
	 * @return new collection instance.
	 */
	public Map newDtoMap() {
		return newCollection(getDtoMapClass(), " Dto field: " + this.getDtoFieldName());
	}

	/**
	 * @return entity collection impl class
	 */
	public Class< ? > getEntityMapOrCollectionClass() {
		return entityMapOrCollectionClass;
	}
	
	/**
	 * @return new collection instance.
	 */
	public Object newEntityMapOrCollection() {
		return newCollection(getEntityMapOrCollectionClass(), " Entity field: " + this.getEntityFieldName());
	}


	/**
	 * @return the entity's collection generic type to identity the type of items in entity collection.
	 */
	public Class<?> getReturnType() {
		return returnType;
	}
	
	/**
	 * @return property whose value will be used as key for dto map.
	 */
	public String getMapKeyForCollection() {
		return mapKeyForCollection;
	}

	/**
	 * @return matcher instance that will help synchronize collections.
	 */
	public DtoToEntityMatcher getDtoToEntityMatcher() {
		return dtoToEntityMatcher;
	}

	private  <T> T newCollection(final Class< ? > clazz, final String type) {
        return (T) newBeanForClass(clazz, "Unable to create collection: " + clazz.getCanonicalName() + " for " + type);
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
