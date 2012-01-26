/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.test;

import java.util.Collection;
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

    void loadDtoBefore(ExtendedDataTransferObject dto, DomainObject entity);

    void loadDtoBefore(Class dtoClass, ExtendedDataTransferObject dto, DomainObject entity);

    void loadDtoBefore(Class dtoClass, List<ExtendedDataTransferObject> dtos, List<DomainObject> entities);

    void loadEntityBefore(ExtendedDataTransferObject dto, DomainObject entity);

    void loadEntityBefore(Class dtoClass, ExtendedDataTransferObject dto, DomainObject entity);

    void loadEntityBefore(Class dtoClass, List<ExtendedDataTransferObject> dtos, Set<DomainObject> entity);

    ExtendedDataTransferObject loadDtoAfter(ExtendedDataTransferObject dto, DomainObject entity, boolean createNew);

    ExtendedDataTransferObject loadDtoAfter(Class dtoClass, ExtendedDataTransferObject dto, DomainObject entity, boolean createNew);

    Collection<ExtendedDataTransferObject> loadDtoAfter(Class dtoClass, List<ExtendedDataTransferObject> dtos, List<DomainObject> entities, boolean createNew);

    DomainObject loadEntityAfter(ExtendedDataTransferObject dto, DomainObject entity, boolean createNew);

    DomainObject loadEntityAfter(Class dtoClass, ExtendedDataTransferObject dto, DomainObject entity, boolean createNew);

    Collection<DomainObject> loadEntityAfter(Class dtoClass, List<ExtendedDataTransferObject> dtos, Set<DomainObject> entity, boolean createNew);

}
