/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension;

/**
 * Disposable container allows to perform last sanity resource releasing, such
 * as holding to class references in other class loaders and references to class
 * loaders themselves.
 *
 * This is in effect a hook for GeDA internals to dispose of these references
 * from the cached graph of assemblers.
 *
 * The releaseResources() chain starts with DTOAssembler.disposeOfDtoAssemblersBy(ClassLoader).
 * Although there is not reason why this cannot be used to flush some other resources
 * such as mapping in DSL registry or extensible bean factories.
 *
 * @since 3.0.0
 *
 * User: denispavlov
 * Date: 13-02-21
 * Time: 10:29 PM
 */
public interface DisposableContainer {

    /**
     * Release resources such as references for class loaders, classes, mappings
     * and static caches.
     *
     * Call this method just before you plan to dispose of the container object
     * itself.
     *
     * Note that this is a cascading method, meaning that if this container
     * contains other disposable containers within then releaseResources()
     * method should be called for those elements as well.
     */
    void releaseResources();

}
