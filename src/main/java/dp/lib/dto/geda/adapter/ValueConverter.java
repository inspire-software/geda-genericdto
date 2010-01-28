/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
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
	 *
     * @param object the object to convert
     * @param beanFactory DTO bean factory
     *
	 * @return converted object.
	 */
	Object convertToDto(final Object object, final BeanFactory beanFactory);

	/**
	 * Convert one object into another to resolve incompatibilities between DTO and Entities.
	 *
     * @param object the object to convert
     * @param oldEntity the old value that was in the entity
     * @param beanFactory entity bean factory
     *
	 * @return converted object.
	 */
	Object convertToEntity(final Object object, final Object oldEntity, final BeanFactory beanFactory);
	
}
