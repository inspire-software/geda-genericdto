

/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.assembler;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.adapter.meta.PipeMetadata;


/**
 * Abstract pipe meta contains information on the metadata of the pipe.
 * 
 * @author DPavlov
 */
class BasePipeMetadata implements PipeMetadata {

	private final String dtoFieldName;
	private final String entityFieldName;
    private final boolean readOnly;
    private final String dtoBeanKey;
    private final String entityBeanKey;
    
    /**
     * @param dtoFieldName key for accessing field on DTO object
     * @param entityFieldName key for accessing field on Entity bean
     * @param dtoBeanKey key for constructing DTO bean
     * @param entityBeanKey key for constructing Entity bean
     * @param readOnly read only marker (true then write to entity is omitted)
     */
	public BasePipeMetadata(final String dtoFieldName,
								final String entityFieldName,
								final String dtoBeanKey, 
								final String entityBeanKey, 
								final boolean readOnly) {
		this.dtoFieldName = dtoFieldName;
		this.entityFieldName = entityFieldName;
		this.dtoBeanKey = dtoBeanKey;
		this.entityBeanKey = entityBeanKey;
		this.readOnly = readOnly;
	}

	/** {@inheritDoc} */
	public String getDtoFieldName() {
		return dtoFieldName;
	}

	/** {@inheritDoc} */
	public String getEntityFieldName() {
		return entityFieldName;
	}

	/** {@inheritDoc} */
	public boolean isReadOnly() {
		return readOnly;
	}

	/** {@inheritDoc} */
	public String getDtoBeanKey() {
		return dtoBeanKey;
	}
	
	/** {@inheritDoc} */
	public Object newDtoBean(final BeanFactory factory) throws IllegalArgumentException {
		return newBean(this.getDtoBeanKey(), factory);
	}

	/** {@inheritDoc} */
	public String getEntityBeanKey() {
		return entityBeanKey;
	}
	
	/** {@inheritDoc} */
	public Object newEntityBean(final BeanFactory factory) throws IllegalArgumentException {
		return newBean(this.getEntityBeanKey(), factory);
	}
	
	private Object newBean(final String beanKey, final BeanFactory factory) throws IllegalArgumentException {
		if (factory == null) {
			throw new IllegalArgumentException("No factory provided for: " 
					+ dtoFieldName + "@" + dtoBeanKey + " - " 
					+ entityFieldName + "@" + entityBeanKey);
		}
		final Object newObject = factory.get(beanKey);
		if (newObject == null) {
			throw new IllegalArgumentException("Unable to construct bean with key: " 
					+ beanKey + " using beanFactory: " + factory + "for: " 
					+ dtoFieldName + "@" + dtoBeanKey + " - " 
					+ entityFieldName + "@" + entityBeanKey);
		}
		return newObject;
	}
			
}
