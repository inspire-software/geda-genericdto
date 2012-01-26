/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.TransferableBefore;

import java.util.List;
import java.util.Set;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 3:04:22 PM
 */
public interface SimpleTransferableService {

    @TransferableBefore(direction = Direction.ENTITY_TO_DTO)
    void loadDto(ExtendedDataTransferObject dto, DomainObject entity);

    @TransferableBefore(direction = Direction.ENTITY_TO_DTO)
    void loadDto(Class dtoClass, ExtendedDataTransferObject dto, DomainObject entity);

    @TransferableBefore(direction = Direction.ENTITY_TO_DTO)
    void loadDto(Class dtoClass, List<ExtendedDataTransferObject> dtos, List<DomainObject> entities);

    @TransferableBefore(direction = Direction.DTO_TO_ENTITY)
    void loadEntity(ExtendedDataTransferObject dto, DomainObject entity);

    @TransferableBefore(direction = Direction.DTO_TO_ENTITY)
    void loadEntity(Class dtoClass, ExtendedDataTransferObject dto, DomainObject entity);

    @TransferableBefore(direction = Direction.DTO_TO_ENTITY)
    void loadEntity(Class dtoClass, List<ExtendedDataTransferObject> dtos, Set<DomainObject> entity);

}
