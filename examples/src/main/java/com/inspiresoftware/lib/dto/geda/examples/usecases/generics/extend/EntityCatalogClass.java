/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics.extend;

import org.junit.Ignore;

import java.util.Collection;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:20 AM
 */
@Ignore
public class EntityCatalogClass<T extends EntityCode> implements EntityCatalog<T> {

    private String id;
    private T type;
    private Collection<T> codes;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public T getType() {
        return type;
    }

    public void setType(final T type) {
        this.type = type;
    }

    public Collection<T> getCodes() {
        return codes;
    }

    public void setCodes(final Collection<T> codes) {
        this.codes = codes;
    }
}
