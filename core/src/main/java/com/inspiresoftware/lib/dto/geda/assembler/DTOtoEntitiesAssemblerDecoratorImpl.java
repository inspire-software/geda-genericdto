/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.extension.PipeDataFlowRule;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

    DTOtoEntitiesAssemblerDecoratorImpl(final Class dto,
                                        final Class[] entities,
                                        final ClassLoader classLoader,
                                        final MethodSynthesizer synthesizer,
                                        final Registry registry)
        throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
               GeDARuntimeException, AnnotationDuplicateBindingException {

        dtoClass = dto;

        for (final Class entity : entities) {

            composite.put(entity, new DTOtoEntityAssemblerImpl(dto, entity, classLoader, synthesizer, registry, false));

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

    @Override
    public void assembleDto(Object dto, Object entity, Map<String, Object> converters, BeanFactory dtoBeanFactory, PipeDataFlowRule rule) throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, NotValueConverterException, ValueConverterNotFoundException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException {
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
                        asm.assembleDto(dto, value, converters, dtoBeanFactory, rule);
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
                } catch (InstantiationException | IllegalAccessException exp) {
                    throw new UnableToCreateInstanceException(this.dtoClass.getCanonicalName(),
                            "Unable to create dto instance for: " + this.dtoClass.getName(), exp);
                }
            }

        } else {
            throw new InvalidDtoCollectionException();
        }

    }

    @Override
    public void assembleDtos(Collection dtos, Collection entities, Map<String, Object> converters, BeanFactory dtoBeanFactory, PipeDataFlowRule rule) throws InvalidDtoCollectionException, UnableToCreateInstanceException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, AnnotationMissingException, NotValueConverterException, ValueConverterNotFoundException, CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException {
        if (dtos instanceof Collection && dtos.isEmpty() && entities instanceof Collection) {

            for (Object entity : entities) {
                try {
                    final Object dto = this.dtoClass.newInstance();
                    assembleDto(dto, entity, converters, dtoBeanFactory, rule);
                    dtos.add(dto);
                } catch (InstantiationException | IllegalAccessException exp) {
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

    @Override
    public void assembleEntity(Object dto, Object entity, Map<String, Object> converters, BeanFactory entityBeanFactory, PipeDataFlowRule rule) throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, AnnotationMissingBeanKeyException, AnnotationMissingException, UnableToCreateInstanceException, CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
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
                        asm.assembleEntity(dto, value, converters, entityBeanFactory, rule);
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

    @Override
    public void assembleEntities(Collection dtos, Collection entities, Map<String, Object> converters, BeanFactory entityBeanFactory, PipeDataFlowRule rule) throws UnableToCreateInstanceException, InvalidEntityCollectionException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, BeanFactoryNotFoundException, BeanFactoryUnableToCreateInstanceException, NotEntityRetrieverException, EntityRetrieverNotFoundException, NotValueConverterException, ValueConverterNotFoundException, AnnotationMissingBeanKeyException, AnnotationMissingException, CollectionEntityGenericReturnTypeException, InspectionScanningException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, AnnotationDuplicateBindingException, DtoToEntityMatcherNotFoundException, NotDtoToEntityMatcherException {
        throw new UnsupportedOperationException("Unsupported conversion of collection of composite DTO's to collection of entities");
    }

    /** {@inheritDoc} */
    public void releaseResources() {

        final Iterator<Map.Entry<Class, Assembler>> it = composite.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<Class, Assembler> entry = it.next();
            entry.getValue().releaseResources();
            it.remove();
        }

    }
}
