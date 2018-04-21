/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.geda;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonDTO;

/**
 * .
 *
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 10:24:56 AM
 */
public class GeDABasicMapper implements Mapper {

    private final BeanFactory bf = new GeDABeanFactory();
    private final Assembler asm;

    public GeDABasicMapper() {
        asm = DTOAssembler.newAssembler(PersonDTO.class, Person.class, this.getClass().getClassLoader());
    }

    public GeDABasicMapper(final String synthesizer) {
        this.asm = DTOAssembler.newCustomAssembler(PersonDTO.class, Person.class, this.getClass().getClassLoader(), synthesizer);
    }

    public Object fromEntity(final Object entity) {
        final PersonDTO person = new PersonDTO();
        asm.assembleDto(person, entity, null, bf);
        return person;
    }

    public Object fromDto(final Object dto) {
        final Person person = new Person();
        asm.assembleEntity(dto, person, null, bf);
        return person;
    }
}
