/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.annotations;

import java.lang.annotation.*;

/**
 * Annotation that allows to specify methods to be advised to trigger
 * automatic dto/entity assembly.
 *
 * There is only one rule to the signatures of the methods that are annotated:
 * The first parameters to the method must match one of the signatures in the
 * {@link com.inspiresoftware.lib.dto.geda.DTOSupport}
 *
 * This annotation will start the transfer process before the method is invoked
 * if before direction is set to other than NONE.
 *
 * After the method had been invoked this annotation will use result as target
 * of the conversion (either DTO or Entity depending on direction) if the
 * after direction is other than NONE.
 *
 * The following signatures are supported (method names are optional but pattern
 * for method arguments and return types must be adhered to. ", ... " specify
 * any number of additional attributes in the method signature):
 *
 * before = DTO_TO_ENTITY:
 *
 *  public void toEntity(DTO dto, Entity entity, ... )
 *  public void toEntity(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
 *
 * before = ENTITY_TO_DTO:
 *
 *  public void toDto(DTO dto, Entity entity, ... )
 *  public void toDto(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
 *
 * after = DTO_TO_ENTITY:
 *
 *  public Entity toEntity(DTO dto, ... )
 *  public void toEntity(DTO dto, Entity entity, ... )
 *  public void toEntity(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
 *
 * after = ENTITY_TO_DTO:
 *  
 *  public DTO toDto(Entity entity, ... )
 *  public void toDto(DTO dto, Entity entity, ... )
 *  public void toDto(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
 *
 * before = DTO_TO_ENTITY, after = ENTITY_TO_DTO (for method with return patterns is source->target->return):
 *
 *  public DTO toEntityAndBackToDto(DTO source, Entity target, ... )
 *  public void toEntityAndBackToDto(DTO source, Entity target, ... )
 *
 * before = ENTITY_TO_DTO, after = DTO_TO_ENTITY (for method with return patterns is source->target->return):
 *
 *  public Entity toDtoAndBackToEntity(DTO target, Entity source, ... )
 *  public void toDtoAndBackToEntity(DTO target, Entity source, ... )
 *
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 7:50:53 PM
 */
@Target( { ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transferable {

    /**
     * @return defines direction of transfer to perform on advised
     *         method before its invocation.
     */
    Direction before() default Direction.NONE;

    /**
     * @return defines direction of transfer to perform on advised
     *         method after its invocation.
     */
    Direction after() default Direction.NONE;

    /**
     * @return key of the dto that will be used as filter. This key has to reference
     *         dto instance that would be a superclass for the one specified in the
     *         dtoKey().
     */
    String dtoFilterKey() default "";

    /**
     * @return key of the "root" dto as method argument dto, return object
     *         dto or element of a collection
     */
    String dtoKey() default "";

    /**
     * @return key of the "root" entity as method argument entity, return object
     *         entity or element of a collection
     */
    String entityKey() default "";

    /**
     * @return context provides additional control over the what is happening to
     *         provide the "context" to event listeners
     */
    String context() default "";

}