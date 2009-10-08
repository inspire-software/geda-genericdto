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
 * Object that handles read and write streams between Dto and Entity objects.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
class DataPipe implements Pipe {
	
	private final String requiresConverter;
	private final boolean readOnly;

	private final Method dtoRead;
	private final Method dtoWrite;
	
	private final Method entityRead;
	private final Method entityWrite;
	
	/**
	 * @param dtoRead method for reading data from DTO field
	 * @param dtoWrite method for writting data to DTO field
	 * @param entityRead method for reading data from Entity field
	 * @param entityWrite method for writting data to Entity field
	 * @param requiresConverter true if data cannot be directly mapped and needs a converter.
	 * @param readOnly if set to true the aseembly of entity will not include this data
	 */
	public DataPipe(final Method dtoRead,
					final Method dtoWrite,
					final Method entityRead,
					final Method entityWrite,
					final String requiresConverter,
					final boolean readOnly) {

		this.readOnly = readOnly;
		this.requiresConverter = requiresConverter;

		this.dtoWrite = dtoWrite;
		this.entityRead = entityRead;
		if (readOnly) {
			this.dtoRead = null;
			this.entityWrite = null;
		} else {
			this.dtoRead = dtoRead;
			this.entityWrite = entityWrite;
		}
		
		
	}
	
	/** {@inheritDoc} */
	public void writeFromEntityToDto(final Object entity, final Object dto, final Map<String, ValueConverter> converters) 
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (entity == null) {
			return;
		}

		final Object entityData = this.entityRead.invoke(entity);
		
		if (usesConverter()) {
			this.dtoWrite.invoke(dto, getConverter(converters).convertToDto(entityData));
		} else {
			this.dtoWrite.invoke(dto, entityData);			
		}
	}

	/** {@inheritDoc} */
	public void writeFromDtoToEntity(final Object entity, final Object dto, final Map<String, ValueConverter> converters) 
		throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (entity == null) {
			// tempoprary fix need to think more about this
			throw new IllegalArgumentException("Nested domain entity is null and cannot be set");
		}
		
		if (readOnly) {
			return;
		}
		
		final Object dtoData = this.dtoRead.invoke(dto);
		if (usesConverter()) {
			this.entityWrite.invoke(entity, getConverter(converters).convertToEntity(dtoData));
		} else {
			this.entityWrite.invoke(entity, dtoData);
		}
	}
	
	private boolean usesConverter() {
		return this.requiresConverter != null && this.requiresConverter.length() > 0;
	}
	
	private ValueConverter getConverter(final Map<String, ValueConverter> converters) 
		throws IllegalArgumentException {
		
		if (converters != null && !converters.isEmpty() && converters.containsKey(this.requiresConverter)) {
			return converters.get(this.requiresConverter);
		}
		throw new IllegalArgumentException("Required converter: " + this.requiresConverter 
				+ " cannot be located");
	}
	
}
