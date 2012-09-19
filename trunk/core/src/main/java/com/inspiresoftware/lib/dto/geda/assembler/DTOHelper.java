

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DTO Helper that allows easily manipulate properties of objects and iterate over them.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 * @deprecated this class is scheduled for removal in version 2.0.5
 */
@Deprecated
public final class DTOHelper {
	
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
     *
     * @deprecated use DTOAssembler.newAssembler(dto.getClass(), HashMap.class);
	 */
    @Deprecated
	public static void load(final Object dto, final Map<String, Object> values) 
			throws InspectionScanningException, InspectionPropertyNotFoundException, NullParametersNotAllowedException, SetFieldValueException {
		
		if (dto != null && values != null && !values.isEmpty()) {

            final Assembler asm = DTOAssembler.newAssembler(dto.getClass(), values.getClass());
            asm.assembleDto(dto, values, null, null);

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
     * @deprecated use DTOAssembler.newAssembler(dto.getClass(), ArrayList.class);
	 */
    @Deprecated
	public static void load(final Object dto, final Object ... fieldValuePairs) 
			throws NullParametersNotAllowedException, InspectionScanningException, InspectionPropertyNotFoundException, SetFieldValueException {
		
		if (dto != null && fieldValuePairs != null && fieldValuePairs.length > 0 && (fieldValuePairs.length % 2 == 0)) {

            final List<Object> entity = new ArrayList<Object>();
            final Assembler asm = DTOAssembler.newAssembler(dto.getClass(), entity.getClass());
            asm.assembleEntity(dto, entity, null, null);

			for (int index = 0; index + 1 < fieldValuePairs.length; index += 2) {

				final String fieldName = (String) fieldValuePairs[index];
				final Object value = fieldValuePairs[index + 1];

                final int fieldNameIndex = entity.indexOf(fieldName);
                if (fieldNameIndex != -1) {
                    entity.set(fieldNameIndex + 1, value);
                } else {
                    throw new SetFieldValueException(dto.getClass().getCanonicalName(), fieldName, value);
                }

            }
            asm.assembleDto(dto, entity, null, null);

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

            final Assembler asm = DTOAssembler.newAssembler(dto.getClass(), HashMap.class);
            final Map<String, Object> allValues = new HashMap<String, Object>();
            asm.assembleEntity(dto, allValues, null, null);

            if (fields != null && fields.length > 0) {
                final Map<String, Object> values = new HashMap<String, Object>();
                for (final String field : fields) {
                    if (allValues.containsKey(field)) {
                        values.put(field, allValues.get(field));
                    } else {
                        throw new GetFieldValueException(dto.getClass().getCanonicalName(), field);
                    }
                }
                return values;
            }
            allValues.put("class", dto.getClass());
            return allValues;

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
     *
     * @deprecated use DTOAssembler.newAssembler(dto.getClass(), ArrayList.class);
	 */
    @Deprecated
	public static Object[] unloadValues(final Object dto, final String ... fields) 
		throws InspectionScanningException, InspectionPropertyNotFoundException, NullParametersNotAllowedException, GetFieldValueException {
		
		if (dto != null) {

            final List<Object> entity = new ArrayList<Object>();
            final Assembler asm = DTOAssembler.newAssembler(dto.getClass(), entity.getClass());
            asm.assembleEntity(dto, entity, null, null);

			if (fields != null && fields.length > 0) {
                final List<Object> values = new ArrayList<Object>();
                for (final String field : fields) {
                    final int fieldNameIndex = entity.indexOf(field);
                    if (fieldNameIndex != -1) {
                        values.add(entity.get(fieldNameIndex + 1));
                    } else {
                        throw new GetFieldValueException(dto.getClass().getCanonicalName(), field);
                    }

                }
                return values.toArray(new Object[values.size()]);
            }

            final List<Object> allValues = new ArrayList<Object>();
            allValues.add(dto.getClass());
            for (int i = 1; i < entity.size(); i += 2) {
                allValues.add(entity.get(i));
            }
			return allValues.toArray(new Object[allValues.size()]);
			
		} else {
			throw new NullParametersNotAllowedException("Dto must not be null");
		}
	}

}
