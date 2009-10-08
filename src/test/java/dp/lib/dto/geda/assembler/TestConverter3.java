/**
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright Denis Pavlov 2009 
 * Web: http://www.inspire-software.com 
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */
package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

import dp.lib.dto.geda.adapter.ValueConverter;
import dp.lib.dto.geda.assembler.TestDto3Class.Decision;

/**
 * Test converter.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
class TestConverter3 implements ValueConverter {

	/** {@inheritDoc} */
	public Object convertToDto(final Object object) {
		final Boolean value = (Boolean) object;
		if (value != null && value) {
			return Decision.Decided;
		}
		return Decision.Undecided;
	}
	
	/** {@inheritDoc} */
	public Object convertToEntity(final Object object) {
		final Decision value = (Decision) object;
		return (value != null && Decision.Decided.equals(value));
	}

}
