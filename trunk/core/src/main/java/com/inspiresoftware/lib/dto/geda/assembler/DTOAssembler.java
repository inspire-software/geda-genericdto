
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
import com.inspiresoftware.lib.dto.geda.assembler.extension.Cache;
import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.IntHashTable;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.SoftReferenceCache;
import com.inspiresoftware.lib.dto.geda.dsl.Registry;
import com.inspiresoftware.lib.dto.geda.exception.*;

import java.util.Properties;
import java.util.WeakHashMap;
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
	
	private static final WeakHashMap<ClassLoader, Cache<Assembler>> CL_CACHE = new WeakHashMap<ClassLoader, Cache<Assembler>>();

    private static final WeakHashMap<ClassLoader, MethodSynthesizer> CL_SYNTHESIZER = new WeakHashMap<ClassLoader, MethodSynthesizer>();

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
		final String synthesizerImpl = props.getProperty(SETTING_SYNTHESIZER_IMPL);
		final String classNameBlacklistPattern = props.getProperty(SETTING_ENTITY_CLASS_NAME_BLACKLIST_PATTERN);
		if (synthesizerImpl != null) {
            for (final MethodSynthesizer synthesizer : CL_SYNTHESIZER.values()) {
                synthesizer.configure("synthesizerImpl", synthesizerImpl);
            }
		}
		if (classNameBlacklistPattern != null) {
			entityClassNameBlacklistPatternValue = Pattern.compile(classNameBlacklistPattern);
		}
	}

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

    private static Assembler getAssemblerFromCache(final ClassLoader cl, final int cacheKey) {
        if (CL_CACHE.containsKey(cl)) {
            return CL_CACHE.get(cl).get(cacheKey);
        }
        return null;
    }

    private static Assembler putAssemblerToCache(final ClassLoader cl, final int cacheKey, final Assembler asm) {
        Cache<Assembler> cache = CL_CACHE.get(cl);
        if (cache == null) {
            cache = new SoftReferenceCache<Assembler>();
            CL_CACHE.put(cl, cache);
        }
        cache.put(cacheKey, asm);
        return asm;
    }

	private static Assembler createNewAssembler(
                final Class< ? > dto,
                final Class< ? > entity,
                final ClassLoader classLoader,
                final Object synthesizer,
                final Registry registry)
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			       GeDARuntimeException, AnnotationDuplicateBindingException {

        if (synthesizer instanceof MethodSynthesizerProxy) {
            return new DTOtoEntityAssemblerImpl(dto, entity, classLoader, (MethodSynthesizer) synthesizer, registry);
        }
        return new DTOtoEntityAssemblerImpl(dto, entity, classLoader, new MethodSynthesizerProxy(classLoader, synthesizer), registry);
	}

	private static Assembler createNewAssembler(
                final Class< ? > dto,
                final Class< ? >[] entities,
                final ClassLoader classLoader,
                final Object synthesizer,
                final Registry registry)
			throws InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
			       InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
			       GeDARuntimeException, AnnotationDuplicateBindingException {

        if (synthesizer instanceof MethodSynthesizerProxy) {
            return new DTOtoEntitiesAssemblerDecoratorImpl(dto, entities, classLoader, (MethodSynthesizer) synthesizer, registry);
        }
        return new DTOtoEntitiesAssemblerDecoratorImpl(dto, entities, classLoader, new MethodSynthesizerProxy(classLoader, synthesizer), registry);
	}

    private static AnnotationProxy getDtoAnnotation(final Class<?> dto) {
        final AnnotationProxy ann = AnnotationProxies.getClassAnnotationProxy(dto);
        if (ann.annotationExists()) {
            return ann;
        }
        throw new AnnotationMissingException(dto.getCanonicalName());
    }


    private static AnnotationProxy getDtoAnnotationAuto(final Class<?> dto)
			throws AnnotationMissingException, AnnotationMissingAutobindingException {
		final AnnotationProxy ann = AnnotationProxies.getClassAnnotationProxy(dto);
        if (!ann.annotationExists()) {
            throw new AnnotationMissingException(dto.getCanonicalName());
        }
        final String[] auto = ann.getValue("value");
		if (auto == null || auto.length == 0) {
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
        final AnnotationProxy ann = getDtoAnnotationAuto(dto);
        final String[] auto = ann.getValue("value");
        final Class[] classes = new Class[auto.length];
        for (int i = 0; i < auto.length; i++) {
            final String clazz = auto[i];
            try {
                if (clazz == null || clazz.length() == 0) {
                    throw new AnnotationMissingAutobindingException(dto.getCanonicalName());
                }
                classes[i] = Class.forName(clazz, true, dto.getClassLoader());
            } catch (ClassNotFoundException cnfe) {
                throw new AutobindingClassNotFoundException(dto.getCanonicalName(), clazz);
            }
        }
        AUTOBINDING.put(cacheKey, classes);
        return classes;
	}

    private static MethodSynthesizer getDefaultSynthesizer(final ClassLoader classLoader) {
        MethodSynthesizer syn = CL_SYNTHESIZER.get(classLoader);
        if (syn == null) {
            syn = new MethodSynthesizerProxy(classLoader);
            CL_SYNTHESIZER.put(classLoader, syn);
        }
        return syn;
    }

    private static <DTO, Entity> int createAssemblerKey(
            final Class<DTO> dto,
            final Class<Entity> entity,
            final Object synthesizer,
            final Registry registry) {
        int result = dto.hashCode();
        result = 31 * result + entity.hashCode();
        result = 31 * result + synthesizer.hashCode();
        if (registry  != null) {
            result = 31 * result + registry.hashCode();
        }
        return result;
	}

	private static <DTO, Entity> int createAssemblerKey(
            final Class<DTO> dto,
            final Class<Entity>[] entities,
            final Object synthesizer,
            final Registry registry) {

        int result = dto.hashCode();
        for (final Class entity : entities) {
            result = 31 * result + entity.hashCode();
        }
        result = 31 * result + synthesizer.hashCode();
        if (registry  != null) {
            result = 31 * result + registry.hashCode();
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
            final Class< ? > dto,
            final Class< ? > entity,
            final Object synthesizer)
        throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
            InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
            InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
            GeDARuntimeException, AnnotationDuplicateBindingException {

        return newCustomAssembler(dto, entity, DTOAssembler.class.getClassLoader(), synthesizer);
    }

	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entity the entity class or interface that has appropriate getters and setters
     * @param classLoader class loader for object graph serviced by this assembler
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
			final Class< ? > dto,
            final Class< ? > entity,
            final ClassLoader classLoader,
            final Object synthesizer)
		throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException, 
			   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException, 
			   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException, 
			   GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
        final int key = createAssemblerKey(dto, realEntity, synthesizer, null);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        getDtoAnnotation(dto);

		return putAssemblerToCache(classLoader, key, createNewAssembler(dto, realEntity, classLoader, synthesizer, null));
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
                final Class< ? > dto,
                final Class< ? > entity,
                final Registry registry,
                final Object synthesizer)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                    InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                    InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                    GeDARuntimeException, AnnotationDuplicateBindingException {

        return newCustomAssembler(dto, entity, DTOAssembler.class.getClassLoader(), registry, synthesizer);
    }

	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entity the entity class or interface that has appropriate getters and setters
     * @param classLoader class loader for object graph serviced by this assembler
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
			final Class< ? > dto,
            final Class< ? > entity,
            final ClassLoader classLoader,
            final Registry registry,
            final Object synthesizer)
		throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
			   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
			   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
			   GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
        final int key = createAssemblerKey(dto, realEntity, synthesizer, registry);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        if (registry == null) {
            throw new GeDARuntimeException("Registry cannot be null");
        }

        return putAssemblerToCache(classLoader, key, createNewAssembler(dto, realEntity, classLoader, synthesizer, registry));
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
                final Class< ? > dto,
                final Class< ? >[] entities,
                final Object synthesizer)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                    InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                    InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                    GeDARuntimeException, AnnotationDuplicateBindingException {

        return newCustomCompositeAssembler(dto, entities, DTOAssembler.class.getClassLoader(), synthesizer);
    }

	/**
	 * @param dto Dto concrete class that is annotated.
	 * @param entities the entity classes or interfaces that have appropriate getters and setters
     * @param classLoader class loader for object graph serviced by this assembler
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
                final Class< ? > dto,
                final Class< ? >[] entities,
                final ClassLoader classLoader,
                final Object synthesizer)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                   GeDARuntimeException, AnnotationDuplicateBindingException {

        getDtoAnnotation(dto);

		return createNewAssembler(dto, entities, classLoader, synthesizer, null);
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
                final Class< ? > dto,
                final Class< ? > entity)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                    InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                    InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                    GeDARuntimeException, AnnotationDuplicateBindingException {

        return newAssembler(dto, entity, DTOAssembler.class.getClassLoader());
    }

    /**
     * @param dto Dto concrete class that is annotated.
     * @param entity the entity class or interface that has appropriate getters and setters
     * @param classLoader class loader for object graph serviced by this assembler
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
                final Class< ? > dto,
                final Class< ? > entity,
                final ClassLoader classLoader)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                   GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
        final MethodSynthesizer synthesizer = getDefaultSynthesizer(classLoader);
        final int key = createAssemblerKey(dto, realEntity, synthesizer, null);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        getDtoAnnotation(dto);

    	return putAssemblerToCache(classLoader, key, createNewAssembler(dto, realEntity, classLoader, synthesizer, null));
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
                final Class< ? > dto,
                final Class< ? > entity,
                final Registry registry)
            throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                    InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                    InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                    GeDARuntimeException, AnnotationDuplicateBindingException {

        return newAssembler(dto, entity, DTOAssembler.class.getClassLoader(), registry);
    }

    /**
     * @param dto Dto concrete class that is annotated.
     * @param entity the entity class or interface that has appropriate getters and setters
     * @param classLoader class loader for object graph serviced by this assembler
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
                final Class< ? > dto,
                final Class< ? > entity,
                final ClassLoader classLoader,
                final Registry registry)
            throws InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                   GeDARuntimeException, AnnotationDuplicateBindingException {

        final Class< ? > realEntity = filterBlacklisted(entity);
        final MethodSynthesizer synthesizer = getDefaultSynthesizer(classLoader);
        final int key = createAssemblerKey(dto, realEntity, synthesizer, registry);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        if (registry == null) {
    		throw new GeDARuntimeException("Registry cannot be null");
    	}

    	return putAssemblerToCache(classLoader, key, createNewAssembler(dto, realEntity, classLoader, synthesizer, registry));
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
                final Class< ? > dto,
                final Class< ? >[] entities)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                    InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                    InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                    GeDARuntimeException, AnnotationDuplicateBindingException {

        return newCompositeAssembler(dto, entities, DTOAssembler.class.getClassLoader());

    }

    /**
     * @param dto Dto concrete class that is annotated.
     * @param entities the entity classes or interfaces that has appropriate getters and setters
     * @param classLoader class loader for object graph serviced by this assembler
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
                final Class< ? > dto,
                final Class< ? >[] entities,
                final ClassLoader classLoader)
            throws AnnotationMissingException, InspectionInvalidDtoInstanceException, InspectionInvalidEntityInstanceException,
                   InspectionScanningException, UnableToCreateInstanceException, InspectionPropertyNotFoundException,
                   InspectionBindingNotFoundException, AnnotationMissingBindingException, AnnotationValidatingBindingException,
                   GeDARuntimeException, AnnotationDuplicateBindingException {

        final MethodSynthesizer synthesizer = getDefaultSynthesizer(classLoader);
        final int key = createAssemblerKey(dto, entities, synthesizer, null);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        getDtoAnnotation(dto);

    	return putAssemblerToCache(classLoader, key, createNewAssembler(dto, entities, classLoader, synthesizer, null));
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
                final Class< ? > dto,
                final Object synthesizer)
            throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException,
                    UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
                    AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
                    AnnotationDuplicateBindingException  {

        return newCustomAssembler(dto, DTOAssembler.class.getClassLoader(), synthesizer);

    }

    /**
     * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
     * @param classLoader class loader for object graph serviced by this assembler
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
                final Class< ? > dto,
                final ClassLoader classLoader,
                final Object synthesizer)
            throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException,
                   UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
                   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
                   AnnotationDuplicateBindingException  {

        final Class[] classes = detectAutobinding(dto);

        final int key = createAssemblerKey(dto, classes, synthesizer, null);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        if (classes.length == 1) {
            return putAssemblerToCache(classLoader, key, createNewAssembler(dto, classes[0], classLoader, synthesizer, null));
        }
		return putAssemblerToCache(classLoader, key, createNewAssembler(dto, classes, classLoader, synthesizer, null));

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

        return newAssembler(dto, DTOAssembler.class.getClassLoader());
    }

	/**
	 * @param dto Dto concrete class that is annotated and value attribute of Dto is supplied.
     * @param classLoader class loader for object graph serviced by this assembler
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
                final Class< ? > dto,
                final ClassLoader classLoader)
            throws AnnotationMissingAutobindingException, AutobindingClassNotFoundException, InspectionScanningException,
                   UnableToCreateInstanceException, InspectionPropertyNotFoundException, InspectionBindingNotFoundException,
                   AnnotationMissingBindingException, AnnotationValidatingBindingException, GeDARuntimeException,
                   AnnotationDuplicateBindingException  {

        final Class[] classes = detectAutobinding(dto);

        final MethodSynthesizer synthesizer = getDefaultSynthesizer(classLoader);
        final int key = createAssemblerKey(dto, classes, synthesizer, null);

        final Assembler asm = getAssemblerFromCache(classLoader, key);
        if (asm != null) {
            return asm;
        }

        if (classes.length == 1) {
            return putAssemblerToCache(classLoader, key, createNewAssembler(dto, classes[0], classLoader, synthesizer, null));
        }

		return putAssemblerToCache(classLoader, key, createNewAssembler(dto, classes, classLoader, synthesizer, null));
	}


    /**
     * Dispose of all GeDA caches and references for given class loader.
     * This is useful when you wish to unload a particular module of your
     * application and ensure no memory leaks occur. An example use would be
     * undeploying an OSGi bundle.
     *
     * @param classLoader class loader to clean references for
     */
    public static void disposeOfDtoAssemblersBy(final ClassLoader classLoader) {

        if (classLoader != null) {
            final Cache<Assembler> cache = CL_CACHE.get(classLoader);
            if (cache != null) {
                cache.releaseResources();
                CL_CACHE.remove(classLoader);
            }
        }
    }


}
