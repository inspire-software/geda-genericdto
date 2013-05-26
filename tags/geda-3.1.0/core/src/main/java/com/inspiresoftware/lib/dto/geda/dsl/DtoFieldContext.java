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
 * DSL version of {@link com.inspiresoftware.lib.dto.geda.annotations.DtoField}.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 8:44 AM
 */
public interface DtoFieldContext extends DtoEntityContextAppender {
    /**
     * Map this DTO field to entity field.
     *
     * field name on entity class that will be bound to this dto field
     * (reflection notation e.g. myField.mySubfield).
     *
     * @param fieldName name of field on DTO class
     * @return dto field context
     */
    DtoFieldContext forField(String fieldName);

    /**
     * Map this DTO field as virtual.
     *
     * @return dto field context (need to specify converter)
     */
    DtoVirtualFieldContext forVirtual();

    /**
     * Marks Dto for read only state. When assembler assembles entity the data in Dto fields with
     * readOnly set to true will be ignored.
     *
     * @return dto field context
     */
    DtoFieldContext readOnly();

    /**
     * Textual reference to a converter to use when assembling DTO's and Entities. This reference is
     * used to lookup converter in adapters map passed into assembleDto and assembleEntity methods.
     * This converter must implement {@link com.inspiresoftware.lib.dto.geda.adapter.ValueConverter}.
     *
     * @param converter converter key (requires adapters parameter during assembly)
     * @return dto field context
     */
    DtoFieldContext converter(String converter);

    /**
     * This annotation is mandatory with deeply nested entity object i.e. when a '.' syntax is used.
     * Failure to supply this parameter will result in
     * {@link com.inspiresoftware.lib.dto.geda.exception.GeDAException}.
     *
     * Specifies entity bean key chain that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler}
     * assembleEntity methods.
     *
     * @param entityBeanKeys bean keys ((requires BeanFactory parameter during assembly)).
     * @return dto field context
     */
    DtoFieldContext entityBeanKeys(String... entityBeanKeys);

    /**
     * This annotation is mandatory for nested objects that are used as fields within the top
     * level DTO.
     *
     * Specifies DTO bean key that will be used by bean factory injected to
     * {@link com.inspiresoftware.lib.dto.geda.assembler.Assembler} assembleDto methods.
     *
     * @param dtoBeanKey bean key ((requires BeanFactory parameter during assembly))
     * @return dto field context
     */
    DtoFieldContext dtoBeanKey(String dtoBeanKey);

    /**
     * field name on entity class that will be bound to this dto field
     * (reflection notation e.g. myField.mySubfield).
     *
     * This fields value will be passed on to {@link com.inspiresoftware.lib.dto.geda.adapter.EntityRetriever}
     * as primary key.
     *
     * @param primaryKeyFieldName field that is a PK for entity retrieval
     * @return dto parent field context
     */
    DtoParentContext dtoParent(String primaryKeyFieldName);

    /**
     * @return dto field name
     */
    String getValueOfDtoField();

    /**
     * @return entity field name
     */
    String getValueOfEntityField();

    /**
     * @return true if read only
     */
    boolean getValueOfReadOnly();

    /**
     * @return entity bean keys chain
     */
    String[] getValueOfEntityBeanKeys();

    /**
     * @return dto bean key
     */
    String getValueOfDtoBeanKey();

    /**
     * @return true if this field is virtual
     */
    boolean getValueOfVirtual();

    /**
     * @return converter key
     */
    String getValueOfConverter();

    /**
     * @return true if this field represents parent
     */
    boolean getValueOfDtoParent();

    /**
     * @return primary key for parent
     */
    String getValueOfDtoParentPrimaryKey();

    /**
     * @return retriever key
     */
    String getValueOfDtoParentRetriever();
}
