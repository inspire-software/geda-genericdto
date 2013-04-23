/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.usecases.generics.extend;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.annotations.DtoParent;
import org.junit.Ignore;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:25 AM
 */
@Ignore
@Dto
public class DtoCatalogCodeClass extends DtoCodeClass implements EntityCatalogCode {

    @DtoField
    private String sectionName;

    @DtoParent(value = "id", retriever = "CatalogRetriever")
    @DtoField(value = "catalog",
            dtoBeanKey = "DtoCatalog" ,
            entityBeanKeys = "Catalog")
    private EntityCatalog<EntityCatalogCode> catalog;

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
}
