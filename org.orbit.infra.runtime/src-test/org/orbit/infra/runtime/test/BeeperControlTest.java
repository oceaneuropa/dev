package org.orbit.infra.runtime.test;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @see https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledExecutorService.html
 *
 */
public class BeeperControlTest {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Test
	public void beepForAnHour() {
		// do the beep
		final Runnable beeper = new Runnable() {
			@Override
			public void run() {
				System.out.println("beep");
			}
		};

		// beep once very 10 seconds
		long initialDelay = 3; // 3 seconds
		long period = 10; // 10 seconds
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, initialDelay, period, SECONDS);

		// cancel the beeperHandle after 1 hour
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				beeperHandle.cancel(true);
			}
		}, 60 * 60, SECONDS);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(BeeperControlTest.class);
		System.out.println("--- --- --- BeeperControlTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
