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
import com.inspiresoftware.lib.dto.geda.annotations.Occurance;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
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


        final Map<Occurance, AdviceConfig> cfg = this.resolver.resolve(method, targetClass);
        if (CollectionUtils.isEmpty(cfg)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("skipping method {} because it is not advised", invocation);
            }
            return invocation.proceed(); // unadvised
        }



        if (cfg.containsKey(Occurance.BEFORE_METHOD_INVOCATION)) {
            final AdviceConfig.DTOSupportMode mode = cfg.get(Occurance.BEFORE_METHOD_INVOCATION).getDtoSupportMode();
            if (LOG.isTraceEnabled()) {
                LOG.trace("performing transfer before invocation {} in mode {}", invocation, mode);
            }
            this.invokeTransferBefore(invocation, mode);
        }

        final Object result = invocation.proceed();

        if (cfg.containsKey(Occurance.AFTER_METHOD_INVOCATION)) {
            final AdviceConfig.DTOSupportMode mode = cfg.get(Occurance.AFTER_METHOD_INVOCATION).getDtoSupportMode();
            if (LOG.isTraceEnabled()) {
                LOG.trace("performing transfer after invocation {} in mode {}", invocation, mode);
            }
            this.invokeTransferAfter(invocation, result, mode);
        }

        return result;
    }

    /**
     * Perform actual assembly of the DTO/Entity
     *
     * @param invocation current invocation
     * @param mode support mode
     */
    protected void invokeTransferBefore(final MethodInvocation invocation,
                                        final AdviceConfig.DTOSupportMode mode) {

        final Object[] args = invocation.getArguments();
        switch (mode) {
            case DTO_TO_ENTITY:
                this.support.assembleEntity(args[0], args[1]);
                break;

            case DTO_BY_CLASS_TO_ENTITY:
                this.support.assembleEntity((Class) args[0], args[1], args[2]);
                break;

            case DTOS_BY_CLASS_TO_ENTITIES:
                this.support.assembleEntities((Class) args[0], (Collection) args[1], (Collection) args[2]);
                break;

            case ENTITY_TO_DTO:
                this.support.assembleDto(args[0], args[1]);
                break;

            case ENTITY_TO_DTO_BY_CLASS:
                this.support.assembleDto((Class) args[0], args[1], args[2]);
                break;

            case ENTITIES_TO_DTOS_BY_CLASS:
                this.support.assembleDtos((Class) args[0], (Collection) args[1], (Collection) args[2]);
                break;

            default:
                LOG.warn("Unknown support mode [" + mode + "] for method: " + invocation);
                break;
        }

    }

    /**
     * Perform actual assembly of the DTO/Entity
     *
     * @param invocation current invocation
     * @param result result of the method invocation
     * @param mode support mode
     */
    protected void invokeTransferAfter(final MethodInvocation invocation,
                                       final Object result,
                                       final AdviceConfig.DTOSupportMode mode) {
        final Object[] args = invocation.getArguments();
        switch (mode) {
            case DTO_TO_ENTITY:
                this.support.assembleEntity(args[0], result);
                break;

            case DTO_BY_CLASS_TO_ENTITY:
                this.support.assembleEntity((Class) args[0], args[1], result);
                break;

            case DTOS_BY_CLASS_TO_ENTITIES:
                this.support.assembleEntities((Class) args[0], (List) args[1], (Collection) args[2]);
                break;

            case ENTITY_TO_DTO:
                this.support.assembleDto(result, args[1]);
                break;

            case ENTITY_TO_DTO_BY_CLASS:
                this.support.assembleDto((Class) args[0], result, args[2]);
                break;

            case ENTITIES_TO_DTOS_BY_CLASS:
                this.support.assembleDtos((Class) args[0], (Collection) result, (Collection) args[2]);
                break;

            default:
                LOG.warn("Unknown support mode [" + mode + "] for method: " + invocation);
                break;
        }

    }
}
