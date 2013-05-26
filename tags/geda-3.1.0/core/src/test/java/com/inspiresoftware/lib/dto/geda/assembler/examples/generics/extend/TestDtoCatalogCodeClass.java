/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.generics.extend;

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
public class TestDtoCatalogCodeClass extends TestDtoCodeClass implements TestEntityCatalogCode {

    @DtoField
    private String sectionName;

    @DtoParent(value = "id", retriever = "CatalogRetriever")
    @DtoField(value = "catalog",
            dtoBeanKey = "DtoCatalog" ,
            entityBeanKeys = "Catalog")
    private TestEntityCatalog<TestEntityCatalogCode> catalog;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(final String sectionName) {
        this.sectionName = sectionName;
    }

    public TestEntityCatalog<TestEntityCatalogCode> getCatalog() {
        return catalog;
    }

    public void setCatalog(final TestEntityCatalog<TestEntityCatalogCode> catalog) {
        this.catalog = catalog;
    }
}
