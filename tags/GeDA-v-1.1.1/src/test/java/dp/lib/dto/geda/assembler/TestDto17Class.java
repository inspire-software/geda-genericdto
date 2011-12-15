
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;
import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoMap;
import org.junit.Ignore;

import java.util.Map;

/**
 * Test DTO for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
@Dto
public class TestDto17Class {

	/** Basic matcher. */
    public static class TestDto16Matcher implements DtoToEntityMatcher<TestDto15Class, TestEntity15Class> {
        /** {@inheritDoc} */
    	public boolean match(final TestDto15Class testDto15Class, final TestEntity15Class testEntity15Class) {
            return testDto15Class.getName().equals(testEntity15Class.getName());
        }
    }

	@DtoMap(readOnly = true,
            value = "items",
            dtoToEntityMatcher = TestDto16Matcher.class,
            entityBeanKeys = "TestEntity15Class",
            dtoBeanKey = "TestDto15Class",
            entityGenericType = TestEntity15Class.class,
            entityCollectionMapKey = "name")    
	private Map<String, TestDto15Class> items;

    /**
     * @return collection
     */
    public Map<String, TestDto15Class> getItems() {
        return items;
    }

    /**
     * @param items collection
     */
    public void setItems(final Map<String, TestDto15Class> items) {
        this.items = items;
    }
}