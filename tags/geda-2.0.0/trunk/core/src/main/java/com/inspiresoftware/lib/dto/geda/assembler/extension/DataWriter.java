
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension;

/**
 * Data writer is an interface to allow dynamic classes to be built
 * for invoking setter methods.
 *
 * @author Denis Pavlov
 * @since 1.1.0
 *
 */
public interface DataWriter {
	
	/**
	 * Invokes the correct setter.
	 * 
	 * @param source the source object instance which setter is to be invoked
	 * @param value data to be set.
	 */
	void write(Object source, Object value);
	
	/**
	 * @return return the class for the parameter value
	 */
	Class< ? > getParameterType();

}
