/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor;

import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * The workhorse of the whole enterprise - interceptor that will use dto support facade
 * in order to enrich DTO/Entities whist method invocation takes place.
 * <p/>
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 8:03:32 PM
 */
public class GeDAInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(GeDAInterceptor.class);

    private final DTOSupport support;
    private final AdviceConfigResolver resolver;

    public GeDAInterceptor(final DTOSupport support,
                           final AdviceConfigResolver resolver) {
        this.support = support;
        this.resolver = resolver;
    }

    /** {@inheritDoc} */
    public Object invoke(final MethodInvocation invocation) throws Throwable {

        final Method method = invocation.getMethod();
        final Class<?> targetClass = (invocation.getThis() != null ? invocation.getThis().getClass() : null);


        final Map<Occurrence, AdviceConfig> cfg = this.resolver.resolve(method, targetClass);
        if (CollectionUtils.isEmpty(cfg)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("skipping method " + invocation.getMethod() + " because it is not advised");
            }
            return invocation.proceed(); // unadvised
        }

        final Object[] args = invocation.getArguments();

        if (cfg.containsKey(Occurrence.BEFORE_METHOD_INVOCATION)) {
            final AdviceConfig configuration = cfg.get(Occurrence.BEFORE_METHOD_INVOCATION);
            if (LOG.isDebugEnabled()) {
                LOG.debug("performing transfer before invocation " + invocation.getMethod()
                        + " in mode " + configuration.getDtoSupportMode().name());
            }
            this.invokeTransferBefore(args, configuration);
        }

        final Object result = invocation.proceed();

        if (cfg.containsKey(Occurrence.AFTER_METHOD_INVOCATION)) {
            final AdviceConfig configuration = cfg.get(Occurrence.AFTER_METHOD_INVOCATION);
            if (LOG.isDebugEnabled()) {
                LOG.debug("performing transfer after invocation " + invocation.getMethod()
                        + " in mode " + configuration.getDtoSupportMode().name());
            }
            return this.invokeTransferAfter(args, result, configuration);
        }

        return result;
    }

    /**
     * Perform actual assembly of the DTO/Entity
     *
     * @param args current invocation arguments
     * @param cfg configuration mode
     */
    protected void invokeTransferBefore(final Object[] args,
                                        final AdviceConfig cfg) {

        final AdviceConfig.DTOSupportMode mode = cfg.getDtoSupportMode();

        switch (mode) {
            case DTO_TO_ENTITY:
                this.support.assembleEntity(args[cfg.getDtoSourceIndex()], args[cfg.getEntityTargetIndex()]);
                break;

            case DTO_TO_ENTITY_KEY:
                this.support.assembleEntityByKey(args[cfg.getDtoSourceIndex()], cfg.getEntityKey());
                break;

            case DTO_BY_FILTER_TO_ENTITY:
                this.support.assembleEntity(cfg.getDtoFilterKey(),
                        args[cfg.getDtoSourceIndex()], args[cfg.getEntityTargetIndex()]);
                break;

            case DTO_BY_FILTER_TO_ENTITY_KEY:
                this.support.assembleEntityByKey(cfg.getDtoFilterKey(),
                        args[cfg.getDtoSourceIndex()], cfg.getEntityKey());
                break;

            case DTOS_TO_ENTITIES:
                this.support.assembleEntities(cfg.getEntityKey(),
                        (Collection) args[cfg.getDtoSourceIndex()], (Collection) args[cfg.getEntityTargetIndex()]);
                break;

            case DTOS_TO_ENTITIES_BY_FILTER:
                this.support.assembleEntities(cfg.getDtoFilterKey(), cfg.getEntityKey(),
                        (Collection) args[cfg.getDtoSourceIndex()], (Collection) args[cfg.getEntityTargetIndex()]);
                break;

            case ENTITY_TO_DTO:
                this.support.assembleDto(args[cfg.getDtoTargetIndex()], args[cfg.getEntitySourceIndex()]);
                break;

            case ENTITY_TO_DTO_KEY:
                this.support.assembleDtoByKey(cfg.getDtoKey(), args[cfg.getEntitySourceIndex()]);
                break;

            case ENTITY_TO_DTO_BY_FILTER:
                this.support.assembleDto(cfg.getDtoFilterKey(),
                        args[cfg.getDtoTargetIndex()], args[cfg.getEntitySourceIndex()]);
                break;

            case ENTITY_TO_DTO_KEY_BY_FILTER:
                this.support.assembleDtoByKey(cfg.getDtoFilterKey(),
                        cfg.getDtoKey(), args[cfg.getEntitySourceIndex()]);
                break;

            case ENTITIES_TO_DTOS:
                this.support.assembleDtos(cfg.getDtoKey(),
                        (Collection) args[cfg.getDtoTargetIndex()], (Collection) args[cfg.getEntitySourceIndex()]);
                break;

            case ENTITIES_TO_DTOS_BY_FILTER:
                this.support.assembleDtos(cfg.getDtoFilterKey(), cfg.getDtoKey(),
                        (Collection) args[cfg.getDtoTargetIndex()], (Collection) args[cfg.getEntitySourceIndex()]);
                break;

            default:
                LOG.warn("Unknown support mode [" + cfg + "]");
                break;
        }

    }

    /**
     * Perform actual assembly of the DTO/Entity
     *
     * @param args current invocation arguments
     * @param result result of the method invocation
     * @param cfg configuration mode
     */
    protected Object invokeTransferAfter(final Object[] args,
                                       final Object result,
                                       final AdviceConfig cfg) {

        final AdviceConfig.DTOSupportMode mode = cfg.getDtoSupportMode();

        switch (mode) {
            case DTO_TO_ENTITY:
                if (cfg.getEntityTargetIndex() == -1) {
                    return this.support.assembleEntity(args[cfg.getDtoSourceIndex()], result);
                } else {
                    this.support.assembleEntity(args[cfg.getDtoSourceIndex()], args[cfg.getEntityTargetIndex()]);
                    return result;
                }

            case DTO_TO_ENTITY_KEY:
                return this.support.assembleEntityByKey(args[cfg.getDtoSourceIndex()], cfg.getEntityKey());

            case DTO_BY_FILTER_TO_ENTITY:
                if (cfg.getEntityTargetIndex() == -1) {
                    return this.support.assembleEntity(cfg.getDtoFilterKey(),
                            args[cfg.getDtoSourceIndex()], result);
                } else {
                    this.support.assembleEntity(cfg.getDtoFilterKey(),
                            args[cfg.getDtoSourceIndex()], args[cfg.getEntityTargetIndex()]);
                    return result;
                }

            case DTO_BY_FILTER_TO_ENTITY_KEY:
                return this.support.assembleEntityByKey(cfg.getDtoFilterKey(),
                        args[cfg.getDtoSourceIndex()], cfg.getEntityKey());

            case DTOS_TO_ENTITIES:
                this.support.assembleEntities(cfg.getEntityKey(),
                        (Collection) args[cfg.getDtoSourceIndex()], (Collection) args[cfg.getEntityTargetIndex()]);
                return result;

            case DTOS_TO_ENTITIES_BY_FILTER:
                this.support.assembleEntities(cfg.getDtoFilterKey(), cfg.getEntityKey(),
                        (Collection) args[cfg.getDtoSourceIndex()], (Collection)  args[cfg.getEntityTargetIndex()]);
                return result;

            case ENTITY_TO_DTO:
                if (cfg.getDtoTargetIndex() == -1) {
                    return this.support.assembleDto(result, args[cfg.getEntitySourceIndex()]);                    
                } else {
                    this.support.assembleDto(args[cfg.getDtoTargetIndex()], args[cfg.getEntitySourceIndex()]);
                    return result;
                }

            case ENTITY_TO_DTO_KEY:
                return this.support.assembleDtoByKey(cfg.getDtoKey(), args[cfg.getEntitySourceIndex()]);

            case ENTITY_TO_DTO_BY_FILTER:
                if (cfg.getDtoTargetIndex() == -1) {
                    return this.support.assembleDto(cfg.getDtoFilterKey(),
                            result, args[cfg.getEntitySourceIndex()]);
                } else {
                    this.support.assembleDto(cfg.getDtoFilterKey(),
                            args[cfg.getDtoTargetIndex()], args[cfg.getEntitySourceIndex()]);
                    return result;
                }

            case ENTITY_TO_DTO_KEY_BY_FILTER:
                return this.support.assembleDtoByKey(cfg.getDtoFilterKey(),
                            cfg.getDtoKey(), args[cfg.getEntitySourceIndex()]);

            case ENTITIES_TO_DTOS:
                this.support.assembleDtos(cfg.getDtoKey(),
                        (Collection) args[cfg.getDtoTargetIndex()], (Collection) args[cfg.getEntitySourceIndex()]);
                return result;

            case ENTITIES_TO_DTOS_BY_FILTER:
                this.support.assembleDtos(cfg.getDtoFilterKey(), cfg.getDtoKey(),
                        (Collection) args[cfg.getDtoTargetIndex()], (Collection) args[cfg.getEntitySourceIndex()]);
                return result;

            default:
                LOG.warn("Unknown support mode [" + cfg + "]");
                return result;
        }

    }

}
