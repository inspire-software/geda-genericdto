
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dp.lib.dto.geda.annotations.DtoCollection;
import dp.lib.dto.geda.annotations.DtoField;
import dp.lib.dto.geda.annotations.DtoMap;
import dp.lib.dto.geda.annotations.DtoParent;
import dp.lib.dto.geda.assembler.meta.PipeMetadata;
import dp.lib.dto.geda.exception.UnableToCreateInstanceException;

/**
 * Builder for creating chains of metadata for annotations.
 * 
 * @author DPavlov
 */
final class MetadataChainBuilder {

	private MetadataChainBuilder() {
		// prevent instantiation
	}
	
	/**
	 * Build metadata chain for this field.
	 * @param dtoField fiel to build pipe for
	 * @return metadata chain.
	 * @throws UnableToCreateInstanceException when collections/map pipe cannot create data readers/writers
	 */
	public static List<PipeMetadata> build(final Field dtoField) throws UnableToCreateInstanceException {
		
		final DtoField dtoFieldAnn =
			(DtoField) dtoField.getAnnotation(DtoField.class);
		if (dtoFieldAnn != null) {

			final DtoParent parentAnn = (DtoParent) dtoField.getAnnotation(DtoParent.class);
			return buildFieldChain(dtoField, dtoFieldAnn, parentAnn);
		}

		final DtoCollection dtoCollAnn =
			(DtoCollection) dtoField.getAnnotation(DtoCollection.class);
		if (dtoCollAnn != null) {
			
			return buildCollectionChain(dtoField, dtoCollAnn);
		}
		
	    final DtoMap dtoMapAnn =
	         (DtoMap) dtoField.getAnnotation(DtoMap.class);
	    if (dtoMapAnn != null) {

	        return buildCollectionChain(dtoField, dtoMapAnn);
	    }
	    
	    return null;
		
	}
	
	private static List<PipeMetadata> buildFieldChain(final Field dtoField, final DtoField dtoFieldAnn, final DtoParent dtoParentAnn) {
		
		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoFieldAnn.value(), dtoField.getName()));
		
		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new dp.lib.dto.geda.assembler.FieldPipeMetadata(
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
	private static List<PipeMetadata> buildCollectionChain(final Field dtoField, final DtoCollection dtoCollAnn) throws UnableToCreateInstanceException {

		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoCollAnn.value(), dtoField.getName()));
		
		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new dp.lib.dto.geda.assembler.CollectionPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoCollAnn.dtoBeanKey(),
				getStringFromArray(dtoCollAnn.entityBeanKeys(), index),
				dtoCollAnn.readOnly(),
				dtoCollAnn.dtoCollectionClass(),
				dtoCollAnn.entityCollectionClass(),
				dtoCollAnn.entityGenericType(),
				dtoCollAnn.dtoToEntityMatcher()
			));
		}
		return metas;
	}
	
	@SuppressWarnings("unchecked")
	private static List<PipeMetadata> buildCollectionChain(final Field dtoField, final DtoMap dtoMapAnn) throws UnableToCreateInstanceException {
		
		final String[] bindings = createFieldBindingChain(getBindingFromAnnotationOrFieldName(dtoMapAnn.value(), dtoField.getName()));
		
		final List<PipeMetadata> metas = new ArrayList<PipeMetadata>(bindings.length);
		for (int index = 0; index < bindings.length; index++) {
			metas.add(new dp.lib.dto.geda.assembler.MapPipeMetadata(
				dtoField.getName(),
				bindings[index],
				dtoMapAnn.dtoBeanKey(),
				getStringFromArray(dtoMapAnn.entityBeanKeys(), index),
				dtoMapAnn.readOnly(),
				dtoMapAnn.dtoMapClass(),
				dtoMapAnn.entityMapOrCollectionClass(),
				dtoMapAnn.entityGenericType(),
				dtoMapAnn.entityCollectionMapKey(),
				dtoMapAnn.useEntityMapKey(),
				dtoMapAnn.dtoToEntityMatcher()
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
