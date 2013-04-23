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

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:18 AM
 */
@Ignore
public class EntityCatalogCodeClass implements EntityCatalogCode {

    private String sectionName;
    private EntityCatalog<EntityCatalogCode> catalog;

    private String code;
    private String id;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(final String sectionName) {
        this.sectionName = sectionName;
    }

    public EntityCatalog<EntityCatalogCode> getCatalog() {
        return catalog;
    }

    public void setCatalog(final EntityCatalog<EntityCatalogCode> catalog) {
        this.catalog = catalog;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
