/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.interceptor;

import com.inspiresoftware.lib.dto.geda.annotations.Occurrence;
import com.inspiresoftware.lib.dto.geda.config.GeDAInfrastructure;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Examined the method being invoked for {@link com.inspiresoftware.lib.dto.geda.annotations.Transferable}.
 * <p/>
 * User: denispavlov
 * Date: Jan 26, 2012
 * Time: 8:03:21 AM
 */
public interface AdviceConfigResolver extends GeDAInfrastructure {

    /**
     * @param method current method invocation
     * @param targetClass class which contains the method
     * @return advice configuration or null if this is not an annotated method.
     */
    Map<Occurrence, AdviceConfig> resolve(final Method method, final Class<?> targetClass);

}
