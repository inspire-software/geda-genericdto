
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.lang.reflect.Method;

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
    static void validatePipeTypes(final Method dtoRead,
                                  final Method dtoWrite,
                                  final Method entityRead,
                                  final Method entityWrite)
            throws IllegalArgumentException {

        validateReadPipeTypes(dtoWrite, entityRead);
        validateWritePipeTypes(dtoRead, entityWrite);
    }

    /**
     * Validates thate read and write pipes for dto to entity match types.
     *
     * @param dtoWrite method for writting data to DTO field
     * @param entityRead method for reading data from Entity field
     *
     * @throws IllegalArgumentException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateReadPipeTypes(final Method dtoWrite,
                                       final Method entityRead)
            throws IllegalArgumentException {

        final Class< ? > dtoWriteClass = dtoWrite.getParameterTypes()[0];
        final Class< ? > entityReadClass = entityRead.getReturnType();

        if (!dtoWriteClass.equals(entityReadClass)) {
            throw new IllegalArgumentException("Type mismatch is detected for: DTO write {" + dtoWrite
                    + "} and Entity read {" + entityRead + "}. Consider using a converter.");
        }
    }

    /**
     * Validates thate read and write pipes for dto to entity match types.
     *
	 * @param dtoRead method for reading data from DTO field
     * @param entityWrite method for writting data to Entity field
     *
     * @throws IllegalArgumentException if arguments do not match (exception is thrown with
     *         a bit more clarification then the generic one).
     */
    static void validateWritePipeTypes(final Method dtoRead,
                                      final Method entityWrite)
            throws IllegalArgumentException {

        final Class< ? > dtoReadClass = dtoRead.getReturnType();
        final Class< ? > entityWriteClass = entityWrite.getParameterTypes()[0];

        if (!dtoReadClass.equals(entityWriteClass)) {
            throw new IllegalArgumentException("Type mismatch is detected for: DTO read {" + dtoRead
                    + "} and Entity write {" + entityWrite + "}. Consider using a converter.");
        }

    }

}
