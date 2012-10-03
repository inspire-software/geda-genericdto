/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.dsl;

/**
 * DSL version of {@link com.inspiresoftware.lib.dto.geda.annotations.Dto}.
 *
 * @since 2.0.4
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 8:12 AM
 */
public interface DtoContext {

    /**
     * @return DTO class for this context.
     */
    Class getDtoClass();

    /**
     * Register bean key alias for Dto factory.
     *
     * @param beanKey associate a bean key with this DTO.
     * @return mapping context
     */
    DtoContext alias(String beanKey);

    /**
     * Create DTO to Entity relation context.
     *
     * @param entityClass class for entity.
     * @return mapping context
     */
    DtoEntityContext forEntity(Class entityClass);

    /**
     * Create DTO to Entity relation context.
     *
     * @param entityInstance instance of entity.
     * @return mapping context
     */
    DtoEntityContext forEntity(Object entityInstance);

    /**
     * Create DTO to Entity relation context.
     *
     * @param beanKey bean key for entity.
     * @return mapping context
     */
    DtoEntityContext forEntity(String beanKey);

}
