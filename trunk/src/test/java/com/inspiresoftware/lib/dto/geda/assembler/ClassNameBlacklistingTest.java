

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import static org.junit.Assert.*;

import org.junit.Test;

import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;


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
