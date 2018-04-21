/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda;

import com.inspiresoftware.lib.dto.geda.adapter.ExtensibleBeanFactory;
import com.inspiresoftware.lib.dto.geda.config.GeDAInfrastructure;

/**
 * Marker interface for GeDA Spring XML configurations.
 *
 * User: denispavlov
 * Date: Jan 25, 2012
 * Time: 2:49:11 PM
 */
public interface DTOFactory extends ExtensibleBeanFactory, GeDAInfrastructure {

}
