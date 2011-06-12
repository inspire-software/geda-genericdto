
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.exception;

/**
 * Denotes exception for missing retriever.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class EntityRetrieverNotFoundException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoFieldName;
	private final String entityFieldName;
	private final String converterKey;

	/**
	 * @param dtoFieldName field name of the dto instance
	 * @param entityFieldName field name of the entity instance
	 * @param converterKey bean key to use in converter map
	 */
	public EntityRetrieverNotFoundException(
			final String dtoFieldName,
			final String entityFieldName,
			final String converterKey) {
		super("Required retriever: " + converterKey 
    			+ " cannot be located to retrieve: " + entityFieldName + " for " + dtoFieldName);
		this.dtoFieldName = dtoFieldName;
		this.entityFieldName = entityFieldName;
		this.converterKey = converterKey;
	}

	/**
	 * @return dto field
	 */
	public String getDtoFieldName() {
		return dtoFieldName;
	}

	/**
	 * @return entity field
	 */
	public String getEntityFieldName() {
		return entityFieldName;
	}

	/**
	 * @return converter key
	 */
	public String getConverterKey() {
		return converterKey;
	}
	
}
