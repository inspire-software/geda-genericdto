package dp.lib.dto.geda.performance;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;


public class RunGeDARun
{
	
	public static void main(String ... args) {
		
		final int numThreads = 5;
		final Executor exec = Executors.newFixedThreadPool(numThreads);
		
		exec.execute(new PerformanceTestThread(300, 50));
		
	}
	
	@Test
	public void testPrimitive() {
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < 10000000; i++) {
			
			doMethod((Integer) i);
			
		}
		
		System.out.println(System.currentTimeMillis() - start);
		
	}
	
	private void doMethod(Integer i) {
		int k = i;
		int z = k;
		i = z;
	}

	@Test
	public void testAutoboxing() {
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < 10000000; i++) {
			
			doMethod(i);
			
		}
		
		System.out.println(System.currentTimeMillis() - start);
		
	}
	
	private void doMethod(int i) {
		int k = i;
		int z = k;
		i = z;
	}
	
}
