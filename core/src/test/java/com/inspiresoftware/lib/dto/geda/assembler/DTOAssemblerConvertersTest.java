
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.ValueConverter;
import com.inspiresoftware.lib.dto.geda.assembler.examples.converter.TestConverter3;
import com.inspiresoftware.lib.dto.geda.assembler.examples.converter.TestDto3Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.converter.TestDto3Class.Decision;
import com.inspiresoftware.lib.dto.geda.assembler.examples.converter.TestEntity3Class;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.assertEquals;


/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@RunWith(value = ParameterizedSynthesizer.class)
public class DTOAssemblerConvertersTest {
	
	private String synthesizer;
	
	/**
	 * @param synthesizer parameter
	 */
	public DTOAssemblerConvertersTest(final String synthesizer) {
		super();
		this.synthesizer = synthesizer;
	}

	/**
	 * @return synthesizers keys
	 */
	@Parameters
	public static Collection<String[]> data() {
		final List<String[]> params = new ArrayList<String[]>();
		for (final String param : MethodSynthesizerProxy.getAvailableSynthesizers()) {
			params.add(new String[] { param });
		}
		return params;
	}
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
			DTOAssembler.newCustomAssembler(TestDto3Class.class, TestEntity3Class.class, synthesizer);
		
		assembler.assembleDto(dto, entity, converters, null);
		
		assertEquals(Decision.Decided, dto.getMyEnum());
		
		dto.setMyEnum(Decision.Undecided);
		
		assembler.assembleEntity(dto, entity, converters, null);
			
	}
	
}
