/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Transferable;
import com.inspiresoftware.lib.dto.geda.test.DomainObject;
import com.inspiresoftware.lib.dto.geda.test.ExtendedDataTransferObject;
import com.inspiresoftware.lib.dto.geda.test.SimpleTransferableService;

import java.util.*;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 3:06:56 PM
 */
public class SimpleTransferableServiceImpl implements SimpleTransferableService {

    @Transferable(before = Direction.ENTITY_TO_DTO)
    public void loadDtoBefore(final ExtendedDataTransferObject dto, final DomainObject entity) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO)
    public void loadDtoBefore(final Class dtoClass, final ExtendedDataTransferObject dto, final DomainObject entity) {

    }

    @Transferable(before = Direction.ENTITY_TO_DTO)
    public void loadDtoBefore(final Class dtoClass, final List<ExtendedDataTransferObject> dtos, final List<DomainObject> entities) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY)
    public void loadEntityBefore(final ExtendedDataTransferObject dto, final DomainObject entity) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY)
    public void loadEntityBefore(final Class dtoClass, final ExtendedDataTransferObject dto, final DomainObject entity) {

    }

    @Transferable(before = Direction.DTO_TO_ENTITY)
    public void loadEntityBefore(final Class dtoClass, final List<ExtendedDataTransferObject> dtos, final Set<DomainObject> entity) {

    }

    @Transferable(before = Direction.NONE, after = Direction.ENTITY_TO_DTO)
    public ExtendedDataTransferObject loadDtoAfter(final ExtendedDataTransferObject dto, final DomainObject entity, final boolean createNew) {
        if (createNew) {
            return new ExtendedDataTransferObjectImpl();
        }
        return dto;
    }

    @Transferable(before = Direction.NONE, after = Direction.ENTITY_TO_DTO)
    public ExtendedDataTransferObject loadDtoAfter(final Class dtoClass, final ExtendedDataTransferObject dto, final DomainObject entity, final boolean createNew) {
        if (createNew) {
            return new ExtendedDataTransferObjectImpl();
        }
        return dto;    }

    @Transferable(before = Direction.NONE, after = Direction.ENTITY_TO_DTO)
    public Collection<ExtendedDataTransferObject> loadDtoAfter(final Class dtoClass, final List<ExtendedDataTransferObject> dtos, final List<DomainObject> entities, final boolean createNew) {
        if (createNew) {
            return new HashSet<ExtendedDataTransferObject>();
        }
        return dtos;
    }

    @Transferable(before = Direction.NONE, after = Direction.DTO_TO_ENTITY)
    public DomainObject loadEntityAfter(final ExtendedDataTransferObject dto, final DomainObject entity, final boolean createNew) {
        if (createNew) {
            return new DomainObjectImpl();
        }
        return entity;
    }

    @Transferable(before = Direction.NONE, after = Direction.DTO_TO_ENTITY)
    public DomainObject loadEntityAfter(final Class dtoClass, final ExtendedDataTransferObject dto, final DomainObject entity, final boolean createNew) {
        if (createNew) {
            return new DomainObjectImpl();
        }
        return entity;
    }

    @Transferable(before = Direction.NONE, after = Direction.DTO_TO_ENTITY)
    public Collection<DomainObject> loadEntityAfter(final Class dtoClass, final List<ExtendedDataTransferObject> dtos, final Set<DomainObject> entity, final boolean createNew) {
        if (createNew) {
            return new ArrayList<DomainObject>();
        }
        return entity;
    }
}
