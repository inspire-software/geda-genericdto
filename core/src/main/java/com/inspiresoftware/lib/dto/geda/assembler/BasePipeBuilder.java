/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.MethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.JavaListMethodSynthesizer;
import com.inspiresoftware.lib.dto.geda.assembler.extension.impl.JavaMapMethodSynthesizer;

/**
 * Common class for Builders.
 * @since 2.0.4
 *
 * User: denispavlov
 * Date: 12-09-18
 * Time: 11:13 AM
 */
public class BasePipeBuilder {

    protected static final MethodSynthesizer mapSynthesizer = new JavaMapMethodSynthesizer();
    protected static final MethodSynthesizer listSynthesizer = new JavaListMethodSynthesizer();

}
