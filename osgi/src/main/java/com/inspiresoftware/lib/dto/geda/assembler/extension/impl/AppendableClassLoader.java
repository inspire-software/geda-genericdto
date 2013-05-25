/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler.extension.impl;

/**
 * Exposes some of the class loader features to append classes staring from
 * another class loader.
 *
 * User: denispavlov
 * Date: 13-02-20
 * Time: 1:14 PM
 */
public class AppendableClassLoader extends ClassLoader {

    public AppendableClassLoader(final ClassLoader classLoader) {
        super(classLoader);
    }

    /**
     * Append class to this class loader.
     *
     * @param clazz class to append
     */
    public void append(Class clazz) {
        resolveClass(clazz);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AppendableClassLoader that = (AppendableClassLoader) o;

        return getParent().equals(that.getParent());

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return getParent().hashCode();
    }
}
