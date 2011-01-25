package dp.lib.dto.geda.performance;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class RunGeDARun
{
	
	public static void main(String ... args) {
		
		final int numThreads = 5;
		final Executor exec = Executors.newFixedThreadPool(numThreads);
		
		exec.execute(new PerformanceTestThread(300, 50));
		
	}

}
