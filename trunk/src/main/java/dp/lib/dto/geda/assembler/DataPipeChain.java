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

import dp.lib.dto.geda.adapter.ValueConverter;


/**
 * Pipe chain describes delegation of nested beans.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipeChain implements Pipe {

	private final Method entityRead;
	private final Pipe pipe;
	
	/**
	 * @param entityRead method for reading data from Entity field
	 * @param pipe the inner pipe.
	 */
	public DataPipeChain(final Method entityRead, 
						 final Pipe pipe) {
		this.entityRead = entityRead;
		this.pipe = pipe;
	}

	/** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		final Object entityDataDelegate = this.entityRead.invoke(entity);
		pipe.writeFromDtoToEntity(entityDataDelegate, dto, converters);
		
	}

	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		final Object entityDataDelegate = this.entityRead.invoke(entity);
		pipe.writeFromEntityToDto(entityDataDelegate, dto, converters);
		
	}
	
	
	
}
