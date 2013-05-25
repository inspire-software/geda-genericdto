/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.maps;

import org.junit.Test;

/**
 * User: denispavlov
 * Date: 13-04-22
 * Time: 11:48 PM
 */
public class MapRunnersTest {

    @Test
    public void testMapToMap() throws Exception {

        new RunMapToMap().mapToMapMapping();

    }

    @Test
    public void testMapToMapKey() throws Exception {

        new RunMapToMapKey().mapToMapKeyMapping();

    }

    @Test
    public void testMapToCollection() throws Exception {

        new RunMapToCollection().mapToCollectionMapping();

    }
}
