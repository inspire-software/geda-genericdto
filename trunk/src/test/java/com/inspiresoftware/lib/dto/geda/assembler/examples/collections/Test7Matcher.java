
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.collections;


import org.junit.Ignore;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;

/**
 * Test matches that matches the Strings.
 * <p/>
 * User: Denis Pavlov
 * Date: Jan 25, 2010
 * Time: 3:34:16 PM
 */
@Ignore
public class Test7Matcher implements DtoToEntityMatcher<TestDto7CollectionSubClass, TestEntity7CollectionSubClass> {

	/** {@inheritDoc} */
    public boolean match(final TestDto7CollectionSubClass testDto7CollectionSubClass, 
    		final TestEntity7CollectionSubClass testEntity7CollectionSubClass) {
        final String dtoName = testDto7CollectionSubClass.getName();
        final String entityName = testEntity7CollectionSubClass.getName();

        return dtoName != null && entityName != null && dtoName.equals(entityName);
    }
}
