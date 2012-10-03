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
 * Binding DSL DTO context for specific entity.
 *
 * @since 2.0.4
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 8:19 AM
 */
public interface DtoEntityContext {

    /**
     * @return DTO class for this context.
     */
    Class getDtoClass();

    /**
     * @return entity class for this context.
     */
    Class getEntityClass();

    /**
     * Register bean key alias for Entity factory.
     *
     * @param beanKey associate a bean key with this Entity.
     * @return mapping context
     */
    DtoEntityContext alias(String beanKey);

    /**
     * Add DTO field mapping on DTO object. Only single field
     * values are applicable. No field chain is permitted.
     *
     * @param fieldName name of field on DTO class
     * @return dto field context
     */
    DtoFieldContext withField(String fieldName);

    /**
     * Add DTO collection mapping on DTO object. Only single field
     * values are applicable. No field chain is permitted.
     *
     * @param fieldName name of collection field on DTO class
     * @return dto field context
     */
    DtoCollectionContext withCollection(String fieldName);

    /**
     * Add DTO map mapping on DTO object. Only single field
     * values are applicable. No field chain is permitted.
     *
     * @param fieldName name of map field on DTO class
     * @return dto field context
     */
    DtoMapContext withMap(String fieldName);

    /**
     * Retrieves one of field context instances or null if
     * no mapping exists.
     *
     * @param fieldName name of a field on DTO class
     * @return dto field context or null.
     */
    Object has(String fieldName);

}
