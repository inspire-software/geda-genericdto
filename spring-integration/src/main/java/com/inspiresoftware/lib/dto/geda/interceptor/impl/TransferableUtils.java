/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.interceptor.impl;

import com.inspiresoftware.lib.dto.geda.adapter.Adapter;
import com.inspiresoftware.lib.dto.geda.annotations.Direction;
import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.annotations.Transferable;
import com.inspiresoftware.lib.dto.geda.config.GeDAInfrastructure;
import com.inspiresoftware.lib.dto.geda.interceptor.AdviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 *
 * User: denispavlov
 * Date: Jan 29, 2012
 * Time: 6:36:19 PM
 */
public final class TransferableUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TransferableUtils.class);

    static int NO_INDEX = Integer.MAX_VALUE;
    static int RETURN_INDEX = -1;


    /**
     * Work out configurations from {@link com.inspiresoftware.lib.dto.geda.annotations.Transferable}
     *
     * @param method method examined.
     * @param targetClass class upon which this method was invoked
     * @return configuration.
     */
    public static Map<Occurrence, AdviceConfig> resolveConfiguration(final Method method,
                                                                     final Class<?> targetClass) {

        if (GeDAInfrastructure.class.isAssignableFrom(targetClass) ||
                Adapter.class.isAssignableFrom(targetClass)) {
            return Collections.emptyMap(); 
        }
        return resolveConfiguration(method, targetClass, true);
    }

    private static Map<Occurrence, AdviceConfig> resolveConfiguration(final Method method,
                                                                      final Class<?> targetClass,
                                                                      final boolean trySpecific) {

        Method specificMethod = method;
        Transferable annotation = specificMethod.getAnnotation(Transferable.class);

        if (annotation == null && trySpecific) {

            specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            annotation = specificMethod.getAnnotation(Transferable.class);

        }

        if (annotation == null) {
            return Collections.emptyMap();
        }

        final Map<Occurrence, AdviceConfig> cfg = new HashMap<Occurrence, AdviceConfig>();
        resolveConfiguration(cfg, specificMethod.getName(), specificMethod.getParameterTypes(), specificMethod.getReturnType(), annotation);
        return cfg;

    }

    private static void resolveConfiguration(final Map<Occurrence, AdviceConfig> cfg,
                                             final String method,
                                             final Class[] args,
                                             final Class retArg,
                                             final Transferable ann) {

        if (ann.before() == Direction.NONE && ann.after() == Direction.NONE) {
            LOG.warn("Both before and after are set to NONE on method: {}. No transfer shall be performed.", method);
            return; // no transfer
        }
        if (ann.before() == Direction.NONE) {
            resolveConfigurationAfter(cfg, method, args, retArg, ann);
        } else if (ann.after() == Direction.NONE) {
            resolveConfigurationBefore(cfg, method, args, retArg, ann);
        } else {
            resolveConfigurationAround(cfg, method, args, retArg, ann);
        }
        
    }

    private static void resolveConfigurationBefore(final Map<Occurrence, AdviceConfig> cfg,
                                                   final String method,
                                                   final Class[] args,
                                                   final Class retArg,
                                                   final Transferable ann) {

        if (args.length >= 2) {

            if (ann.before() == Direction.DTO_TO_ENTITY) {

                final boolean filtered = StringUtils.hasLength(ann.dtoFilterKey());
                final boolean firstArgIsCollection = argIsCollection(args[0]);
                final boolean secondArgIsCollection = argIsCollection(args[1]);

                if (filtered) {

                    if (firstArgIsCollection && secondArgIsCollection) {
                        // public void toEntity(Class<DTO> dtoFilter, Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                        assertKey(method, ann.entityKey(), false);
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", ann.entityKey(),
                                AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES_BY_FILTER,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        return;
                    } else if (!firstArgIsCollection && !secondArgIsCollection) {
                        // public void toEntity(Class<DTO> dtoFilter, DTO dto, Entity entity, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        return;
                    }

                } else {

                    if (firstArgIsCollection && secondArgIsCollection) {
                        // public void toEntity(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                        assertKey(method, ann.entityKey(), false);
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", ann.entityKey(),
                                AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        return;
                    } else if (!firstArgIsCollection && !secondArgIsCollection) {
                        // public void toEntity(DTO dto, Entity entity, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", "",
                                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        return;
                    }

                }

            } else if (ann.before() == Direction.ENTITY_TO_DTO) {

                final boolean filtered = StringUtils.hasLength(ann.dtoFilterKey());
                final boolean firstArgIsCollection = argIsCollection(args[0]);
                final boolean secondArgIsCollection = argIsCollection(args[1]);

                if (filtered) {

                    if (firstArgIsCollection && secondArgIsCollection) {
                        // public void toDto(Class<DTO> dtoFilter, Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                        assertKey(method, ann.dtoKey(), true);
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), ann.dtoKey(), "",
                                AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_FILTER,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        return;
                    } else if (!firstArgIsCollection && !secondArgIsCollection) {
                        // public void toDto(Class<DTO> dtoFilter, DTO dto, Entity entity, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        return;
                    }

                } else {

                    if (firstArgIsCollection && secondArgIsCollection) {
                        // public void toDto(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                        assertKey(method, ann.dtoKey(), true);
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", ann.dtoKey(), "",
                                AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        return;
                    } else if (!firstArgIsCollection && !secondArgIsCollection) {
                        // public void toDto(DTO dto, Entity entity, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        return;
                    }

                }

            }
        }

        throw new UnsupportedOperationException("Unsupported configuration see @Transferable.");

    }

    private static void resolveConfigurationAfter(final Map<Occurrence, AdviceConfig> cfg,
                                                  final String method,
                                                  final Class[] args,
                                                  final Class retArg,
                                                  final Transferable ann) {
        if (args.length >= 1) {

            if (ann.after() == Direction.ENTITY_TO_DTO) {

                final boolean filtered = StringUtils.hasLength(ann.dtoFilterKey());

                if (filtered) {

                    final boolean firstArgIsCollection = argIsCollection(args[0]);
                    final boolean returnIsVoid = isReturnVoid(retArg);

                    if (firstArgIsCollection || args.length >= 2) {
                        final boolean secondArgIsCollection = argIsCollection(args[1]);

                        if (firstArgIsCollection && secondArgIsCollection) {
                            // public void toDto(Class<DTO> dtoFilter, Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                            assertKey(method, ann.dtoKey(), true);
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), ann.dtoFilterKey(), ann.dtoKey(), "",
                                    AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS_BY_FILTER,
                                    NO_INDEX, 0, 1, NO_INDEX, ann.context());
                            return;
                        } else if (!firstArgIsCollection && !returnIsVoid) {
                             // public DTO toDto(Class<DTO> dtoFilter, Entity entity, ... )
                             assertKey(method, ann.dtoKey(), true);
                             addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                     ann.after(), ann.dtoFilterKey(), ann.dtoKey(), "",
                                     AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER,
                                     NO_INDEX, RETURN_INDEX, 0, NO_INDEX, ann.context());
                             return;

                        } else if (!firstArgIsCollection && !secondArgIsCollection) {
                            // public void toDto(Class<DTO> dtoFilter, DTO dto, Entity entity, ... )
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), ann.dtoFilterKey(), "", "",
                                    AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER,
                                    NO_INDEX, 0, 1, NO_INDEX, ann.context());
                            return;
                        }
                    } else if (args.length == 1 && !returnIsVoid) {
                        // public DTO toDto(Class<DTO> dtoFilter, Entity entity, ... )
                         assertKey(method, ann.dtoKey(), true);
                         addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                 ann.after(), ann.dtoFilterKey(), ann.dtoKey(), "",
                                 AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER,
                                 NO_INDEX, RETURN_INDEX, 0, NO_INDEX, ann.context());
                         return;
                    }


                } else {

                    final boolean firstArgIsCollection = argIsCollection(args[0]);
                    final boolean returnIsVoid = isReturnVoid(retArg);

                    if (firstArgIsCollection || args.length >= 2) {
                        final boolean secondArgIsCollection = argIsCollection(args[1]);

                        if (firstArgIsCollection && secondArgIsCollection) {
                            // public void toDto(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                            assertKey(method, ann.dtoKey(), true);
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), "", ann.dtoKey(), "",
                                    AdviceConfig.DTOSupportMode.ENTITIES_TO_DTOS,
                                    NO_INDEX, 0, 1, NO_INDEX, ann.context());
                            return;
                        } else if (!firstArgIsCollection && !returnIsVoid) {
                             // public DTO toDto(Entity entity, ... )
                             assertKey(method, ann.dtoKey(), true);
                             addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                     ann.after(), "", ann.dtoKey(), "",
                                     AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY,
                                     NO_INDEX, RETURN_INDEX, 0, NO_INDEX, ann.context());
                             return;
                            
                        } else if (!firstArgIsCollection && !secondArgIsCollection) {
                            // public void toDto(DTO dto, Entity entity, ... )
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), "", "", "",
                                    AdviceConfig.DTOSupportMode.ENTITY_TO_DTO,
                                    NO_INDEX, 0, 1, NO_INDEX, ann.context());
                            return;
                        }
                    } else if (args.length == 1 && !returnIsVoid) {
                         // public DTO toDto(Entity entity, ... )
                         assertKey(method, ann.dtoKey(), true);
                         addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                 ann.after(), "", ann.dtoKey(), "",
                                 AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY,
                                 NO_INDEX, RETURN_INDEX, 0, NO_INDEX, ann.context());
                         return;
                    }

                }

            } else if (ann.after() == Direction.DTO_TO_ENTITY) {

                final boolean filtered = StringUtils.hasLength(ann.dtoFilterKey());
                final boolean returnIsVoid = isReturnVoid(retArg);

                if (filtered) {

                    final boolean firstArgIsCollection = argIsCollection(args[0]);

                    if (firstArgIsCollection || args.length >= 2) {
                        final boolean secondArgIsCollection = argIsCollection(args[1]);

                        if (firstArgIsCollection && secondArgIsCollection) {
                            // public void toEntity(Class<DTO> dtoFilter, Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                            assertKey(method, ann.entityKey(), false);
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), ann.dtoFilterKey(), "", ann.entityKey(),
                                    AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES_BY_FILTER,
                                    0, NO_INDEX, NO_INDEX, 1, ann.context());
                            return;

                        } else if (!firstArgIsCollection && !returnIsVoid) {
                            // public Entity toEntity(Class<DTO> dtoFilter, DTO dto, ... )
                            assertKey(method, ann.entityKey(), false);
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), ann.dtoFilterKey(), "", ann.entityKey(),
                                    AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY,
                                    0, NO_INDEX, NO_INDEX, RETURN_INDEX, ann.context());
                            return;

                        } else if (!firstArgIsCollection && !secondArgIsCollection) {
                            // public void toEntity(Class<DTO> dtoFilter, DTO dto, Entity entity, ... )
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), ann.dtoFilterKey(), "", "",
                                    AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY,
                                    0, NO_INDEX, NO_INDEX, 1, ann.context());
                            return;

                        }
                    } else if (args.length == 1 && !returnIsVoid) {
                        // public Entity toEntity(Class<DTO> dtoFilter, DTO dto, ... )
                        assertKey(method, ann.entityKey(), false);
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), ann.dtoFilterKey(), "", ann.entityKey(),
                                AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY,
                                0, NO_INDEX, NO_INDEX, RETURN_INDEX, ann.context());
                        return;
                    }

                } else {

                    final boolean firstArgIsCollection = argIsCollection(args[0]);

                    if (firstArgIsCollection || args.length >= 2) {
                        final boolean secondArgIsCollection = argIsCollection(args[1]);

                        if (firstArgIsCollection && secondArgIsCollection) {
                            // public void toEntity(Collection<DTO> dto, Collection<Entity> blankCollection, ... )
                            assertKey(method, ann.entityKey(), false);
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), "", "", ann.entityKey(),
                                    AdviceConfig.DTOSupportMode.DTOS_TO_ENTITIES,
                                    0, NO_INDEX, NO_INDEX, 1, ann.context());
                            return;
                            
                        } else if (!firstArgIsCollection && !returnIsVoid) {
                            // public Entity toEntity(DTO dto, ... )
                            assertKey(method, ann.entityKey(), false);
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), "", "", ann.entityKey(),
                                    AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY,
                                    0, NO_INDEX, NO_INDEX, RETURN_INDEX, ann.context());
                            return;

                        } else if (!firstArgIsCollection && !secondArgIsCollection) {
                            // public void toEntity(DTO dto, Entity entity, ... )
                            addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                    ann.after(), "", "", "",
                                    AdviceConfig.DTOSupportMode.DTO_TO_ENTITY,
                                    0, NO_INDEX, NO_INDEX, 1, ann.context());
                            return;

                        }
                    } else if (args.length == 1 && !returnIsVoid) {
                        // public Entity toEntity(DTO dto, ... )
                        assertKey(method, ann.entityKey(), false);
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), "", "", ann.entityKey(),
                                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY,
                                0, NO_INDEX, NO_INDEX, RETURN_INDEX, ann.context());
                        return;
                    }

                }

            }

        }

        throw new UnsupportedOperationException("Unsupported configuration see @Transferable.");
    }

    private static void resolveConfigurationAround(final Map<Occurrence, AdviceConfig> cfg,
                                                   final String method,
                                                   final Class[] args,
                                                   final Class retArg,
                                                   final Transferable ann) {

        if (args.length >= 2) {

            final boolean filtered = StringUtils.hasLength(ann.dtoFilterKey());
            final boolean returnIsVoid = isReturnVoid(retArg);

            if (ann.before() == Direction.DTO_TO_ENTITY && ann.after() == Direction.ENTITY_TO_DTO) {

                if (filtered && args.length >= 2) {
                    if (returnIsVoid) {
                        // public void toEntityAndBackToDto(Class<DTO> dtoFilter, DTO source, Entity target, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        return;

                    } else {
                        // public DTO toEntityAndBackToDto(Class<DTO> dtoFilter, DTO source, Entity target, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        assertKey(method, ann.dtoKey(), true);
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), ann.dtoFilterKey(), ann.dtoKey(), "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY_BY_FILTER,
                                NO_INDEX, RETURN_INDEX, 1, NO_INDEX, ann.context());
                        return;

                    }

                } else {
                    if (returnIsVoid) {
                        // public void toEntityAndBackToDto(DTO source, Entity target, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", "",
                                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), "", "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        return;

                    } else {
                        // public DTO toEntityAndBackToDto(DTO source, Entity target, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", "",
                                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        assertKey(method, ann.dtoKey(), true);
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), "", ann.dtoKey(), "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_KEY,
                                NO_INDEX, RETURN_INDEX, 1, NO_INDEX, ann.context());
                        return;

                    }
                }

            } else if (ann.before() == Direction.ENTITY_TO_DTO && ann.after() == Direction.DTO_TO_ENTITY) {

                if (filtered && args.length >= 2) {
                    if (returnIsVoid) {
                        // public void toDtoAndBackToEntity(Class<DTO> dtoFilter, DTO target, Entity source, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        return;

                    } else {
                        // public Entity toDtoAndBackToEntity(Class<DTO> dtoFilter, DTO target, Entity source, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), ann.dtoFilterKey(), "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO_BY_FILTER,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        assertKey(method, ann.entityKey(), false);
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), ann.dtoFilterKey(), "", ann.entityKey(),
                                AdviceConfig.DTOSupportMode.DTO_BY_FILTER_TO_ENTITY_KEY,
                                0, NO_INDEX, NO_INDEX, RETURN_INDEX, ann.context());
                        return;

                    }
                } else {
                    if (returnIsVoid) {
                        // public void toDtoAndBackToEntity(DTO target, Entity source, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), "", "", "",
                                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY,
                                0, NO_INDEX, NO_INDEX, 1, ann.context());
                        return;

                    } else {
                        // public Entity toDtoAndBackToEntity(DTO target, Entity source, ... )
                        addConfiguration(cfg, Occurrence.BEFORE_METHOD_INVOCATION,
                                ann.before(), "", "", "",
                                AdviceConfig.DTOSupportMode.ENTITY_TO_DTO,
                                NO_INDEX, 0, 1, NO_INDEX, ann.context());
                        assertKey(method, ann.entityKey(), false);
                        addConfiguration(cfg, Occurrence.AFTER_METHOD_INVOCATION,
                                ann.after(), "", "", ann.entityKey(),
                                AdviceConfig.DTOSupportMode.DTO_TO_ENTITY_KEY,
                                0, NO_INDEX, NO_INDEX, RETURN_INDEX, ann.context());
                        return;

                    }
                }

            }

        }

        throw new UnsupportedOperationException("Unsupported configuration see @Transferable.");
    }


    private static void addConfiguration(final Map<Occurrence, AdviceConfig> cfg,
                                         final Occurrence occur,
                                         final Direction direction,
                                         final String dtoFilterKey,
                                         final String dtoKey,
                                         final String entityKey,
                                         final AdviceConfig.DTOSupportMode mode,
                                         final int dtoSourceIndex,
                                         final int dtoTargetIndex,
                                         final int entitySourceIndex,
                                         final int entityTargetIndex,
                                         final String context) {
        final AdviceConfig bCfg =
                new ImmutableAdviceConfig(
                    direction, occur, mode, dtoFilterKey, dtoKey, entityKey,
                    dtoSourceIndex, dtoTargetIndex,
                    entitySourceIndex, entityTargetIndex, context);

        cfg.put(occur, bCfg);
    }

    private static void assertKey(final String method, final String key, final boolean isDto) {
        if (!StringUtils.hasLength(key)) {
            throw new IllegalArgumentException("Key must be specified for " + (isDto ? "DTO" : "Entity") + " in " + method);
        }
    }

    private static boolean argIsClass(final Class arg) {
        return "java.lang.Class".equals(arg.getName());
    }

    private static boolean argIsCollection(final Class arg) {
        return Collection.class.isAssignableFrom(arg);
    }

    private static boolean isReturnVoid(final Class retArg) {
        return "void".equals(retArg.getName());
    }

}
