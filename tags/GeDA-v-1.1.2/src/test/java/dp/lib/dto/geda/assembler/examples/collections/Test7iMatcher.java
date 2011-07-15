
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler.examples.collections;

import dp.lib.dto.geda.adapter.DtoToEntityMatcher;

import org.junit.Ignore;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 12:00:16 PM
 */
@Ignore
public class Test7iMatcher implements DtoToEntityMatcher<TestDto7CollectionSubInterface, TestEntity7CollectionSubInterface> {

	/** {@inheritDoc} */
    public boolean match(final TestDto7CollectionSubInterface testDto7CollectionSubClass,
                         final TestEntity7CollectionSubInterface testEntity7CollectionSubClass) {

        final String dtoName = testDto7CollectionSubClass.getName();
        final String entityName = testEntity7CollectionSubClass.getName();

        return dtoName != null && entityName != null && dtoName.equals(entityName);
    }
}
