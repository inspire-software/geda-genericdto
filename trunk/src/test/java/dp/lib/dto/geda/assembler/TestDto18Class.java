

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.util.Collection;
import java.util.Map;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;

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
