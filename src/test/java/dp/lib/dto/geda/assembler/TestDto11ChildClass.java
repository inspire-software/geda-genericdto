
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
import dp.lib.dto.geda.annotations.DtoParent;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Dto
@Ignore
public class TestDto11ChildClass implements TestDto11ChildInterface {
	
	@DtoParent
	@DtoField(value = "parent",
			  dtoBeanKeys = { "dp.lib.dto.geda.assembler.TestDto11ParentClass" },
			  entityBeanKeys = { "dp.lib.dto.geda.assembler.TestEntity11ParentClass" })
	private TestDto11ParentInterface parent;
	
	@DtoField
	private String name;
	
	/**
	 * @return parent entity.
	 */
	public TestDto11ParentInterface getParent() {
		return parent;
	}
	/**
	 * @param parent parent entity.
	 */
	public void setParent(final TestDto11ParentInterface parent) {
		this.parent = parent;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name name
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
}
