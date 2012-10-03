/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Composite assembler to allow many entities to become a single Dto.
 *
 * @author Denis Pavlov
 * @since 2.0.2
 *
 */
@SuppressWarnings("unchecked")
public class DTOtoEntitiesAssemblerDecoratorImpl implements Assembler {

    private final Map<Class, Assembler> composite = new HashMap<Class, Assembler>();

    private final Class dtoClass;

    DTOtoEntitiesAssemblerDecoratorImpl(final Class dto, final Class[] entities,
                                        final MethodSynthesizer synthesizer, final Registry registry)
        throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
               GeDARuntimeException, AnnotationDuplicateBindingException {

        dtoClass = dto;

        for (final Class entity : entities) {

            composite.put(entity, new DTOtoEntityAssemblerImpl(dto, entity, synthesizer, registry, false));

        }


    }


    /** {@inheritDoc} */
    public void assembleDto(final Object dto, final Object entity,
            final Map<String, Object> converters,
            final BeanFactory dtoBeanFactory)
        throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException,
               BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, NotValueConverterException,
               ValueConverterNotFoundException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException,
               InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
               AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
               AnnotationDuplicateBindingException {

        final Object[] values;
        if (entity instanceof Object[]) {
            values = (Object[]) entity;
        } else {
            values = new Object[] { entity };
        }

        for (final Object value : values) {
            if (value != null) {
                for (final Class type : composite.keySet()) {
                    if (type.isAssignableFrom(value.getClass())) {
                        final Assembler asm = composite.get(type);
                        asm.assembleDto(dto, value, converters, dtoBeanFactory);
                        break;
                    }
                }
            }
        }

    }

    /** {@inheritDoc} */
    public void assembleDtos(final Collection dtos, final Collection entities,
                            final Map<String, Object> converters,
                            final BeanFactory dtoBeanFactory)
        throws InvalidDtoCollectionException, UnableToCreateInstanceException, InspectionInvalidDtoInstanceException,
               InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException,
               AnnotationMissingException, NotValueConverterException, ValueConverterNotFoundException,
               CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException,
               InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
               GeDARuntimeException, AnnotationDuplicateBindingException {

        if (dtos instanceof Collection && dtos.isEmpty() && entities instanceof Collection) {

            for (Object entity : entities) {
                try {
                    final Object dto = this.dtoClass.newInstance();
                    assembleDto(dto, entity, converters, dtoBeanFactory);
                    dtos.add(dto);
                } catch (InstantiationException exp) {
                    throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(),
                            "Unable to create dto instance for: " + this.dtoClass.getName(), exp);
                } catch (IllegalAccessException exp) {
                    throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(),
                            "Unable to create dto instance for: " + this.dtoClass.getName(), exp);
                }
            }

        } else {
            throw new InvalidDtoCollectionException();
        }

    }

    /** {@inheritDoc} */
    public void assembleEntity(final Object dto, final Object entity,
            final Map<String, Object> converters, final BeanFactory entityBeanFactory)
        throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException,
               BeanFactoryUnableToCreateInstanceException, NotEntityRetrieverException, EntityRetrieverNotFoundException,
               NotValueConverterException, ValueConverterNotFoundException, AnnotationMissingBeanKeyException,
               AnnotationMissingException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException,
               InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
               AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
               AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {

        final Object[] values;
        if (entity instanceof Object[]) {
            values = (Object[]) entity;
        } else {
            values = new Object[] { entity };
        }

        for (final Object value : values) {
            if (value != null) {
                for (final Class type : composite.keySet()) {
                    if (type.isAssignableFrom(value.getClass())) {
                        final Assembler asm = composite.get(type);
                        asm.assembleEntity(dto, value, converters, entityBeanFactory);
                        break;
                    }
                }
            }
        }

    }

    /** {@inheritDoc} */
    public void assembleEntities(final Collection dtos, final Collection entities,
            final Map<String, Object> converters, final BeanFactory entityBeanFactory)
        throws UnableToCreateInstanceException, InvalidEntityCollectionException, InspectionInvalidDtoInstanceException,
               InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException,
               NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException,
               ValueConverterNotFoundException, AnnotationMissingBeanKeyException, AnnotationMissingException,
               CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException,
               InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
               GeDARuntimeException, AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException,
               NotDtoToEntityMatcherException {

        throw new UnsupportedOperationException("Unsupported conversion of collection of composite DTO's to collection of entities");
    }

}
