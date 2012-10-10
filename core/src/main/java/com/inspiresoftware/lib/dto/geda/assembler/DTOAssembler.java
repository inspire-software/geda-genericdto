
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
import com.inspiresoftware.lib.dto.geda.assembler.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.assembler.extension.Cache;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.IntHashTable;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.SoftReferenceCache;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.*;
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
	 * int number that allows to define the number of JavassistMethodSynthesizer.READER_CACHE.put() calls after which cache cleanup is launched.
	 * Default setting for this is 100 (i.e. after 100 put's the clean up is launched).
	 */
	public static final String SETTING_DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE = 
		"com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler.DYNAMIC_READER_CLASS_CACHE_CLEANUP_CYCLE";
	
	/**
	 * int number that allows to define the number of JavassistMethodSynthesizer.WRITER_CACHE.put() calls after which cache cleanup is launched.
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
	
	
	private static final Cache<Assembler> CACHE = new SoftReferenceCache<Assembler>(500);

    private static final IntHashTable<Boolean> WHITELIST_ENTITIES = new IntHashTable<Boolean>();

    private static final IntHashTable<Class[]> AUTOBINDING = new IntHashTable<Class[]>();
	
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
        if (!WHITELIST_ENTITIES.containsKey(hash)) {
            if (matches(className.getSimpleName())) {
                if (!className.getSuperclass().equals(Object.class)) {
                    // some proxies are derived straight from Object.class - we do not want those
                    return filterBlacklisted(className.getSuperclass());
                }
            } else {
                WHITELIST_ENTITIES.put(hash, Boolean.TRUE);
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

    private static Assembler getAssemblerFromCache(final int cacheKey) {
        return CACHE.get(cacheKey);
    }

    private static Assembler putAssemblerToCache(final int cacheKey, final Assembler asm) {
        CACHE.put(cacheKey, asm);
        return asm;
    }

	private static Assembler createNewAssembler(final Class< ? > dto,
                                                final Class< ? > entity,
                                                final Object synthesizer,
                                                final Registry registry)
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			       GeDARuntimeException, AnnotationDuplicateBindingException {

        if (synthesizer == null) {
            return new DTOtoEntityAssemblerImpl(dto, entity, SYNTHESIZER, registry);
        }
        return new DTOtoEntityAssemblerImpl(dto, entity, new MethodSynthesizerProxy(synthesizer), registry);
	}

	private static Assembler createNewAssembler(final Class< ? > dto,
                                                final Class< ? >[] entities,
                                                final Object synthesizer,
                                                final Registry registry)
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
			       GeDARuntimeException, AnnotationDuplicateBindingException {

        if (synthesizer == null) {
            return new DTOtoEntitiesAssemblerDecoratorImpl(dto, entities, SYNTHESIZER, registry);
        }
        return new DTOtoEntitiesAssemblerDecoratorImpl(dto, entities, new MethodSynthesizerProxy(synthesizer), registry);
	}

    private static Dto getDtoAnnotation(final Class<?> dto) {
        final Dto ann = dto.getAnnotation(Dto.class);
        if (ann == null) {
            throw new AnnotationMissingException(dto.getCanonicalName());
        }
        return ann;
    }


    private static Dto getDtoAnnotationAuto(final Class<?> dto)
			throws AnnotationMissingException, AnnotationMissingAutobindingException {
		final Dto ann = dto.getAnnotation(Dto.class);
        if (ann == null) {
            throw new AnnotationMissingException(dto.getCanonicalName());
        }
		if (ann == null || ann.value() == null || ann.value().length == 0) {
			throw new AnnotationMissingAutobindingException(dto.getCanonicalName());
		}
		return ann;
	}

	private static Class[] detectAutobinding(final Class< ? > dto)
			throws AutobindingClassNotFoundException {
        final int cacheKey = dto.hashCode();
        if (AUTOBINDING.containsKey(cacheKey)) {
            return AUTOBINDING.get(cacheKey);
        }
        final Dto ann = getDtoAnnotationAuto(dto);
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
        AUTOBINDING.put(cacheKey, classes);
        return classes;
	}

	private static <DTO, Entity> int createAssemberKey(final Class<DTO> dto,
			final Class<Entity> entity, final Object synthesizer) {
        int result = dto.hashCode();
        result = 31 * result + entity.hashCode();
        if (synthesizer == null) {
            result = 31 * result + SYNTHESIZER.hashCode();
        } else {
            result = 31 * result + synthesizer.hashCode();
        }
        return result;
	}

	private static <DTO, Entity> int createAssemberKey(final Class<DTO> dto,
			final Class<Entity>[] entities, final Object synthesizer) {

        int result = dto.hashCode();
        for (final Class entity : entities) {
            result = 31 * result + entity.hashCode();
        }
        if (synthesizer == null) {
            result = 31 * result + SYNTHESIZER.hashCode();
        } else {
            result = 31 * result + synthesizer.hashCode();
        }
        return result;
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

        final Class< ? > realEntity = filterBlacklisted(entity);
        final int key = createAssemberKey(dto, realEntity, synthesizer);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        getDtoAnnotation(dto);

		return putAssemblerToCache(key, createNewAssembler(dto, realEntity, synthesizer, null));
	}

	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entity the entity class or interface that has appropriate getters and setters
     * @param registry DSL registry that contains all mappings
     * @param synthesizer custom method synthesizer to use (see {@link com.inspiresoftware.lib.dto.geda.assembler.MethodSynthesizerProxy} )
	 * @return assembler instance for this conversion.
	 *
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
			final Class< ? > dto, final Class< ? > entity, final Registry registry, final Object synthesizer)
		throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
			   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
			   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
			   GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
        final int key = createAssemberKey(dto, realEntity, synthesizer);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        if (registry == null) {
            throw new GeDARuntimeException("Registry cannot be null");
        }

        return putAssemblerToCache(key, createNewAssembler(dto, realEntity, synthesizer, registry));
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

        getDtoAnnotation(dto);

		return createNewAssembler(dto, entities, synthesizer, null);
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

        final Class< ? > realEntity = filterBlacklisted(entity);
        final int key = createAssemberKey(dto, realEntity, null);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        getDtoAnnotation(dto);

    	return putAssemblerToCache(key, createNewAssembler(dto, realEntity, null, null));
    }

    /**
     * @param dto Dto concrete class that is annotated.
     * @param entity the entity class or interface that has appropriate getters and setters
     * @param registry DSL registry that contains all mappings
     * @return assembler instance for this conversion.
     *
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
    		final Class< ? > dto, final Class< ? > entity, final Registry registry)
    	throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
    	       InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
    	       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
    	       GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
        final int key = createAssemberKey(dto, realEntity, null);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        if (registry == null) {
    		throw new GeDARuntimeException("Registry cannot be null");
    	}

    	return putAssemblerToCache(key, createNewAssembler(dto, realEntity, null, registry));
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

        final int key = createAssemberKey(dto, entities, null);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        getDtoAnnotation(dto);

    	return putAssemblerToCache(key, createNewAssembler(dto, entities, null, null));
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

        final Class[] classes = detectAutobinding(dto);

        final int key = createAssemberKey(dto, classes, synthesizer);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        if (classes.length == 1) {
            return putAssemblerToCache(key, createNewAssembler(dto, classes[0], synthesizer, null));
        }
		return putAssemblerToCache(key, createNewAssembler(dto, classes, synthesizer, null));

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

        final Class[] classes = detectAutobinding(dto);

        final int key = createAssemberKey(dto, classes, null);

        final Assembler asm = getAssemblerFromCache(key);
        if (asm != null) {
            return asm;
        }

        if (classes.length == 1) {
            return putAssemblerToCache(key, createNewAssembler(dto, classes[0], null, null));
        }

		return putAssemblerToCache(key, createNewAssembler(dto, classes, null, null));
	}

}
