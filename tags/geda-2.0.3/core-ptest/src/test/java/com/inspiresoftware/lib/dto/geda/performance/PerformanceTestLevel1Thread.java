
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package com.inspiresoftware.lib.dto.geda.performance;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.Assembler;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.performance.dto.Level1Dto;
import com.inspiresoftware.lib.dto.geda.performance.dto.Level2Dto;
import com.inspiresoftware.lib.dto.geda.performance.dto.Level3Dto;
import com.inspiresoftware.lib.dto.geda.performance.entity.Level1Entity;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test thread for level 1 full tree test.
 * 
 * @author DPavlov
 */
@Ignore
public class PerformanceTestLevel1Thread implements Runnable {

	private final Sampler sampler = new Sampler();
	private final int lvl1;
	private final int lvl2;
	
	private final ShutdownListener listener;
	
	private final StringBuilder out = new StringBuilder();
	
	public PerformanceTestLevel1Thread(final int lvl1, final int lvl2, final ShutdownListener listener) {
		this.lvl1 = lvl1;
		this.lvl2 = lvl2;
		this.listener = listener;
		this.listener.addObservable(this);
	}

	public void run() {

        try {

            MemUtils.statsToStream(out);
		
            final int lvl2Count = (lvl2 * lvl1 * 5);
            final int lvl3Count = (lvl2 * lvl1 * 5 * 5);
            final int allCount = lvl1 + lvl2Count + lvl3Count;

            final Date start = new Date();

            out.append(start + " - Initializing GeDA performance test on " + allCount + " entities\n");

            final List<Level1Entity> testSample = sampler.getLevel1Entities(1, lvl1, lvl2);

            final Date assemberStart = new Date();
            out.append(assemberStart + " - Finished creating objects in " + (assemberStart.getTime() - start.getTime()) + " millis\n");

            out.append(assemberStart + " - Initializing assembler\n");
            final Assembler assembler = DTOAssembler.newAssembler(Level1Dto.class, Level1Entity.class);
            final Date assemberEnd = new Date();
            out.append(assemberEnd + " - Initialized assembler in " + (assemberEnd.getTime() - assemberStart.getTime()) + " millis\n");

            List<Level1Dto> dtos = new ArrayList<Level1Dto>();
            long assemblyMillis = 0L;

            try {

                final Date startProcess = new Date();
                out.append(startProcess + " - started assembly on "
                        + lvl1 + " lvl1, "
                        + lvl2Count + " lvl2, and "
                        + lvl3Count + " lvl3 entities (total of "
                        + allCount + " entities\n");

                assembler.assembleDtos(dtos, testSample, null, getFactory());

                final Date end = new Date();
                assemblyMillis = (end.getTime() - startProcess.getTime());
                out.append(end + " - finished in " + assemblyMillis + " millis\n");

                dtos = null;

            } catch (Exception iae) {
                out.append(iae.getMessage()).append("\n");
                if (iae.getCause() != null) {
                    out.append(iae.getCause().getMessage()).append("\n");
                }
                iae.printStackTrace();
            }

            final Date startManual = new Date();
            out.append(startManual + " - trying manual copy\n");

            final List<Level1Dto> manualDto = new ArrayList<Level1Dto>();
            for (Level1Entity entity : testSample) {
                manualDto.add(new Level1Dto(entity));
            }
            final Date endManual = new Date();
            final long manualMillis = (endManual.getTime() - startManual.getTime());
            out.append(endManual + " - finished manual copy in " + manualMillis + " millis\n");

            out.append("Performance " + ((double) manualMillis / (double) assemblyMillis) * 100 + "%\n");

        } finally {
            this.listener.notifyFinished(this);            
        }

	}
	
	private BeanFactory getFactory() {
		return new BeanFactory() {

			public Object get(String entityBeanKey) {
				if ("lvl1".equals(entityBeanKey)) {
					return new Level1Dto();
				} else if ("lvl2".equals(entityBeanKey)) {
					return new Level2Dto();
				} else if ("lvl3".equals(entityBeanKey)) {
					return new Level3Dto();
				}
				return null;
			}
			
		};
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return out.toString();
	}
	
	
	
}
