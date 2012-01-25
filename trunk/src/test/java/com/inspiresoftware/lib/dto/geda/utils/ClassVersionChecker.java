
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Ignore;

/**
 * Manual check for class version.
 * 
 * major  minor Java platform version 
 * 45       3           1.0
 * 45       3           1.1
 * 46       0           1.2
 * 47       0           1.3
 * 48       0           1.4
 * 49       0           1.5
 * 50       0           1.6
 * 
 * @author denispavlov
 *
 */
@Ignore
public class ClassVersionChecker {

	/**
	 * @param args args
	 * @throws IOException exp
	 */
    public static void main(final String[] args) throws IOException {
    	
    	final String[] files = args; 
        for (int i = 0; i < files.length; i++) {
			checkClassVersion(files[i]);
		}
    }

    private static void checkClassVersion(final String filename)
        throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(filename));

        final int marker = 0xcafebabe;
        int magic = in.readInt();
        if (magic != marker) {
          System.out.println(filename + " is not a valid class!");
        }
        int minor = in.readUnsignedShort();
        int major = in.readUnsignedShort();
        final String version;
        final int java5 = 49;
        final int java4 = 48;
        final int java3 = 47;
        if (major > java5) {
        	version = "1.6"; 
        } else if (major > java4) {
        	version = "1.5";
        } else if (major > java3) {
        	version = "1.4";
        } else {
        	version = " < 1.4";
        }
        System.out.println(filename + ": " + major + " . " + minor + " --> java " + version);
        in.close();
    }
	
}
