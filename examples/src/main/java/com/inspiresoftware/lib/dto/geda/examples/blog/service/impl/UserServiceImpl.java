/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.examples.blog.service.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 2:01:13 PM
 */
public class UserServiceImpl implements UserService {

    private final UserDAO dao;
    private final BeanFactory bf;

    private final Map<String, Object> converters = new HashMap<String, Object>() {{

    }};

    public UserServiceImpl(final UserDAO dao, final BeanFactory bf) {
        this.dao = dao;
        this.bf = bf;
    }

    public List<BaseUserDTO> list() {
        final List<User> users = dao.list();
        final List<BaseUserDTO> dtos = new ArrayList<BaseUserDTO>();
        DTOAssembler.newAssembler(bf.getClazz("BaseUserDTO"), bf.getClazz("User"))
                .assembleDtos(dtos, users, converters, bf);
        return dtos;
    }

    public List<UserDTO> list(final String filter) {
        final List<User> users = dao.list();
        final List<UserDTO> dtos = new ArrayList<UserDTO>();

        final Assembler asm = DTOAssembler.newAssembler(bf.getClazz(filter), bf.getClazz("User"));

        for (final User user : users) {
            final UserDTO dto = (UserDTO) bf.get("UserDTO");
            asm.assembleDto(dto, user, converters, bf);
            dtos.add(dto);
        }
        return dtos;
    }

    public UserDTO findUser(final Long userId) {
        final User user = dao.findUser(userId);
        final UserDTO dto = (UserDTO) bf.get("UserDTO");
        DTOAssembler.newAssembler(dto.getClass(), bf.getClazz("User"))
                .assembleDto(dto, user, converters, bf);
        return dto;
    }

    public UserEntryDTO findEntry(final Long entryId) {
        final UserEntry entry = dao.findEntry(entryId);
        final UserEntryDTO dto = (UserEntryDTO) bf.get("UserEntryDTO");
        DTOAssembler.newAssembler(dto.getClass(), bf.getClazz("UserEntry"))
                .assembleDto(dto, entry, converters, bf);
        return dto;
    }
}
