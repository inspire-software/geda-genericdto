
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.examples.chained;

//would normally be a persistent class (JPA/Hibernate)
// coding standards dictate there shall be no non-accessor methods staring with get and set
public class ChainedSetterClass {
    private Integer id;
    private String description;

    public ChainedSetterClass(){
        //Hibernate!
    }

    public Integer getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public ChainedSetterClass setId(Integer id) {
        this.id = id;
        return this;
    }

    public ChainedSetterClass setDescription(String description) {
        this.description = description;
        return this;
    }
}
