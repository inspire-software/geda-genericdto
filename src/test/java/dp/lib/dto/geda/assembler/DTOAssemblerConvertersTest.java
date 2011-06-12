
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.assembler.TestDto3Class.Decision;
import dp.lib.dto.geda.exception.GeDAException;

/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
public class DTOAssemblerConvertersTest {

	/**
	 * Test assembler that uses converter.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testWithConversion() throws GeDAException {
		final TestDto3Class dto = new TestDto3Class();
		final TestEntity3Class entity = new TestEntity3Class();
		entity.setDecision(true);
		final ValueConverter conv3toDto = new TestConverter3();
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("boolToEnum", conv3toDto);
		
		final DTOAssembler assembler =
			DTOAssembler.newAssembler(TestDto3Class.class, TestEntity3Class.class);
		
		assembler.assembleDto(dto, entity, converters, null);
		
		assertEquals(Decision.Decided, dto.getMyEnum());
		
		dto.setMyEnum(Decision.Undecided);
		
		assembler.assembleEntity(dto, entity, converters, null);
			
	}
	
}
