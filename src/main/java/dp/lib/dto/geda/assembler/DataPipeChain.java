
/*
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
import dp.lib.dto.geda.adapter.meta.PipeMetadata;


/**
 * Pipe chain describes delegation of nested beans.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipeChain implements Pipe {

	private final PipeMetadata meta;
	
	private final Method entityRead;
	private final Method entityWrite;

	private final Pipe pipe;
	
	/**
	 * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writing data to Entity field
	 * @param pipe the inner pipe.
	 * @param meta meta data for this data delegate
	 */
	public DataPipeChain(final Method entityRead, 
					     final Method entityWrite,
						 final Pipe pipe,
						 final PipeMetadata meta) {
		this.entityRead = entityRead;
		this.entityWrite = entityWrite;
		this.pipe = pipe;
		this.meta = meta;
	}

	/** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity, final Object dto,
			final Map<String, Object> converters, final BeanFactory entityBeanFactory)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		Object entityDataDelegate = null;
		if (!(entity instanceof NewDataProxy)) {
			entityDataDelegate = this.entityRead.invoke(entity);
		}
		if (entityDataDelegate == null) {
			
			entityDataDelegate = new NewDataProxy(
					entityBeanFactory,
					this.meta,
					false,
					entity,
					this.entityWrite
			);

		}
		pipe.writeFromDtoToEntity(entityDataDelegate, dto, converters, entityBeanFactory);
		
	}

	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, Object> converters, final BeanFactory dtoBeanFactory)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		
		if (entity == null) {
			return;
		}

		final Object entityDataDelegate = this.entityRead.invoke(entity);
		pipe.writeFromEntityToDto(entityDataDelegate, dto, converters, dtoBeanFactory);
		
	}
	
	
	
}
