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
 * .
 *
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:39:45 AM
 */
@Ignore
public class TestEntity9InheritanceClass {
	
	private String name;
	private String nameChild;

	/** {@inheritDoc} */
	public String getNameChild() {
		return nameChild;
	}

	/** {@inheritDoc} */
	public void setNameChild(final String name) {
		this.nameChild = name;
	}
	
	/** {@inheritDoc} */
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	public void setName(final String name) {
		this.name = name;
	}

}
