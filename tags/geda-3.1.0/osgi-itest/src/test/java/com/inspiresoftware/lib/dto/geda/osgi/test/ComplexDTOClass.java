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

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.annotations.DtoMap;

import java.util.List;
import java.util.Map;

/**
 * User: denispavlov
 * Date: 13-02-21
 * Time: 8:17 AM
 */
@Dto
public class ComplexDTOClass implements ComplexDTO {

    @DtoField
    private String name;
    @DtoField(
            dtoBeanKey = "SimpleDTO",
            entityBeanKeys = "SimpleEntity"
    )
    private SimpleDTO inner;
    @DtoCollection(
            dtoBeanKey = "SimpleDTO",
            entityBeanKeys = "SimpleEntity",
            entityGenericType = SimpleEntity.class,
            dtoToEntityMatcherKey = "EqualsByString"
    )
    private List<SimpleDTO> collection;
    @DtoMap(
            dtoBeanKey = "SimpleDTO",
            entityBeanKeys = "SimpleEntity",
            entityGenericType = SimpleEntity.class,
            dtoToEntityMatcherKey = "EqualsByString"
    )
    private Map<String, SimpleDTO> map;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public SimpleDTO getInner() {
        return inner;
    }

    public void setInner(final SimpleDTO inner) {
        this.inner = inner;
    }

    public List<SimpleDTO> getCollection() {
        return collection;
    }

    public void setCollection(final List<SimpleDTO> collection) {
        this.collection = collection;
    }

    public Map<String, SimpleDTO> getMap() {
        return map;
    }

    public void setMap(final Map<String, SimpleDTO> map) {
        this.map = map;
    }
}
