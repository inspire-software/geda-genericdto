

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Collection/Map pipe meta contains information on the metadata of the pipe.
 * 
 * @author DPavlov
 */
@SuppressWarnings("unchecked")
public class MapPipeMetadata extends BasePipeMetadata implements com.inspiresoftware.lib.dto.geda.assembler.meta.MapPipeMetadata {

	private static final Map<Class, DtoToEntityMatcher> CACHE = new HashMap<Class, DtoToEntityMatcher>();
	
	private final Class< ? extends Collection> dtoMapClass;
    private final String dtoMapClassKey;
    private final Class< ? > entityMapOrCollectionClass;
    private final String entityMapOrCollectionClassKey;

    private final Class< ? > returnType;
    private final String returnTypeKey;
    private final String mapKeyForCollection;
    private final boolean entityMapKey;
    
    private final DtoToEntityMatcher dtoToEntityMatcher;
    private final String dtoToEntityMatcherKey;
    
	/**
     *
     * @param dtoFieldName key for accessing field on DTO object
     * @param entityFieldName key for accessing field on Entity bean
     * @param dtoBeanKey key for constructing DTO bean
     * @param entityBeanKey key for constructing Entity bean
     * @param readOnly read only marker (true then write to entity is omitted)
     * @param dtoMapClass the dto collection class for creating new collection instance
     * @param dtoMapClassKey key for dto map class fetched from beanFactory
     * @param entityMapOrCollectionClass the entity collection/map class for creating new collection/map instance
     * @param entityMapOrCollectionClassKey key for entity collection/map class fetched from beanFactory
     * @param returnType the generic type for entity collection/map item
     * @param returnTypeKey bean factory key for generic type for entity collection/map item
     * @param mapKeyForCollection property whose value will be used as key for dto map.
     * @param entityMapKey true if map key is entity object, false if map value is entity object.
     * @param dtoToEntityMatcherClass matcher for synchronising collections
     * @param dtoToEntityMatcherKey key of matcher in the converters map
     * @throws UnableToCreateInstanceException if unable to create item matcher
	 */
	public MapPipeMetadata(final String dtoFieldName,
                           final String entityFieldName,
                           final String dtoBeanKey,
                           final String entityBeanKey,
                           final boolean readOnly,
                           final Class<? extends Collection> dtoMapClass,
                           final String dtoMapClassKey,
                           final Class<?> entityMapOrCollectionClass,
                           final String entityMapOrCollectionClassKey,
                           final Class<?> returnType,
                           final String returnTypeKey, final String mapKeyForCollection,
                           final boolean entityMapKey,
                           final Class<? extends DtoToEntityMatcher> dtoToEntityMatcherClass,
                           final String dtoToEntityMatcherKey) throws UnableToCreateInstanceException {
		
		super(dtoFieldName, entityFieldName, dtoBeanKey, entityBeanKey, readOnly);
		this.dtoMapClass = dtoMapClass;
        this.returnTypeKey = returnTypeKey != null && returnTypeKey.length() > 0 ? returnTypeKey : null;
        this.dtoMapClassKey = dtoMapClassKey != null && dtoMapClassKey.length() > 0 ? dtoMapClassKey : null;
		this.entityMapOrCollectionClass = entityMapOrCollectionClass;
		this.entityMapOrCollectionClassKey = 
			entityMapOrCollectionClassKey != null && entityMapOrCollectionClassKey.length() > 0 ? entityMapOrCollectionClassKey : null;
		this.returnType = returnType;
		this.mapKeyForCollection = mapKeyForCollection;
		this.entityMapKey = entityMapKey;
		
		if (dtoToEntityMatcherKey == null || dtoToEntityMatcherKey.length() == 0) {
			if (CACHE.containsKey(dtoToEntityMatcherClass)) {
				this.dtoToEntityMatcher = CACHE.get(dtoToEntityMatcherClass);
			} else {
				this.dtoToEntityMatcher = newBeanForClass(
						dtoToEntityMatcherClass, "Unable to create matcher: {0} for: {1} - {2}",
                        dtoToEntityMatcherClass, this.getDtoBeanKey(), this.getEntityBeanKey());
				CACHE.put(dtoToEntityMatcherClass, this.dtoToEntityMatcher);
			}
			this.dtoToEntityMatcherKey = null;
		} else {
			this.dtoToEntityMatcher = null;
			this.dtoToEntityMatcherKey = dtoToEntityMatcherKey;
		}

	}
	
