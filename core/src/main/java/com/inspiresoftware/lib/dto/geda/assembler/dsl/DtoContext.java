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

    /**
     * Create DTO generic mapping (no specific restriction on the entity class).
     *
     * @return mapping context
     */
    DtoEntityContext forEntityGeneric();

    /**
     * Check if this DTO context has applicable mapping for entity.
     *
     * @param entityClass entity class
     * @return dto entity mapping context or empty.
     */
    DtoEntityContext has(Class entityClass);

    /**
     * Link entity class mapping to already existing context for this DTO.
     *
     * e.g. dtoCtx.useContextFor(dtoCtx.forEntity("myEntity"), MyAnotherEntity.class)
     *      will reuse dto-"myEntity" mapping for dto-MyAnotherEntity.
     *      This will be the same context instance but available through
     *      different class selection, so that:
     *      dtoCtx.forEntity("myEntity") == dtoCtx.forEntity(MyAnotherEntity.class)
     *
     * @param ctx already existing context for this DTO
     * @param entityClass another class to use ctx for
     * @return ctx
     */
    DtoEntityContext useContextFor(DtoEntityContext ctx, Class entityClass);

    /**
     * Link entity class mapping to already existing context for this DTO.
     *
     * e.g. dtoCtx.useContextFor(dtoCtx.forEntity("myEntity"), MyAnotherEntity.class)
     *      will reuse dto-"myEntity" mapping for dto-MyAnotherEntity.
     *      This will be the same context instance but available through
     *      different class selection, so that:
     *      dtoCtx.forEntity("myEntity") == dtoCtx.forEntity(MyAnotherEntity.class)
     *
     * @param ctx already existing context for this DTO
     * @param beanKey bean key for another class to use ctx for
     * @return ctx
     */
    DtoEntityContext useContextFor(DtoEntityContext ctx, String beanKey);

}
