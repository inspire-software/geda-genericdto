

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class ClassNameBlacklistingTest {

	/**
	 * Default pattern covers Javassist implementation. Javassist enhances hibernate classes by 
	 * subclassing them and using _$$_javassist_ suffix. We need to avoid using those for 
	 * entity class, since this create incompatibility issue with cached DTO assemblers.
	 */
	@Test
	public void testDefaultBlacklistPattern() {
		
		assertFalse(DTOAssembler.matches("MyClassName"));
		assertTrue(DTOAssembler.matches("MyClassName_$$_javassist_7"));

		
	}
	
}