	/** {@inheritDoc} */
	public Map newDtoMap(final BeanFactory beanFactory) throws UnableToCreateInstanceException, BeanFactoryNotFoundException {
		if (this.dtoMapClassKey != null) {
			return newCollection(this.dtoMapClassKey, beanFactory, true);
		}
		return newCollection(this.dtoMapClass, " Dto field: ", this.getDtoFieldName());
	}
	
	/** {@inheritDoc} */
	public Object newEntityMapOrCollection(final BeanFactory beanFactory) throws UnableToCreateInstanceException, BeanFactoryNotFoundException {
		if (this.entityMapOrCollectionClassKey != null) {
			return newCollection(this.entityMapOrCollectionClassKey, beanFactory, false);
		}
		return newCollection(this.entityMapOrCollectionClass, " Entity field: ", this.getEntityFieldName());
	}

    /** {@inheritDoc} */
    public Class< ? > getReturnType(BeanFactory beanFactory)
            throws BeanFactoryUnableToLocateRepresentationException, BeanFactoryNotFoundException {
        if (this.returnTypeKey == null) {
            return returnType;
        }
        return getRepresentation(this.returnTypeKey, beanFactory, false);
    }

    /** {@inheritDoc} */
	public String getMapKeyForCollection() {
		return mapKeyForCollection;
	}
	
	/** {@inheritDoc} */
	public boolean isEntityMapKey() {
		return entityMapKey;
	}

	/** {@inheritDoc} */
	public DtoToEntityMatcher getDtoToEntityMatcher(final Map<String, Object> converters)
			throws DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
		if (this.dtoToEntityMatcherKey == null) {
			return dtoToEntityMatcher;
		}
		if (converters == null) {
			throw new DtoToEntityMatcherNotFoundException(this.getDtoFieldName(), this.getEntityFieldName(), this.dtoToEntityMatcherKey);
		}
		final Object matcher = converters.get(this.dtoToEntityMatcherKey);
		if (matcher == null) {
			throw new DtoToEntityMatcherNotFoundException(this.getDtoFieldName(), this.getEntityFieldName(), this.dtoToEntityMatcherKey);
		}
		if (matcher instanceof DtoToEntityMatcher) {
			return (DtoToEntityMatcher) matcher;
		}
		throw new NotDtoToEntityMatcherException(this.getDtoFieldName(), this.getEntityFieldName(), this.dtoToEntityMatcherKey);
	}

	private <T> T newCollection(final String clazzKey, final BeanFactory beanFactory, final boolean isDto) 
			throws UnableToCreateInstanceException, BeanFactoryNotFoundException {
		if (beanFactory == null) {
			throw new BeanFactoryNotFoundException(this.getDtoFieldName(), clazzKey, isDto);
		}
		final Object coll = beanFactory.get(clazzKey);
		if (coll instanceof Collection || coll instanceof Map) {
			return (T) coll;
		}
		throw new UnableToCreateInstanceException(clazzKey, 
				(isDto ? " Map Dto" : " Collection/Map Entity") + " field: " + this.getDtoFieldName()
				+ "@key:" + clazzKey
				+ " (Check if beanFactory [" + beanFactory + "] returns a correct instance)", null);
		
	}
	
	private  <T> T newCollection(final Class< ? > clazz, final String type, final String field) throws UnableToCreateInstanceException {
        return (T) newBeanForClass(clazz, "Unable to create collection: {0} for {1} {2}", clazz, type, field);
    }

    private <T> T newBeanForClass(final Class<T> clazz, final String errMsg, final Object ... msgParams) throws UnableToCreateInstanceException {
        try {
            return clazz.newInstance();
        } catch (Exception iex) {
            throw new UnableToCreateInstanceException(clazz.getCanonicalName(), String.format(errMsg, msgParams), iex);
        }
    }
	
}
