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

import org.junit.Ignore;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:18 AM
 */
@Ignore
public class TestEntityCatalogCodeClass implements TestEntityCatalogCode {

    private String sectionName;
    private TestEntityCatalog<TestEntityCatalogCode> catalog;

    private String code;
    private String id;

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
