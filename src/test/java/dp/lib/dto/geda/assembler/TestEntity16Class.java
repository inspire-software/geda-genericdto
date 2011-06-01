
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
 * Test Entity for Assembler.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@Ignore
public class TestEntity16Class {

	private final Collection<TestEntity15Class> items;

	/**
	 * @param items items
	 */
    public TestEntity16Class(final Collection<TestEntity15Class> items) {
        this.items = items;
    }

    /**
     * @return items
     */
	public Collection<TestEntity15Class> getItems() {
		return items;
	}


}