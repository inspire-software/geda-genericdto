

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.generics;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import org.junit.Ignore;

import java.util.Collection;
import java.util.Map;


/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.1.1
 *
 */
@Dto
@Ignore
public class TestDto18Class extends TestDto18bClass<String, Collection<TestDto18aClass<String>>, Map<String, TestDto18aClass<String>>> {
	
}
