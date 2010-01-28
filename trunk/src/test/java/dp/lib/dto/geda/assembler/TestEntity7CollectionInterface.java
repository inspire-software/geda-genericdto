
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

import java.util.Collection;

/**
 * .
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 26, 2010
 * Time: 11:33:40 AM
 */
@Ignore
public interface TestEntity7CollectionInterface {

    Collection<TestEntity7CollectionSubInterface> getCollection();

    void setCollection(Collection<TestEntity7CollectionSubInterface> collection);
}
