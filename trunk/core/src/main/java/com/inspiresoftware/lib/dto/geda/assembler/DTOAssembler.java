
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */


package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.assembler.extension.Cache;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.SoftReferenceCache;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Assemble DTO and Entities depending on the annotations of Dto.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@SuppressWarnings("unchecked")
public final class DTOAssembler {
	
	/**
	 * int number that allows to define the number of DTOAssembler.CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 50 (i.e. after 50 put's the clean up is launched).
	 */
	public static final String SETTING_ASSEMBLER_CACHE_CLEANUP_CYCLE = 
		"com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler.ASSEMBLER_CACHE_CLEANUP_CYCLE";
	/**
	 * int number that allows to define the number of JavassitMethodSynthesizer.READER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE = 
		"com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE";
	
	/**
	 * int number that allows to define the number of JavassitMethodSynthesizer.WRITER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE = 
		"com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE";

	/**
	 * String that defines key for synthesizer implementation to use.
	 */
	public static final String SETTING_SYNTHESIZER_IMPL = 
		"com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler.SETTING_SYNTHESIZER_IMPL";

	/**
	 * String that defines key for pattern for blacklisting entity class names.
	 * E.g. Javasist implementation enhances classes by subclassing and adding _$$_javasist
	 * prefix. This pattern will allow to detect these classes and look up parent class names
	 * to overcome class cast exceptions.
	 */
	public static final String SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN = 
		"com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler.SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN";
	private static final String SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN_DEFAULT = "_\\$\\$_";
	private static Pattern entityClassNameBlacklistPatternValue = Pattern.compile(SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN_DEFAULT);
	
	
	private static final Cache<Integer, Assembler> CACHE = new SoftReferenceCache<Integer, Assembler>(500);

    private static final Set<Integer> WHITELIST_ENTITIES = new HashSet<Integer>();
	
	/**
	 * Setup allows to configure some of the behaviour of GeDA. Currently it is used to tune the caching cleanup cycles.
	 * There are two caches used in GeDA:
	 * <ul>
	 * <li>DTOAssembler cache - that caches the assemblers instances</li>
	 * <li>Dynamic classes cache - that caches the instances of {@link com.inspiresoftware.lib.dto.geda.assembler.extension.DataReader}s 
	 * and {@link com.inspiresoftware.lib.dto.geda.assembler.extension.DataWriter}s</li>
	 * </ul>
	 * 
	 * @param props properties with key specified by DTOAssembler.SETTINGS_* keys
	 * 
	 * @throws NumberFormatException if the number of cycles specified in properties cannot be converted to int.
	 * @throws GeDAException if any of the configurations cause exception
	 */
	public static void setup(final Properties props) throws NumberFormatException, GeDAException {
		final String assemblerCache = props.getProperty(SETTING_ASSEMBLER_CACHE_CLEANUP_CYCLE);
		final String javassistReaderCache = props.getProperty(SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE);
		final String javassistWriterCache = props.getProperty(SETTING_DYNAMIC_WRITER_CLASS_CACHE_CLEANUP_CYCLE);
		final String synthesizerImpl = props.getProperty(SETTING_SYNTHESIZER_IMPL);
		final String classNameBlacklistPattern = props.getProperty(SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN);
		if (assemblerCache != null) {
			CACHE.configure("cleanUpCycle", Integer.valueOf(assemblerCache));
		}
		if (javassistReaderCache != null) {
			SYNTHESIZER.configure("readerCleanUpCycle", Integer.valueOf(javassistReaderCache));
		}
		if (javassistWriterCache != null) {
			SYNTHESIZER.configure("writerCleanUpCycle", Integer.valueOf(javassistWriterCache));
		}
		if (synthesizerImpl != null) {
			SYNTHESIZER.configure("synthesizerImpl", synthesizerImpl);
		}
		if (classNameBlacklistPattern != null) {
			entityClassNameBlacklistPatternValue = Pattern.compile(classNameBlacklistPattern);
		}
	}

	private static final MethodSynthesizer SYNTHESIZER = new MethodSynthesizerProxy();
	

	private static Class filterBlacklisted(final Class className) {
        final int hash = className.hashCode();
        if (!WHITELIST_ENTITIES.contains(hash)) {
            if (matches(className.getSimpleName())) {
                if (!className.getSuperclass().equals(Object.class)) {
                    // some proxies are derived straight from Object.class - we do not want those
                    return filterBlacklisted(className.getSuperclass());
                }
            } else {
                WHITELIST_ENTITIES.add(hash);
            }
        }
		return className;
	}
	
	/**
	 * Exposed for testing. 
	 * 
	 * @param className entity class name
	 * @return true if class name matches pattern, which will result in going one class level up.
	 */
	static boolean matches(final String className) {
		final Matcher match = entityClassNameBlacklistPatternValue.matcher(className);
		return match.find();
	}

