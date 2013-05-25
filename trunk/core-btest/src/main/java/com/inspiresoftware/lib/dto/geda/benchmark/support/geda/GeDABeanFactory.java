/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.benchmark.support.geda;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Address;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Country;
import com.inspiresoftware.lib.dto.geda.benchmark.domain.Name;
import com.inspiresoftware.lib.dto.geda.benchmark.dto.AddressDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 17, 2012
 * Time: 9:27:58 AM
 */
public class GeDABeanFactory implements BeanFactory {

    private static interface Init {

        Class getType();

        Object newInstance();

    }

    private static final Map<String, Init> impl = new HashMap<String, Init>() {{
        put("addressDto", new Init() {
            public Class getType() {
                return AddressDTO.class;
            }
            public Object newInstance() {
                return new AddressDTO();
            }
        });
        put("countryEntity", new Init() {
            public Class getType() {
                return Country.class;
            }
            public Object newInstance() {
                return new Country();
            }
        });
        put("nameEntity", new Init() {
            public Class getType() {
                return Name.class;
            }
            public Object newInstance() {
                return new Name();
            }
        });
        put("addressEntity", new Init() {
            public Class getType() {
                return Address.class;
            }
            public Object newInstance() {
                return new Address();
            }
        });
    }};

    /** {@inheritDoc} */
    public Class getClazz(final String entityBeanKey) {
        return impl.get(entityBeanKey).getType();
    }

    /** {@inheritDoc} */
    public Object get(final String entityBeanKey) {
        return impl.get(entityBeanKey).newInstance();
    }
}
