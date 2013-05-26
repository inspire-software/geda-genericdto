
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

import com.inspiresoftware.lib.dto.geda.assembler.meta.PipeMetadata;
import com.inspiresoftware.lib.dto.geda.exception.GeDAException;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Builder for creating chains of metadata for annotations.
 * 
 * @author DPavlov
 */
interface MetadataChainBuilder {

	/**
	 * Build metadata chain for this field.
     *
	 * @param dtoField field to build pipe for
	 * @return metadata chain.
     *
	 * @throws GeDAException if errors occur
	 */
	public List<PipeMetadata> build(final Field dtoField) throws GeDAException;

}
