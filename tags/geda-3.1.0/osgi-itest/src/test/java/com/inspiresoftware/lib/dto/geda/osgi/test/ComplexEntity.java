/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.osgi.test;

import java.util.List;
import java.util.Map;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 8:28 AM
 */
public interface ComplexEntity {

    String getName();

    void setName(String name);

    SimpleEntity getInner();

    void setInner(SimpleEntity inner);

    List<SimpleEntity> getCollection();

    void setCollection(List<SimpleEntity> collection);

    Map<String, SimpleEntity> getMap();

    void setMap(Map<String, SimpleEntity> map);
}
