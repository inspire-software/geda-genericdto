
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.performance.dto.matcher;

import org.junit.Ignore;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.performance.Level2;

/**
 * Simple matched by primary key.
 * 
 * @author DPavlov
 */
@Ignore
public class Level2DtoMatcher implements DtoToEntityMatcher<Level2, Level2> {

	/** {@inheritDoc} */
	public boolean match(final Level2 dto, final Level2 entity) {
		return dto != null && dto.getField1() != null 
			&& entity != null && dto.getField1().equals(entity.getField1());
	}

}
