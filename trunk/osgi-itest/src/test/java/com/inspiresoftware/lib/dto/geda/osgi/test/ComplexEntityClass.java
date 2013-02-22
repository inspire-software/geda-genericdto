/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.osgi.test;

import java.util.List;
import java.util.Map;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 8:17 AM
 */
public class ComplexEntityClass implements ComplexEntity {

    private String name;
    private SimpleEntity inner;
    private List<SimpleEntity> collection;
    private Map<String, SimpleEntity> map;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public SimpleEntity getInner() {
        return inner;
    }

    public void setInner(final SimpleEntity inner) {
        this.inner = inner;
    }

    public List<SimpleEntity> getCollection() {
        return collection;
    }

    public void setCollection(final List<SimpleEntity> collection) {
        this.collection = collection;
    }

    public Map<String, SimpleEntity> getMap() {
        return map;
    }

    public void setMap(final Map<String, SimpleEntity> map) {
        this.map = map;
    }
}
