
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ClassLoader that loads classes from specified directory.
 * 
 * @author denispavlov
 *
 */
public class FileClassLoader extends ByteClassLoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileClassLoader.class);

    private final BaseDirectoryProvider directoryProvider;
	
	/**
	 * @param parent parent class loader
	 * @param directoryProvider directory provider for this class loader
	 */
	public FileClassLoader(final ClassLoader parent, 
						   final BaseDirectoryProvider directoryProvider) {
		super(parent);
		this.directoryProvider = directoryProvider;
	}

    /** {@inheritDoc} */
	@Override
	public Class< ? > loadClass(final String name) throws ClassNotFoundException {
		
		try {
			return getParent().loadClass(name);
		} catch (ClassNotFoundException exp) {
			// it's ok - need to load this one
		}
		
		final String baseDir = this.directoryProvider.getBaseDir(name);
		final String readerSimpleName = name.substring(name.lastIndexOf('.') + 1);
		final String filename = baseDir + readerSimpleName + ".class";
		try {
			final File file = new File(filename);
			file.deleteOnExit();

            if (LOG.isDebugEnabled()) {
				LOG.debug("Trying to read class file: {}", file.getAbsolutePath());
			}

            final FileInputStream fis = new FileInputStream(file);
			byte[] clazz = new byte[(int) file.length()];
			fis.read(clazz);

            if (LOG.isDebugEnabled()) {
				LOG.debug("Successfully loaded class file: {}", file.getAbsolutePath());
			}

            return loadClass(name, clazz);
		} catch (FileNotFoundException e) {
			throw new ClassNotFoundException("No class: " + name + " located at " + filename, e);
		} catch (IOException ioe) {
			throw new ClassNotFoundException("Unable to read: " + name + " located at " + filename, ioe);
		}
	}
	
}
