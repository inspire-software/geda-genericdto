/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.assembler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;


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
		if (!(entity instanceof NewEntityProxy)) {
			entityDataDelegate = this.entityRead.invoke(entity);
		}
		if (entityDataDelegate == null) {
			
			final NewEntityProxy newEntityProxy = new NewEntityProxy(
					entityBeanFactory,
					this.entityBeanKey,
					entity,
					this.entityWrite
			);
			entityDataDelegate = newEntityProxy;

		}
		pipe.writeFromDtoToEntity(entityDataDelegate, dto, converters, entityBeanFactory);
		
	}

	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		
		if (entity == null) {
			return;
		}

		final Object entityDataDelegate = this.entityRead.invoke(entity);
		pipe.writeFromEntityToDto(entityDataDelegate, dto, converters);
		
	}
	
	
	
}
