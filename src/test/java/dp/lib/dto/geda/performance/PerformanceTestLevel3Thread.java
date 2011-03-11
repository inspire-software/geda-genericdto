
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.inspire-software.com
 * SVN: https://geda-genericdto.svn.sourceforge.net/svnroot/geda-genericdto
 */

package dp.lib.dto.geda.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;

import dp.lib.dto.geda.adapter.BeanFactory;
import dp.lib.dto.geda.assembler.DTOAssembler;
import dp.lib.dto.geda.performance.dto.Level1Dto;
import dp.lib.dto.geda.performance.dto.Level2Dto;
import dp.lib.dto.geda.performance.dto.Level3Dto;
import dp.lib.dto.geda.performance.entity.Level3Entity;

/**
 * Test thread for simple level 3 objects
 * 
 * @author DPavlov
 */
@Ignore
public class PerformanceTestLevel3Thread implements Runnable {

	private final Sampler sampler = new Sampler();
	private final int lvl3;
	
	private final ShutdownListener listener;
	
	public PerformanceTestLevel3Thread(final int lvl3, final ShutdownListener listener) {
		this.lvl3 = lvl3;
		this.listener = listener;
		this.listener.addObservable(this);
	}

	public void run() {
		
		final int allCount = lvl3;
		
		final Date start = new Date(); 
		
		System.out.println(start + " - Initializing GeDA performance test on " + allCount + " entities");
		
		final List<Level3Entity> testSample = sampler.getLevel3Entities(allCount); 
		
		final Date assemberStart = new Date();
		System.out.println(assemberStart + " - Finished creating objects in " + (assemberStart.getTime() - start.getTime()) + " millis");
		
		System.out.println(assemberStart + " - Initializing assembler");
		final DTOAssembler assembler = DTOAssembler.newAssembler(Level3Dto.class, Level3Entity.class);
		final Date assemberEnd = new Date();
		System.out.println(assemberEnd + " - Initialized assembler in " + (assemberEnd.getTime() - assemberStart.getTime()) + " millis");
		
		List<Level1Dto> dtos = new ArrayList<Level1Dto>();
		long assemblyMillis = 0L;
		
		try {
		
			final Date startProcess = new Date();
			System.out.println(startProcess + " - started assembly on " 
					+ allCount + " entities)");
			
			assembler.assembleDtos(dtos, testSample, null, getFactory());
			
			final Date end = new Date();
			assemblyMillis = (end.getTime() - startProcess.getTime());
			System.out.println(end + " - finished in " + assemblyMillis + " millis");
			
			dtos = null;
			
		} catch (IllegalArgumentException iae) {
			System.out.println(iae.getMessage());
			if (iae.getCause() != null) {
				System.out.println(iae.getCause().getMessage());
			}
			iae.printStackTrace();
		}
		
		final Date startManual = new Date();
		System.out.println(startManual + " - trying manual copy");
		
		final List<Level3Dto> manualDto = new ArrayList<Level3Dto>();
		for (Level3Entity entity : testSample) {
			manualDto.add(new Level3Dto(entity));
		}
		final Date endManual = new Date();
		final long manualMillis = (endManual.getTime() - startManual.getTime());
		System.out.println(endManual + " - finished manual copy in " + manualMillis + " millis");
		
		System.out.println("Performance " + ((double) manualMillis / (double) assemblyMillis) * 100 + "%");
		
		this.listener.notifyFinished(this);
		
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
	
}
