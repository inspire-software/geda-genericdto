

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;


/**
 * DTO Helper that allows easily manipulate properties of objects and iterate over them.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public final class DTOHelper {
	
	private static final Object[] NULL = new Object[] { null };
	
	private DTOHelper() {
		// prevent instantiation
	}
	
	/**
	 * Load values into  the specified dto object.
	 * 
	 * @param dto the dto to be loaded with values
	 * @param values field name value map to be used for dto loading
	 * 
	 * @throws InspectionScanningException if binding is invalid
	 * @throws InspectionPropertyNotFoundException if field not found
	 * @throws NullParametersNotAllowedException when #dto or #values is null
	 * @throws SetFieldValueException when value cannot be set to a field
	 */
	public static void load(final Object dto, final Map<String, Object> values) 
			throws InspectionScanningException, InspectionPropertyNotFoundException, NullParametersNotAllowedException, SetFieldValueException {
		
		if (dto != null && values != null && !values.isEmpty()) {
			
			final PropertyDescriptor[] descriptors = 
				PropertyInspector.getPropertyDescriptorsForClass(dto.getClass());
			
			for (String fieldName : values.keySet()) {
				
				final Object value = values.get(fieldName);
				final PropertyDescriptor descriptor = 
					PropertyInspector.getDtoPropertyDescriptorForField(dto.getClass(), fieldName, descriptors);
				
				try {
					if (value == null) {
						descriptor.getWriteMethod().invoke(dto, NULL);
					} else {
						descriptor.getWriteMethod().invoke(dto, value);
					}
				} catch (Exception exp) {
					throw new SetFieldValueException(dto.getClass().getCanonicalName(), fieldName, value);
				}	
			}			
		} else {
			throw new NullParametersNotAllowedException("Dto must not be null, Map must not be null or empty");
		}
	}

	/**
	 * Load values into  the specified dto object.
	 * 
	 * @param dto the dto to be loaded with values
	 * @param fieldValuePairs field and value array (field1, value1, field2, value2 .... fieldN, valueN) 
	 *        to be used for dto loading
	 *        
	 * @throws NullParametersNotAllowedException when #dto is null or #fieldValuePairs is null or 
	 *         #fieldValuePairs.length is not even
	 * @throws InspectionScanningException if dto cannot be scanned for properties 
	 * @throws InspectionPropertyNotFoundException if field is not found on the dto
	 * @throws SetFieldValueException if cannot set value to the dto
 	 * 
	 * @ if field not found or cannot be accessed or cannot 
	 *         be set with a provided value
	 */
	public static void load(final Object dto, final Object ... fieldValuePairs) 
			throws NullParametersNotAllowedException, InspectionScanningException, InspectionPropertyNotFoundException, SetFieldValueException {
		
		if (dto != null && fieldValuePairs != null && fieldValuePairs.length > 0 && (fieldValuePairs.length % 2 == 0)) {
			
			final PropertyDescriptor[] descriptors = 
				PropertyInspector.getPropertyDescriptorsForClass(dto.getClass());
			
			for (int index = 0; index + 1 < fieldValuePairs.length; index += 2) {
				
				final String fieldName = (String) fieldValuePairs[index];
				final Object value = fieldValuePairs[index + 1];
				final PropertyDescriptor descriptor = 
					PropertyInspector.getDtoPropertyDescriptorForField(dto.getClass(), fieldName, descriptors);
				
				try {
					if (value == null) {
						descriptor.getWriteMethod().invoke(dto, NULL);
					} else {
						descriptor.getWriteMethod().invoke(dto, value);
					}
				} catch (Exception exp) {
					throw new SetFieldValueException(dto.getClass().getCanonicalName(), fieldName, value);
				}	
			}			
		} else {
			throw new NullParametersNotAllowedException(
					"Dto must not be null, Array must not be null or empty or not have enough parameters (length % 2)");
		}
		
	}

	/**
	 * Unload values from dto to map (key = field name, value = the field value).
	 * 
	 * @param dto the dto to unload values from
	 * @param fields limits the unloading to these field only (omit to unload all fields from dto)
	 * @return field/values map
	 * @throws InspectionScanningException if dto cannot be scanned for properties
	 * @throws InspectionPropertyNotFoundException if field is not found on the dto
	 * @throws NullParametersNotAllowedException if #dto is null
	 * @throws GetFieldValueException if cannot retrieve a value from dto
	 */
	public static Map<String, Object> unloadMap(final Object dto, final String ... fields) 
			throws InspectionScanningException, InspectionPropertyNotFoundException, NullParametersNotAllowedException, GetFieldValueException {
		if (dto != null) {
			
			final PropertyDescriptor[] descriptors = 
				PropertyInspector.getPropertyDescriptorsForClass(dto.getClass());
			
			final String[] unloadFields;
			if (fields != null && fields.length > 0) {
				unloadFields = fields;
			} else {
				unloadFields = new String[descriptors.length];
				for (int index = 0; index < descriptors.length; index++) {
					unloadFields[index] = descriptors[index].getName();
				}
			}
			
			final Map<String, Object> values = new HashMap<String, Object>();
			for (String fieldName : unloadFields) {
				
				final PropertyDescriptor descriptor = 
					PropertyInspector.getDtoPropertyDescriptorForField(dto.getClass(), fieldName, descriptors);
				
				try {
					
					values.put(fieldName, descriptor.getReadMethod().invoke(dto));
					
				} catch (Exception exp) {
					throw new GetFieldValueException(dto.getClass().getCanonicalName(), fieldName);
				}
				
			}
			return values;
			
		} else {
			throw new NullParametersNotAllowedException("Dto must not be null");
		}
	}
	
	/**
	 * Unload values from dto to array. The array contains only values in the order specified by
	 * fields parameter. If this parameter omitted (to extract all values) the the order is the
	 * same as the order of fields in the dto class.
	 * 
	 * @param dto the dto to unload values from
	 * @param fields limits the unloading to these field only (omit to unload all fields from dto)
	 * @return array of values.
	 * @throws InspectionScanningException if dto cannot be scanned for properties
	 * @throws InspectionPropertyNotFoundException if field cannot be found on dto
	 * @throws NullParametersNotAllowedException if dto is null
	 * @throws GetFieldValueException if cannot get value from dto
	 */
	public static Object[] unloadValues(final Object dto, final String ... fields) 
		throws InspectionScanningException, InspectionPropertyNotFoundException, NullParametersNotAllowedException, GetFieldValueException {
		
		if (dto != null) {
			
			final PropertyDescriptor[] descriptors = 
				PropertyInspector.getPropertyDescriptorsForClass(dto.getClass());
			
			final String[] unloadFields;
			if (fields != null && fields.length > 0) {
				unloadFields = fields;
			} else {
				unloadFields = new String[descriptors.length];
				for (int index = 0; index < descriptors.length; index++) {
					unloadFields[index] = descriptors[index].getName();
				}
			}
			
			final Object[] values = new Object[unloadFields.length];
			for (int index = 0; index < unloadFields.length; index++) {
				
				final PropertyDescriptor descriptor = 
					PropertyInspector.getDtoPropertyDescriptorForField(dto.getClass(), unloadFields[index], descriptors);
				
				try {
					
					values[index] = descriptor.getReadMethod().invoke(dto);
					
				} catch (Exception exp) {
					throw new GetFieldValueException(dto.getClass().getCanonicalName(), unloadFields[index]);
				}
				
			}
			return values;
			
		} else {
			throw new NullParametersNotAllowedException("Dto must not be null");
		}
	}

}
