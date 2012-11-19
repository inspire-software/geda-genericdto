
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.dsl.*;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Builder for creating chains of metadata for annotations.
 * 
 * @author DPavlov
 */
class MetadataChainDSLBuilder implements MetadataChainBuilder {

    private final DtoEntityContext context;

	MetadataChainDSLBuilder(final Registry registry, final Class dtoClass, final Class entityClass) {
        final DtoContext ctx = registry.has(dtoClass);
        if (ctx != null) {
            this.context = ctx.has(entityClass);
        } else {
            this.context = null;
        }
    }

	/**
	 * Build metadata chain for this field.
	 * @param dtoField fiel to build pipe for
	 * @return metadata chain.
	 * @throws com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException when collections/map pipe cannot create data readers/writers
	 */
	public List<PipeMetadata> build(final Field dtoField) throws UnableToCreateInstanceException {

        if (this.context == null) {
            return null;
        }

        final Object fieldContext = this.context.has(dtoField.getName());

        if (fieldContext == null) {
            return null;
        }

        if (fieldContext instanceof DtoFieldContext) {

            final DtoFieldContext field = (DtoFieldContext) fieldContext;

            if (field.getValueOfVirtual()) {
                return buildVirtualFieldChain(dtoField, field);
            } else {
                return buildFieldChain(dtoField, field);
            }

        } else if (fieldContext instanceof DtoCollectionContext) {

            return buildCollectionChain(dtoField, (DtoCollectionContext) fieldContext);

        } else if (fieldContext instanceof DtoMapContext) {

            return buildMapChain(dtoField, (DtoMapContext) fieldContext);

        }

	    return null;

	}

	private List<PipeMetadata> buildVirtualFieldChain(final Field dtoField, final DtoFieldContext dtoFieldContext) {

		final String[] bindings = { "#this#" + dtoField.getName() };

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new FieldPipeMetadata(
                    dtoField.getName(),
                    bindings[index],
                    dtoFieldContext.getValueOfDtoBeanKey(),
                    getStringFromArray(dtoFieldContext.getValueOfEntityBeanKeys(), index),
                    dtoFieldContext.getValueOfReadOnly(),
                    dtoFieldContext.getValueOfConverter(),
                    false,
                    null,
                    null
            ));
		}
		return metas;
	}

	private List<PipeMetadata> buildFieldChain(final Field dtoField, final DtoFieldContext dtoFieldContext) {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoFieldContext.getValueOfEntityField(), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new FieldPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoFieldContext.getValueOfDtoBeanKey(),
				getStringFromArray(dtoFieldContext.getValueOfEntityBeanKeys(), index),
				dtoFieldContext.getValueOfReadOnly(),
				dtoFieldContext.getValueOfConverter(),
				dtoFieldContext.getValueOfDtoParent(),
                dtoFieldContext.getValueOfDtoParent() ? dtoFieldContext.getValueOfDtoParentPrimaryKey() : null,
                dtoFieldContext.getValueOfDtoParent() ? dtoFieldContext.getValueOfDtoParentRetriever() : null
			));
		}
		return metas;
	}

	@SuppressWarnings("unchecked")
	private static List<PipeMetadata> buildCollectionChain(final Field dtoField, final DtoCollectionContext dtoCollContext)
		throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoCollContext.getValueOfEntityField(), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new CollectionPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoCollContext.getValueOfDtoBeanKey(),
				getStringFromArray(dtoCollContext.getValueOfEntityBeanKeys(), index),
				dtoCollContext.getValueOfReadOnly(),
				dtoCollContext.getValueOfDtoCollectionClass(),
				dtoCollContext.getValueOfDtoCollectionClassKey(),
				dtoCollContext.getValueOfEntityCollectionClass(),
				dtoCollContext.getValueOfEntityCollectionClassKey(),
				dtoCollContext.getValueOfEntityGenericType(),
                dtoCollContext.getValueOfEntityGenericTypeKey(),
                dtoCollContext.getValueOfDtoToEntityMatcher(),
				dtoCollContext.getValueOfDtoToEntityMatcherKey()
			));
		}
		return metas;
	}

	@SuppressWarnings("unchecked")
	private static List<PipeMetadata> buildMapChain(final Field dtoField, final DtoMapContext dtoMapContext) throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoMapContext.getValueOfEntityField(), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new MapPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoMapContext.getValueOfDtoBeanKey(),
				getStringFromArray(dtoMapContext.getValueOfEntityBeanKeys(), index),
				dtoMapContext.getValueOfReadOnly(),
				dtoMapContext.getValueOfDtoMapClass(),
				dtoMapContext.getValueOfDtoMapClassKey(),
				dtoMapContext.getValueOfEntityMapOrCollectionClass(),
				dtoMapContext.getValueOfEntityMapOrCollectionClassKey(),
				dtoMapContext.getValueOfEntityGenericType(),
                dtoMapContext.getValueOfEntityGenericTypeKey(),
                dtoMapContext.getValueOfEntityCollectionMapKey(),
				dtoMapContext.getValueOfUseEntityMapKey(),
				dtoMapContext.getValueOfDtoToEntityMatcher(),
				dtoMapContext.getValueOfDtoToEntityMatcherKey()
			));
		}
		return metas;
	}
	
    private static String getBindingFromAnnotationOrFieldName(final String annotation, final String fieldName) {
    	if (annotation == null || annotation.length() == 0) {
    		return fieldName;
    	}
    	return annotation;
    }
	
    private static String[] createFieldBindingChain(final String binding) {
		if (binding.indexOf('.') == -1) {
			return new String[] { binding };
		} 
		return binding.split("\\.");
	}
    
    private static String getStringFromArray(final String[] array, final int index) {
    	if (array != null && index < array.length) {
    		return array[index];
    	}
    	return "";
    }
	
}