	private static Assembler createNewAssembler(final Class< ? > dto, final Class< ? > entity, final Object synthesizer)
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			       GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
		final Integer key;
        if (synthesizer == null) {
            key = createAssemberKey(dto, realEntity, SYNTHESIZER);
        } else {
            key = createAssemberKey(dto, realEntity, synthesizer);
        }
    	
		Assembler assembler = CACHE.get(key);
		if (assembler == null) {
            final MethodSynthesizer syn;
            if (synthesizer == null) {
                syn = SYNTHESIZER;
            } else {
                syn = new MethodSynthesizerProxy(synthesizer);
            }
            assembler = new DTOtoEntityAssemblerImpl(dto, realEntity, syn);
	    	CACHE.put(key, assembler);
		}
    	return assembler;
	}

	private static Assembler createNewAssembler(final Class< ? > dto, final Class< ? >[] entities, final Object synthesizer)
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
			       GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? >[] realEntities = new Class< ? >[entities.length];
        for (int i = 0; i < entities.length; i++) {
            realEntities[i] = filterBlacklisted(entities[i]);
        }

        final Integer key;
        if (synthesizer == null) {
            key = createAssemberKey(dto, realEntities, SYNTHESIZER);
        } else {
            key = createAssemberKey(dto, realEntities, synthesizer);
        }

		Assembler assembler = CACHE.get(key);
		if (assembler == null) {
            final MethodSynthesizer syn;
            if (synthesizer == null) {
                syn = SYNTHESIZER;
            } else {
                syn = new MethodSynthesizerProxy(synthesizer);
            }
            assembler = new DTOtoEntitiesAssemblerDecoratorImpl(dto, realEntities, syn);
	    	CACHE.put(key, assembler);
		}
    	return assembler;
	}

	private static Dto getDtoAnnotation(final Class< ? > dto)
			throws AnnotationMissingAutobindingException {
		final Dto ann = dto.getAnnotation(Dto.class);
		if (ann == null || ann.value() == null || ann.value().length == 0) {
			throw new AnnotationMissingAutobindingException(dto.getCanonicalName());
		}
		return ann;
	}

	private static Class[] detectAutobinding(final Class< ? > dto, final Dto ann)
			throws AutobindingClassNotFoundException {
        final Class[] classes = new Class[ann.value().length];
        for (int i = 0; i < ann.value().length; i++) {
            final String clazz = ann.value()[i];
            try {
                if (clazz == null || clazz.length() == 0) {
                    throw new AnnotationMissingAutobindingException(dto.getCanonicalName());
                }
                classes[i] = Class.forName(clazz);
            } catch (ClassNotFoundException cnfe) {
                throw new AutobindingClassNotFoundException(dto.getCanonicalName(), clazz);
            }
        }
        return classes;
	}

	private static <DTO, Entity> Integer createAssemberKey(final Class<DTO> dto,
			final Class<Entity> entity, final Object synthesizer) {
        int result = dto.hashCode();
        result = 31 * result + entity.hashCode();
        result = 31 * result + synthesizer.hashCode();
        return Integer.valueOf(result);
	}

	private static <DTO, Entity> Integer createAssemberKey(final Class<DTO> dto,
			final Class<Entity>[] entities, final Object synthesizer) {

        int result = dto.hashCode();
        for (final Class entity : entities) {
            result = 31 * result + entity.hashCode();
        }
        result = 31 * result + synthesizer.hashCode();
        return Integer.valueOf(result);
	}

	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entity the entity class or interface that has appropriate getters and setters
     * @param synthesizer custom method synthesizer to use (see {@link com.inspiresoftware.lib.dto.geda.assembler.MethodSynthesizerProxy} )
	 * @return assembler instance for this conversion.
	 * 
	 * @throws AnnotationMissingException if dto class is missing the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} annotation.
	 * @throws InspectionInvalidDtoInstanceException if dto instance used for read/write operation is not valid
	 * @throws InspectionInvalidEntityInstanceException if entity instance used for read/write operation is not valid
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
	 * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	public static Assembler newCustomAssembler(
			final Class< ? > dto, final Class< ? > entity, final Object synthesizer) 
		throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
			   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			   GeDARuntimeException, AnnotationDuplicateBindingException {
		
		if (dto.getAnnotation(Dto.class) == null) {
			throw new AnnotationMissingException(dto.getName());
		}
		
		return createNewAssembler(dto, entity, synthesizer);
	}

	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entities the entity classes or interfaces that have appropriate getters and setters
     * @param synthesizer custom method synthesizer to use (see {@link com.inspiresoftware.lib.dto.geda.assembler.MethodSynthesizerProxy} )
	 * @return assembler instance for this conversion.
	 *
	 * @throws AnnotationMissingException if dto class is missing the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} annotation.
	 * @throws InspectionInvalidDtoInstanceException if dto instance used for read/write operation is not valid
	 * @throws InspectionInvalidEntityInstanceException if entity instance used for read/write operation is not valid
	 * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
	 * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
	 * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
	 * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
	 * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
	 * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
	 * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
	 * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	public static Assembler newCustomCompositeAssembler(
			final Class< ? > dto, final Class< ? >[] entities, final Object synthesizer)
		throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
			   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
			   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
			   GeDARuntimeException, AnnotationDuplicateBindingException {

		if (dto.getAnnotation(Dto.class) == null) {
			throw new AnnotationMissingException(dto.getName());
		}

		return createNewAssembler(dto, entities, synthesizer);
	}

    /**
     * @param dto Dto concrete class that is annotated.
     * @param entity the entity class or interface that has appropriate getters and setters
     * @return assembler instance for this conversion.
     * 
     * @throws AnnotationMissingException if dto class is missing the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} annotation.
     * @throws InspectionInvalidDtoInstanceException if dto instance used for read/write operation is not valid
     * @throws InspectionInvalidEntityInstanceException if entity instance used for read/write operation is not valid
     * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    public static Assembler newAssembler(
    		final Class< ? > dto, final Class< ? > entity) 
    	throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
    	       InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
    	       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
    	       GeDARuntimeException, AnnotationDuplicateBindingException {
    	
    	if (dto.getAnnotation(Dto.class) == null) {
    		throw new AnnotationMissingException(dto.getName());
    	}
    	
    	return createNewAssembler(dto, entity, null);
    }

    /**
     * @param dto Dto concrete class that is annotated.
     * @param entities the entity classes or interfaces that has appropriate getters and setters
     * @return assembler instance for this conversion.
     *
     * @throws AnnotationMissingException if dto class is missing the {@link com.inspiresoftware.lib.dto.geda.annotations.Dto} annotation.
     * @throws InspectionInvalidDtoInstanceException if dto instance used for read/write operation is not valid
     * @throws InspectionInvalidEntityInstanceException if entity instance used for read/write operation is not valid
     * @throws AnnotationDuplicateBindingException if during mapping scan same dto field is mapped more than once
     * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    public static Assembler newCompositeAssembler(
    		final Class< ? > dto, final Class< ? >[] entities)
    	throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
    	       InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
    	       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
    	       GeDARuntimeException, AnnotationDuplicateBindingException {

    	if (dto.getAnnotation(Dto.class) == null) {
    		throw new AnnotationMissingException(dto.getName());
    	}

    	return createNewAssembler(dto, entities, null);
    }

    /**
     * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
     * @param synthesizer custom method synthesizer to use (see {@link com.inspiresoftware.lib.dto.geda.assembler.MethodSynthesizerProxy} )
     * @return assembler instance for this conversion.
     * 
     * @throws AnnotationMissingAutobindingException if autobinding parameter is missing
     * @throws AutobindingClassNotFoundException if class specified for auto binding cannot be found
     * @throws AnnotationDuplicateBindingException if same field name is mapped more than once
     * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
     */
    public static Assembler newCustomAssembler(
    		final Class< ? > dto, final Object synthesizer) 
	    throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException, 
			   UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
			   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
			   AnnotationDuplicateBindingException  {

        final Class[] classes = detectAutobinding(dto, getDtoAnnotation(dto));
        if (classes.length == 1) {
            return createNewAssembler(dto, classes[0], synthesizer);
        }
		return createNewAssembler(dto, classes, synthesizer);

    }
    	
	/**
	 * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
	 * @return assembler instance for this conversion.
	 * 
	 * @throws AnnotationMissingAutobindingException if autobinding parameter is missing
	 * @throws AutobindingClassNotFoundException if class specified for auto binding cannot be found
	 * @throws AnnotationDuplicateBindingException if same field name is mapped more than once
	 * @throws GeDARuntimeException unhandled cases - this is (if GeDA was not tampered with) means library failure and should be reported
     * @throws AnnotationValidatingBindingException in case binding create has a mismatching return type/parameters
     * @throws AnnotationMissingBindingException in case when no valid property on entity is specified to bind to 
     * @throws InspectionBindingNotFoundException in case when no valid property on entity is found to bind to
     * @throws InspectionPropertyNotFoundException in case a binding field cannot be found
     * @throws UnableToCreateInstanceException if an instance of an auto created class (one that is directly created by GeDA) cannot be instantiated
     * @throws InspectionScanningException general error that may occur during scanning a class for fields and method descriptors
	 */
	public static Assembler newAssembler(
			final Class< ? > dto) 
		throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException, 
		       UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException, 
		       AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException, 
		       AnnotationDuplicateBindingException  {

        final Class[] classes = detectAutobinding(dto, getDtoAnnotation(dto));
        if (classes.length == 1) {
            return createNewAssembler(dto, classes[0], null);
        }

		return createNewAssembler(dto, classes, null);
	}

}
