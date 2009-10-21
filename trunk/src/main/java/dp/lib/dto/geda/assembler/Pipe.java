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
import java.util.Map;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.ValueConverter;

/**
 * A pipe is an object that allows to pipe data between Dto and Entity field.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
interface Pipe {

	/**
	 * write data from entity field to dto field.
	 * @param entity the entity to read data from.
	 * @param dto the dto to write data to.
	 * @param converters the converters to be used during conversion.
	 * @throws IllegalArgumentException {@link java.lang.reflect.Method}
	 * @throws IllegalAccessException {@link java.lang.reflect.Method}
	 * @throws InvocationTargetException {@link java.lang.reflect.Method}
	 */
	void writeFromEntityToDto(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException;

	/**
	 * write data from dto field to entity field.
	 * @param entity the entity to write data to.
	 * @param dto the dto to read data from.
	 * @param converters the converters to be used during conversion.
	 * @param entityBeanFactory bean factory for creating new instances of nested domain objects mapped to DTO.
	 * @throws IllegalArgumentException {@link java.lang.reflect.Method}
	 * @throws IllegalAccessException {@link java.lang.reflect.Method}
	 * @throws InvocationTargetException {@link java.lang.reflect.Method}
	 */
	void writeFromDtoToEntity(final Object entity, final Object dto,
			final Map<String, ValueConverter> converters,
			final BeanFactory entityBeanFactory)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException;

}