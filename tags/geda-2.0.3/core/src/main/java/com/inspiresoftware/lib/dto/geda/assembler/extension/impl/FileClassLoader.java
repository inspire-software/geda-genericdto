
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
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
public class FileClassLoader extends ClassLoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileClassLoader.class);
	
	/**
	 * Directory provides allows runtime decision of base directory.
	 * 
	 * @author denispavlov
	 *
	 */
	public static interface BaseDirectoryProvider {
		/** @return base directory for file search */
		String getBaseDir();
		
	}
	
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
			return super.loadClass(name);
		} catch (ClassNotFoundException exp) {
			// it's ok - need to load this one
		}
		
		final String baseDir = this.directoryProvider.getBaseDir();
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

            return defineClass(name, clazz, 0, clazz.length);
		} catch (FileNotFoundException e) {
			throw new ClassNotFoundException("No class: " + name + " located at " + filename, e);
		} catch (IOException ioe) {
			throw new ClassNotFoundException("Unable to read: " + name + " located at " + filename, ioe);
		}
	}
	
}
