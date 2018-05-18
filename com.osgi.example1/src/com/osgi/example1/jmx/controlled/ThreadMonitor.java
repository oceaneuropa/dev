package com.osgi.example1.jmx.controlled;

import java.lang.management.ManagementFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Controlled application: run it with the folowing VM parameters:
 * 
 * <pre>
 * -Dcom.sun.management.jmxremote
 * -Dcom.sun.management.jmxremote.port=9999
 * -Dcom.sun.management.jmxremote.authenticate=false
 * -Dcom.sun.management.jmxremote.ssl=false
 * </pre>
 * 
 * @see https://stackoverflow.com/questions/191215/how-to-stop-java-process-gracefully
 * 
 */
public class ThreadMonitor implements ThreadMonitorMBean {

	protected Thread thread = null;

	public ThreadMonitor(Thread thread) {
		this.thread = thread;
	}

	@Override
	public String getName() {
		return "JMX Controlled App";
	}

	@Override
	public void start() {
		// Start application here
		System.out.println("remote start called");
	}

	@Override
	public void stop() {
		// Stop application here
		System.out.println("remote stop called");

		thread.interrupt();
	}

	public boolean isRunning() {
		return Thread.currentThread().isAlive();
	}

	public static void main(String[] args) {
		MBeanServer mbeanServer = null;
		ThreadMonitorMBean threadMonitor = null;
		ObjectName threadMonitorName = null;

		try {
			System.out.println("JMX started");
			mbeanServer = ManagementFactory.getPlatformMBeanServer();

			threadMonitor = new ThreadMonitor(Thread.currentThread());
			threadMonitorName = new ObjectName(ThreadMonitorMBean.MBEAN_NAME);
			mbeanServer.registerMBean(threadMonitor, new ObjectName(ThreadMonitorMBean.MBEAN_NAME));

			while (!Thread.interrupted()) {
				// loop until interrupted
				System.out.println(".");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// Some final clean up could be here also
			if (mbeanServer != null) {
				if (threadMonitor != null && threadMonitorName != null) {
					try {
						mbeanServer.unregisterMBean(threadMonitorName);
					} catch (MBeanRegistrationException e) {
						e.printStackTrace();
					} catch (InstanceNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("JMX stopped");
		}
	}

}
