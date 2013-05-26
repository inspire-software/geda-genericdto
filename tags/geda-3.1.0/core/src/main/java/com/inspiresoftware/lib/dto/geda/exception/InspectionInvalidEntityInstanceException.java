
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.exception;

/**
 * Denotes exception for assembler being used with unsupported Entity instance.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class InspectionInvalidEntityInstanceException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String className;
	private final String entityName;

	/**
	 * @param className bean class name
	 * @param entity entity instance
	 */
	public InspectionInvalidEntityInstanceException(
			final String className,
			final Object entity) {
		super("This assembler is only applicable for entity: " + className 
				+ (entity != null ? ", found: " + entity.getClass().getCanonicalName() : ""));
		this.className = className;
		this.entityName = entity != null ? entity.getClass().getCanonicalName() : null;
	}

	/**
	 * @return entity name
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @return supported entity class name
	 */
	public String getClassName() {
		return className;
	}

	
}
