
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
 * Denotes exception for case when biding between dto and entity field is not matching.
 * 
 * @author denispavlov
 *
 * @since 1.1.2
 */
public class AnnotationValidatingBindingException extends GeDAException {

	private static final long serialVersionUID = 20110609L;
	
	private final String dtoField;
	private final String dtoBinder;
	private final String dtoBinderClass;
	private final String entityField;
	private final String entityBinder;
	private final String entityBinderClass;
	private final boolean dtoRead;

	/**
	 * @param dtoField dto field
	 * @param dtoBinder dto reader/writer class name
	 * @param dtoBinderClass class of the return type/parameter for dto reader/writer
	 * @param entityField entity field
	 * @param entityBinder entity reader/writer class name
	 * @param entityBinderClass class of the return type/parameter for entity reader/writer
	 * @param dtoRead true if dto binder is a reader, false if it is a writer
	 */
	public AnnotationValidatingBindingException(
			final String dtoField,
			final String dtoBinder,
			final String dtoBinderClass,
			final String entityField,
			final String entityBinder,
			final String entityBinderClass,
			final boolean dtoRead) {
		super("Type mismatch is detected for: DTO " + dtoField 
				+ (dtoRead ? " read" : " write") + " {" + dtoBinder + "[" + dtoBinderClass + "]} " 
				+ "and Entity " + entityField 
				+ (dtoRead ? " write" : " read") + " {" + entityBinder + "[" + entityBinderClass 
				+ "]}. Consider using a converter.");
		this.dtoBinder = dtoBinder;
		this.dtoBinderClass = dtoBinderClass;
		this.entityBinder = entityBinder;
		this.entityBinderClass = entityBinderClass;
		this.dtoField = dtoField;
		this.entityField = entityField;
		this.dtoRead = dtoRead;
	}

	/**
	 * @return dto reader/writer
	 */
	public String getDtoBinder() {
		return dtoBinder;
	}

	/**
	 * @return dto reader/writer return type/parameter
	 */
	public String getDtoBinderClass() {
		return dtoBinderClass;
	}

	/**
	 * @return entity reader/writer
	 */
	public String getEntityBinder() {
		return entityBinder;
	}

	/**
	 * @return entity reader/writer return type/parameter
	 */
	public String getEntityBinderClass() {
		return entityBinderClass;
	}
	
	/**
	 * @return dto field
	 */
	public String getDtoField() {
		return dtoField;
	}

	/**
	 * @return entity field
	 */
	public String getEntityField() {
		return entityField;
	}

	/**
	 * @return true if {@link #dtoBinder} is a reader, false if is a writer
	 */
	public boolean isDtoRead() {
		return dtoRead;
	}

}
