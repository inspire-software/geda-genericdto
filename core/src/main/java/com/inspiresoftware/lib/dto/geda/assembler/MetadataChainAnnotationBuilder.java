
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

import com.inspiresoftware.lib.dto.geda.assembler.annotations.AnnotationProxy;
import com.inspiresoftware.lib.dto.geda.assembler.annotations.impl.AnnotationProxies;
import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.UnableToCreateInstanceException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

		final Map<String, AnnotationProxy> dtoFieldAnn = AnnotationProxies.getFieldAnnotationProxy(dtoField);

        if (dtoFieldAnn.isEmpty()) {
            return null;
        }

		if (dtoFieldAnn.containsKey("DtoField")) {

			return buildFieldChain(dtoField, dtoFieldAnn.get("DtoField"), dtoFieldAnn.get("DtoParent"));
		}

		if (dtoFieldAnn.containsKey("DtoVirtualField")) {
			return buildVirtualFieldChain(dtoField, dtoFieldAnn.get("DtoVirtualField"));
		}

		if (dtoFieldAnn.containsKey("DtoCollection")) {
			return buildCollectionChain(dtoField, dtoFieldAnn.get("DtoCollection"));
		}

	    if (dtoFieldAnn.containsKey("DtoMap")) {
	        return buildMapChain(dtoField, dtoFieldAnn.get("DtoMap"));
	    }

	    return null;

	}

	private List<PipeMetadata> buildVirtualFieldChain(final Field dtoField, final AnnotationProxy dtoFieldAnn) {

		final String[] bindings = { "#this#" + dtoField.getName() };

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new FieldPipeMetadata(
					dtoField.getName(),
					bindings[index],
					(String) dtoFieldAnn.getValue("dtoBeanKey"),
					getStringFromArray((String[]) dtoFieldAnn.getValue("entityBeanKeys"), index),
					(Boolean) dtoFieldAnn.getValue("readOnly"),
					(String) dtoFieldAnn.getValue("converter"),
					false,
					null,
					null
			));
		}
		return metas;
	}

	private List<PipeMetadata> buildFieldChain(final Field dtoField, final AnnotationProxy dtoFieldAnn, final AnnotationProxy dtoParentAnn) {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName((String) dtoFieldAnn.getValue("value"), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new FieldPipeMetadata(
				dtoField.getName(),
				bindings[index],
				(String) dtoFieldAnn.getValue("dtoBeanKey"),
				getStringFromArray((String[]) dtoFieldAnn.getValue("entityBeanKeys"), index),
                (Boolean) dtoFieldAnn.getValue("readOnly"),
                (String) dtoFieldAnn.getValue("converter"),
				dtoParentAnn != null,
				dtoParentAnn != null ? (String) dtoParentAnn.getValue("value") : null,
				dtoParentAnn != null ? (String) dtoParentAnn.getValue("retriever") : null
			));
		}
		return metas;
	}

	@SuppressWarnings("unchecked")
	private List<PipeMetadata> buildCollectionChain(final Field dtoField, final AnnotationProxy dtoCollAnn)
		throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName((String) dtoCollAnn.getValue("value"), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new CollectionPipeMetadata(
				dtoField.getName(),
				bindings[index],
				(String) dtoCollAnn.getValue("dtoBeanKey"),
				getStringFromArray((String[]) dtoCollAnn.getValue("entityBeanKeys"), index),
				(Boolean) dtoCollAnn.getValue("readOnly"),
				(Class) dtoCollAnn.getValue("dtoCollectionClass"),
				(String) dtoCollAnn.getValue("dtoCollectionClassKey"),
				(Class) dtoCollAnn.getValue("entityCollectionClass"),
				(String) dtoCollAnn.getValue("entityCollectionClassKey"),
				(Class) dtoCollAnn.getValue("entityGenericType"),
                (String) dtoCollAnn.getValue("entityGenericTypeKey"),
                (Class) dtoCollAnn.getValue("dtoToEntityMatcher"),
				(String) dtoCollAnn.getValue("dtoToEntityMatcherKey")
			));
		}
		return metas;
	}

	@SuppressWarnings("unchecked")
	private List<PipeMetadata> buildMapChain(final Field dtoField, final AnnotationProxy dtoMapAnn) throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName((String) dtoMapAnn.getValue("value"), dtoField.getName()));

		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new MapPipeMetadata(
				dtoField.getName(),
				bindings[index],
				(String) dtoMapAnn.getValue("dtoBeanKey"),
				getStringFromArray((String[]) dtoMapAnn.getValue("entityBeanKeys"), index),
				(Boolean) dtoMapAnn.getValue("readOnly"),
				(Class) dtoMapAnn.getValue("dtoMapClass"),
				(String) dtoMapAnn.getValue("dtoMapClassKey"),
				(Class) dtoMapAnn.getValue("entityMapOrCollectionClass"),
				(String) dtoMapAnn.getValue("entityMapOrCollectionClassKey"),
				(Class) dtoMapAnn.getValue("entityGenericType"),
                (String) dtoMapAnn.getValue("entityGenericTypeKey"),
                (String) dtoMapAnn.getValue("entityCollectionMapKey"),
				(Boolean) dtoMapAnn.getValue("useEntityMapKey"),
				(Class) dtoMapAnn.getValue("dtoToEntityMatcher"),
				(String) dtoMapAnn.getValue("dtoToEntityMatcherKey")
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
