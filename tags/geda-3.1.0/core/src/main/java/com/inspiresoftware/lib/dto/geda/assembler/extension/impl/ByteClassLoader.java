
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
 * Byte array class loader, load classes directly from bytes of generated classes.
 * 
 * @author denispavlov
 *
 */
public class ByteClassLoader extends ClassLoader {
	
	/**
	 * @param parent parent class loader
	 */
	public ByteClassLoader(final ClassLoader parent) {
		super(parent);
	}

    /** {@inheritDoc} */
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        return getParent().loadClass(name);
    }

    /**
	 * @param name full name of class
	 * @param clazz class as byte array
	 * @return class object
	 */
	public Class< ? > loadClass(final String name, final byte[] clazz) {
        return defineClass(name, clazz, 0, clazz.length);
	}

}