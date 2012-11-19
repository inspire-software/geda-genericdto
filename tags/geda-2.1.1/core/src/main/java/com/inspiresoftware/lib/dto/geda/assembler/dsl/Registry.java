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
 * Registry allows user to use DSL style mapping for DTO objects.
 * When methods are invoked all information is stored within
 * registry and kept for when it is used with assembler.
 *
 * @since 2.1.0
 *
 * User: denispavlov
 * Date: 12-09-20
 * Time: 8:11 AM
 */
public interface Registry {

    /**
     * Check if this registry has this DTO.
     *
     * @param dtoClass dto class
     * @return dto mapping context or null
     */
    DtoContext has(Class dtoClass);

    /**
     * New DTO mapping by given dto class.
     *
     * @param dtoClass dto class
     * @return dto mapping context
     */
    DtoContext dto(Class dtoClass);

    /**
     * New DTO mapping by given dto instance.
     *
     * @param dtoInstance instance of dto class
     * @return dto mapping context
     */
    DtoContext dto(Object dtoInstance);

    /**
     * New DTO mapping by given dto bean key (used by {@link com.inspiresoftware.lib.dto.geda.adapter.BeanFactory}).
     *
     * @param beanKey bean key of dto class
     * @return dto mapping context
     */
    DtoContext dto(String beanKey);

}
