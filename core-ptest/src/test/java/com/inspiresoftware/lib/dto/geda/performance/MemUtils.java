/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.performance;

import org.junit.Ignore;

import java.io.PrintStream;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Sep 11, 2012
 * Time: 11:05:20 AM
 */
@Ignore
public class MemUtils {

    private static final Runtime runtime = Runtime.getRuntime();

    private static final long MBINBYTES = 1024 * 1024;

    /**
     * @return max memory in mb
     */
    public static int maxMb() {

        return (int) (runtime.maxMemory() / MBINBYTES);

    }

    /**
     * @return total current memory in mb
     */
    public static int totalMb() {

        return (int) (runtime.totalMemory() / MBINBYTES);

    }

    /**
     * @return free current memory in mb
     */
    public static int freeMb() {

        return (int) (runtime.freeMemory() / MBINBYTES);

    }

    /**
     * @return free current memory in mb
     */
    public static int availableMb() {

        final long freeMemory = freeMb();
        final long maxMemory = maxMb();
        final long allocatedMemory = totalMb();
        final long available = freeMemory + (maxMemory - allocatedMemory);

        return (int) available;

    }

    /**
     * @param out stream to write stats to
     */
    public static void statsToStream(PrintStream out) {

        out.println("free memory        (MB): " + freeMb());
        out.println("allocated memory   (MB): " + totalMb());
        out.println("max memory         (MB): " + maxMb());
        out.println("total free memory  (MB): " + availableMb());

    }

    /**
     * @param out stream to write stats to
     */
    public static void statsToStream(final StringBuilder out) {

        out.append("free memory        (MB): ").append(freeMb()).append('\n');
        out.append("allocated memory   (MB): ").append(totalMb()).append('\n');
        out.append("max memory         (MB): ").append(maxMb()).append('\n');
        out.append("total free memory  (MB): ").append(availableMb()).append('\n');
        
    }
}
