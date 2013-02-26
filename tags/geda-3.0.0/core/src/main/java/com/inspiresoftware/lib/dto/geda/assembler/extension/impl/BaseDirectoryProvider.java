/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

/**
 * Directory provides allows runtime decision of base directory.
 *
 * @author denispavlov
 *
 */
public interface BaseDirectoryProvider {
    /** @return base directory for file search */
    String getBaseDir(final String name);

}
