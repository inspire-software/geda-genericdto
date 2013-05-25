/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.modelmapper;

import com.inspiresoftware.lib.dto.geda.benchmark.Mapper;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Person;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.PersonWithHistoryDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 11:39:32 AM
 */
public class ModelMapperListMapper implements Mapper {

    private ModelMapper mapperFromDto;
    private ModelMapper mapperFromEntity;

    public ModelMapperListMapper() {
        mapperFromDto = new ModelMapper();
        mapperFromDto.addMappings(new PropertyMap<PersonWithHistoryDTO, Person>() {
            @Override
            protected void configure() {
                map().getName().setFirstname(source.getFirstName());
                map().getName().setSurname(source.getLastName());
            }
        });
        mapperFromEntity = new ModelMapper();
        mapperFromEntity.addMappings(new PropertyMap<Person, PersonWithHistoryDTO>() {
            @Override
            protected void configure() {
                map().setFirstName(source.getName().getFirstname());
                map().setLastName(source.getName().getSurname());
            }
        });
    }

    public Object fromEntity(final Object entity) {
        return mapperFromEntity.map(entity, PersonWithHistoryDTO.class);
    }

    public Object fromDto(final Object dto) {
        return mapperFromDto.map(dto, Person.class);
    }
}
