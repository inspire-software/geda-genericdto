/*
 * Copyright (c) 2010. The intellectual rights for this code remain to the NPA developer team.
 * Code distribution, sale or modification is prohibited unless authorized by all members of NPA
 * development team.
 */
package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * Pipe chain describes delegation of nested beans.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipeChain implements Pipe {

	private final String entityBeanKey;

	private final Method entityRead;
	private final Method entityWrite;

	private final Pipe pipe;
	
	/**
	 * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writting data to Entity field
	 * @param pipe the inner pipe.
	 * @param entityBeanKey bean key for this data delegate
	 */
	public DataPipeChain(final Method entityRead, 
					     final Method entityWrite,
						 final Pipe pipe,
						 final String entityBeanKey) {
		this.entityRead = entityRead;
		this.entityWrite = entityWrite;
		this.pipe = pipe;
		this.entityBeanKey = entityBeanKey;
	}

	/** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters, final BeanFactory entityBeanFactory)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		Object entityDataDelegate = null;
		if (!(entity instanceof NewObjectProxy)) {
			entityDataDelegate = this.entityRead.invoke(entity);
		}
		if (entityDataDelegate == null) {
			
			entityDataDelegate = new NewObjectProxy(
					entityBeanFactory,
					this.entityBeanKey,
					entity,
					this.entityWrite
			);

		}
		pipe.writeFromDtoToEntity(entityDataDelegate, dto, converters, entityBeanFactory);
		
	}

	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters, final BeanFactory dtoBeanFactory)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		
		if (entity == null) {
			return;
		}

		final Object entityDataDelegate = this.entityRead.invoke(entity);
		pipe.writeFromEntityToDto(entityDataDelegate, dto, converters, dtoBeanFactory);
		
	}
	
	
	
}
