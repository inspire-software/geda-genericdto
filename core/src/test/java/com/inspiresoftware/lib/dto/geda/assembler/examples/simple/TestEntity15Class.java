
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.simple;

import org.junit.Ignore;

/**
 * Test Entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity15Class {

	private final String name;
	private final String desc;

	/**
	 * @param name name
	 * @param desc desc
	 */
    public TestEntity15Class(final String name, final String desc) {
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