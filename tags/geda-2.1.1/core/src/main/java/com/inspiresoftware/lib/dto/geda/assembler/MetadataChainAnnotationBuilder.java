
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.annotations.*;
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
class MetadataChainAnnotationBuilder implements MetadataChainBuilder {

	/**
	 * Build metadata chain for this field.
	 * @param dtoField fiel to build pipe for
	 * @return metadata chain.
	 * @throws com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException when collections/map pipe cannot create data readers/writers
	 */
	public List<PipeMetadata> build(final Field dtoField) throws UnableToCreateInstanceException {

		final DtoField dtoFieldAnn =
			(DtoField) dtoField.getAnnotation(DtoField.class);
		if (dtoFieldAnn != null) {

			final DtoParent parentAnn = (DtoParent) dtoField.getAnnotation(DtoParent.class);
			return buildFieldChain(dtoField, dtoFieldAnn, parentAnn);
		}

		final DtoVirtualField dtoVirtualFieldAnn =
			(DtoVirtualField) dtoField.getAnnotation(DtoVirtualField.class);
		if (dtoVirtualFieldAnn != null) {
			return buildVirtualFieldChain(dtoField, dtoVirtualFieldAnn);
		}

		final DtoCollection dtoCollAnn =
			(DtoCollection) dtoField.getAnnotation(DtoCollection.class);
		if (dtoCollAnn != null) {

			return buildCollectionChain(dtoField, dtoCollAnn);
		}

	    final DtoMap dtoMapAnn =
	         (DtoMap) dtoField.getAnnotation(DtoMap.class);
	    if (dtoMapAnn != null) {

	        return buildMapChain(dtoField, dtoMapAnn);
	    }

	    return null;

	}

	private List<PipeMetadata> buildVirtualFieldChain(final Field dtoField, final DtoVirtualField dtoFieldAnn) {

		final String[] bindings = { "#this#" + dtoField.getName() };

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new FieldPipeMetadata(
					dtoField.getName(),
					bindings[index],
					dtoFieldAnn.dtoBeanKey(),
					getStringFromArray(dtoFieldAnn.entityBeanKeys(), index),
					dtoFieldAnn.readOnly(),
					dtoFieldAnn.converter(),
					false,
					null,
					null
			));
		}
		return metas;
	}

	private List<PipeMetadata> buildFieldChain(final Field dtoField, final DtoField dtoFieldAnn, final DtoParent dtoParentAnn) {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoFieldAnn.value(), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new FieldPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoFieldAnn.dtoBeanKey(),
				getStringFromArray(dtoFieldAnn.entityBeanKeys(), index),
				dtoFieldAnn.readOnly(),
				dtoFieldAnn.converter(),
				dtoParentAnn != null,
				dtoParentAnn != null ? dtoParentAnn.value() : null,
				dtoParentAnn != null ? dtoParentAnn.retriever() : null
			));
		}
		return metas;
	}

	@SuppressWarnings("unchecked")
	private List<PipeMetadata> buildCollectionChain(final Field dtoField, final DtoCollection dtoCollAnn)
		throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoCollAnn.value(), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new CollectionPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoCollAnn.dtoBeanKey(),
				getStringFromArray(dtoCollAnn.entityBeanKeys(), index),
				dtoCollAnn.readOnly(),
				dtoCollAnn.dtoCollectionClass(),
				dtoCollAnn.dtoCollectionClassKey(),
				dtoCollAnn.entityCollectionClass(),
				dtoCollAnn.entityCollectionClassKey(),
				dtoCollAnn.entityGenericType(),
                dtoCollAnn.entityGenericTypeKey(),
                dtoCollAnn.dtoToEntityMatcher(),
				dtoCollAnn.dtoToEntityMatcherKey()
			));
		}
		return metas;
	}

	@SuppressWarnings("unchecked")
	private List<PipeMetadata> buildMapChain(final Field dtoField, final DtoMap dtoMapAnn) throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoMapAnn.value(), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new MapPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoMapAnn.dtoBeanKey(),
				getStringFromArray(dtoMapAnn.entityBeanKeys(), index),
				dtoMapAnn.readOnly(),
				dtoMapAnn.dtoMapClass(),
				dtoMapAnn.dtoMapClassKey(),
				dtoMapAnn.entityMapOrCollectionClass(),
				dtoMapAnn.entityMapOrCollectionClassKey(),
				dtoMapAnn.entityGenericType(),
                dtoMapAnn.entityGenericTypeKey(),
                dtoMapAnn.entityCollectionMapKey(),
				dtoMapAnn.useEntityMapKey(),
				dtoMapAnn.dtoToEntityMatcher(),
				dtoMapAnn.dtoToEntityMatcherKey()
			));
		}
		return metas;
	}
	
    private String getBindingFromAnnotationOrFieldName(final String annotation, final String fieldName) {
    	if (annotation == null || annotation.length() == 0) {
    		return fieldName;
    	}
    	return annotation;
    }
	
    private String[] createFieldBindingChain(final String binding) {
		if (binding.indexOf('.') == -1) {
			return new String[] { binding };
		} 
		return binding.split("\\.");
	}
    
    private String getStringFromArray(final String[] array, final int index) {
    	if (array != null && index < array.length) {
    		return array[index];
    	}
    	return "";
    }
	
}
