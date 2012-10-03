/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.impl;

import com.inspiresoftware.lib.dto.geda.DTOAdaptersRegistrar;
import com.inspiresoftware.lib.dto.geda.DTOSupport;
import com.inspiresoftware.lib.dto.geda.DTOSupportAwareAdapter;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Basic registrar that calls {@link com.inspiresoftware.lib.dto.geda.DTOSupport}#registerValueConverter()
 * for each entry in map. All bean instances with {@link com.inspiresoftware.lib.dto.geda.DTOSupportAwareAdapter}
 * will be enriched.
 * <p/>
 * User: denispavlov
 * Date: Feb 21, 2012
 * Time: 8:42:37 AM
 */
public class MappingAdapterRegistrar implements DTOAdaptersRegistrar {

    private final Map<String, Object> adapters;

    public MappingAdapterRegistrar(final Map<String, Object> adapters) {
        this.adapters = adapters;
    }

    /**
     * Hook for implementors.
     *
     * @return map of loaded adapters
     */
    protected Map<String, Object> getAdapters() {
        return adapters;
    }

    public void registerAdapters(final DTOSupport dtoSupport) {
        final Map<String, Object> adapters = this.getAdapters();
        if (CollectionUtils.isEmpty(adapters)) {
            return;
        }
        for (Map.Entry<String, Object> adapterEntry : adapters.entrySet()) {
            if (adapterEntry.getValue() instanceof DTOSupportAwareAdapter) {
                ((DTOSupportAwareAdapter) adapterEntry.getValue()).setDtoSupport(dtoSupport);
            }
            dtoSupport.registerAdapter(adapterEntry.getKey(), adapterEntry.getValue());            
        }
    }
}
