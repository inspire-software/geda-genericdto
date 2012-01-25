
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.inspiresoftware.lib.dto.geda.exception.InspectionBindingNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionPropertyNotFoundException;
import com.inspiresoftware.lib.dto.geda.exception.InspectionScanningException;


/**
 * Assembles Pipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
final class PropertyInspector {

	private PropertyInspector() {
		// prevent instantiation
	}
	
	/**
	 * @param descriptor the descriptor whose get method's return type will be inspected to retrieve
	 *        all descriptors for it.
	 * @return all descriptors for specified class with deep inheritance on interfaces.
	 * @throws InspectionScanningException if fails to get bean info from a class 
	 */
	public static PropertyDescriptor[] getPropertyDescriptorsForClassReturnedByGet(final PropertyDescriptor descriptor) 
		throws InspectionScanningException {
		
		final Method entityFieldRead = descriptor.getReadMethod();
		final Class returnType = (Class) entityFieldRead.getGenericReturnType();
		return getPropertyDescriptorsForClass(returnType);
		
	}
	
	/**
	 * @param clazz class to inspect.
	 * @return all descriptors for specified class with deep inheritance on interfaces.
	 * @throws InspectionScanningException if fails to get bean info from a class
	 */
	public static PropertyDescriptor[] getPropertyDescriptorsForClass(final Class clazz) throws InspectionScanningException {
		try {
			PropertyDescriptor[] basic = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
		
			if (clazz.isInterface()) {
				final Type[] extendedInterfaces = (Type[]) clazz.getGenericInterfaces();
				
				if (extendedInterfaces != null && extendedInterfaces.length > 0) {
					final ArrayList<PropertyDescriptor> descs = new ArrayList<PropertyDescriptor>();
					for (Type extendedInterface : extendedInterfaces) {
						
						if (extendedInterface instanceof Class) {
							addToList(descs, getPropertyDescriptorsForClass((Class) extendedInterface));
						} else if (extendedInterface instanceof ParameterizedType) {
							addToList(descs, getPropertyDescriptorsForClass(
									(Class) ((ParameterizedType) extendedInterface).getRawType()));
						}
						
					}
					addToList(descs, basic);
					return descs.toArray(new PropertyDescriptor[descs.size()]);
				}
				
			}
			return basic;
		} catch (IntrospectionException itex) {
			throw new InspectionScanningException(clazz.getCanonicalName(), itex);
		}
		
	}
	
	private static void addToList(final List<PropertyDescriptor> list, final PropertyDescriptor[] descs) {
		if (descs == null || descs.length == 0) {
			return;
		}
		list.addAll(Arrays.asList(descs));
	}

	/**
	 * @param dtoClass the DTO
	 * @param entityClass the Entity
	 * @param dtoFieldName the DTO field
	 * @param binding the Entity field binding
	 * @param entityPropertyDescriptors all Entity property descriptors
	 * @return property descriptor for Entity field.
	 * @throws InspectionBindingNotFoundException thrown when unable to find descriptor for field.
	 */
	public static PropertyDescriptor getEntityPropertyDescriptorForField(
			final Class dtoClass, final Class entityClass,
			final String dtoFieldName, final String binding,
			final PropertyDescriptor[] entityPropertyDescriptors) throws InspectionBindingNotFoundException {
		
		for (PropertyDescriptor current : entityPropertyDescriptors) {
			if (current.getName().equals(binding)) {
				return current;
			}
		}

		throw new InspectionBindingNotFoundException(
				dtoClass.getCanonicalName(), dtoFieldName, entityClass.getCanonicalName(), binding);
	}
	
	/**
	 * @param dtoClass the DTO
	 * @param dtoFieldName the DTO field
	 * @param dtoPropertyDescriptors all DTO property descriptors
	 * @return property descriptor for DTO field.
	 * @throws InspectionPropertyNotFoundException thrown when unable to find descriptor for field.
	 */
	public static PropertyDescriptor getDtoPropertyDescriptorForField(
			final Class dtoClass,
			final String dtoFieldName,
			final PropertyDescriptor[] dtoPropertyDescriptors) throws InspectionPropertyNotFoundException {

		for (PropertyDescriptor current : dtoPropertyDescriptors) {
			if (current.getName().equals(dtoFieldName)) {
				return current;
			}
		}
		
		throw new InspectionPropertyNotFoundException(dtoClass.getCanonicalName(), dtoFieldName);
	}

	
}
