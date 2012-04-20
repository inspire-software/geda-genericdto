

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

/**
 * Field pipe meta contains information on the metadata of the pipe.
 * 
 * @author DPavlov
 */
public class FieldPipeMetadata extends BasePipeMetadata implements com.inspiresoftware.lib.dto.geda.assembler.meta.FieldPipeMetadata {

	private final String converterKey;
	private final boolean child;
	private final String parentEntityPrimaryKeyField;
	private final String entityRetrieverKey;

	/**
     * @param dtoFieldName key for accessing field on DTO object
     * @param entityFieldName key for accessing field on Entity bean
     * @param dtoBeanKey key for constructing DTO bean
     * @param entityBeanKey key for constructing Entity bean
     * @param readOnly read only marker (true then write to entity is omitted)
     * @param converterKey converter to use for this field data piping
     * @param child true if field has {@link com.inspiresoftware.lib.dto.geda.annotations.DtoParent} annotation
     * @param parentEntityPrimaryKeyField must be specified for child=true to use as PK for parent
	 * @param entityRetrieverKey must be specified for child=true to use for parent retrieval.
	 */
	public FieldPipeMetadata(final String dtoFieldName, 
							 final String entityFieldName, 
							 final String dtoBeanKey, 
							 final String entityBeanKey,
							 final boolean readOnly, 
							 final String converterKey, 
							 final boolean child, 
							 final String parentEntityPrimaryKeyField,
							 final String entityRetrieverKey) {
		super(dtoFieldName, entityFieldName, dtoBeanKey, entityBeanKey, readOnly);
		this.converterKey = converterKey;
		this.child = child;
		this.parentEntityPrimaryKeyField = parentEntityPrimaryKeyField;
		this.entityRetrieverKey = entityRetrieverKey;
	}

	/** {@inheritDoc} */
	public String getConverterKey() {
		return converterKey;
	}

	/** {@inheritDoc} */
	public boolean isChild() {
		return child;
	}

	/** {@inheritDoc} */
	public String getParentEntityPrimaryKeyField() {
		return parentEntityPrimaryKeyField;
	}

	/** {@inheritDoc} */
	public String getEntityRetrieverKey() {
		return entityRetrieverKey;
	}
	
}
