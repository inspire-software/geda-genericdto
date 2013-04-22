
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.simple;

import org.junit.Ignore;

/**
 * Test Entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class ReadOnlyEntityClass {

	private final String name;
	private final String desc;

	/**
	 * @param name name
	 * @param desc desc
	 */
    public ReadOnlyEntityClass(final String name, final String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * @return name
     */
	public String getName() {
		return name;
	}

    /**
     * @return description
     */
	public String getDesc() {
		return desc;
	}
	
}