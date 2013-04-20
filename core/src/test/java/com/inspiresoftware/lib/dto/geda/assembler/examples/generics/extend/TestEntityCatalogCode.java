/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.generics.extend;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:16 AM
 */
public interface TestEntityCatalogCode extends TestEntityCode {

    public String getSectionName();
    public void setSectionName(String sectionName);

    public TestEntityCatalog<TestEntityCatalogCode> getCatalog();
    public void setCatalog(TestEntityCatalog<TestEntityCatalogCode> catalog);

}
