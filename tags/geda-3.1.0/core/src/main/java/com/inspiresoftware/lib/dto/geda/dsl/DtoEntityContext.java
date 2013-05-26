/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.dsl;

/**
 * Binding DSL DTO context for specific entity.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 9:13 AM
 */
public interface DtoEntityContext {
    /**
     * This is just a reference and not to be used for creating Assembler instance
     * configurations. Those parameter should come from DTOAssembler.new* methods.
     *
     * @return DTO class for this context.
     */
    Class getDtoClass();

    /**
     * This is just a reference and not to be used for creating Assembler instance
     * configurations. Those parameter should come from DTOAssembler.new* methods.
     *
     * @return entity class for this context.
     */
    Class getEntityClass();

    /**
     * Register bean key alias for Entity factory.
     *
     * @param beanKey associate a bean key with this Entity (requires BeanFactory for DSL registry).
     * @param representative interface that best describes entity class (or set to null if no interface)
     * @return mapping context
     */
    DtoEntityContext alias(String beanKey, Class representative);

    /**
     * Add DTO field mapping on DTO object. Only single field
     * values are applicable. No field chain is permitted.
     *
     * @param fieldName name of field on DTO class
     * @return dto field context
     */
    DtoFieldContext withField(String fieldName);

    /**
     * Add all DTO fields mappings on DTO object that are same as on
     * entity class. The fields are regarded same if their name and
     * type are the same. If field name or type is different then
     * those fields are ignored.
     *
     * If beanKey reference a class then all fields of this class (including
     * super classes fields will be scanned). For beanKey that references an interface
     * only getters on that interface will be counted towards fields mapping. There are
     * no "extends" scanning for interfaces.
     *
     * @param beanKey bean key to look up entity class for checking
     *                applicable fields. (requires BeanFactory for DSL registry).
     * @param excluding field names that are blacklisted manually and will ignored
     *
     * @return context appender to continue DSL chain
     */
    DtoEntityContextAppender withFieldsSameAsIn(String beanKey, String ... excluding);

    /**
     * Add all DTO fields mappings on DTO object that are same as on
     * entity class. The fields are regarded same if their name and
     * type are the same. If field name or type is different then
     * those fields are ignored.
     *
     * If clazz is a class then all fields of this class (including
     * super classes fields will be scanned). For clazz that is an interface
     * only getters on that interface will be counted towards fields mapping. There are
     * no "extends" scanning for interfaces.
     *
     * @param clazz class or interface for checking applicable fields.
     *              If an interface is specified DTO fields will be matched against
     *              getters on this interface only. There is no inheritance look up.
     * @param excluding field names that are blacklisted manually and will ignored
     *
     * @return context appender to continue DSL chain
     */
    DtoEntityContextAppender withFieldsSameAsIn(Class clazz, String ... excluding);

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
