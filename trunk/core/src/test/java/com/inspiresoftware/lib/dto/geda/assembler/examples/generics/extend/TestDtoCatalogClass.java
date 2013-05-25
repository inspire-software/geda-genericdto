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
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

import java.util.Collection;

/**
 * User: denispavlov
 * Date: 13-04-20
 * Time: 9:30 AM
 */
@Ignore
@Dto
public class TestDtoCatalogClass<T extends TestEntityCode> implements TestEntityCatalog<T> {

    @DtoField
    private String id;

    /*
     * CatalogCode has reference to Catalog so we need to limit recursion by
     * specifying dtoBeanKey as DtoCode which does not include catalog reference
     */
    @DtoField(
            dtoBeanKey = "DtoCode",
            entityBeanKeys = "CatalogCode")
    private T type;

    /*
     * CatalogCode has reference to Catalog so we need to limit recursion by
     * specifying dtoBeanKey as DtoCode which does not include catalog reference
     */
    @DtoCollection(
            dtoBeanKey = "DtoCode",
            entityBeanKeys = "CatalogCode",
            entityGenericType = TestEntityCode.class,
            dtoToEntityMatcherKey = "CatalogCodeMatcher"
    )
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
