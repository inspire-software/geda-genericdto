
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * Assembles DataPipe.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
class DataPipeBuilder extends BasePipeBuilder<FieldPipeMetadata> {

	/**
	 * {@inheritDoc}
     *
	 * @throws InspectionPropertyNotFoundException when fails to find descriptors for fields
	 * @throws InspectionBindingNotFoundException when fails to find property on entity
	 * @throws InspectionScanningException when fails to find parent entity properties
	 * @throws UnableToCreateInstanceException when fails to create data reader for parent class
	 * @throws AnnotationValidatingBindingException when fails to bind the parent field
	 * @throws AnnotationMissingBindingException when fails to bind the parent field
	 * @throws GeDARuntimeException  unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 */
	public Pipe build(
            final AssemblerContext context,
			final Class dtoClass, final Class entityClass,
			final PropertyDescriptor[] dtoPropertyDescriptors,
			final PropertyDescriptor[] entityPropertyDescriptors,
			final FieldPipeMetadata meta, final Pipe pipe)
		throws InspectionPropertyNotFoundException, InspectionBindingNotFoundException, InspectionScanningException, 
		       UnableToCreateInstanceException, AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException {

        final boolean isMapEntity = Map.class.isAssignableFrom(entityClass);
        final boolean isListEntity = !isMapEntity && List.class.isAssignableFrom(entityClass);

		final PropertyDescriptor dtoFieldDesc = PropertyInspector.getDtoPropertyDescriptorForField(
				dtoClass, meta.getDtoFieldName(), dtoPropertyDescriptors);

        final MethodSynthesizer synthesizer = context.getMethodSynthesizer();
        final MethodSynthesizer entitySynthesizer;
		final PropertyDescriptor entityFieldDesc;
        if (isMapEntity || isListEntity) {
            if (isMapEntity) {
                entitySynthesizer = mapSynthesizer;
            } else {
                entitySynthesizer = listSynthesizer;
            }
            entityFieldDesc = dtoFieldDesc;
        } else {
            entitySynthesizer = synthesizer;
            entityFieldDesc = PropertyInspector.getEntityPropertyDescriptorForField(
				dtoClass, entityClass, meta.getDtoFieldName(), meta.getEntityFieldName(), entityPropertyDescriptors);
        }
		
		final DataReader dtoParentReadMethod;
		

		if (meta.isChild()) {
			final Method parentGet = dtoFieldDesc.getReadMethod();

            Class returnTypeClass;
            try {
                returnTypeClass = PropertyInspector.getClassForType(parentGet.getGenericReturnType());
            } catch (GeDARuntimeException gre) {
                throw new GeDARuntimeException(
                        "Generics tree is too complex only rawTypes are supported class: " + dtoClass.getSimpleName()
                                + ", method: " + parentGet.getName(), gre);
            }

            final PropertyDescriptor[] dtoSubPropertyDescriptors = PropertyInspector.getPropertyDescriptorsForClass(returnTypeClass);
			final PropertyDescriptor dtoParentDesc = PropertyInspector.getDtoPropertyDescriptorForField(
					dtoClass, meta.getParentEntityPrimaryKeyField(), dtoSubPropertyDescriptors);
			dtoParentReadMethod = context.getMethodSynthesizer().synthesizeReader(dtoParentDesc);

		} else {
			
			dtoParentReadMethod = null;
			
		}
		
		return new DataPipe(context,
                meta.isReadOnly() ? null : synthesizer.synthesizeReader(dtoFieldDesc),
				synthesizer.synthesizeWriter(dtoFieldDesc),
				dtoParentReadMethod,
                entitySynthesizer.synthesizeReader(entityFieldDesc),
				meta.isReadOnly() ? null : entitySynthesizer.synthesizeWriter(entityFieldDesc),
				meta
		);
	}
	
}
