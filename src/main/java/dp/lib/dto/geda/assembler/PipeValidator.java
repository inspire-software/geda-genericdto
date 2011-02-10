
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.annotations.Dto;

/**
 * Small utility class that validates pipes.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 3:43:30 PM
 */
final class PipeValidator {

    private PipeValidator() {
        // prevent instanciation
    }
    
    /**
     * Validates that read and write pipes for dto to entity are non null.
     *
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     *
     * @throws IllegalArgumentException if any of pipe is null (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validatePipeNonNull(final DataReader dtoRead,
                                    final DataWriter dtoWrite,
                                    final DataReader entityRead,
                                    final DataWriter entityWrite)
            throws IllegalArgumentException {

    	validateReadPipeNonNull(dtoWrite, entityRead);
    	validateWritePipeNonNull(dtoRead, entityWrite);
    }
    
    /**
     * Validates that read and write pipes for dto to entity are non null.
     *
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * 
     * @throws IllegalArgumentException if any of pipe is null (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateReadPipeNonNull(final DataWriter dtoWrite,
    									final DataReader entityRead)
    	throws IllegalArgumentException {
    	
    	validatePipeNonNull(entityRead, "Entity read i.e. Entity.get()");
    	validatePipeNonNull(dtoWrite, "DTO write i.e. DTO.set()");
    }

    /**
     * Validates that read and write pipes for dto to entity match types.
     *
     * @param dtoRead method for reading data from DTO field
     * @param entityWrite method for writting data to Entity field
     *
     * @throws IllegalArgumentException if any of pipe is null (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateWritePipeNonNull(final DataReader dtoRead,
            						     final DataWriter entityWrite)
    	throws IllegalArgumentException {
    	
    	validatePipeNonNull(dtoRead, "DTO read i.e. DTO.get()");
    	validatePipeNonNull(entityWrite, "Entity write i.e. Entity.set()");

    }
    
    /**
     * Validate that method is not null.
     * 
     * @param meth method to check
     * @param desc description of failure
     * 
     * @throws IllegalArgumentException if method is null with some sensible message
     */
    static void validatePipeNonNull(final Object meth, final String desc) throws IllegalArgumentException {
    	if (meth == null) {
    		throw new IllegalArgumentException("Data pipe method for [" + desc 
    				+ "] is not initialized. Please check parameter and return types of your getters/setters");
    	}
    }

    /**
     * Validates thate read and write pipes for dto to entity match types.
     *
	 * @param dtoRead method for reading data from DTO field
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     * @param entityWrite method for writting data to Entity field
     *
     * @throws IllegalArgumentException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validatePipeTypes(final DataReader dtoRead,
                                  final DataWriter dtoWrite,
                                  final DataReader entityRead,
                                  final DataWriter entityWrite)
            throws IllegalArgumentException {

        validateReadPipeTypes(dtoWrite, entityRead);
        validateWritePipeTypes(dtoRead, entityWrite);
    }

    private static boolean sameDataType(final Class< ? > data1, final Class< ? > data2) {
    	return data1.equals(data2)
	    	|| (data1.isPrimitive() && !data2.isPrimitive() && samePrimitiveDataType(data2, data1))
	    	|| (!data1.isPrimitive() && data2.isPrimitive() && samePrimitiveDataType(data1, data2));  	
    }
    
    private static boolean samePrimitiveDataType(final Class< ? > wrapper, final Class< ? > primitive) {
    	try {
	    	return wrapper.getDeclaredField("TYPE").get(null).equals(primitive);
    	} catch (Throwable thr) {
			return false;
		}
	    	
    }
    
    /**
     * Validates that read and write pipes for dto to entity match types.
     *
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     *
     * @throws IllegalArgumentException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateReadPipeTypes(final DataWriter dtoWrite,
                                       final DataReader entityRead)
            throws IllegalArgumentException {

        final Class< ? > dtoWriteClass = dtoWrite.getParameterType();
        final Class< ? > entityReadClass = entityRead.getReturnType();

        
        if (
        		// if it is interface we cannot find out until we get an annotated class instance.
        		!dtoWriteClass.isInterface()

        		// check if it is a nested dto
        		&& dtoWriteClass.getAnnotation(Dto.class) == null 

        		// Object checking is for generics - they are too much effort just let it go
        		&& !entityReadClass.equals(Object.class) && !dtoWriteClass.equals(Object.class) 
        		
        		// check the same types
        		&& !sameDataType(dtoWriteClass, entityReadClass)
        	) {
            throw new IllegalArgumentException("Type mismatch is detected for: DTO write {" + dtoWrite
                    + "}{" + dtoWriteClass + "} and Entity read {" + entityRead + "}{" + entityReadClass + "}. Consider using a converter.");
        }
    }

    /**
     * Validates that read and write pipes for dto to entity match types.
     *
	 * @param dtoRead method for reading data from DTO field
     * @param entityWrite method for writting data to Entity field
     *
     * @throws IllegalArgumentException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateWritePipeTypes(final DataReader dtoRead,
                                      final DataWriter entityWrite)
            throws IllegalArgumentException {

        final Class< ? > dtoReadClass = dtoRead.getReturnType();
        final Class< ? > entityWriteClass = entityWrite.getParameterType();

        // Object checking is for generics - they are too much effort just let it go
        if (
        		// if it is interface we cannot find out until we get an annotated class instance.
        		!dtoReadClass.isInterface()
        		
        		// check if it is a nested dto
        		&& dtoReadClass.getAnnotation(Dto.class) == null 
        		
        		// Object checking is for generics - they are too much effort just let it go
        		&& !entityWriteClass.equals(Object.class) && !dtoReadClass.equals(Object.class)
        		
        		// check the same types
        		&& !sameDataType(dtoReadClass, entityWriteClass)
        		
        	) {
            throw new IllegalArgumentException("Type mismatch is detected for: DTO read {" + dtoRead
                    + "} and Entity write {" + entityWrite + "}. Consider using a converter.");
        }

    }

}
