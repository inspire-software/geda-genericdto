
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

/**
 * Data reader is an interface to allow dynamic classes to be built
 * for invoking getter methods.
 *
 * @author Denis Pavlov
 * @since 1.1.0
 *
 */
public interface DataReader {

	/**
	 * Invokes the correct getter.
	 * 
	 * @param source the source object instance which getter is to be invoked
	 * @return data returned by getter.
	 */
	Object read(Object source);
	
	/**
	 * @return return the class for this return type
	 */
	Class< ? > getReturnType();
	
}
