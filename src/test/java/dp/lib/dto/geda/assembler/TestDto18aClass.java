

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import org.junit.Ignore;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.1.1
 * 
 * @param <V> simple value
 *
 */
@Dto
@Ignore
public class TestDto18aClass<V> {

	@DtoField("myProp")
	private V myProp;
	
	/**
	 * @return property
	 */
	public V getMyProp() {
		return myProp;
	}

	/**
	 * @param myProp property
	 */
	public void setMyProp(final V myProp) {
		this.myProp = myProp;
	}
	
}
